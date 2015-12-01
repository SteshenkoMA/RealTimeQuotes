package realtimequotes;


import java.net.*;
import java.io.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Данный класс испольуется для вызова списка тикеров, которые содержат в себе
// символы введенные пользователем в JTextField

public class AutoComplete {
    
    public static JSONArray tickersList = new JSONArray();
    
    public static void showVariants(String symbol){
        
    lab1: try {
                
        URL url = new URL("http://autoc.finance.yahoo.com/autoc?query="+symbol+"&region=US&lang=en-US&callback=YAHOO.Finance.SymbolSuggest.ssCallback");
                
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        String error ="/**/YAHOO.Finance.SymbolSuggest.ssCallback({\"ResultSet\":{\"result\":null,\"error\":{\"code\":\"internal-error\",\"description\":\"Timeout after";
       
        inputLine = in.readLine();
                
        // labl - метка, она используется по следующей причине:
        // иногда сервер autoc.finance.yahoo.com выдает ошибку (выше error)
        // ниже цикл проверяет: вернул ли сервер при вызове этого метода ошибку,
        // если вернул, то метод showVariants() вызывается еще раз (внутри самого себя)
        // и так может продолжается до тех пор, пока метод не вернет список тикеров без ошибки,
        // а далее используется break lab1 - выход из блока кода помеченного меткой,
        // чтобы метод не продолжал выполнятся, так как список тикеров уже был получен, 
        // и нет необходимости выполнять оставшийся код
                
        if (inputLine.contains(error)){
            
                showVariants(symbol);
                in.close();
                break lab1;
            }
        
        // http://autoc.finance.yahoo.com/autoc?query - возвращает список тикеров в json формате
        // org.json - библиотека, которая позволяет преобраовать этот формат в удобный вид
        // код ниже:
        // 1) обрезает строчку inputLine, до нужного json формата, так как изначально url
        //    содержит в себе ненужные символы
        // 2) добавляет тикеры в массив ra вида JSONArray
        
        if (inputLine != null){
                        
            String jsonFormat = inputLine.substring(39, inputLine.length()-2 );

               JSONObject js = null;
	        try {
				js = new JSONObject(jsonFormat);
				JSONArray ra = js.getJSONObject("ResultSet").getJSONArray("Result");
                                tickersList = ra;
				for(int i=0;i<ra.length();++i)
				{
					JSONObject item = ra.getJSONObject(i);
					String s = "";
					s+=item.getString("symbol") + " ";
					s+=item.getString("name");
					s+="(" + item.getString("exch") + ")";
                                        
				}
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
 
        }
        
        in.close();
        
        } catch (IOException e) {
        }
}
    }
