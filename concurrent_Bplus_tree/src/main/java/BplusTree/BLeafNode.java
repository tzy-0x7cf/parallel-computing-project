package BplusTree;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BLeafNode<K extends Comparable, V> extends BNode<K,V>{
    /**
     * remind that the number of children is the same as the number of keys in BLeafNode
     */
    private List<V> children;

    public BLeafNode(K key,V value) {
        super(key);
        children = new ArrayList<>(maxNumKeysPerNode + 1);
        children.set(0,value);
        isLeaf = true;
    }

    private BLeafNode(List<K> keys, List<V> values, int numKeys, BNode<K,V> parent) {
        super(keys, numKeys, parent);
        children = values;
    }

    public boolean containsValue(V value){
        return children.contains(value);
    }

    public boolean containsKey(K key){
        return keys.contains(key);
    }

    /** get the child of the given key
     *
     * @param key
     * @return
     */
    public V getChild(K key) {
        //TODO: implement this
        return null;
    }

    /** adds a (key:value) pair to the current BLeafNode.
     *
     * @param key
     * @param value
     * @return
     */
    public boolean addKV(K key, V value){
        //TODO: implement this
        return true;
    }

    /** in the case that the number of keys extends the maximum
     *  add a child to the right position by splitting
     *
     *  IMPORTANT THING:
     *  after this operation, this node is just OK
     *  but the parent node still need a proper index to insert
     *
     * @param key
     * @param value
     * @return
     */
    public BLeafNode<K,V> splitAddKV(K key, V value){
        //TODO: implement this
        return null;
    }

    /** convert the leaf node for visualization
     *
     * @return
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("BLeafNode : [");
        for(K key : keys){
            sb.append(key.toString());
            sb.append(";");
        }
        sb.append("]");
        return sb.toString();
    }
}
