import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class FileStoProtocol implements IOStoStrategy{
	
	public void serviceNode(DatagramPacket packet) {
		String information=Tool.toStringFromUnicode(packet.getData());
		String tempInfo=information.trim();
		if(information.startsWith("0"))  //����ע��
		{		
			tempInfo=tempInfo.substring(information.indexOf(" ")+1);
			String nodeName=tempInfo.substring(0,tempInfo.indexOf(" "));
			tempInfo=tempInfo.substring(tempInfo.indexOf(" ")+1);
			String nodeIP=tempInfo.substring(0,tempInfo.indexOf(" ")); 
			tempInfo=tempInfo.substring(tempInfo.indexOf(" ")+1);
			String nodePort=tempInfo.substring(0,tempInfo.indexOf(" "));
			tempInfo=tempInfo.substring(tempInfo.indexOf(" ")+1);
			String volume=tempInfo.substring(0,tempInfo.indexOf(" "));
			tempInfo=tempInfo.substring(tempInfo.indexOf(" ")+1);
			String currentVolume=tempInfo.substring(0,tempInfo.indexOf(" "));
			tempInfo=tempInfo.substring(tempInfo.indexOf(" ")+1);
			String fileNumber=tempInfo;
			
			Map map=new HashMap();
			map.put("Flag", "1");//����
			map.put("NodeIP", nodeIP);
			map.put("NodePort", nodePort);
			map.put("Volume", volume);
			map.put("CurrentVolume", currentVolume);
			map.put("FileNumber", fileNumber);
			long tempVolume=StorageNode.dealWithVolume(volume);
			long tempCurrentVolume=StorageNode.dealWithVolume(currentVolume);
			long usedVolume=tempVolume-tempCurrentVolume;
			map.put("UsedVolume", usedVolume+"");
			map.put("Dangerous", "0");
			
			FileServer.storageNodeMap.put(nodeName,map);
			System.out.println("�ڵ�"+nodeName+"������......");
			
			//ÿ��һ���ڵ㣬��Ϊ�ýڵ��һ����ʱ����ʱ��˵���ô洢�ڵ��Ѿ�����
			new Thread(new CountTimer(nodeName)).start();
		}
		else if(information.startsWith("1")) {
			
			tempInfo=tempInfo.substring(tempInfo.indexOf(" ")+1);
			String nodeName=tempInfo.substring(0,tempInfo.indexOf(" "));
			tempInfo=tempInfo.substring(tempInfo.indexOf(" ")+1);
			String currentVolume=tempInfo.substring(0,tempInfo.indexOf(" "));
			tempInfo=tempInfo.substring(tempInfo.indexOf(" ")+1);
			String fileNumber=tempInfo;
			
			Map map=FileServer.storageNodeMap.get(nodeName);  
			map.put("flag", "1");
			map.put("CurrentVolume",currentVolume);		
			long volume=StorageNode.dealWithVolume((String)map.get("Volume"));
			long usedVolume=volume-StorageNode.dealWithVolume(currentVolume);
			map.put("UsedVolume", usedVolume);
			map.put("FileNumber", fileNumber);
			map.put("Dangerous", "0");
			
			FileServer.storageNodeMap.put(nodeName,map);
		}
	}
}
