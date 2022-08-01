package BplusTree;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class BPlusTree<K extends Comparable,V> implements Btree<K, V> {

    private AtomicInteger size;
    private BNode<K,V> root;

    private BLeafNode<K,V> leafNodeHead;

    private K lowestKey;

    BPlusTree(){
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
        //TODO: implement this
        return false;
    }

    @Override
    public V get(K key) {
        //TODO: implement this

        return null;
    }

    @Override
    public boolean insert(K key, V value) {
        //TODO: implement this
        
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