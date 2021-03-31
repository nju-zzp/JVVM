package cases.mytest;

import cases.TestUtil;
import cases.mytest.Father;
import cases.mytest.Son;

public class InstanceOfTest {
    public static void main(String[] args) {
        Son son = new Son();
        if (son instanceof Father) {
            TestUtil.reach(1);
        } else {
            TestUtil.reach(0);
        }
    }
}
