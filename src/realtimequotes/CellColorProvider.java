package realtimequotes;


/*
   Данный интерфейс взят здесь: http://www.devx.com/DevX/10MinuteSolution/17167
   
   This interface is taken from here: http://www.devx.com/DevX/10MinuteSolution/17167
*/

/**
 * CellColorProvider
 *
 * Interface used by interested Renderers to
 * get the colours for a table cell.
 *
 * @author L.D'Abreo
 * @see TableCellRenderer
 * @see JTable
 */


import java.awt.Color;

/**
 * CellColorProvider.
 * 
 * @author clalleme - 26 mai 08
 *
 *
 * <!-- $Id: PreTradeEclipseCodeTemplates.xml,v 1.8 2007/03/12 12:35:12 anavarro Exp $ -->.
 *
 */
public interface CellColorProvider
{
    
    /**
     * @param row 
     * @param column 
     * 
     * @return the foreground color for a cell
     */
    public Color getForeground(int row, int column);
    
   
    
    /**
     * @param row 
     * @param column 
     * @return the backgound color for a cell
     */
    public Color getBackground(int row, int column);
}