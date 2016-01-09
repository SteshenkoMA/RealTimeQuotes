package realtimequotes;


import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
 

/*
    Данный класс выступает в роли Mouse Listener для таблицы.
    Используется для выбора нужной строки при реализации всплывающего меню
    
    This class acts as a Mouse Listener for the table.
    Is used to select the desired line when you implement a pop-up menu
 */
 
public class TableMouseListener extends MouseAdapter {
     
    private JTable table;
     
    public TableMouseListener(JTable table) {
        this.table = table;
    }
    
     /*
       Этот метод выбирает строку в таблице, по которой щелкнул правой кнопкой пользователь
    
       This method fetches the row in the table you right-clicked the user
    */
    
    @Override
    public void mousePressed(MouseEvent event) {
        
        Point point = event.getPoint();
        int currentRow = table.rowAtPoint(point);
        table.setRowSelectionInterval(currentRow, currentRow);
    }
}