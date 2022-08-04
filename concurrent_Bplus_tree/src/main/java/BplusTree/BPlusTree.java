package BplusTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

public class BPlusTree<K extends Comparable, V> implements Btree<K, V> {

    private AtomicInteger size;
    private BNode<K, V> root;

    public BPlusTree() {
        size = new AtomicInteger(0);
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /*
     * DON'T CALL THIS, TRADITIONAL BPLUS TREE CAN'T DO IT
     */
    @Override
    public boolean containsVal(V value) {
        return false;
    }

    @Override
    public V get(K key) {
        BNode<K, V> current = root;

        current.sharedLock();
        while (current instanceof BInternalNode) {
            BNode<K, V> son = ((BInternalNode<K, V>) current).getChild(key);
            son.sharedLock();
            current.sharedUnlock();
            current = son;
        }

        V res = ((BLeafNode<K, V>) current).getChild(key);
        current.sharedUnlock();
        return res;
    }

    @Override
    public boolean insert(K key, V value) {
        // empty tree
        if (root == null) {
            root = new BLeafNode<K, V>(key, value);
            return true;
        }

        Stack<BNode<K, V>> lockedAncestors = new Stack<>();
        BNode<K, V> current = root;
        current.exclusiveLock();

        while (current instanceof BInternalNode) {
            BNode<K, V> son = ((BInternalNode<K, V>) current).getChild(key);
            son.exclusiveLock();
            lockedAncestors.push(current);
            current = son;

            if (current.isSafe()) {
                while (!lockedAncestors.isEmpty()) {
                    lockedAncestors.pop().exclusiveUnlock();
                }
            }
        }

        
        if (current.isSafe()) {
            ((BLeafNode<K, V>) current).addKV(key, value);
        } else {
            // split
            BNode<K, V> newNode = ((BLeafNode<K, V>) current).splitAddKV(key, value);
            BInternalNode<K,V> parent = (BInternalNode<K,V>) current.parent;
            if (parent == null) {
                parent = new BInternalNode<K, V>(
                    new ArrayList<BNode<K,V>>(Arrays.asList(current, newNode)),
                    new ArrayList<K>(Collections.singletonList(newNode.keys.get(0))),
                    1,
                    null
                );
                if (root == current) root = parent;
            }
            else if (!parent.isSafe()) {
                K newKey = newNode.keys.get(0);
                while (!parent.isSafe()) {
                    newNode = parent.splitAddChild(newNode.keys.get(0), newNode);
                    newKey = newNode.keys.get(0);
                    if (newNode.keys.size() == ((BInternalNode<K,V>)newNode).getChildren().size()) {
                        newNode.keys.remove(0);
                        newNode.numKeys--;
                    }

                    BInternalNode<K,V> curr = parent;
                    parent = (BInternalNode<K,V>) newNode.parent;
                    if (parent == null) {
                        parent = new BInternalNode<K,V>(
                            new ArrayList<BNode<K,V>>(Arrays.asList(curr, newNode)),
                            new ArrayList<K>(Collections.singletonList(newKey)),
                            1,
                            null
                        );
                        if (root == curr) root = parent;
                    }
                    else if (parent.isSafe()) {
                        parent.addChild(newKey, newNode);
                    }
                }
            }
            else {
                parent.addChild(newNode.keys.get(0), newNode);
            }
        }

        current.exclusiveUnlock();
        while (!lockedAncestors.isEmpty()) {
            lockedAncestors.pop().exclusiveUnlock();
        }

        size.getAndAdd(1);

        return true;
    }

    @Override
    public boolean delete(K key) {
        // TODO
        return false;
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public int size() {
        return size.get();
    }
}