package cases.mytest;

import cases.TestUtil;

public class Son extends Father {
    private int age;

    @Override
    public void eat() {
        age++;
    }

    public static void main(String[] args) {
        Father person = new Son();
        person.eat();
        TestUtil.reach(1);
    }
}
