package bstmap;

import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private BSTNode root;             // root of BST

    public BSTMap() {
    }


    private boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        return cotainsKey(root, key);
    }

    private boolean cotainsKey(BSTNode x, K key) {
        if (key == null) throw new IllegalArgumentException("calls cotainsKey() with a null key");
        if (x == null) return false;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return cotainsKey(x.left, key);
        else if (cmp > 0) return cotainsKey(x.right, key);
        else return true;
    }

    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(BSTNode x, K key) {
        if (key == null) throw new IllegalArgumentException("calls get() with a null key");
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else return x.val;
    }

    @Override
    public int size() {
        return size(root);
    }

    private int size(BSTNode x) {
        if (x == null) return 0;
        else return x.size;
    }

    @Override
    public void put(K key, V val) {
        if (key == null) throw new IllegalArgumentException("calls put() with a null key");
        root = put(root, key, val);
    }

    private BSTNode put(BSTNode x, K key, V val) {
        if (x == null) return new BSTNode(key, val, 1);
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = put(x.left, key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else x.val = val;
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    @Override
    public Set<K> keySet() {
        HashSet<K> keyset = new HashSet<>();
        keySet(root, keyset);
        return keyset;
    }

    private void keySet(BSTNode x, Set<K> keyset) {
        if (x == null) return;
        else keyset.add(x.key);
        keySet(x.left, keyset);
        keySet(x.right, keyset);
    }


    private BSTNode deleteMin(BSTNode x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    @Override
    public V remove(K key) {
        if (key == null) throw new IllegalArgumentException("calls remove() with a null key");
        V val = get(root, key);
        root = remove(root, key);
        return val;
    }

    @Override
    public V remove(K key, V value) {
        if (key == null || value == null) throw new IllegalArgumentException("calls remove() with a null key");
        V val = get(root, key);
        if (val == value) {
            root = remove(root, key);
            return val;
        } else {
            return null;
        }
    }

    private BSTNode remove(BSTNode x, K key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = remove(x.left, key);
        else if (cmp > 0) x.right = remove(x.right, key);
        else {
            if (x.right == null) return x.left;
            if (x.left == null) return x.right;
            BSTNode t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    private K min() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return min(root).key;
    }

    private BSTNode min(BSTNode x) {
        if (x.left == null) return x;
        else return min(x.left);
    }

    private K max() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
        return max(root).key;
    }

    private BSTNode max(BSTNode x) {
        if (x.right == null) return x;
        else return max(x.right);
    }

    public void printInOrder() {
        for (K key : this) {
            System.out.println(key);
        }
    }

    @Override
    public Iterator<K> iterator() {
        return new keysIterator();
    }

    private class BSTNode {
        private final K key;           // sorted by key
        private V val;         // associated data
        private BSTNode left, right;  // left and right subtrees
        private int size;          // number of BSTNodes in subtree

        public BSTNode(K key, V val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }

    private class keysIterator implements Iterator<K> {
        List<K> keyQue;

        keysIterator() {
            keyQue = new ArrayList<>();
            keys(root, keyQue, min(), max());
        }

        private void keys(BSTNode x, List<K> queue, K lo, K hi) {
            if (x == null) return;
            int cmplo = lo.compareTo(x.key);
            int cmphi = hi.compareTo(x.key);
            if (cmplo < 0) keys(x.left, queue, lo, hi);
            if (cmplo <= 0 && cmphi >= 0) queue.add(x.key);
            if (cmphi > 0) keys(x.right, queue, lo, hi);
        }

        @Override
        public boolean hasNext() {
            return !keyQue.isEmpty();
        }

        @Override
        public K next() {
            return keyQue.remove(0);
        }
    }
}
