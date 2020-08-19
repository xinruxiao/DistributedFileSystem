package network;
import java.io.*;
import java.net.*;

public class RemoteCalc implements Calc {
	private DataInputStream dis = null;
	private DataOutputStream dos = null;

	public RemoteCalc(String host, int port) throws Exception {
		Socket s = new Socket(host, port); // 这个Socket对象创建完毕后何时销毁？
		dis = new DataInputStream(s.getInputStream());
		dos = new DataOutputStream(s.getOutputStream());
	}

	public int add(int i, int j) {
		try {
			dos.writeInt(1); // 将加法操作映射成命令1，然后发送命令
			dos.writeInt(i); // 发送加法操作第一个操作数
			dos.writeInt(j); // 发送加法操作第二个操作数
			dos.flush();
			return dis.readInt();
		} catch (Exception e) { // 将检查异常转换为运行异常并抛出
			throw new ArithmeticException(e.getMessage());
		}
	}

	public int sub(int i, int j) {
		try {
			dos.writeInt(2); // 将加法操作映射成命令2，然后发送命令
			dos.writeInt(i); // 发送减法操作第一个操作数
			dos.writeInt(j); // 发送减法操作第二个操作数
			dos.flush();
			return dis.readInt();
		} catch (Exception e) {
			throw new ArithmeticException(e.getMessage());
		}
	}
}
