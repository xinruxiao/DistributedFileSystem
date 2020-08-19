package network;
import java.io.*;
import java.net.*;

public class CalcProtocol implements IOStrategy { // CalcProtocol.java
	
	Calc c = new CalcImpl(); // ��������ת�Ƶ�service�����л���Σ�

	public void service(Socket socket) { // ��������ֻ��һ��Э�������ô
		try { // ����������Ҳֻ��һ�������̹߳���ġ�
			// �罫�������˵�Calc����Ĵ��������ڷ����ڣ������ʲô���ı仯��
			// Calc c = new CalcImpl();
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			DataOutputStream dos = new DataOutputStream(
					socket.getOutputStream());
			int command = 0;
			while (true) {
				command = dis.readInt(); // ʵ���ϣ�Э���������ֵ
				switch (command) { // û�ж�������
				case 1: // ����1ӳ�䵽add����
					dos.writeInt(c.add(dis.readInt(), dis.readInt()));
					dos.flush();
					break;
				case 2: // ����2ӳ�䵽sub����
					dos.writeInt(c.sub(dis.readInt(), dis.readInt()));
					dos.flush();
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("client disconnected.");
		}
	}
}
