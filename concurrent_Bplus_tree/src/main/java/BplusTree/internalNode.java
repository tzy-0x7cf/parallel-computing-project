package BplusTree;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class internalNode<K extends Comparable, V> extends Node<K,V> {
    private List<Node<K,V>> children;

    public internalNode(K key, K upKey){
        super(key, upKey);
        children = new ArrayList<>(maxNumKeysPerNode + 1);
        isLeaf = false;
    }

    public internalNode(List<Node<K,V>> children,List<K> keys,int numKeys,Node<K,V> next, Node<K,V> parent, K upKey){
        super(keys, numKeys, next, parent, upKey);
        for(Node<K,V> node : children){
            node.setParent(this);
        }
        this.children = children;
        isLeaf = false;
    }

    /** find a child that contains key
     *
     * @param key
     * @return
     */
    public Node<K, V> getChild(K key) {
        //we now assume this operation is atomic according to the paper
        //the lock to ensure the Atomicity is required

        if(key.compareTo(UpKey()) >= 0){
            return this.next;
        }
        int index = 0;
        while(index < numKeys && key.compareTo(keys.get(index)) >= 0){
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
        //we now assume this operation is atomic according to the paper
        //the lock to ensure the Atomicity is required

        if(numKeys == maxNumKeysPerNode) return false;
        int index = 0;
        while(index < numKeys && key.compareTo(keys.get(index)) > 0){
            ++index;
        }

        if(index < numKeys && keys.get(index).compareTo(key) == 0){
            throw new RuntimeException("the insert key can not be duplicate");
        }

        if (index == numKeys) {
            keys.add(key);
            children.add(addNode);
        }
        else {
            keys.add(index,key);
            children.add(index,addNode);
        }

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
     *  but the parent node still need a proper (index,newNode) to insert
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

        if (index == numKeys) {
            keys.add(key);
            children.add(addNode);
        }
        else {
            keys.add(index,key);
            children.add(index,addNode);
        }

        if(index != 0 && children.get(index - 1) != null){
            children.get(index - 1).next = children.get(index);
        }
        if(index != numKeys - 1 && children.get(index + 1) != null){
            children.get(index).next = children.get(index + 1);
        }
        numKeys++;

        //create a newNode and change the originalNode
        internalNode<K,V> newNode;
        newNode = new internalNode<K,V>(
                commonUtils.ArrayCopy(children,keys.size()/2 + 1,children.size() - 1),
                commonUtils.ArrayCopy(keys,keys.size()/2,keys.size() - 1),
                numKeys/2 + 1,
                this.next,
                this.parent,
                this.upKey);
        this.next = newNode;
        this.numKeys = numKeys/2;
        this.children = commonUtils.ArrayCopy(children,0,keys.size()/2);
        this.keys = commonUtils.ArrayCopy(keys,0,keys.size()/2 - 1);

        //return the newNode
        //parent need to add child(oldNode.upKey(),newNode)
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
