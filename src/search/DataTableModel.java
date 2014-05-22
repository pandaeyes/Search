package search;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;

public class DataTableModel extends AbstractTableModel {
	
	private List<Link> list = new ArrayList<Link>();
	
	public String getColumnName(int col){
		switch (col) {
			case 0:	return "日期";
			case 1: return "标题";
			case 2: return "索引";
			case 3:	return "图片";
			default: return null;
		}
	}

	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Link link = list.get(rowIndex);
		switch (columnIndex) {
			case 0: return "[" + link.getDate() + "]";
			case 1: return link.getTitle();
			case 2: return link.getIndex();
			case 3: return "查看";
			default: return null;
		}
	}
	
	public List<Link> getList() {
		return list;
	}
	
	public void setList(List<Link> list) {
		this.list = list;
	}

}
