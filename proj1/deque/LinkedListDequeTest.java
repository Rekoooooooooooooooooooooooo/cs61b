package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

		assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
		lld1.addFirst("front");

		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

		lld1.addLast("middle");
		assertEquals(2, lld1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());

		System.out.println("Printing out deque: ");
		lld1.printDeque();

    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());

		lld1.removeFirst();
		// should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());

    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {


        LinkedListDeque<String>  lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double>  lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();

    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());


    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }


    }

    @Test
    /* Check if get() can correctly return items */
    public void getTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (int i = 0; i < 500; i++) {
            assertEquals("Should have the same value", i, lld1.get(i), 0.0);
        }
    }

    @Test
    /* Check if getRecursive() can correctly return items */
    public void getRecursiveTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (int i = 0; i < 10; i++) {
            assertEquals("Should have the same value", i, lld1.getRecursive(i), 0.0);
        }

    }

    @Test
    public void equalTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        LinkedListDeque<Integer> lld2 = new LinkedListDeque<>();
        LinkedListDeque<Integer> lld3 = new LinkedListDeque<>();
        LinkedListDeque<Integer> lld4 = new LinkedListDeque<>();
        LinkedListDeque<Double> lld5 = new LinkedListDeque<>();
        for (int i = 0; i < 10; i++) {
            lld1.addLast(i);
            lld2.addLast(i);
            lld3.addLast(i);
            lld4.addLast(i);
            lld5.addLast((double) i);
        }

        assertTrue(lld1.equals(lld2));

        assertFalse(lld1.equals(lld5));

        lld3.removeLast();
        assertFalse(lld1.equals(lld3));

        lld4.removeFirst();
        assertFalse(lld1.equals(lld4));

    }


    @Test
    public void testThreeAddThreeRemove() {
        int[] items = {4, 5, 6};
        LinkedListDeque testLLD = new LinkedListDeque<Integer>();
        ArrayDeque testArD = new ArrayDeque<Integer>();
        for (int i = 0; i < items.length; i++) {
            testLLD.addLast(items[i]);
            testArD.addLast(items[i]);
        }
        for (int i = 0; i < items.length; i++){ assertTrue(testLLD.removeLast() == testArD.removeLast()); }
    }

    @Test
    public void randomizedTest() {
        LinkedListDeque testLLD = new LinkedListDeque<Integer>();
        ArrayDeque testArD = new ArrayDeque<Integer>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 5);
            if (operationNumber == 0) {
                // addFirst
                int randVal = StdRandom.uniform(0, 100);
                testLLD.addFirst(randVal);
                testArD.addFirst(randVal);
                //System.out.println("addLast(" + randVal + ")");
                System.out.println("ard.addFirst(" + randVal + ");");
            } else if (operationNumber == 1) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                testLLD.addLast(randVal);
                testArD.addLast(randVal);
                //System.out.println("addLast(" + randVal + ")");
                System.out.println("ard.addLast(" + randVal + ");");
            } else if (operationNumber == 2) {
                // size
                int size_LLD = testLLD.size();
                int size_ArD = testArD.size();
                assertEquals(size_LLD, size_ArD);
                //System.out.println("size: " + size_LLD);
            } else if (testLLD.size() > 0) {
                //removeLast
                if (operationNumber == 3) {
                    int rmlast_LLD = (int) testLLD.removeLast();
                    int rmlast_ArD = (int) testArD.removeLast();
                    assertEquals(rmlast_LLD, rmlast_ArD);
                    //System.out.println("removeLast(" + rmlast_LLD + ")");
                    System.out.println("ard.removeLast();");
                }
                //removeFirst
                if (operationNumber == 4) {
                    int last_LLD = (int) testLLD.removeFirst();
                    int last_ArD = (int) testArD.removeFirst();
                    assertEquals(last_LLD, last_ArD);
                    //System.out.println("removeFirst(" + last_LLD + ")");
                    System.out.println("ard.removeFirst();");
                }
            }
        }
    }


    @Test
    public void oooo() {
        assertEquals(null, null);
    }
}
