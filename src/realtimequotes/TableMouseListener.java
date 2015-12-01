package realtimequotes;


import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
 

 // Данный класс выступает в роли Mouse Listener для таблицы.
 // Используется для выбора нужной строки при реализации всплывающего меню
 
public class TableMouseListener extends MouseAdapter {
     
    private JTable table;
     
    public TableMouseListener(JTable table) {
        this.table = table;
    }
    
    // Этот метод выбирает строку в таблице, по которой щелкнул правой кнопкой пользователь
        
    @Override
    public void mousePressed(MouseEvent event) {
        
        Point point = event.getPoint();
        int currentRow = table.rowAtPoint(point);
        table.setRowSelectionInterval(currentRow, currentRow);
    }
}