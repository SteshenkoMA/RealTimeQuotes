package realtimequotes;

//Данный класс основан на этой работе: http://www.devx.com/DevX/10MinuteSolution/17167

/**
 * OrderTableModel
 *
 * Table Model for managing Orders.
 *
 * @author L.D'Abreo
 *
 * @see JTable
 */


import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.table.AbstractTableModel;

public final class OrderTableModel
    extends AbstractTableModel
{
    //В данной переменной будут хранится значения старых котирировок
    public static ArrayList<Object> prices = new ArrayList<Object>();
    
    // Statics
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
  
    // Attributes
    String[]     columnName  = new String[] { "Ticker", "Name", "Price", "Change", "Percent" };
    Class[]      columnClass = new Class[] { String.class, String.class, String.class, String.class, String.class };
    Object[][]   rows        = {     };
    
    TableFlasher flashProvider;
    
    public OrderTableModel(TableFlasher aFlashProvider)
    {
        this.flashProvider = aFlashProvider;
    }
    
    public OrderTableModel(final Object[][] orders)
    {
        this.rows = orders;
    }
    
    public int getColumnCount()
    {
        return this.columnName.length;
    }
    
    public void updateOrderPrice(final int row, final String newPrice)
    {
        
        final Object[] order = this.rows[row];
        order[2] = newPrice;
        // super.fireTableRowsUpdated(row, row);
        flashProvider.flashCell(row, 2);
        
    }
    
    public void updateOrderChange(final int row, final String newChange)
    {
        final Object[] order = this.rows[row];
        order[3] = newChange;
        super.fireTableRowsUpdated(row, row);
               
    }
       
    public void updateOrderPercent(final int row, final String newPercent)
    {
        final Object[] order = this.rows[row];
        order[4] = newPercent;
        super.fireTableRowsUpdated(row, row);
               
    }
    
    public void addRow(Object[] newRow) {
        rows = Arrays.copyOf(rows, rows.length+1);
        rows[rows.length-1] = newRow;
        fireTableRowsInserted(rows.length-1, rows.length-1);
    }
    
    public void removeRow(int row) { 
        System.arraycopy(rows,row+1,rows,row,rows.length-row-1); 
        rows = Arrays.copyOf(rows, rows.length-1); 
        fireTableRowsDeleted(row, row); } 
 
    public Object getValueAt(final int row, final int col)
    {
        return this.rows[row][col];
    }
    
    public int getRowCount()
    {
        return this.rows.length;
    }
    
    @Override
    public String getColumnName(final int col)
    {
        return this.columnName[col];
    }
    
    @Override
    public Class getColumnClass(final int col)
    {
        return this.columnClass[col];
    }
    
}