package cryptonalysis;

public class MA {
    private static int module;

    public MA(int module) {
        this.module = module;
    }

    public static int add(int a, int b) {
        return (a + b) % module;
    }

    public static int mult(int a, int b) {
        return (a * b) % module;
    }
}
