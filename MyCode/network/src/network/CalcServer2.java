package network;
public class CalcServer2 { // CalcServer2.java��������������
	public static void main(String[] args) {
		new NwServer(4321, new ThreadPoolSupport(new CalcProtocol()));
	}
}
