package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
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
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        AList Ns = new AList<Integer>();
        int[] add2Ns = new int[]{1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000};
        for (int i = 0; i < add2Ns.length; i++) { Ns.addLast(add2Ns[i]); }
        AList opCounts = new AList<Integer>();
        int opCount = 10000;
        for (int i = 0; i < add2Ns.length; i++) { opCounts.addLast(opCount); }
        AList times = new AList<Double>();
        for (int i = 0; i < Ns.size(); i++) {
            SLList testSLList = new SLList<Integer>();
            int N = (int) Ns.get(i);
            for (int j = 0; j < N; j++) { testSLList.addLast(1); }
            Stopwatch sw = new Stopwatch();
            for (int k = 0; k < opCount; k++) { testSLList.getLast(); }
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
        }
        printTimingTable(Ns, times, opCounts);
    }

}
