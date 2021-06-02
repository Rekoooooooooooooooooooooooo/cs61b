package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
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
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        AList Ns = new AList<Integer>();
        int[] add2Ns = new int[]{1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000};
        for (int i = 0; i < add2Ns.length; i++) {
            Ns.addLast(add2Ns[i]);
        }
        AList times = new AList<Double>();
        AList opCounts = new AList<Integer>();
        for (int i = 0; i < Ns.size(); i++) {
            int N = (int) Ns.get(i);
            int opCount = 0;
            AList testAList = new AList<Integer>();
            Stopwatch sw = new Stopwatch();
            for (int j = 0; j < N; j++) {
                testAList.addLast(1);
                opCount++;
            }
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
            opCounts.addLast(opCount);
        }
        printTimingTable(Ns, times, opCounts);
    }
}
