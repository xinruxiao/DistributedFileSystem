package network;
public class CalcServer { // CalcServer.java��������������
	public static void main(String[] args) {
		new NwServer(4321, new ThreadSupport(new CalcProtocol()));
	}
}
