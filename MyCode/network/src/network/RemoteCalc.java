package network;
import java.io.*;
import java.net.*;

public class RemoteCalc implements Calc {
	private DataInputStream dis = null;
	private DataOutputStream dos = null;

	public RemoteCalc(String host, int port) throws Exception {
		Socket s = new Socket(host, port); // ���Socket���󴴽���Ϻ��ʱ���٣�
		dis = new DataInputStream(s.getInputStream());
		dos = new DataOutputStream(s.getOutputStream());
	}

	public int add(int i, int j) {
		try {
			dos.writeInt(1); // ���ӷ�����ӳ�������1��Ȼ��������
			dos.writeInt(i); // ���ͼӷ�������һ��������
			dos.writeInt(j); // ���ͼӷ������ڶ���������
			dos.flush();
			return dis.readInt();
		} catch (Exception e) { // ������쳣ת��Ϊ�����쳣���׳�
			throw new ArithmeticException(e.getMessage());
		}
	}

	public int sub(int i, int j) {
		try {
			dos.writeInt(2); // ���ӷ�����ӳ�������2��Ȼ��������
			dos.writeInt(i); // ���ͼ���������һ��������
			dos.writeInt(j); // ���ͼ��������ڶ���������
			dos.flush();
			return dis.readInt();
		} catch (Exception e) {
			throw new ArithmeticException(e.getMessage());
		}
	}
}
