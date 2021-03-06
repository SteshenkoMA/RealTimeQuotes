package realtimequotes;




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
   Данный класс используется для чтения исторических данных с http://finance.yahoo.com
   
   This class is used for reading historical data http://finance.yahoo.com
*/

public class QuotesParser {
    
    
public static JSONArray data = null;
public static JSONObject dat = null;
public static JSONObject oneTickerData = null;

/*
   Данный метод возвращает список ("resources"), который содержит в себе данные тиккеров. Например resources[] : [0] resource (IBM); [1] resource (AAPL)
   
   This method returns list ("resources"), which contains data of the tickers. For example, resources[] : [0] resource (IBM); [1] resource (AAPL)
*/

public static  JSONArray getStockData(){
  try {
      
        String symbol = "";
        
        /*
           В строку symbol передаем название всех тикеров из TickerList
        
           Pass the name of all tickers from TickerList in string symbol
        */

for (String s : TickersList.tickers)
{
    symbol += s + ",";
}



       URL url = new URL("http://finance.yahoo.com/webservice/v1/symbols/"+symbol+"/quote?format=json&view=detail");
     
       BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
       
        String inputLine;
       
        /*
           Считываем построчно inputline
         
           Read inputline line by line 
        */
        
        String a="";
         while ((inputLine = in.readLine()) != null) {
               a+=inputLine;
               }
  
                       
            
        if (a!= null){
                        
            String jsonFormat = a;

               JSONObject js = null;
	        try {
                    
                    /*
                       Получем список resources с данными тикеров
                       
                       Get a list of resources with data tickers
                    */
                    
                              data = null;
                    
				js = new JSONObject(jsonFormat);
			        JSONArray ra = js.getJSONObject("list").getJSONArray("resources");
                                
                              data = ra;                               
				
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}

        }
        
        in.close();

        
        } catch (IOException e) {
        }
        
  return data;
        
    };

/*
   Данный метод получает индекс, и возвращает возвращает данные соответствующего элемента

   This method gets the index, and returns returns the data of the corresponding element
*/

public static JSONObject getArrayData(int a){

   try{
				        dat = null;
                                    
					JSONObject resource = data.getJSONObject(a);
                                        JSONObject resource2 = resource.getJSONObject("resource");
                                        JSONObject fields = resource2.getJSONObject("fields");

                                        dat = fields;
   }
   catch(JSONException e){};
                                        
                                        
				
    
    

return dat;
};

/*
   Данный метод возвращает данные одного тикера, используется при добавлении тикера в таблицу (кнопка "Add")
   
   This method returns the data of one symbol, used when adding fields to the table (button "Add")
*/

public static JSONObject getOneTickerData(String symbol){
  try {
         
        URL url = new URL("http://finance.yahoo.com/webservice/v1/symbols/"+symbol+"/quote?format=json&view=detail");
                
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
				JSONArray ra = js.getJSONObject("list").getJSONArray("resources");
                              
                                
                                                             
				for(int i=0;i<ra.length();++i)
				{
                                    oneTickerData = null;
                                    
					JSONObject resource = ra.getJSONObject(i);		
                                        JSONObject resource2 = resource.getJSONObject("resource");
                                        JSONObject fields = resource2.getJSONObject("fields");
                                        
                                        String s = "";
					s+=fields.getString("name");
                                        
                                        oneTickerData = fields;
                                        
                                        
				}
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}

            
            
        }
        
        in.close();

        
        } catch (IOException e) {
        }
        
  return oneTickerData;
        
    };

}
       

