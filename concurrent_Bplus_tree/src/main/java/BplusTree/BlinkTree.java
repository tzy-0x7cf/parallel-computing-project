package BplusTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class BlinkTree<K extends Comparable,V> implements Btree<K, V> {

    private AtomicInteger size;
    private Node<K,V> root;

    private leafNode<K,V> leafNodeHead;

    private K lowestKey;
    private K maxValue;

    public BlinkTree(K maxValue){
        size = new AtomicInteger(0);
        root = null;
        leafNodeHead = null;
        this.maxValue = maxValue;
    }


    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public boolean containsVal(V value) {
        leafNode ln = leafNodeHead;
        while(ln != null){
            System.out.println(ln);
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
        return ((leafNode<K,V>)currentNode).getChild(key);
    }

    @Override
    public boolean insert(K key, V value) {
        Node<K,V> currentNode = root;

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
                K newKey = newNode.keys.get(0);
                internalNode<K,V> parent = (internalNode<K, V>) newNode.parent;
                if(parent == null){
                    parent = new internalNode<>(
                        new ArrayList<Node<K,V>>(Arrays.asList(currentLeaf, newNode)),
                        new ArrayList<K>(Collections.singletonList(newKey)),
                        1,
                        null,
                        null,
                        newNode.upKey
                    );
                    if (root == currentLeaf) root = parent;
                    return true;
                }
                //add (key,newNode) to the child of oldNode's parent
                if (parent.numKeys() == Node.maxNumKeysPerNode) {
                    while(parent.numKeys() == Node.maxNumKeysPerNode){
                        //parent need split
                        parent.lock();
                        newNode = parent.splitAddChild(newKey,newNode);
                        newKey = newNode.keys.get(0);
                        if (newNode.keys.size() == ((internalNode)newNode).getChildren().size()) {
                            newNode.keys.remove(0);
                            newNode.numKeys--;
                        }

                        internalNode<K,V> current = parent;
                        parent.unlock();
                        //loop
                        parent = (internalNode<K, V>) newNode.parent;
                        if(parent == null){
                            parent = new internalNode<>(
                                new ArrayList<Node<K,V>>(Arrays.asList(current, newNode)),
                                new ArrayList<K>(Collections.singletonList(newKey)),
                                1,
                                null,
                                null,
                                newNode.UpKey()
                            );
                            if (root == current) root = parent;
                        }
                        else if (parent.numKeys() < Node.maxNumKeysPerNode) {
                            parent.lock();
                            parent.addChild(newKey,newNode);
                            parent.unlock();
                            break;
                        }
                    }
                }
                else {
                    //parent need not split
                    parent.lock();
                    parent.addChild(newKey,newNode);
                    parent.unlock();
                }
            }
        }else{
            //empty tree
            root = new leafNode<K,V>(key,value, maxValue);
            leafNodeHead = (leafNode<K, V>) root;
            lowestKey = key;
        }

        size.getAndAdd(1);
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
