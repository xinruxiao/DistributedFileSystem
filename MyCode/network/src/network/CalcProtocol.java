package network;
import java.io.*;
import java.net.*;

public class CalcProtocol implements IOStrategy { // CalcProtocol.java
	
	Calc c = new CalcImpl(); // 此语句如果转移到service方法中会如何？

	public void service(Socket socket) { // 本例子中只有一个协议对象，那么
		try { // 计算器对象也只有一个，多线程共享的。
			// 如将服务器端的Calc对象的创建定义在方法内，会产生什么样的变化？
			// Calc c = new CalcImpl();
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			DataOutputStream dos = new DataOutputStream(
					socket.getOutputStream());
			int command = 0;
			while (true) {
				command = dis.readInt(); // 实际上，协议命令的数值
				switch (command) { // 没有多大的意义
				case 1: // 命令1映射到add方法
					dos.writeInt(c.add(dis.readInt(), dis.readInt()));
					dos.flush();
					break;
				case 2: // 命令2映射到sub方法
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
