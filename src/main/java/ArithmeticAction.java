import java.util.Arrays;
import java.util.TreeSet;

public class ArithmeticAction {

    public int sum(int a, int b) {
        return a + b;
    }

    public int dif(int a, int b) {
        return a - b;
    }

    public int div(int a, int b) {
        return a / b;
    }

    public int arraySize(int [] arr) {
        return arr.length;
    }

    public int arrayElementSum(int [] arr) {
        return arr[0] + arr[2];
    }

    public TreeSet<String> sort(String ... str) {
        return new TreeSet<>(Arrays.asList(str));
    }
}