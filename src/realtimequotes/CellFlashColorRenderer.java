package realtimequotes;

//Данный класс основан на этой работе: http://www.devx.com/DevX/10MinuteSolution/17167

/**
 * CellFlashColorRenderer
 *
 * Specialised renderer class for flashing the colors of a cell
 *
 * @author L.D'Abreo
 *
 * @see TableCellRenderer
 * @see JTable
 * @see CellFlashProvider
 */

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * CellFlashColorRenderer.
 * 
 * @author clalleme - 2 juin 08
 *
 *
 * <!-- $Id: PreTradeEclipseCodeTemplates.xml,v 1.8 2007/03/12 12:35:12 anavarro Exp $ -->.
 *
 */
public class CellFlashColorRenderer
    implements TableCellRenderer
{
    
    protected TableCellRenderer delegate;
    protected CellFlashProvider provider;
    
    /**
     * Constructor.
     *
     * @param delegate
     * @param provider
     */
    public CellFlashColorRenderer(final TableCellRenderer delegate, final CellFlashProvider provider)
    {
        this.delegate = delegate;
        this.provider = provider;
    }
    
    /** 
     * (non-Javadoc)
     *
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
                                                   final boolean hasFocus, final int row, final int column)
    {
        
        // Get the component from the delegate
        final Component c = this.delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        // Convert view column to model column
        final int mcol = table.convertColumnIndexToModel(column);
        // invert the colours to flash
        if (this.provider.isFlashOn(row, mcol))
        {
      
            //В зависимости от значения новой котировки, задаем цвет мигания клетки 
    
            String oldP = (String)OrderTableModel.prices.get(row);
            String newP = (String)value;
            
            double oldPrice = Double.valueOf(oldP);
            double newPrice = Double.valueOf(newP);
            
            Color color;
            if (newPrice<oldPrice){
            color = Color.RED;
            }else{
            color = fDarkGreen;}
            
            final Color bgrd = color; 
            final Color fgrd = c.getBackground();
            c.setBackground(bgrd);
            c.setForeground(fgrd);
        }
        
        return c;
        
    }
    private Color fDarkGreen = Color.green.darker();
}