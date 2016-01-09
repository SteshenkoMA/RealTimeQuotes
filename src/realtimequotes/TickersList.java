package realtimequotes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


/* 
   Данный класс создает файл TickersList.txt в папке с программой,
   отвечает за чтение и запись данного файла
   
   This class creates the file TickersList.txt in the program folder, and
   responsible for reading and writing this file
*/

public class TickersList {
    
   public static ArrayList <String> tickers = new ArrayList<String>();
  
   /*
      Данный метод считывает ArrayList из TickersList.txt и присваивает его данные переменной tickers
   
      This method reads the ArrayList from TickersList.txt and assigns it to the variable data tickers
   */
   
   public static void read () {

             try {
                 
                 
                 File file = new File("TickersList.txt");

            /*
               Если файл не создан, то создаем его
                 
               If the file is not created, then create it
            */

            if (!file.exists()) {
                
                file.createNewFile();
            }

            

            FileInputStream fis = new FileInputStream(file.getAbsoluteFile());
            ObjectInputStream ois = new ObjectInputStream(fis);
            
                 try {

          ArrayList<String> a   = (ArrayList<String>) ois.readObject();  
             
 tickers = a;
         
                 } catch (ClassNotFoundException ex) {
                    // Logger.getLogger(TickersList.class.getName()).log(Level.SEVERE, null, ex);
                 }
              ois.close();
                 

        } catch (IOException e) {
            e.printStackTrace();
        }
   
 }

   /*
      Данный метод записывает переменную tickers в TickersList.txt
      
      This method writes the variable tickers in TickersList.txt
   */
   
   public static void write () {

             try {
                 
                 
                 File file = new File("TickersList.txt");

            if (!file.exists()) {
               
                file.createNewFile();
            }

            FileOutputStream  fos = new FileOutputStream(file.getAbsoluteFile());
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(tickers);
            oos.close();
                 

        } catch (IOException e) {
            e.printStackTrace();
        }


    } 

     
}
