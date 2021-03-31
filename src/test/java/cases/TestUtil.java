package cases;

/**
 * Description:
 *
 * @author xxz
 * Created on 07/01/2020
 */
public class TestUtil {
    public static void reach(int x){
        System.out.println(x);
    }

    public static boolean equalInt(int a, int b) {
        if(a==b){
            return true;
        }else{
            throw new RuntimeException(a+"!="+b);
        }
    }

    public static boolean equalFloat(float a, float b){
        if (Math.abs(a - b) < 1e-5) {
            return true;
        } else {
            throw new RuntimeException(a+"!="+b);
        }
    }
}
