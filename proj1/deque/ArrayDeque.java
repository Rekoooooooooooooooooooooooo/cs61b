package deque;
import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int size;
    private final int minLength = 8;
    private int First;
    private int nextFirst;
    private int Last;
    private int nextLast;
    private final double minusage = 0.25;
    private final double maxusage = 0.75;

    /** Creates an empty list. */
    @SuppressWarnings("unchecked")
    public ArrayDeque() {
        items = (T[]) new Object[minLength];
        size = 0;
        First = 0;
        nextFirst = items.length - 1;
        Last = 0;
        nextLast = 1;
    }


    /** The Deque API*/

    /** check if usage is ok. */
    private void checkuasge() {
        double usage = (double) size / items.length;
        if (items.length != minLength && usage < minusage) {
            resize((int) items.length / 2);
        } else if (usage > maxusage) {
            resize((int) items.length * 2);
        }
    }

    /** FIRST is a pointer to the first item in ArrayDeque,
     * this method is used to move the pointer,
     * TRUE for forward and FALSE for backward
     */
    private void moveFirst(boolean forward) {
        if (forward) {
            First = nextFirst;
            if (nextFirst == 0) {
                nextFirst = items.length - 1;
            } else {
                nextFirst -= 1;
            }
        } else {
            nextFirst = First;
            if (First == items.length - 1) {
                First = 0;
            } else {
                First += 1;
            }
        }
    }

    /** Last is a pointer to the last item in ArrayDeque,
     * this method is used to move the pointer,
     * TRUE for forward and FALSE for backward
     */
    private void moveLast(boolean forward) {
        if (forward) {
            nextLast = Last;
            if (Last == 0) {
                Last = items.length - 1;
            } else {
                Last -= 1;
            }
        } else {
            Last = nextLast;
            if (nextLast == items.length - 1) {
                nextLast = 0;
            } else {
                nextLast += 1;
            }
        }
    }

    @Override
    public void addFirst(T item) {
        if (isEmpty()) {
            items[First] = item;
        } else {
            checkuasge();
            moveFirst(true);
            items[First] = item;
        }
        size += 1;
    }

    @Override
    public void addLast(T item) {
        if (isEmpty()) {
            items[Last] = item;
        } else {
            checkuasge();
            moveLast(false);
            items[Last] = item;
        }
        size += 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (int index = 0; index < size; index++) {
            if (index + First == items.length) {
                for (int newindex = 0; newindex <= Last; newindex++) {
                    System.out.print(items[newindex] + " ");
                }
                break;
            }
            System.out.print(items[index + First] + " ");
        }
        System.out.print("\n");
    }

    @Override
    public T removeFirst() {
        if (!isEmpty()) {
            T FirstValue = items[First];
            items[First] = null;
            if (size != 1) {
                checkuasge();
                moveFirst(false);
            }
            size -= 1;
            return FirstValue;
        } else {
            return null;
        }
    }

    @Override
    public T removeLast() {
        if (!isEmpty()) {
            T LastValue = items[Last];
            items[Last] = null;
            if (size != 1) {
                checkuasge();
                moveLast(true);
            }
            size -= 1;
            return LastValue;
        } else {
            return null;
        }
    }

    @Override
    public T get(int index) {
        if (index >= 0 && index < size) {
            int realindex = First + index;
            if (realindex >= items.length) {
                return items[realindex - items.length];
            } else {
                return items[realindex];
            }
        } else {
            return null;
        }

    }

    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        if (First < Last) {
            System.arraycopy(items, First, a, 0, size);
        } else {
            System.arraycopy(items, First, a, 0, items.length - First);
            System.arraycopy(items, 0, a, items.length - First, Last + 1);
        }
        items = a;
        First = 0;
        nextFirst = items.length - 1;
        Last = size - 1;
        nextLast = Last + 1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
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
        for (int i = 0; i < size; i++) {
            if (!get(i).equals(other.get(i))) {
                return false;
            }
        }
        return true;
    }

     @Override
     public Iterator<T> iterator(){
        return new ArrayDequeIterator();
     }

     private class ArrayDequeIterator implements Iterator<T> {
         private int wizPos;

         public ArrayDequeIterator() {
             wizPos = 0;
         }

         @Override
         public boolean hasNext() {
             return wizPos < size;
         }

         @Override
         public T next() {
             T returnItem = get(wizPos);
             wizPos += 1;
             return returnItem;
         }
     }
}
