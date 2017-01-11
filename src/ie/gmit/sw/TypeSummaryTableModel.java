package ie.gmit.sw;

import javax.swing.table.*;
public class TypeSummaryTableModel extends AbstractTableModel{
	private static final long serialVersionUID = 777L;
	private String[] cols = {"Col 1", "Col 2", "Col 3"};
	private Object[][] data = {
		{"Stuff 1", "Other Stuff 1", "Even More Stuff 1"},
		{"Stuff 2", "Other Stuff 2", "Even More Stuff 2"},
		{"Stuff 3", "Other Stuff 3", "Even More Stuff 3"},
		{"Stuff 4", "Other Stuff 4", "Even More Stuff 4"},
		{"Stuff 5", "Other Stuff 5", "Even More Stuff 5"},
		{"Stuff 6", "Other Stuff 6", "Even More Stuff 6"},
		{"Stuff 7", "Other Stuff 7", "Even More Stuff 7"}
	};
	
	public int getColumnCount() {
        return cols.length;
    }
	
    public int getRowCount() {
        return data.length;
	}

    public String getColumnName(int col) {
    	return cols[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
	}
   
    public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
	}
}