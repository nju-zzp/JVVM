package cases.mytest;

import cases.TestUtil;

public class MultianewarrayTest {
    private static int[][][] threeDimensionIntArray = new int[2][3][4];

    static {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 4; k++) {
                    threeDimensionIntArray[i][j][k] = i*12 + j*4 + k;
                }
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 4; k++) {
                    TestUtil.reach(threeDimensionIntArray[i][j][k]);
                }
            }
        }
    }
}
