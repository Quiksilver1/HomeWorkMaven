package refAnn;

public class aTest {

    public A a;
    public boolean equ=false;


    /* public static void main(String[] args) {
         sum();
     }*/
    @MyAnnot(value = 8)
    @Test
    public void sum() {
        boolean equ=false;
        int result = A.sum(5, 6);
        if (result == 11) {
            result = A.sum(-5, 6);
            if (result == 1) {
                result = A.sum(5, -6);
                if (result == -1) {
                    result = A.sum(-5, -6);
                    if (result == -11) {
                        equ = true;
                    }
                }
            }
        }
        if(equ){
            System.out.println("Test sum ok");
        }else {
            System.out.println("Test sum fail");
        }
    }


    @MyAnnot(value = 7)
    @Test
    public void dif() {
        boolean equ=false;
        int result = A.dif(9, 6);
        if (result == 3) {
            result = A.dif(-9, 6);
            if (result == -15) {
                result = A.dif(9, -6);
                if (result == 15) {
                    result = A.dif(-9, -6);
                    if (result == -3) {
                        equ = true;
                    }
                }
            }
        }
        if(equ){
            System.out.println("Test dif ok");
        }else {
            System.out.println("Test dif fail");
        }
    }


    @MyAnnot(value = 6)
    @Test
    public void multi() {
        boolean equ=false;
        int result = A.multi(4, 2);
        if (result == 8) {
            result = A.multi(6, 6);
            if (result == 36) {
                result = A.multi(6, 0);
                if (result == 0) {
                    result = A.multi(0, 6);
                    if (result == 0) {
                        equ = true;
                    }
                }
            }
        }
        if(equ){
            System.out.println("Test multi ok");
        }else {
            System.out.println("Test multi fail");
        }
    }

    @BeforeSuit
    @Test
    public void foo() {
        System.out.println("before all");
    }

    @AfterSuit
    @Test
    public void bar() {
        System.out.println("after all");
    }
}
