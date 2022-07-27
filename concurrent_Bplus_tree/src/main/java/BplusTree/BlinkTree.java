package BplusTree;

import java.util.Deque;
import java.util.LinkedList;

public class BlinkTree<K extends Comparable,V> implements Btree<K, V> {

    private int size;
    private Node<K,V> root;

    private leafNode leafNodeHead;

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
    public void insert(K key, V value) {
        Node<K,V> currentNode = root;
        Deque<Node<K,V>> stack = new LinkedList<>();

        while( currentNode instanceof internalNode )
        {

            Node<K,V> nextNode = ((internalNode<K, V>) currentNode).getChild(key);
            if(currentNode.next != nextNode){
                stack.push(nextNode);
            }
            currentNode = nextNode;
        }





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
