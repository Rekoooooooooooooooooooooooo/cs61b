package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;


public class ArrayDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        ArrayDeque<String> ard1 = new ArrayDeque<String>();

        assertTrue("A newly initialized ardeque should be empty", ard1.isEmpty());
        ard1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, ard1.size());
        assertFalse("ard1 should now contain 1 item", ard1.isEmpty());

        ard1.addLast("middle");
        assertEquals(2, ard1.size());

        ard1.addLast("back");
        assertEquals(3, ard1.size());

        System.out.println("Printing out deque: ");
        ard1.printDeque();

    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        ArrayDeque<Integer> ard1 = new ArrayDeque<Integer>();
        // should be empty
        assertTrue("ard1 should be empty upon initialization", ard1.isEmpty());

        ard1.addFirst(10);
        // should not be empty
        assertFalse("ard1 should contain 1 item", ard1.isEmpty());

        ard1.removeFirst();
        // should be empty
        assertTrue("ard1 should be empty after removal", ard1.isEmpty());

    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        ArrayDeque<Integer> ard1 = new ArrayDeque<>();
        ard1.addFirst(3);

        ard1.removeLast();
        ard1.removeFirst();
        ard1.removeLast();
        ard1.removeFirst();

        int size = ard1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

    }

    @Test
    /* Check if you can create ArrayDeques with different parameterized types*/
    public void multipleParamTest() {


        ArrayDeque<String>  ard1 = new ArrayDeque<String>();
        ArrayDeque<Double>  ard2 = new ArrayDeque<Double>();
        ArrayDeque<Boolean> ard3 = new ArrayDeque<Boolean>();

        ard1.addFirst("string");
        ard2.addFirst(3.14159);
        ard3.addFirst(true);

        String s = ard1.removeFirst();
        double d = ard2.removeFirst();
        boolean b = ard3.removeFirst();

    }

    @Test
    /* check if null is return when removing from an empty ArrayDeque. */
    public void emptyNullReturnTest() {

        ArrayDeque<Integer> ard1 = new ArrayDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, ard1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, ard1.removeLast());


    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigardequeTest() {

        ArrayDeque<Integer> ard1 = new ArrayDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            ard1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) ard1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) ard1.removeLast(), 0.0);
        }


    }

    @Test
    /* Check if get() can correctly return items */
    public void getTest() {

        ArrayDeque<Integer> ard1 = new ArrayDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            ard1.addLast(i);
        }

        for (int i = 0; i < 500; i++) {
            assertEquals("Should have the same value", i, ard1.get(i), 0.0);
        }
    }

     @Test
     public void equalTest() {

     ArrayDeque<Integer> ard1 = new ArrayDeque<>();
     ArrayDeque<Integer> ard2 = new ArrayDeque<>();
     ArrayDeque<Integer> ard3 = new ArrayDeque<>();
     ArrayDeque<Integer> ard4 = new ArrayDeque<>();
     ArrayDeque<Double> ard5 = new ArrayDeque<>();
     for (int i = 0; i < 10; i++) {
     ard1.addLast(i);
     ard2.addLast(i);
     ard3.addLast(i);
     ard4.addLast(i);
     ard5.addLast((double) i);
     }

     assertTrue(ard1.equals(ard2));

     assertFalse(ard1.equals(ard5));

     ard3.removeLast();
     assertFalse(ard1.equals(ard3));

     ard4.removeFirst();
     assertFalse(ard1.equals(ard4));

    }
}
