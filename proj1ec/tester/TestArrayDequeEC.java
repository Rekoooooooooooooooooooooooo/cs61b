package tester;
import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {

    @Test
    public void randomizedTest() {
        StudentArrayDeque<Integer>  SAD = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer>  ADS = new ArrayDequeSolution<>();
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
                assertEquals("size: " + size_SAD, size_SAD, size_ADS);
                System.out.println("size()");
            } else if (SAD.size() > 0) {
                //removeLast
                if (operationNumber == 3 && !ADS.isEmpty() && !SAD.isEmpty()) {
                    Integer rmlast_SAD = (int) SAD.removeLast();
                    Integer rmlast_ADS = (int) ADS.removeLast();
                    assertEquals("removeLast(" + rmlast_SAD + ")", rmlast_SAD, rmlast_ADS);
                    System.out.println("removeLast()");
                }
                //removeFirst
                if (operationNumber == 4 && !ADS.isEmpty() && !SAD.isEmpty()) {
                    Integer last_SAD = (int) SAD.removeFirst();
                    Integer last_ADS = (int) ADS.removeFirst();
                    assertEquals("removeFirst(" + last_SAD + ")", last_SAD, last_ADS);
                    System.out.println("removeFirst()");
                }
            }
        }
    }
}
