import java.io.*;
import java.util.*;
import java.net.*;

public class StorageNode {
	
	/*节点基本参数*/
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
		if(flag) {//增加文件
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
			//创建根文件夹
			new File(rootFolder).mkdir();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
 	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{		
			//启动存储节点时读取配置文件
			Properties p=new Properties();
			//该参数运行时自己输入，配置文件在执行文件所在的文件夹
			p.load(new FileInputStream(args[0]));   
			
			//注册本节点
			StorageNode sn=new StorageNode(p);
			String information="0 "+sn.getNodeName()+" "+sn.getNodeIP()+" "
					+sn.getNodePort()+" "+sn.getVolume()+" "+sn.getCurrentVolume()+" "+sn.getFileNumber();
			System.out.println("注册信息："+information);
			byte[] sendData=Tool.getUnicodeBytes(information);
			DatagramSocket da=new DatagramSocket();
			DatagramPacket packet = 
					new DatagramPacket(sendData, sendData.length);
			packet.setSocketAddress(new InetSocketAddress(InetAddress.getByName(sn.getFileServerIP()), 8900));
			da.send(packet);
			da.close();
			
			//每隔一段固定的时间告诉服务器本节点的存在
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
					//作为节点服务器，处理客户的请求
					new StorageNodeServer(Integer.parseInt(StorageNode.getNodePort()),new ThreadPoolSupport(new NodeProtocol()));			
				}
			}.start();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
