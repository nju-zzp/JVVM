package cases.mytest;

public  class Test1 {

    private int age = 2020;

    @Override
    public String toString () {
        return String.valueOf(age);
    }

    public static void main(String[] args) {
        Test1 test1 = new Test1();
        test1.toString();
    }

}
