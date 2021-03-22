import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ArithmeticActionTest {

    public static ArithmeticAction action;
    static int i;

    @BeforeAll
    public static void init() {
        action = new ArithmeticAction();
        i = 0;
    }

    @BeforeEach
    public void foo() {
        System.out.println(i++);
    }

    @Test
    @Timeout(value = 5)
    public void sumTest() {
        int result = action.sum(7, 8);
        assertEquals(15, result);
        result = action.sum(-7, 8);
        assertEquals(1, result);
        result = action.sum(7, -8);
        assertEquals(-1, result);
        result = action.sum(-7, -8);
        assertEquals(-15, result);
    }

    @CsvSource({
            "7, 8, 15",
            "-7, 8, 1",
            "7, -8, -1",
            "-7, -8, -15",
    })
    @ParameterizedTest
    public void sumTestTwo(int a, int b, int result) {
        assertEquals(result, action.sum(a, b));
    }

    @ParameterizedTest
    @MethodSource("paramsForSumTestThree")
    public void sumTestThree(int a, int b, int result) {
        assertEquals(result, action.sum(a, b));
    }

    public static Stream<Arguments> paramsForSumTestThree() {
        List<Arguments> list = new ArrayList<>();
        list.add(Arguments.arguments(7, 8, 15));
        list.add(Arguments.arguments(-7, 8, 1));
        list.add(Arguments.arguments(7, -8, -1));
        list.add(Arguments.arguments(-7, -8, -15));
        return list.stream();
    }

    @Test
    public void difTest() {
        int result = action.dif(7, 8);
        assertEquals(-1, result);
        result = action.dif(-7, 8);
        assertEquals(-15, result);
        result = action.dif(7, -8);
        assertEquals(15, result);
        result = action.dif(-7, -8);
        assertEquals(1, result);
    }

    @Test
    public void divTest() {
        int result = action.div(16, 4);
        assertEquals(4, result);

        try {
            action.div(16, 0);
            fail("Должно выброситься исключение ArithmeticException");
        } catch (ArithmeticException ignored) {
        }

        try {
            action.div(16, 1);
        } catch (ArithmeticException ignored) {
            fail("Не Должно выброситься исключение");
        }

        assertThrows(ArithmeticException.class, () -> {
            action.div(16, 0);
        });

        assertDoesNotThrow(() -> {
            action.div(16, 5);
        });
    }

    @Test
    public void arraySizeTest() {
        int [] arr = new int[5];
        int result = action.arraySize(arr);
        assertEquals(5, result);
    }

    @Test
    public void arrayElementSumTest() {
        int [] arr = new int[5];
        arr[0] = 6;
        arr[2] = 7;
        assertNotNull(arr);
        assertEquals(5, arr.length);
        int result = action.arrayElementSum(arr);
        assertEquals(13, result);
    }

    @Test
    public void sortTest() {
        TreeSet<String> testSet = new TreeSet<>();
        testSet.add("A");
        testSet.add("B");
        testSet.add("C");
        testSet.add("D");
        TreeSet<String> set = action.sort("B", "D", "A", "C");
        assertNotNull(set);
        assertEquals(testSet, set);
    }

    @AfterEach
    public void bar() {
        System.out.println("A");
    }

    @AfterAll
    public static void destroy() {
        action = null;
    }
}