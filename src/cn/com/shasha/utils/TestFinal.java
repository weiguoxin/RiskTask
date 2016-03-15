package cn.com.shasha.utils;

/**
 * Created with IntelliJ IDEA.
 * User: IceKingNew
 * Date: 13-12-6
 * Time: ионГ10:03
 * To change this template use File | Settings | File Templates.
 */
public class TestFinal {
    private int test1 = 0;

    public void getTest1(int arg) {
        test1 = arg;
        for(int i=0; i<=1000; i++)
        {
            System.out.println(test1);
        }
    }
    public static void main(String[] args) {

        (new TestFinal()).getTest1(12);
        (new TestFinal()).getTest1(19);

    }
}
