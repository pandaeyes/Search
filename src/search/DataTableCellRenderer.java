package search;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DataTableCellRenderer extends DefaultTableCellRenderer {
	
	public DataTableCellRenderer() {
		setHorizontalAlignment(JLabel.CENTER);  
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (isSelected) {
			cell.setForeground(table.getSelectionForeground());
		} else {
			if( column == 3 || column == 4){
				cell.setForeground(Color.BLUE);
			} else {
				cell.setForeground(table.getForeground());
			}
		}
		return cell;
	}
}

