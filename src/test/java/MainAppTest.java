import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class MainAppTest {

    public static MainApp mainApp;

    static int i;

    @Test
    public void resortArrTest() {

        Integer[] arr = {1, 2, 4, 4, 2, 3, 4, 1, 7};
        Integer[] arrRes = {1, 7};
        Integer[] ar1 = {1, 2, 4, 4, 2, 3, 1, 1, 7};
        Integer[] arrRes1 = {2, 3, 1, 1, 7};
        Integer[] ar2 = {1, 2, 4, 7, 2, 3, 7, 1, 7};
        Integer[] arrRes2 = {7, 2, 3, 7, 1, 7};
        Integer[] ar3 = {1, 2, 1, 1, 2, 3, 1, 1, 7};
        Integer[] arrRes3 = {2, 3, 1, 1, 7};
        assertNotNull(arr);

        try {
            mainApp.resortArr(arrRes3, 4);
            fail("Должно выброситься исключение RuntimeException");
        } catch (RuntimeException ignored) {
        }

        assertThrows(RuntimeException.class, () -> {
            mainApp.resortArr(arrRes3, 4);
        });

        assertArrayEquals(arrRes, mainApp.resortArr(arr, 4));
        assertArrayEquals(arrRes1, mainApp.resortArr(ar1, 4));
        assertArrayEquals(arrRes2, mainApp.resortArr(ar2, 4));
    }


    @Test
    public void checkIncludeTest() {
        boolean haveA = false;
        boolean haveB = false;
        int a=4;
        int b=1;
        Integer[] arr={1,1,1,4};
        assertNotNull(arr);
        assertEquals(true,MainApp.checkInclude(arr,a,b));
        Integer[] ar1={4,1,4,1};
        assertEquals(true,MainApp.checkInclude(arr,a,b));
        Integer[] ar2={1,1,1,1};
        assertEquals(true,MainApp.checkInclude(arr,a,b));
        Integer[] ar3={1,1,1,3};

        try {
            MainApp.checkInclude(ar3, 4,1);
            fail("Должно выброситься исключение RuntimeException");
        } catch (RuntimeException ignored) {
        }

        assertThrows(RuntimeException.class, () -> {
            MainApp.checkInclude(ar3, 4,1);;
        });
    }
}


