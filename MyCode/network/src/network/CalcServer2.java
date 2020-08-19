package network;
public class CalcServer2 { // CalcServer2.java，服务器主程序
	public static void main(String[] args) {
		new NwServer(4321, new ThreadPoolSupport(new CalcProtocol()));
	}
}
