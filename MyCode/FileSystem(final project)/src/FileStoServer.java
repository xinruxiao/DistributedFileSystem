import java.io.*;
import java.util.*;
import java.net.*;

public class FileStoServer{
	
	public FileStoServer(int port,IOStoStrategy ios) {	
		try {		
			DatagramSocket ds=new DatagramSocket(port);
			while(true) {
				byte[] storageNodeData=new byte[1024];
				DatagramPacket dp=new DatagramPacket
						(storageNodeData,storageNodeData.length);
				ds.receive(dp);
				ios.serviceNode(dp);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
