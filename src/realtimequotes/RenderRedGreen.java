package realtimequotes;


import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/*
   Данный класс отображает данные таблицы (не изменяя их) следующим образом:
   1) отображает данные ближе к правому краю клетки
   2) округляет числовые данные
   3) задает цвет в зависимости от значений данных
   4) добавляет "+" к положительным числам в колонках Change и Percent

   This class displays the data in the table (without changing the value) as follows:
   1) displays the data closer to the right edge of the cell
   2) rounds numeric data
   3) sets the color depending on data value
   4) adds "+" to the positive numbers in the columns Change and Percent
*/

final class RenderRedGreen extends DefaultTableCellRenderer {
  
   
  private static final DecimalFormat formatter = new DecimalFormat( "#.###" );
    
    
  RenderRedGreen () {
    setHorizontalAlignment(SwingConstants.RIGHT);  
    formatter.setMinimumFractionDigits(3);
  }
  
  @Override public Component getTableCellRendererComponent(
    JTable aTable, Object aNumberValue, boolean aIsSelected, 
    boolean aHasFocus, int aRow, int aColumn
  ) {  
   
    if (aNumberValue == null) return this;
    Component renderer = super.getTableCellRendererComponent(
      aTable, aNumberValue, aIsSelected, aHasFocus, aRow, aColumn
    );
    String value = (String)aNumberValue;
    double val = Double.valueOf(value);
    
   
    aNumberValue = new BigDecimal(val).setScale(2, RoundingMode.HALF_UP); //.doubleValue() убрать нули
     
    
  
    if (val<0) {
      renderer.setForeground(Color.RED);
     
    }
    else {
      renderer.setForeground(fDarkGreen);
    }
    
    if (aColumn ==2){
    renderer.setForeground(Color.BLACK);}
    
     if ((aColumn ==3&&val>0) ||(aColumn ==4&&val>0)){
         aNumberValue= "+"+aNumberValue;
     }
     
        
    
    return super.getTableCellRendererComponent(
            aTable, aNumberValue, aIsSelected, aHasFocus, aRow, aColumn );
  }
  

  //the default green is too bright and illegible
  private Color fDarkGreen = Color.green.darker();
  
  

} 