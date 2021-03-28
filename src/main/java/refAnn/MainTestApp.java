package refAnn;

import java.lang.reflect.InvocationTargetException;

public class MainTestApp {


    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        aTest a=new aTest();
        B.start(a);
    }
}
