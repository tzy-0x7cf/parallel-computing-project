package BplusTree;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BInternalNode<K extends Comparable, V> extends BNode<K,V> {
    private List<BNode<K,V>> children;

    public BInternalNode(K key){
        super(key);
        children = new ArrayList<>(maxNumKeysPerNode + 1);
        isLeaf = false;
    }

    public BInternalNode(List<BNode<K,V>> children,List<K> keys,int numKeys, BNode<K,V> parent){
        super(keys, numKeys, parent);
        for(BNode<K,V> node : children){
            node.setParent(node);
        }
        isLeaf = false;
    }

    /** find a child that contains key
     *
     * @param key
     * @return
     */
    public BNode<K, V> getChild(K key) {
        //TODO: implement this

        return null;
    }

    /** add a child to the right position
     *
     * @param key
     * @param addNode
     * @return
     */
    public boolean addChild(K key, BNode<K,V> addNode){
        //TODO: implement this

        return false;
    }

    /** in the case that the number of keys extends the maximum
     *  add a child to the right position by splitting
     *
     *  IMPORTANT THING:
     *  after this operation, this node is just OK
     *  but the parent node still need a proper (index,newNode) to insert
     *
     * @param key
     * @param addNode
     * @return
     */
    public BInternalNode<K,V> splitAddChild(K key, BNode<K,V> addNode){
        //TODO: implement this

        return null;
    }

    /** convert the internal node for visualization
     *
     * @return
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("internal : [");
        for(K key : keys){
            sb.append(key.toString());
            sb.append(";");
        }
        sb.append("]");
        return sb.toString();
    }




}