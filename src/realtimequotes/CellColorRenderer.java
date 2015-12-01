package realtimequotes;

//Данный класс взят здесь: http://www.devx.com/DevX/10MinuteSolution/17167

/**
 * ColorCellRenderer
 *
 * Table Cell Renderer class for setting the foreground and background
 * colors for a cell.
 *
 * @author L.D'Abreo
 *
 * @see TableCellRenderer
 * @see JTable
 * @see CellColorProvider
 */


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.JComponent;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

class CellColorRenderer
    implements TableCellRenderer
{
    protected TableCellRenderer delegate;
    protected CellColorProvider provider;
    
    /**
     * Constructor.
     * 
     * @param delegate
     * @param provider
     */
    public CellColorRenderer(final TableCellRenderer delegate, final CellColorProvider provider)
    {
        this.delegate = delegate;
        this.provider = provider;
    }
    
    /**
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int,
     *      int)
     */
    public Component getTableCellRendererComponent(final JTable table,  Object value, final boolean isSelected,
                                                   final boolean hasFocus, final int row, final int column)
    {
       Color bgrd = null;
        Color fgrd = null;
        
        
    
        
        if (isSelected)
        {
            fgrd = table.getSelectionForeground();
            bgrd = table.getSelectionBackground();
        }
        else
        {
            // As column may have been moved, convert the view column index
            // to a model column index
            final int mcol = table.convertColumnIndexToModel(column);
            fgrd = this.provider.getForeground(row, mcol);
            bgrd = this.provider.getBackground(row, mcol);
        }
        final Component c = this.delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        // Set the component colours
        
        
        
        
       c.setBackground(bgrd);
   
       // Код ниже отвечает за именение шрифта
    
       //      final JComponent jc = (JComponent) c;
       //      final Font f = jc.getFont();   
       //      jc.setFont(f.deriveFont(Font.BOLD));
       //      if (column ==2){
       //      jc.setFont(f.deriveFont(Font.PLAIN));}
      
      
       
    
        
        return c;
    }

}