package cases.mytest;

import cases.TestUtil;

public class invokeSpecialTest extends Father {

    @Override
    public void eat() {
        TestUtil.reach(1);
        super.eat();
    }

    public static void main(String[] args) {
        invokeSpecialTest test = new invokeSpecialTest();
        test.eat();
    }
}
