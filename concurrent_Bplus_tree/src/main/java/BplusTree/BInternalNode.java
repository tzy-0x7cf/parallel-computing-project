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
    public BNode<K, V> getChild(K key) {
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
    public boolean addChild(K key, BNode<K,V> addNode){
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
    public BInternalNode<K,V> splitAddChild(K key, BNode<K,V> addNode){
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
        numKeys++;

        //create a newNode and change the originalNode
        BInternalNode<K,V> newNode;
        newNode = new BInternalNode<K,V>(
                commonUtils.ArrayCopy(children,keys.size()/2 + 1,children.size() - 1),
                commonUtils.ArrayCopy(keys,keys.size()/2,keys.size() - 1),
                numKeys/2 + 1,
                this.parent);
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