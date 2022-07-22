package BplusTree;

public class BlinkTree<K extends Comparable,V> implements Btree<K, V> {

    private int size;
    private Node root;

    BlinkTree(){
        size = 0;
        root = null;
    }


    @Override
    public boolean containsKey(K key) {
        //TODO
        return false;
    }

    @Override
    public boolean containsVal(V value) {
        //TODO
        return false;
    }

    @Override
    public V get(K key) {
        //TODO
        return null;
    }

    @Override
    public void insert(K key, V value) {
        //TODO
    }

    @Override
    public boolean delete(K key) {
        //TODO
        return false;
    }

    @Override
    public void clear() {
        //TODO
    }

    @Override
    public int size() {
        //TODO
        return size;
    }
}
