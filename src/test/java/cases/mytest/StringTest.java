package cases.mytest;

public class StringTest {
    private String name;

    public StringTest (String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        StringTest stringTest = new StringTest("zzp");
        stringTest.name.length();
    }
}
