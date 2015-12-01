package realtimequotes;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.json.JSONException;
import org.json.JSONObject;


// Данный класс создает окно с графиком тикера и дополнительной информацией

public class TickerInfo {
    
  private String symbol;
  
  TickerInfo(String symbol){
  this.symbol = symbol;
  buildGUI();
  };  
    
  // В данной переменной хранятся данные тикера 
  
  private ArrayList <String> data = new ArrayList();
   
  private JLabel imageLabel = new JLabel();
  
  // Данный метод скачивает картинку  с http://chart.finance.yahoo.com , затем изменяет imageLabel
  private void showChart() {
      
                try {
                    //http://ichart.finance.yahoo.com/b?s= - with volume
                    //http://chart.finance.yahoo.com/z?s= + &t=1d&q=l - editable
                    
                    //more info:
                    //https://code.google.com/p/yahoo-finance-managed/wiki/miscapiImageDownload
                    
                    String path = "http://chart.finance.yahoo.com/t?s="+symbol+"&width=500&height=250";
          
                    URL url = new URL(path);
                    BufferedImage image = ImageIO.read(url);
                    imageLabel = new JLabel(new ImageIcon(image));
                    
                } catch (Exception exp) {
                    exp.printStackTrace();
                }

            }
        
  // Данный метод считывает дополнительную информацю по тикеру, добавляет данные в ArrayList <String> data  
  
  private void getFromUrl(){
       try {
       URL url = new URL("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20IN%20%28%22"+symbol+"%22%29&format=json&env=http://datatables.org/alltables.env");
     
        
       BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
       
        String inputLine;
       
        String a="";
         while ((inputLine = in.readLine()) != null) {
               a+=inputLine;
               }
         if (a!= null){
                        
            String jsonFormat = a;

               JSONObject js = null;
	        try {
                    
                                js = new JSONObject(jsonFormat);
				JSONObject query = js.getJSONObject("query");
                                JSONObject results = query.getJSONObject("results");
                                JSONObject quote = results.getJSONObject("quote");
                                
                                String  previousClose = quote.optString("PreviousClose");
                                String  open = quote.optString("Open");
                                String  daysRange = quote.optString("DaysRange");
                                String  yearRange = quote.optString("YearRange");
                                String  volume = quote.optString("Volume");
                                String  averageDailyVolume = quote.optString("AverageDailyVolume");
                                String  marketCapitalization = quote.optString("MarketCapitalization") ;                   
                                String  peRatio = quote.optString("PERatio");
                                String  earningsShare = quote.optString("EarningsShare");     
                                String  dividendShare = quote.optString("DividendShare");
                                String  dividendYield = quote.optString("DividendYield");
                                
                                String  DivAndYield;
                                if (dividendShare.equals("")){
                                DivAndYield= "";}
                                else{
                                DivAndYield= dividendShare + " ("+dividendYield+"%)";
                                }
                                
                                String  currency = quote.optString("Currency");
                                String  stockExchange = quote.optString("StockExchange");
                             
                                data.add(0,previousClose);
                                data.add(1,open);
                                data.add(2,daysRange);
                                data.add(3,yearRange);
                                data.add(4,volume);
                                data.add(5,averageDailyVolume);
                                data.add(6,marketCapitalization);
                                data.add(7,peRatio);
                                data.add(8,earningsShare);
                                data.add(9,DivAndYield);
                                data.add(10,currency);
                                data.add(11,stockExchange);
                                                              

			} catch (JSONException e) {
				
				e.printStackTrace();
                               
			}

            
            
        }
        
        in.close();

        
        } catch (IOException e) {
        }
       
       
       }
   
  // Данный метод создает окно с графиком и дополнительной информацией по тикеру
  
  public void buildGUI (){
      
 
   showChart ();   
   getFromUrl();   
      
   DefaultTableModel model = new DefaultTableModel();
   model.addColumn("1-6");
   model.addColumn("Data");
   model.addColumn("7-12");
   model.addColumn("Data2");
   
   String[][]a = {
                   {"Prev Close:" ,data.get(0),"Market Cap:"   ,data.get(6)},
                   {"Open:"       ,data.get(1),"P/E"           ,data.get(7)},
                   {"Day's Range:",data.get(2),"EPS"           ,data.get(8)},
                   {"52wk Range:" ,data.get(3),"Div & Yield"   ,data.get(9)},
                   {"Volume:"     ,data.get(4),"Currency"      ,data.get(10)},
                   {"Avg Vol (3m)",data.get(5),"Stock exchange",data.get(11)}
                 };
   
   for(int i =0;i<6;i++){
   model.addRow(a[i]);  
   }
   
   JTable table = new JTable(model);

   JFrame MainWindow = new JFrame(symbol);
        
   JPanel panel = new JPanel();
   panel.setLayout(new BorderLayout());
 
        panel.add( imageLabel ,BorderLayout.CENTER);
        panel.add( table ,BorderLayout.SOUTH);
                 
        MainWindow.setContentPane(panel);
        MainWindow.setResizable(false);
        MainWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        MainWindow.setSize(510, 395);
        MainWindow.setLocationRelativeTo(null);
        MainWindow.setVisible(true);
   
   
   };
  

  
}
    

