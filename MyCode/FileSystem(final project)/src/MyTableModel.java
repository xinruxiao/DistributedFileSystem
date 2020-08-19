import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.util.*;

public class MyTableModel implements TableModel {
	private Map<String,Map> al; // 根据加载的数据集合生成一个表格模型
	private int rowCount = 0; // 表格数据行数

	public MyTableModel(Map<String,Map> al) {
		this.al = al;
		rowCount = al.size();
	}

	public int getRowCount() { // 返回表格模型的记录行数
		return rowCount;
	}

	public int getColumnCount() { // 返回表格模型的列数
		return 8;
	}

	public String getColumnName(int columnIndex) { // 返回表格模型的列名
		switch (columnIndex) {
		case 0:
			return "节点名称";
		case 1:
			return "是否可用";
		case 2:
			return "IP地址";
		case 3:
			return "端口号";
		case 4:
			return "容量";
		case 5:
			return "剩余容量";
		case 6:
			return "实际容量";
		case 7:
			return "文件数量";
		}
		return null;
	}

	public Class<?> getColumnClass(int columnIndex) { // 表格列数据的类型
		switch (columnIndex) {
		
		//数据均存为字符串类型
		case 0: 
		case 1:
		case 2:
		case 3:
		case 4: 
		case 5:
		case 6:
		case 7:
			return String.class;
		}
		return null;
	}

	// 表格中的数据能否编辑，设置为false，不可编辑。
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public Object getValueAt(int rowIndex, int columnIndex) { // 返回表格中的数据
		
		int count=0;
		String name="";
		Map tempMap=new HashMap();
		Set set=FileServer.storageNodeMap.keySet();
		Iterator it=set.iterator();
		while(it.hasNext()) {
			Object key=it.next();
			name=(String)key;
			tempMap=FileServer.storageNodeMap.get(key);
			if(count==rowIndex) 
				break;
			++count;
		}

		switch (columnIndex) { // 接下来返回该行数据的某个列的值
		case 0:
			return name;
		case 1:
			if(tempMap.get("Flag").equals("1"))
				return "可用";
			else {
				return "不可用";
			}
		case 2:
			return tempMap.get("NodeIP");
		case 3:
			return tempMap.get("NodePort");
		case 4:
			return tempMap.get("Volume");
		case 5:
			return tempMap.get("CurrentVolume");
		case 6:
			return tempMap.get("UsedVolume");
		case 7:
			return tempMap.get("FileNumber");
		}
		return null;
	}

	// 可以修改表格模型中的数据，在例子中，仅仅是显示，所以没有实现。
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		return;
	}

	// 处理模型的事件听众的方法，这里也没有必要实现。
	public void addTableModelListener(TableModelListener l) {
		return;
	}

	public void removeTableModelListener(TableModelListener l) {
		return;
	}
}