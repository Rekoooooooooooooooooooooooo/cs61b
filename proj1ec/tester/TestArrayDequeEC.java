package tester;
import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {

    @Test
    public void randomizedTest() {
        StudentArrayDeque  SAD = new StudentArrayDeque<Integer>();
        ArrayDequeSolution  ADS = new ArrayDequeSolution<Integer>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 5);
            if (operationNumber == 0) {
                // addFirst
                int randVal = StdRandom.uniform(0, 100);
                SAD.addFirst(randVal);
                ADS.addFirst(randVal);
                System.out.println("addFirst(" + randVal + ");");
            } else if (operationNumber == 1) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                SAD.addLast(randVal);
                ADS.addLast(randVal);
                System.out.println("addLast(" + randVal + ");");
            } else if (operationNumber == 2) {
                // size
                int size_SAD = SAD.size();
                int size_ADS = ADS.size();
                assertEquals(size_SAD, size_ADS);
                System.out.println("size: " + size_SAD);
            } else if (SAD.size() > 0) {
                //removeLast
                if (operationNumber == 3) {
                    int rmlast_SAD = (int) SAD.removeLast();
                    int rmlast_ADS = (int) ADS.removeLast();
                    assertEquals("Oh noooo!\nThis is bad:\n   SAD " + rmlast_SAD
                            + " not equal to ADS" + rmlast_ADS + "!", rmlast_SAD, rmlast_ADS);
                    System.out.println("removeLast(" + rmlast_SAD + ")");
                }
                //removeFirst
                if (operationNumber == 4) {
                    int last_SAD = (int) SAD.removeFirst();
                    int last_ADS = (int) ADS.removeFirst();
                    assertEquals("Oh noooo!\nThis is bad:\n   SAD " + last_SAD
                            + " not equal to ADS" + last_ADS + "!", last_SAD, last_ADS);
                    System.out.println("removeFirst(" + last_SAD + ")");
                }
            }
        }
    }
}
