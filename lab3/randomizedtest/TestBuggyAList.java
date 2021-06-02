package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
      int[] items = {4, 5, 6};
      AListNoResizing testAListNoRs = new AListNoResizing<Integer>();
      BuggyAList testBgAList = new BuggyAList<Integer>();
      for (int i = 0; i < items.length; i++) {
        testAListNoRs.addLast(items[i]);
        testBgAList.addLast(items[i]);
      }
      for (int i = 0; i < items.length; i++){ assertTrue(testAListNoRs.removeLast() == testBgAList.removeLast()); }
    }

    @Test
    public void randomizedTest() {
      AListNoResizing testAListNoRs = new AListNoResizing<Integer>();
      BuggyAList testBgAList = new BuggyAList<Integer>();
      int N = 5000;
      for (int i = 0; i < N; i += 1) {
        int operationNumber = StdRandom.uniform(0, 4);
        if (operationNumber == 0) {
          // addLast
          int randVal = StdRandom.uniform(0, 100);
          testAListNoRs.addLast(randVal);
          testBgAList.addLast(randVal);
          System.out.println("addLast(" + randVal + ")");
        } else if (operationNumber == 1) {
          // size
          int size_NoRs = testAListNoRs.size();
          int size_bg = testBgAList.size();
          assertEquals(size_NoRs, size_bg);
          System.out.println("size: " + size_NoRs);
        } else if (testAListNoRs.size() > 0) {
          //removeLast
          if (operationNumber == 2) {
            int rmlast_NoRs = (int) testAListNoRs.removeLast();
            int rmlast_bg = (int) testBgAList.removeLast();
            assertEquals(rmlast_NoRs, rmlast_bg);
            System.out.println("removeLast(" + rmlast_NoRs + ")");
          }
          //getLast
          if (operationNumber == 3) {
            int last_NoRs = (int) testAListNoRs.getLast();
            int last_bg = (int) testBgAList.getLast();
            assertEquals(last_NoRs, last_bg);
            System.out.println("getLast(" + last_NoRs + ")");
          }
        }
      }
    }
}
