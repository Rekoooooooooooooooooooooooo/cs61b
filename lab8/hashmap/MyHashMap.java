package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Collection<Node>[] buckets;
    private int size;
    private Set<K> keySet;

    private int initialSize = 16;
    private double loadFactor = 0.75;

    /** Constructors */
    @SuppressWarnings("unchecked")
    public MyHashMap() {
        buckets = createTable(this.initialSize);
        keySet = new HashSet<>();
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public MyHashMap(int initialSize) {
        this.initialSize = initialSize;
        buckets = createTable(this.initialSize);
        keySet = new HashSet<>();
        size = 0;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    @SuppressWarnings("unchecked")
    public MyHashMap(int initialSize, double maxLoad) {
        this.initialSize = initialSize;
        this.loadFactor = maxLoad;
        buckets = createTable(this.initialSize);
        keySet = new HashSet<>();
        size = 0;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    @SuppressWarnings("unchecked")
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] a = new Collection[tableSize];
        for(int i = 0; i < tableSize; i++) {
            a[i] = createBucket();
        }
        return a;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        buckets = createTable(this.initialSize);
        keySet.clear();
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public V get(K key) {
        int index = indexOfHash(key, buckets.length);
        Collection<Node> bucket = this.buckets[index];
        for (Node i : bucket) {
            if (i.key.equals(key)) {
                return i.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void put(K key, V value) {
        checkLoad();
        Node a = createNode(key, value);
        int index = indexOfHash(key, buckets.length);
        Collection<Node> bucket = this.buckets[index];
        for (Node i : bucket) {
            if (i.key.equals(key)) {
                i.value = value;
                return;
            }
        }
        bucket.add(a);
        keySet.add(key);
        size++;
    }

    private int indexOfHash(K key, int length) {
        int index = (key.hashCode() - 1) % length;
        if (index < 0) index += length;
        return index;
    }

    private void checkLoad() {
        double load = size * 1.0 / buckets.length;
        if(load > loadFactor) {
            Collection<Node>[] newBuckets = createTable(buckets.length * 2);
            for (Collection<Node> bucket : buckets) {
                for (Node i : bucket) {
                    int index = indexOfHash(i.key, newBuckets.length);
                    newBuckets[index].add(i);
                }
            }
            buckets = newBuckets;
        }
    }

    @Override
    public Set<K> keySet() {
        return keySet;
    }

    @Override
    public V remove(K key) {
        if (!keySet.remove(key)) return null;
        int index = indexOfHash(key, buckets.length);
        V res = null;
        Collection<Node> bucket = this.buckets[index];
        for (Node i  : bucket) {
            if (i.key.equals(key)) {
                res = i.value;
                bucket.remove(i);
            }
        }
        size--;
        return res;
    }

    @Override
    public V remove(K key, V value) {
        if (!keySet.remove(key)) return null;
        int index = indexOfHash(key, buckets.length);
        V res = null;
        Collection<Node> bucket = this.buckets[index];
        for (Node i  : bucket) {
            if (i.key.equals(key)) {
                res = i.value;
                bucket.remove(i);
            }
        }
        size--;
        return res;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet.iterator();
    }
}
