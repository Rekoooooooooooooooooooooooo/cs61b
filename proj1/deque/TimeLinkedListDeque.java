package deque;

import edu.princeton.cs.algs4.Stopwatch;

public class TimeLinkedListDeque {
    private static void printTimingTable(ArrayDeque<Integer> Ns, ArrayDeque<Double> times, ArrayDeque<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeArrayDequeConstruction();
    }

    public static void timeArrayDequeConstruction() {
        ArrayDeque Ns = new ArrayDeque<Integer>();
        int[] add2Ns = new int[]{128000, 256000, 512000, 1024000, 2048000, 4096000, 8192000};
        for (int i = 0; i < add2Ns.length; i++) {
            Ns.addLast(add2Ns[i]);
        }
        ArrayDeque times = new ArrayDeque<Double>();
        ArrayDeque opCounts = new ArrayDeque<Integer>();
        for (int i = 0; i < Ns.size(); i++) {
            int N = (int) Ns.get(i);
            int opCount = 0;
            ArrayDeque testArrayDeque = new ArrayDeque<Integer>();
            Stopwatch sw = new Stopwatch();
            for (int j = 0; j < N; j++) {
                testArrayDeque.addLast(1);
                opCount++;
            }
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
            opCounts.addLast(opCount);
        }
        printTimingTable(Ns, times, opCounts);
    }
}
