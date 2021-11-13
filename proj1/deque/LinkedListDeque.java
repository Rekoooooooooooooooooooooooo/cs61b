package deque;
import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private static class ItemNode<T> {
        private ItemNode<T> last;
        private T item;
        private ItemNode<T> rest;

        private ItemNode() {
        }

        private ItemNode(ItemNode<T> before, T candidate, ItemNode<T> after) {
            last = before;
            item = candidate;
            rest = after;
        }
    }

    private final ItemNode<T> sentinel = new ItemNode<>();
    private int size;

    public LinkedListDeque() {
        sentinel.rest = sentinel;
        sentinel.last = sentinel;
        size = 0;
    }

    /** The Deque API*/

    @Override
    public void addFirst(T item) {
        if (size == 0) {
            sentinel.rest = new ItemNode<T>(sentinel, item, sentinel);
            sentinel.last = sentinel.rest;
        } else {
            ItemNode<T> p = sentinel.rest;
            sentinel.rest = new ItemNode<>(sentinel, item, p);
            p.last = sentinel.rest;
        }
        size += 1;
    }

    @Override
    public void addLast(T item) {
        if (size == 0) {
            sentinel.last = new ItemNode<>(sentinel, item, sentinel);
            sentinel.rest = sentinel.last;
        } else {
            ItemNode<T> p = sentinel.last;
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
        for (ItemNode<T> p = sentinel.rest; p != sentinel; p = p.rest) {
            System.out.print(p.item + " ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size >= 1) {
            ItemNode<T> p = sentinel.rest;
            T first = (T) p.item;
            if (size > 1) {
                ItemNode<T> pp = p.rest;
                pp.last = sentinel;
                sentinel.rest = pp;
            } else {
                sentinel.last = sentinel;
                sentinel.rest = sentinel;
            }
            size -= 1;
            return first;
        } else {
            return null;
        }
    }

    @Override
    public T removeLast() {
        if (size >= 1) {
            ItemNode<T> p = sentinel.last;
            T last = (T) p.item;
            if (size > 1) {
                ItemNode<T> pp = p.last;
                pp.rest = sentinel;
                sentinel.last = pp;
            } else {
                sentinel.last = sentinel;
                sentinel.rest = sentinel;
            }
            size -= 1;
            return last;
        } else {
            return null;
        }
    }

    @Override
    public T get(int index) {
        if (index < size) {
            ItemNode<T> p;
            if (index < size / 2) {
                p = sentinel.rest;
                for (int i = 1; i <= index; i++) {
                    p = p.rest;
                }
            } else {
                p = sentinel.last;
                for (int i = size - 1; i > index; i--) {
                    p = p.last;
                }
            }
            return (T) p.item;
        } else { return null; }
    }

    //Same as get, but uses recursion.
    public T getRecursive(int index) {
        if (index < size) {
            ItemNode<T> p;
            if (index < size / 2) {
                p = sentinel.rest;
                return getRecursive_helper_smallindex(p, index);
            } else {
                p = sentinel.last;
                return getRecursive_helper_largeindex(p, index);
            }
        } else { return null; }
    }

    private T getRecursive_helper_smallindex(ItemNode<T> p, int index) {
        if (index == 0) {
            return p.item;
        } else {
            return getRecursive_helper_smallindex(p.rest, --index);
        }
    }

    private T getRecursive_helper_largeindex(ItemNode<T> p, int index) {
        if (index == size - 1) {
            return p.item;
        } else {
            return getRecursive_helper_largeindex(p.last, ++index);
        }
    }

    private ItemNode<T> getNode(int index) {
        if (index < size) {
            ItemNode<T> p;
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

     @Override
     public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
     }

     private class LinkedListDequeIterator implements Iterator<T> {
         private ItemNode<T> wizNode;

         public LinkedListDequeIterator() {
             wizNode = sentinel.rest;
         }

         @Override
         public boolean hasNext() {
            return wizNode != sentinel;
         }

         @Override
         public T next() {
             T returnItem = wizNode.item;
             wizNode = wizNode.rest;
             return returnItem;
         }
     }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> other = (Deque<T>) o;
        if (other.size() != size) {
            return false;
        }
        if ((o instanceof LinkedListDeque)) {
            LinkedListDeque<T> other1 = (LinkedListDeque<T>) o;
            Iterator<T> itemsInThis = this.iterator();
            for (T item : other1) {
                T itemInThis = itemsInThis.next();
                if (!item.equals(itemInThis)) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (!get(i).equals(other.get(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        LinkedListDeque<Integer> L = new LinkedListDeque<>();
        L.addLast(0);
        L.removeLast();
        L.addFirst(2);
        L.get(0);
        System.out.println(L.get(0));
    }
}
