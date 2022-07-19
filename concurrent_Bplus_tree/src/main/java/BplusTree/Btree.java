package BplusTree;

public interface Btree<K extends Comparable,V> {

    /**
     * Determine if the key is in the tree.
     *
     * @param key The key want to find.
     * @return True if the key is in the tree.
     */
    public boolean containsKey(K key);

    /**
     * Determine if the value is in the tree.
     *
     * @param value The value want to find.
     * @return True if the value is in the tree.
     */
    public boolean containsVal(V value);

    /**
     * search value by the key.
     *
     * @param key The key want to find.
     * @return Value you get.
     */
    public V get(K key);

    /**
     * insert KV pair.
     *
     * @param key The key to insert.
     * @param value The value to insert.
     */
    public void insert(K key, V value);

    /**
     * delete value by the key.
     *
     * @param key The key want to delete.
     * @return True if success.
     */
    public boolean delete(K key);

    /**
     * empty the tree
     */
    public void clear();

    /**
     * size of the tree
     */
    public int size();
}
