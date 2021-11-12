package deque;
import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T>{

    private Comparator<T> cmp;

    public MaxArrayDeque(Comparator<T> c) {
        cmp = c;
    }

    public T max() {
        T maxItem = get(0);
        for (T t : this) {
            if (cmp.compare(t, maxItem) > 0) {
                maxItem = t;
            }
        }
        return maxItem;
    }

    public T max(Comparator<T> c) {
        T maxItem = get(0);
        for (T t : this) {
            if (c.compare(t, maxItem) > 0) {
                maxItem = t;
            }
        }
        return maxItem;

    }
}
