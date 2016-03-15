
public class test {

	public static void main(String sssss[]){
		A a = new test().new A();
		a.a = "a";
		String b = "a";
		turn(a,b);
		System.out.println(a.a+"\n"+b);
		
	}
	public static void turn(A a,String b){
		a = new test().new A();
		a.a = "ccc";
		b = "ccc";
	}
	class A {
		String a ;
	}
}
