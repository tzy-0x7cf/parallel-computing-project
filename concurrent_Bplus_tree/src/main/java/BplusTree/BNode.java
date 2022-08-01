package BplusTree;

import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Setter
public abstract class BNode <K extends Comparable, V>{
    protected static int maxNumKeysPerBNode = 8;
    protected int numKeys;
    protected List<K> keys;
    protected BNode<K,V> parent = null;
    protected ReadWriteLock readWriteLock = null;
    protected Lock readLock = null;
    protected Lock writeLock = null;
    protected boolean isLeaf;

    public BNode (K key){
        numKeys = 1;
        keys = new ArrayList<>(maxNumKeysPerBNode + 1);
        keys.set(0,key);
        readWriteLock = new ReentrantReadWriteLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
    }

    public BNode(List<K> keys,int numKeys, BNode<K,V> parent){
        this.keys = keys;
        this.numKeys = numKeys;
        this.parent = parent;
        readWriteLock = new ReentrantReadWriteLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
    }

    public int numKeys(){
        return numKeys;
    }

    public void sortKeys(){
        Collections.sort(keys);
    }

    /**
     * shared lock and unlock this node
     */
    public void sharedLock() {
        readLock.lock();
    }

    public void sharedUnlock() {
        readLock.unlock();
    }

    /*
     * exclusive lock and unlock this node
     */
    public void exclusiveLock() {
        writeLock.lock();
    }

    public void exclusiveUnlock() {
        writeLock.unlock();
    }

    public boolean isExclusiveLocked() {
        return ((ReentrantReadWriteLock)readWriteLock).isWriteLocked();
    }

}