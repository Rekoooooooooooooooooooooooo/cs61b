package deque;

public class LinkedListDeque<T> implements Deque<T> {
    private static class ItemNode<T> {
        private ItemNode last;
        private T item;
        private ItemNode rest;
        private ItemNode(ItemNode before, T candidate, ItemNode after) {
            last = before;
            item = candidate;
            rest = after;
        }
    }

    private ItemNode<Integer> sentinel = new ItemNode<>(null, 42, null);
    private int size;

    public LinkedListDeque() {
        sentinel.rest = sentinel;
        sentinel.last = sentinel;
        size = 0;
    }

    public LinkedListDeque(T item) {
        sentinel.rest = new ItemNode<>(sentinel, item, sentinel);
        sentinel.last = sentinel.rest;
        size = 1;
    }

    /** The Deque API*/

    @Override
    public void addFirst(T item) {
        if (sentinel.rest == null) {
            sentinel.rest = new ItemNode<>(sentinel, item, sentinel);
            sentinel.last = sentinel.rest;
        } else {
            ItemNode p = sentinel.rest;
            sentinel.rest = new ItemNode<>(sentinel, item, p);
            p.last = sentinel.rest;
        }
        size += 1;
    }

    @Override
    public void addLast(T item) {
        if (sentinel.last == null) {
            sentinel.last = new ItemNode<>(sentinel, item, sentinel);
            sentinel.rest = sentinel.last;
        } else {
            ItemNode p = sentinel.last;
            sentinel.last = new ItemNode<>(p, item, sentinel);
            p.rest = sentinel.last;
        }
        size += 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (ItemNode p = sentinel.rest; p != sentinel; p = p.rest) {
            System.out.print(p.item + " ");
        }
        System.out.print("\n");
    }

    @Override
    public T removeFirst() {
        if (sentinel.rest != sentinel) {
            ItemNode p = sentinel.rest;
            T First = (T) p.item;
            p.rest.last = sentinel;
            sentinel.rest = p.rest;
            size -= 1;
            return First;
        } else {
            return null;
        }
    }

    @Override
    public T removeLast() {
        if (sentinel.last != sentinel) {
            ItemNode p = sentinel.last;
            T Last = (T) p.item;
            p.last.rest = sentinel;
            sentinel.last = p.last;
            size -= 1;
            return Last;
        } else {
            return null;
        }
    }

    @Override
    public T get(int index) {
        if (index < size) {
            ItemNode p;
            if (index < size / 2) {
                p = sentinel.rest;
                for (int i = 1; i <= index; i++) {
                    p = p.rest;
                }
            } else {
                p = sentinel.last;
                for (int i = size - 1; i >= index; i--) {
                    p = p.last;
                }
            }
            return (T) p.item;
        } else { return null; }
    }

    //Same as get, but uses recursion.
    public T getRecursive(int index) {
        if (index < size) {
            ItemNode p;
            if (index < size / 2) {
                p = sentinel.rest;
                return getRecursive_helper_smallindex(p, index);
            } else {
                p = sentinel.last;
                return getRecursive_helper_largeindex(p, index);
            }
        } else { return null; }
    }

    private T getRecursive_helper_smallindex(ItemNode p, int index) {
        if (index == 0) {
            return (T) p.item;
        } else {
            return getRecursive_helper_smallindex(p.rest, --index);
        }
    }

    private T getRecursive_helper_largeindex(ItemNode p, int index) {
        if (index == size - 1) {
            return (T) p.item;
        } else {
            return getRecursive_helper_largeindex(p.last, ++index);
        }
    }

    public ItemNode getNode(int index) {
        if (index < size) {
            ItemNode p;
            if (index < size / 2) {
                p = sentinel.rest;
                for (int i = 1; i <= index; i++) {
                    p = p.rest;
                }
            } else {
                p = sentinel.last;
                for (int i = size - 1; i >= index; i--) {
                    p = p.last;
                }
            }
            return p;
        } else { return null; }
    }

    public static void main(String[] args) {
        /* Creates a list of one integer, namely 10 */

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 5; i++) {
            lld1.addLast(i);
        }

        lld1.getRecursive(2);
    }

    /**
     @Override
    public Iterator<T> iterator() {
        //TODO
    }
     */

    @Override
    public boolean equals(Object o){
        if (o instanceof LinkedListDeque && ((LinkedListDeque<?>) o).size() == size) {
            ItemNode p_this = sentinel.rest;
            ItemNode p_o = ((LinkedListDeque<?>) o).getNode(0);
            while (p_this != sentinel) {
                if (p_o.item != p_this.item) {
                    return false;
                }
                p_o = p_o.rest;
                p_this = p_this.rest;
            }
            return true;
        } else {
            return false;
        }
    }
}