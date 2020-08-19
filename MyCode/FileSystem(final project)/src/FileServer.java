import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.*;

public class FileServer {
	
	// �洢�ļ���Ϣ
	public static volatile Map<String, Map> clientFileMap=null;
	// �洢�ڵ���Ϣ
	public static volatile Map<String, Map> storageNodeMap=null;
	
	public static NodeDisplay nd;
	
	public static FileDisplay fd;
		
	public FileServer() {
				
		try{
			File file1=new File("clientFile");
			file1.createNewFile();
			File file2=new File("storageNode");
			file2.createNewFile();
			
			if(file1.length()!=0) {
				ObjectInputStream ois=
						new ObjectInputStream(new FileInputStream("clientFile"));
				FileServer.clientFileMap=(Map)ois.readObject();
				ois.close();
			}
			else {
				FileServer.clientFileMap=new HashMap();
			}
			if(file2.length()!=0) {
				ObjectInputStream ois=new ObjectInputStream(new FileInputStream("storageNode"));
				FileServer.storageNodeMap=(Map)ois.readObject();
				ois.close();
			}
			else {
				
				FileServer.storageNodeMap=new HashMap();
			}
			
			nd=new NodeDisplay(FileServer.storageNodeMap);
			fd=new FileDisplay(FileServer.clientFileMap);
	
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub	
		try {		
			//��ȡ�ļ��н�㼰�ļ�����Ϣ
			new FileServer();
			new Thread() {
				public void run() {
					Timer timer=new Timer();
					TimerTask timerTask=new TimerTask() {
						public void run() {
							try{
								nd.doFlush(FileServer.storageNodeMap);	
								fd.doFlush(FileServer.clientFileMap);
								
								/*�������ر�ʱ����Ϣ���л����ļ���*/	
								ObjectOutputStream oos = 
										new ObjectOutputStream(new FileOutputStream("clientFile"));
								//��ʹmap==null,�ļ���СҲ��Ϊ0
								oos.writeObject(clientFileMap);
								oos.close();
								oos=new ObjectOutputStream(new FileOutputStream("storageNode"));
								oos.writeObject(storageNodeMap);
								oos.close();
							} 
							catch(Exception e) {
								e.printStackTrace();
							}
						}
					};
					timer.schedule(timerTask, 1000,9000);
				}
			}.start();		
			
			//����ͻ�������
			new Thread(){
				public void run() {
					new FileCliServer(8800,new ThreadPoolSupport(new FileProtocol()));
				}
			}.start();
 						
			//����ڵ������
			new Thread() {
				public void run() {
					new FileStoServer(8900,new StoThreadPoolSupport(new FileStoProtocol()));
				}
			}.start();
				
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
