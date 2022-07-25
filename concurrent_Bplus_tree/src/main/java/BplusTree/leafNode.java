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
    public V getChild(K key) {
        if(key.compareTo(UpKey()) >= 0){
            return null;
        }
        int index = 0;
        while(index < numKeys && key.compareTo(keys.get(index)) > 0){
            index++;
        }
        return children.get(index);
    }

    /** adds a (key:value) pair to the current LeafNode.
     *
     * @param key
     * @param value
     * @return
     */
    public boolean addKV(K key, V value){
        if(numKeys == maxNumKeysPerNode || key.compareTo(UpKey()) >= 0) return false;
        int index = 0;
        while(index < numKeys && key.compareTo(keys.get(index)) > 0){
            ++index;
        }

        if(keys.get(index).compareTo(key) == 0){
            throw new RuntimeException("the insert key can not be duplicate");
        }

        keys.add(index,key);
        children.add(index,value);
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
    public leafNode<K,V> splitAddKV(K key, V value){
        assert numKeys == maxNumKeysPerNode;

        if(key.compareTo(UpKey()) >= 0){
            throw new RuntimeException("can not add a key greater than highKey");
        }

        //find the correct place
        int index = 0;
        while(index < numKeys && key.compareTo(keys.get(index)) > 0){
            ++index;
        }
        keys.add(index,key);
        children.add(index,value);

        //create a newNode and change the originalNode
        leafNode<K,V> newNode;
        newNode = new leafNode<K,V>(
                commonUtils.ArrayCopy(keys,keys.size()/2,keys.size() - 1),
                commonUtils.ArrayCopy(children,children.size()/2,children.size() - 1),
                numKeys/2 + 1,
                this.parent,
                this.next);
        this.next = newNode;
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
        StringBuilder sb = new StringBuilder("leafNode : [");
        for(K key : keys){
            sb.append(key.toString());
            sb.append(";");
        }
        sb.append("]");
        return sb.toString();
    }
}
