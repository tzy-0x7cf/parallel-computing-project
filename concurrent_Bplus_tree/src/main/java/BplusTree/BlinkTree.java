package BplusTree;

public class BlinkTree<K extends Comparable,V> implements Btree<K, V> {

    private int size;
    private Node root;

    private leafNode leafNodeHead;

    BlinkTree(){
        size = 0;
        root = null;
        leafNodeHead = null;
    }


    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public boolean containsVal(V value) {
        leafNode ln = leafNodeHead;
        while(ln != null){
            if(ln.containsValue(value)){
                return true;
            }
            ln = (leafNode) ln.next;
        }
        return false;
    }

    @Override
    public V get(K key) {
        Node<K,V> currentNode = root;

        while( currentNode instanceof internalNode )
        {
            currentNode = ((internalNode<K, V>) currentNode).getChild(key);
            if(currentNode == null){
                return null;
            }
        }

        while( currentNode instanceof leafNode ) {
            V res = ((leafNode<K, V>) currentNode).getChild(key);
            if(res == null){
                currentNode = currentNode.next;
            }else{
                return res;
            }
        }

        return null;
    }

    @Override
    public void insert(K key, V value) {
        //TODO
    }

    @Override
    public boolean delete(K key) {
        //TODO
        return false;
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public int size() {
        return size;
    }
}
