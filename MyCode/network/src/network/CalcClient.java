package network;
//最后，创建一个客户端调用主程序，为了比较，也调用了一个
//客户端的计算器的本地对象。
public class CalcClient { // CalcClient.java，客户端主程序
	public static void main(String[] args) throws Exception {
		Calc c1 = new CalcImpl(); // 客户端虚拟机内部的计算器对象
		Calc c2 = new RemoteCalc("localhost", 4321); // 远程对象的本地代理
		System.out.println("call local object:");
		System.out.println("5 + 6 = " + c1.add(5, 6));
		System.out.println("11 - 5 = " + c1.sub(11, 5));
		System.out.println("call remote object:");
		System.out.println("5 + 6 = " + c2.add(5, 6));
		System.out.println("11 - 5 = " + c2.sub(11, 5));
	}
}
