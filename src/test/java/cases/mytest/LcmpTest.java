package cases.mytest;

import cases.TestUtil;

public class LcmpTest {
    private static void lcmp (long v1, long v2) {
        if (v1 > v2) {
            TestUtil.reach(1);
        } else if (v1 == v2){
            TestUtil.reach(0);
        } else {
            TestUtil.reach(-1);
        }
    }

    public static void main(String[] args) {
        lcmp(1L, 0L);
        lcmp(1L, 1L);
        lcmp(1L, 2L);
    }
}
