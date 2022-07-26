package BplusTree;

import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Setter
public abstract class Node <K extends Comparable, V>{
    protected static int maxNumKeysPerNode = 8;
    protected int numKeys;
    protected List<K> keys;
    protected Node<K,V> parent = null;
    protected Node<K,V> next = null;//pointer to the next node in the same level
    protected Lock lock = null;
    protected boolean isLeaf;

    public Node (K key){
        numKeys = 1;
        keys = new ArrayList<>(maxNumKeysPerNode + 1);
        keys.set(0,key);
        lock = new ReentrantLock();
    }

    public Node(List<K> keys,int numKeys,Node<K,V> next, Node<K,V> parent){
        this.keys = keys;
        this.numKeys = numKeys;
        this.next = next;
        this.parent = parent;
        lock = new ReentrantLock();
    }

    public int numKeys(){
        return numKeys;
    }

    public void sortKeys(){
        Collections.sort(keys);
    }

    public K LowKey(){
        return keys.get(0);
    }

    public K UpKey(){
        return keys.get(numKeys - 1);
    }

    /**
     * Obtains a lock on this node.
     */
    public void lock() {
        lock.lock();
    }

    /**
     * Unlocks this node if called by the Thread owning the lock.
     */
    public void unlock() {
        lock.unlock();
    }

    public boolean isLocked() {
        return ((ReentrantLock)lock).isLocked();
    }

}
