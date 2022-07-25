package BplusTree;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class internalNode<K extends Comparable, V> extends Node<K,V> {
    private List<Node<K,V>> children;

    public internalNode(K key){
        super(key);
        children = new ArrayList<>(maxNumKeysPerNode + 1);
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
        if(key.compareTo(UpKey()) >= 0){
            return null;
        }
        int index = 0;
        while(index < numKeys && key.compareTo(keys.get(index)) > 0){
            ++index;
        }
        return children.get(index);
    }

    /** add a child to the right position
     *
     * @param key
     * @param addNode
     * @return
     */
    public boolean addChild(K key, Node<K,V> addNode){
        if(numKeys == maxNumKeysPerNode) return false;
        int index = 0;
        while(index < numKeys && key.compareTo(keys.get(index)) > 0){
            ++index;
        }

        if(index < numKeys && keys.get(index).compareTo(key) == 0){
            throw new RuntimeException("the insert key can not be duplicate");
        }

        keys.add(index,key);
        children.add(index,addNode);
        if(index != 0 && children.get(index - 1) != null){
            children.get(index - 1).next = children.get(index);
        }
        if(index != numKeys - 1 && children.get(index + 1) != null){
            children.get(index).next = children.get(index + 1);
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
     * @param addNode
     * @return
     */
    public internalNode<K,V> splitAddChild(K key, Node<K,V> addNode){
        assert numKeys == maxNumKeysPerNode;

        //find the correct place
        int index = 0;
        while(index < numKeys && key.compareTo(keys.get(index)) > 0){
            ++index;
        }
        if(index < numKeys && keys.get(index).compareTo(key) == 0){
            throw new RuntimeException("the insert key can not be duplicate");
        }

        keys.add(index,key);
        children.add(index,addNode);
        if(index != 0 && children.get(index - 1) != null){
            children.get(index - 1).next = children.get(index);
        }
        if(index != numKeys - 1 && children.get(index + 1) != null){
            children.get(index).next = children.get(index + 1);
        }

        //create a newNode and change the originalNode
        internalNode<K,V> newNode;
        newNode = new internalNode<K,V>(
                commonUtils.ArrayCopy(children,children.size()/2,children.size() - 1),
                commonUtils.ArrayCopy(keys,keys.size()/2,keys.size() - 1),
                numKeys/2 + 1,
                this.next,
                this.parent);
        this.next = newNode;
        this.numKeys = numKeys/2;
        this.children = commonUtils.ArrayCopy(children,0,children.size()/2 - 1);
        this.keys = commonUtils.ArrayCopy(keys,0,keys.size()/2 - 1);

        //return the newNode
        return newNode;
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
