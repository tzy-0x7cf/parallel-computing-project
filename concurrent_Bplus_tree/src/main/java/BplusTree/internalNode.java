package BplusTree;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class internalNode<K extends Comparable, V> extends Node<K,V> {
    private List<Node<K,V>> children;

    public internalNode(K key){
        super(key);
        children = new ArrayList<>();
        isLeaf = false;
    }

    public internalNode(List<Node<K,V>> children,List<K> keys,int numKeys,Node<K,V> next, Node<K,V> parent){
        super(keys, numKeys, next, parent);
        for(Node<K,V> node : children){
            node.setParent(node);
        }
        isLeaf = false;
    }

    /** find a child that contains key
     *
     * @param key
     * @return
     */
    public Node<K, V> getChild(K key) {
        //TODO
        return null;
    }

    /** add a child to the right position
     *
     * @param key
     * @param addNode
     * @return
     */
    public boolean addChild(K key, Node<K,V> addNode){
        //TODO
        return true;
    }

    /** in the case that the number of keys extends the maximum
     *  add a child to the right position by splitting
     *
     * @param key
     * @param addNode
     * @return
     */
    public internalNode<K,V> splitAddChild(K key, Node<K,V> addNode){
        //TODO
        return null;
    }

    /** convert the internal node for visualization
     *
     * @return
     */
    public String toString() {
        //TODO
        return null;
    }




}
