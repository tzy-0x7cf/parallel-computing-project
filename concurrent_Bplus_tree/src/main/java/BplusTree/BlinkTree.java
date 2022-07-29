package BplusTree;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class BlinkTree<K extends Comparable,V> implements Btree<K, V> {

    private AtomicInteger size;
    private Node<K,V> root;

    private leafNode<K,V> leafNodeHead;

    private K lowestKey;

    BlinkTree(){
        size = new AtomicInteger(0);
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

        //find the leafNode
        while( currentNode instanceof internalNode )
        {
            currentNode = ((internalNode<K, V>) currentNode).getChild(key);
            if(currentNode == null){
                return null;
            }
        }

        //find the Value
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
        if(get(key) != null) size.getAndAdd(1);

        //find the right leafNode
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
            //lock the leafNode
            currentLeaf.lock();
            if(key.compareTo(lowestKey) <= 0){
                lowestKey = key;
                leafNodeHead = currentLeaf;
            }
            if(currentLeaf.containsKey(key) || currentLeaf.numKeys() < Node.maxNumKeysPerNode){
                //not split
                boolean res = currentLeaf.addKV(key,value);
                currentLeaf.unlock();
                return res;
            }else{
                //split
                Node<K,V> newNode = currentLeaf.splitAddKV(key,value);
                currentLeaf.unlock();
                K newKey = newNode.UpKey();
                internalNode<K,V> parent = (internalNode<K, V>) newNode.parent;
                if(parent == null){
                    parent = new internalNode<>(Collections.singletonList(currentLeaf),Collections.singletonList(newKey)
                    ,1,null,null);
                    newNode.parent = parent;
                }
                //add (key,newNode) to the child of oldNode's parent
                while(parent.numKeys() == Node.maxNumKeysPerNode){
                    //parent need split
                    parent.lock();
                    newNode = parent.splitAddChild(newKey,newNode);
                    internalNode<K,V> current = parent;
                    newKey = newNode.UpKey();
                    parent.unlock();
                    //loop
                    parent = (internalNode<K, V>) newNode.parent;
                    if(parent == null){
                        parent = new internalNode<>(Collections.singletonList(current),Collections.singletonList(newKey)
                                ,1,null,null);
                        newNode.parent = parent;
                    }
                }
                //parent need not split
                parent.lock();
                parent.addChild(newKey,newNode);
                parent.unlock();
            }
        }else{
            //empty tree
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
        return size.get();
    }
}
