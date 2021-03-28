package refAnn;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.NavigableSet;
import java.util.TreeSet;

public class B {



    private static int touchB = 0;
    private static int touchA = 0;

    public static void start(Object a) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = a.getClass().getDeclaredMethods();
        TreeSet<Integer> set = new TreeSet<>();
        for (Method m : methods) {
            if (m.getAnnotation(MyAnnot.class) != null) {
                set.add(m.getAnnotation(MyAnnot.class).value());
            }
        }
        NavigableSet<Integer> navSet = set.descendingSet();
        for (Method o : methods) {
            if (o.getAnnotation(Test.class) != null) {
                if (o.getAnnotation(BeforeSuit.class) != null) {
                    if (touchB != 0) {
                        throw new RuntimeException();
                    }
                    o.invoke(a);
                    touchB++;
                    for (Integer s : navSet) {
                        for (int i = 0; i < methods.length; i++) {
                            if (methods[i].getAnnotation(MyAnnot.class) != null) {
                                if (methods[i].getAnnotation(MyAnnot.class).value() == s) {
                                    methods[i].invoke(a);
                                }
                            }
                        }
                    }
                    for (Method m:methods) {
                        if (m.getAnnotation(AfterSuit.class) != null) {
                            if (touchA != 0) {
                                throw new RuntimeException();
                            }
                            m.invoke(a);
                            touchA++;
                        }
                    }
                }
            }
        }
    }
}





