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
        children = new ArrayList<>(maxNumKeysPerNode);
        children.add(value);
        isLeaf = true;
    }

    private BLeafNode(List<K> keys, List<V> values, int numKeys, BNode<K,V> parent) {
        super(keys, numKeys, parent);
        children = values;
        isLeaf = true;
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
        for (int i = 0; i < numKeys; i++) {
            if (key.compareTo(keys.get(i)) == 0) return children.get(i);
        }
        return null;
    }

    /** adds a (key:value) pair to the current BLeafNode.
     *
     * @param key
     * @param value
     * @return
     */
    public boolean addKV(K key, V value){
        int index = 0;
        while(index < numKeys && key.compareTo(keys.get(index)) > 0){
            ++index;
        }

        if(index < numKeys && keys.get(index).compareTo(key) == 0){
            children.set(index,value);
            return true;
        }else{
            if(numKeys == maxNumKeysPerNode) return false;
            keys.add(index,key);
            children.add(index,value);
        }
        numKeys++;
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
        assert numKeys == maxNumKeysPerNode;

        //find the correct place
        int index = 0;
        while(index < numKeys && key.compareTo(keys.get(index)) > 0){
            ++index;
        }
        keys.add(index,key);
        children.add(index,value);

        //create a newNode and change the originalNode
        BLeafNode<K,V> newNode;
        newNode = new BLeafNode<K,V>(
                commonUtils.ArrayCopy(keys,keys.size()/2,numKeys - 1),
                commonUtils.ArrayCopy(children,children.size()/2,numKeys - 1),
                numKeys/2 + 1,
                this.parent);
        this.numKeys = numKeys/2;
        this.children = commonUtils.ArrayCopy(children,0,children.size()/2 - 1);
        this.keys = commonUtils.ArrayCopy(keys,0,keys.size()/2 - 1);

        //return the newNode
        return newNode;
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
