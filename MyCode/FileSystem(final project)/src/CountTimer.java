import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CountTimer implements Runnable{
	public String nodeName;
	public CountTimer(String nodeName) {
		this.nodeName=nodeName;
	}
	public void run() {
		try{
			while(true) {
				Map tempMap=FileServer.storageNodeMap.get(nodeName);
				String flag=(String)tempMap.get("Dangerous");
				if(flag.equals("1")){
//					FileServer.storageNodeMap.remove(nodeName);
					tempMap.put("Flag", "0");
					FileServer.storageNodeMap.put(nodeName, tempMap);
					break;
				}
				else{
					tempMap.put("Dangerous", "1");
					FileServer.storageNodeMap.put(nodeName, tempMap);
				}
				Thread.sleep(10000);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("节点"+nodeName+"已失去连接......");
	}
}
