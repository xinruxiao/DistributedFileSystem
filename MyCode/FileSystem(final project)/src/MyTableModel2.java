import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.util.*;

public class MyTableModel2 implements TableModel {
	private Map<String,Map> al; // 根据加载的数据集合生成一个表格模型
	private int rowCount = 0; // 表格数据行数

	public MyTableModel2(Map<String,Map> al) {
		this.al = al;
		rowCount = al.size();
	}

	public int getRowCount() { // 返回表格模型的记录行数
		return rowCount;
	}

	public int getColumnCount() { // 返回表格模型的列数
		return 5;
	}

	public String getColumnName(int columnIndex) { // 返回表格模型的列名
		switch (columnIndex) {
		case 0:
			return "编号";
		case 1:
			return "文件原始名称";
		case 2:
			return "文件大小";
		case 3:
			return "主存储节点";
		case 4:
			return "备份存储节点";
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
		Set set=FileServer.clientFileMap.keySet();
		Iterator it=set.iterator();
		while(it.hasNext()) {
			Object key=it.next();
			name=(String)key;
			tempMap=FileServer.clientFileMap.get(key);
			if(count==rowIndex) 
				break;
			++count;
		}
		
		switch (columnIndex) { // 接下来返回该行数据的某个列的值
		case 0:
			return name;
		case 1:
			return tempMap.get("FileName");
		case 2:
			return tempMap.get("FileLength");
		case 3:
			return tempMap.get("StorageNodeOne");
		case 4:
			return tempMap.get("StorageNodeTwo");
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
