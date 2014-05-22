package search;

import javax.swing.JButton;
import javax.swing.JList;

public class SearchThread extends Thread {
	
	private DataTableModel tableModel;
	private SearchInfo info;
	private JButton runBut;
	
	public SearchThread(DataTableModel tableModel, SearchInfo info, JButton runBut) {
		this.tableModel = tableModel;
		this.info = info;
		this.runBut = runBut;
	}
	
	public void run() {
		SearchService.getInstance().search(tableModel, info);
		tableModel.fireTableDataChanged();
		runBut.setEnabled(true);
	}
}
