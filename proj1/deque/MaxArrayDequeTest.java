package deque;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class MaxArrayDequeTest {

    public static class StringComparator implements Comparator<String> {
        public int compare(String o1,String o2) {
            return o1.compareTo(o2);
        }
    }

    public static Comparator<String> getStringComparator() {
        return new StringComparator();
    }

    public static class StringComparator1 implements Comparator<String> {
        public int compare(String o1,String o2) {
            return o2.compareTo(o1);
        }
    }

    public static Comparator<String> getStringComparator1() {
        return new StringComparator1();
    }

    @Test
    public void maxTest() {

        MaxArrayDeque<String> ard = new MaxArrayDeque<>(getStringComparator());
        ard.addLast("A");
        ard.addLast("B");
        ard.addLast("C");
        ard.addLast("D");
        ard.addLast("E");
        ard.addLast("F");
        ard.addLast("G");
        assertEquals("G", ard.max());
        assertEquals("A", ard.max(getStringComparator1()));
    }
}
