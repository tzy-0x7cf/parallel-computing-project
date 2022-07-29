package BplusTree;

import java.util.Collections;

public class BlinkTree<K extends Comparable,V> implements Btree<K, V> {

    private int size;
    private Node<K,V> root;

    private leafNode<K,V> leafNodeHead;

    BlinkTree(){
        size = 0;
        root = null;
        leafNodeHead = null;
    }


    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public boolean containsVal(V value) {
        leafNode ln = leafNodeHead;
        while(ln != null){
            if(ln.containsValue(value)){
                return true;
            }
            ln = (leafNode) ln.next;
        }
        return false;
    }

    @Override
    public V get(K key) {
        Node<K,V> currentNode = root;

        while( currentNode instanceof internalNode )
        {
            currentNode = ((internalNode<K, V>) currentNode).getChild(key);
            if(currentNode == null){
                return null;
            }
        }

        while( currentNode instanceof leafNode ) {
            V res = ((leafNode<K, V>) currentNode).getChild(key);
            if(res == null){
                currentNode = currentNode.next;
            }else{
                return res;
            }
        }

        return null;
    }

    @Override
    public boolean insert(K key, V value) {
        Node<K,V> currentNode = root;

        while( currentNode instanceof internalNode )
        {
            Node<K,V> nextNode = ((internalNode<K, V>) currentNode).getChild(key);

            /*
            if(nextNode == null){
                currentNode.keys.set(currentNode.numKeys - 1,key);
                nextNode = ((internalNode<K, V>) currentNode).getChild(key);
            }
            */

            currentNode = nextNode;
        }

        leafNode<K,V> currentLeaf = (leafNode<K, V>) currentNode;
        if(currentLeaf != null){
            currentLeaf.lock();
            if(currentLeaf.containsKey(key) || currentLeaf.numKeys() < Node.maxNumKeysPerNode){
                boolean res = currentLeaf.addKV(key,value);
                currentLeaf.unlock();
                return res;
            }else{
                Node<K,V> newNode = currentLeaf.splitAddKV(key,value);
                currentLeaf.unlock();
                K newKey = newNode.UpKey();
                internalNode<K,V> parent = (internalNode<K, V>) newNode.parent;
                if(parent == null){
                    parent = new internalNode<>(Collections.singletonList(currentLeaf),Collections.singletonList(newKey)
                    ,1,null,null);
                    newNode.parent = parent;
                }
                while(parent.numKeys() == Node.maxNumKeysPerNode){
                    parent.lock();
                    newNode = parent.splitAddChild(newKey,newNode);
                    internalNode<K,V> current = parent;
                    newKey = newNode.UpKey();
                    parent.unlock();
                    parent = (internalNode<K, V>) newNode.parent;
                    if(parent == null){
                        parent = new internalNode<>(Collections.singletonList(current),Collections.singletonList(newKey)
                                ,1,null,null);
                        newNode.parent = parent;
                    }
                }
                parent.lock();
                parent.addChild(newKey,newNode);
                parent.unlock();
            }
        }else{
            root = new leafNode<K,V>(key,value);
            leafNodeHead = (leafNode<K, V>) root;
        }
        return true;
    }

    @Override
    public boolean delete(K key) {
        //TODO
        return false;
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public int size() {
        return size;
    }
}
