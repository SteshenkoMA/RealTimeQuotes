package realtimequotes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

// Данный класс создает файл TickersList.txt в папке с программой,
// отвечает за чтение и запись данного файла

public class TickersList {
    
   public static ArrayList <String> tickers = new ArrayList<>();
  
   // Данный метод считывает ArrayList из TickersList.txt и присваивает его данные переменной tickers
   public static void read () {

             try {
                 
                 
                 File file = new File("TickersList.txt");

            // Если файл не создан, то создаем его

            if (!file.exists()) {
                
                file.createNewFile();
            }

            

            FileInputStream fis = new FileInputStream(file.getAbsoluteFile());
            ObjectInputStream ois = new ObjectInputStream(fis);
                 try {
                     tickers = (ArrayList<String>) ois.readObject();
                 } catch (ClassNotFoundException ex) {
                     Logger.getLogger(TickersList.class.getName()).log(Level.SEVERE, null, ex);
                 }
              ois.close();
                 

        } catch (IOException e) {
            e.printStackTrace();
        }
   
 }

   // Данный метод записывает переменную tickers в TickersList.txt
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
