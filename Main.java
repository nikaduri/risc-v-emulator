import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) {
        testSum();
        testFactorial();
        testStoreLoad();
        testFibonacci();
        testEcall();
//        test(args); // Uncomment this line if you want to test your own file
    }


    private static void testSum() {
        // Initially this should sum numbers from 1 to whatever is written
        // in x11 in the .s file
        System.out.println("Testing Sum:\n");
        RiscEmulator em = new RiscEmulator("sum_iter.s"); // If it doesn't work put in the whole path of the file
        em.executeCode();
        System.out.println("Result: " + em.registerValue(10) + ", excepted " + calculateSum() + "\n\n"); // x10 register stores the value
    }

    private static void testFactorial() {
        // This test recursively calculates factorial of whatever you
        // write in x11 in the .s file.
        System.out.println("Testing Factorial:\n");
        RiscEmulator em = new RiscEmulator("recursive_factorial.s"); // If it doesn't work put in the whole path of the file
        em.executeCode();
        System.out.println("Result: " + em.registerValue(10) + ", excepted " + calculateFactorial() + "\n\n"); // x10 register stores the value
    }

    private static void testStoreLoad() {
        System.out.println("Testing STORE/LOAD:\n");
        RiscEmulator em = new RiscEmulator("store_load.s");
        em.executeCode();
        System.out.println("x11 Register: " + em.registerValue(11) + ", expected 22136");
        System.out.println("x12 Register: " + em.registerValue(12) + ", expected 4660");
        System.out.println("x13 Register: " + em.registerValue(13) + ", expected 120");
        System.out.println("x14 Register: " + em.registerValue(14) + ", expected 86");
        System.out.println("x15 Register: " + em.registerValue(15) + ", expected 52");
        System.out.println("x16 Register: " + em.registerValue(16) + ", expected 18" + "\n\n");
    }

    private static void testFibonacci() {
        // This test recursively calculates nth fibonacci number
        // (n should be in the x11 register initially)
        System.out.println("Testing Fibonacci:\n");
        RiscEmulator em = new RiscEmulator("fib.s");
        em.executeCode();
        System.out.println("Result: " + em.registerValue(10) + ", excepted " + calculateFibonacci() + "\n\n");
    }

    // Test for ecall print_int, print_char and exit instructions.
    private static void testEcall() {
        System.out.println("Testing ecall:\n");
        RiscEmulator em = new RiscEmulator("abc.s");
        em.executeCode();
        System.out.println("\nExcepted String: " + "abcdefghijklmnopqrstuvwxyz0123456789\n");
    }

    // If you want to test your own file you can do it by passing the filename as an argument
    // and calling this function.
    private static void test(String[] args) {
        RiscEmulator em = new RiscEmulator(args[0]);
        em.executeCode();
        System.out.println("Result: " + em.registerValue(10)); // x10 register stores the value
    }


    ////////////////////////////////////////////////////////////////////////////////////////////
    //////// These methods are just calculating the test results in Java for comparison ////////
    ////////////////////////////////////////////////////////////////////////////////////////////
    private static int calculateSum() {
        String fileName = "sum_iter.s";
        int n = getNumber(fileName);
        int sum = 0;
        for (int i = 0; i <= n; i++) {
            sum += i;
        }
        return sum;
    }

    private static int calculateFactorial() {
        String fileName = "recursive_factorial.s";
        int n = getNumber(fileName);
        int ans = 1;
        for (int i = 1; i <= n; i++) {
            ans *= i;
        }
        return ans;
    }

    private static int calculateFibonacci() {
        String fileName = "fib.s";
        int n = getNumber(fileName);
        return fibonacciHelper(n);
    }

    private static int fibonacciHelper(int n) {
        if (n == 0) {
            return 0;
        } else if (n == 1) {
            return 1;
        }
        return fibonacciHelper(n - 1) + fibonacciHelper(n - 2);
    }


    private static int getNumber(String fileName) {
        int n = 0;
        try {
            BufferedReader rd = new BufferedReader(new FileReader(fileName));
            String line = "";
            while ((line = rd.readLine()) != null) {
                if (line.contains("li") && line.contains("x11")) {
                    break;
                }
            }
            StringTokenizer tok = new StringTokenizer(line, ", ");
            String num = "";
            for (int i = 0; i < 3; i++) {
                num = tok.nextToken();
            }
            return Integer.parseInt(num);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
