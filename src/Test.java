import java.util.Arrays;


public class Test {
	double[] d=new double[2];
	void dd(double[] dd){
		dd[0]=1;
		dd[1]=2;
		}

	public static void main(String[] args) {
		Test t=new Test();
		t.dd(t.d);
		System.out.println(Arrays.toString(t.d));

	}

}
