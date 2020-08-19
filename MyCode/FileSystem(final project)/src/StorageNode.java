import java.io.*;
import java.util.*;
import java.net.*;

public class StorageNode {
	
	/*�ڵ��������*/
	private static String nodeName;
	private static String nodeIP;
	private static String nodePort;
	private static String rootFolder;
	private static String volume;
	private static String currentVolume;
	private static String fileServerIP;
	private static String fileServerPort;
	private static String fileNumber;
	
	public static String getNodeName() {
		return nodeName;
	}
	public static String getNodeIP() {
		return nodeIP;
	}
	public static String getNodePort() {
		return nodePort;
	}
	public static String getRootFolder(){
		return rootFolder;
	}
	public static String getVolume() {
		return volume;
	}
	public static String getFileServerIP() {
		return fileServerIP;
	}
	public static String getFileServerPort() {
		return fileServerPort;
	}
	public static String getCurrentVolume() {
		return currentVolume;
	}
	public static String getFileNumber() {
		return fileNumber;
	}
	public static void setCurrentVolume(long fileLength,boolean flag) {
		if(flag) {//�����ļ�
			long tempLength=dealWithVolume(StorageNode.currentVolume);
			StorageNode.currentVolume=(tempLength-fileLength)+"";
			StorageNode.fileNumber=(Integer.parseInt(StorageNode.fileNumber)+1)+"";
		}
		else {
			long tempLength=dealWithVolume(StorageNode.currentVolume);
			StorageNode.currentVolume=(tempLength+fileLength)+"";
			StorageNode.fileNumber=(Integer.parseInt(StorageNode.fileNumber)-1)+"";
		}
	}
	
	public static long dealWithVolume(String tempVolume) {
		if(tempVolume.endsWith("GB")) {
			tempVolume=tempVolume.substring(0, tempVolume.indexOf("GB"));
			return Long.parseLong(tempVolume)*(1024*1024*1024);
		}
		else if(tempVolume.endsWith("MB")) {
			tempVolume=tempVolume.substring(0, tempVolume.indexOf("MB"));
			return Long.parseLong(tempVolume)*(1024*1024);
		}
		else if(tempVolume.endsWith("KB")){
			tempVolume=tempVolume.substring(0, tempVolume.indexOf("KB"));
			return Long.parseLong(tempVolume)*1024;
		}
		else {
			return Long.parseLong(tempVolume);
		}
	}
		
 	public StorageNode(Properties p) {
		try {
			nodeName=p.getProperty("NodeName");
			nodeIP=p.getProperty("NodeIP");
			nodePort=p.getProperty("NodePort");
			rootFolder=p.getProperty("RootFolder");
			volume=p.getProperty("Volume");
			fileServerIP=p.getProperty("FileServerIP");
			fileServerPort=p.getProperty("FileServerPort");
			currentVolume=p.getProperty("CurrentVolume");
			fileNumber="0";
			//�������ļ���
			new File(rootFolder).mkdir();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
 	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{		
			//�����洢�ڵ�ʱ��ȡ�����ļ�
			Properties p=new Properties();
			//�ò�������ʱ�Լ����룬�����ļ���ִ���ļ����ڵ��ļ���
			p.load(new FileInputStream(args[0]));   
			
			//ע�᱾�ڵ�
			StorageNode sn=new StorageNode(p);
			String information="0 "+sn.getNodeName()+" "+sn.getNodeIP()+" "
					+sn.getNodePort()+" "+sn.getVolume()+" "+sn.getCurrentVolume()+" "+sn.getFileNumber();
			System.out.println("ע����Ϣ��"+information);
			byte[] sendData=Tool.getUnicodeBytes(information);
			DatagramSocket da=new DatagramSocket();
			DatagramPacket packet = 
					new DatagramPacket(sendData, sendData.length);
			packet.setSocketAddress(new InetSocketAddress(InetAddress.getByName(sn.getFileServerIP()), 8900));
			da.send(packet);
			da.close();
			
			//ÿ��һ�ι̶���ʱ����߷��������ڵ�Ĵ���
			new Thread(){
				public void run() {
					Timer timer=new Timer();
					TimerTask timerTask=new TimerTask() {
						public void run() {
							try{		
								String information="1"+" "+StorageNode.getNodeName()
									+" "+StorageNode.getCurrentVolume()+" "+StorageNode.getFileNumber();
								byte[] sendData=Tool.getUnicodeBytes(information);
								DatagramSocket da=new DatagramSocket();
								DatagramPacket packet = 
										new DatagramPacket(sendData, sendData.length);
								packet.setSocketAddress(new InetSocketAddress(InetAddress.getByName(StorageNode.getFileServerIP()), 8900));
								da.send(packet);
								
								da.close();
								
							} catch(Exception e) {
								e.printStackTrace();
							}
						}
					};
					timer.schedule(timerTask, 1000,3000);
				}
			}.start();
			
			new Thread() {
				public void run() {
					//��Ϊ�ڵ������������ͻ�������
					new StorageNodeServer(Integer.parseInt(StorageNode.getNodePort()),new ThreadPoolSupport(new NodeProtocol()));			
				}
			}.start();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
