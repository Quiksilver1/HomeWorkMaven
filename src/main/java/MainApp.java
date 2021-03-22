import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;


public class MainApp {
    private static final Logger LOGGER = LogManager.getLogger(MainApp.class);

    private static int a = 1;
    private static int b = 4;

    public static Integer[] resortArr(Integer[] arr, int b) {
        ArrayList<Integer> list = new ArrayList<>();
        int ind = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == b) {
                ind = i;
            }
            if ((i == arr.length - 1) && ind == -1) {
                throw new RuntimeException();
            }
        }
        for (int i = ind + 1; i < arr.length; i++) {
            list.add(arr[i]);
        }

        Integer[] arr2 = list.toArray(new Integer[list.size()]);

        return arr2;
    }

    public static boolean checkInclude(Integer[] arr, int a, int b) {
        boolean haveA = false;
        boolean haveB = false;
        for (Integer i : arr) {
            if (i == a) {
                haveA = true;
            } else {
                if (i == b) {
                    haveB = true;
                } else {
                    throw new RuntimeException();
                }
            }
        }
        if (haveA && haveB) {
            return true;
        }
        return false;
    }
}

