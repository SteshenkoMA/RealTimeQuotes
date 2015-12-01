package realtimequotes;

import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;
import static realtimequotes.OrderTableModel.prices;
import static realtimequotes.GUI.model;

// Данный класс обновляет таблицу новыми значениями каждые 10 секунд

public class TableUpdater {
     private Timer timer = new Timer();
    
    public void startTask() {
        timer.schedule(new PeriodicTask(), 0);
    }

    private class PeriodicTask extends TimerTask {
        @Override
        public void run() {
            
                // Получаем список тикеров с данными например:
                // В TickersList содержатся [0]IBM, [1]GOOG, [2]AAPL
                // Метод ниже возвращает следущий список: "resources", где 
                // resource[0] =IBM, resource[1]GOOG, resource[2]APPL
          
                QuotesParser.getStockData();
                
                // Проходим циклом по каждому элементу TickersList.tickers
                
                for(int i=0; i<TickersList.tickers.size(); i++){
                    try {
		
                    // Передаем в метод  QuotesParser.getArrayData() индекс элемента
                    // Например для элемента [0]IBM, передадим в QuotesParser.getArrayData(0),
                    // в итоге получи данные resource[0]IBM    
                        
                    JSONObject stockData = QuotesParser.getArrayData(i);
                    
                    // Находим данный элемент в таблце
                    
                    for (int d = model.getRowCount() - 1; d >= 0; --d) {
                        
                         if (model.getValueAt(d, 0).equals(TickersList.tickers.get(i))) {
                        
                         // Для начала получаем из stockData новые значения тикера
                         String price = stockData.getString("price");
                         String change = stockData.getString("change");
                         String chg_percent = stockData.getString("chg_percent");
                         
                         // Затем записываем его цену (которая отображается в настоящий момент в таблице) в OrderTableModel.prices
                         prices.add(d,model.getValueAt(d,2));
                       
                       String oldPrice = (String) model.getValueAt(d,2);
                       
                       // Если новая цена price не равна старой цене oldPrice, то обновляем данные для тикера
                       if (!price.equals(oldPrice)){
                          model.updateOrderPrice(d, price); 
                          model.updateOrderChange(d, change); 
                          model.updateOrderPercent(d, chg_percent);
                       }
                       
                       // Если price равна  oldPrice, то данные не обновляются
                       
                        if (price.equals(oldPrice)){
                       // System.out.println("price "+price+" = "+"oldPrice "+oldPrice);
                       }
                       
                       }
                         
                         
                    }
                   
                     } catch (Exception ex) {
				
			}

        }
           
            timer.schedule(new PeriodicTask(), 10 * 1000);
    }
}
}