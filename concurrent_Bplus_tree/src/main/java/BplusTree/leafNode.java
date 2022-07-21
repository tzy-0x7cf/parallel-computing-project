package BplusTree;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class leafNode<K extends Comparable, V> extends Node<K,V>{
    /**
     * remind that the number of children is the same as the number of keys in leafNode
     */
    private List<V> children;

    public leafNode(K key,V value) {
        super(key);
        children = new ArrayList<>();
        children.set(0,value);
        isLeaf = true;
    }

    private leafNode(List<K> keys, List<V> values, int numKeys, Node<K,V> parent, Node<K,V> next) {
        super(keys, numKeys, parent, next);
        children = values;
    }

    /** get the child of the given key
     *
     * @param key
     * @return
     */
    public Node<K,V> getChild(K key) {
        //TODO
        return null;
    }

    /** adds a (key:value) pair to the current LeafNode.
     *
     * @param key
     * @param value
     * @return
     */
    public boolean addKV(K key, V value){
        //TODO
        return true;
    }

    /** in the case that the number of keys extends the maximum
     *  add a child to the right position by splitting
     *
     * @param key
     * @param value
     * @return
     */
    public leafNode<K,V> splitAddKV(K key, V value){
        //TODO
        return null;
    }

    /** convert the leaf node for visualization
     *
     * @return
     */
    public String toString() {
        //TODO
        return null;
    }
}
