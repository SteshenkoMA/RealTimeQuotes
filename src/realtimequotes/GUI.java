package realtimequotes;

/*
   Данный класс основан на этой работе: http://www.devx.com/DevX/10MinuteSolution/17167
   
   This class is based on this work: http://www.devx.com/DevX/10MinuteSolution/17167
*/

/**
 * StockMarketOrderDisplay
 *
 * An example App that demonstrates the use of 
 * TableCellRenderers.
 *
 * @author L.D'Abreo
 *
 * @see JTable
 */

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.json.JSONException;
import org.json.JSONObject;

/*
   Данный класс создает интерфейс программы

   This class creates the programm interface
*/

public class GUI extends JFrame
{
    CellColorProvider  provider        = new CellColorProvider()
                                       {
                                           public Color getForeground(int row, int column)
                                           {
                                              return Color.black;
                                              
                                           }
                                           
                                           public Color getBackground(int row, int column)
                                           {
                                              return Color.white;
                                               
                                           }
                                       };

    /* 
       Объявляем элементы интерфейса
    
       Declare interface elements
    */
    
    JPanel             contentPane;
    TableFlasher       flashProvider;
    public static OrderTableModel    model;
    JTable             table;
    Thread             feed;
    
    JPanel mainPanel = new JPanel();
       
    GridBagConstraints c;
    JTextField stockSymbol = new JTextField();
    JButton button1 = new JButton("Add");
       
  
    public GUI()
    {
        table = new JTable();
        flashProvider = new TableFlasher(table);
        model = new OrderTableModel(flashProvider);
        table.setModel(model);
        
        
        addFromTickersList();
        initialise();
    }
    
    /*
       В данном методе настраиваются все элементы интерфейса, добавляются слушатели событий
       
       This method sets all the elements of the interface, adds event listeners
    */
    
    private void initialise()
    {
        contentPane = (JPanel) this.getContentPane();
        this.setTitle("RealTime Stock Quotes");
        
        registerRendererForClass(String.class);
       
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().add(table, null);
        contentPane.add(scrollPane);
                  
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItemShow = new JMenuItem("Show");
        JMenuItem menuItemRemove = new JMenuItem("Delete");
        popup.add(menuItemShow);
        popup.add(menuItemRemove);
        table.setComponentPopupMenu(popup);
        table.addMouseListener(new TableMouseListener(table));
               
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints c;
          
        c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 0);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.1;
        mainPanel.add(stockSymbol, c);
       
        c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 1);
        c.gridx = 2;
        c.gridy = 0;
        // c.ipadx = 20; - button width
        mainPanel.add(button1, c);
                
        contentPane.setLayout(new BorderLayout());
        contentPane.add(mainPanel,BorderLayout.NORTH);
        contentPane.add(scrollPane,BorderLayout.CENTER);
        
        AutoCompleteSetup.setupAutoComplete(stockSymbol);
        
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JSONObject stockData = QuotesParser.getOneTickerData(stockSymbol.getText());
                
                try{
                    
                String symbol = stockData.getString("symbol");
                String name = stockData.getString("name");
                String price = stockData.getString("price");
                String change = stockData.getString("change");
                String chg_percent = stockData.getString("chg_percent");
                
                String[] data= { symbol,name,price,change,chg_percent };
                
                TickersList.tickers.add(symbol);
                
                TickersList.write();
                
                model.addRow(data);
                
                } catch (JSONException ex) {
								
			}
                
                
              
            }
        });
         
        menuItemShow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int sel = table.getSelectedRow();
                String symbol  = model.getValueAt(sel,0).toString();
   
                TickerInfo a = new TickerInfo(symbol);
            
            }
        });
          
        menuItemRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int sel = table.getSelectedRow();
                String symbol  =model.getValueAt(sel,0).toString();
                TickersList.tickers.remove(symbol);
                TickersList.write();
                model.removeRow(sel);
                  
            }
        });
        
       
    }
    
    /*
       Данный метод регистрирует рендереры таблицы
    
       This method registers the renderers in the table
    */
    
    private void registerRendererForClass(Class klass)
    {
        /*
           Инициализируем рендереры
           
           Initialize the renderers
        */
        
        DefaultTableCellRenderer defaultRenderer = new RenderRedGreen();
        TableCellRenderer colorRenderer = new CellColorRenderer(defaultRenderer, provider);
        TableCellRenderer flashRenderer = new CellFlashColorRenderer(colorRenderer, flashProvider);

        /*
           Добавляем рендереры к соответствующим колонкам таблицы
        
           Add renderers to the corresponding columns of the table 
        */
        
        table.getColumnModel().getColumn(2).setCellRenderer(flashRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(flashRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(flashRenderer);

    }
    
    /*
       Данный метод добавляет в таблицу тикеры из TickersList.txt
    
       This method adds to the table the tickers from TickersList.txt
    */
    
    private void addFromTickersList(){
        
        TickersList.read();
        
        QuotesParser.getStockData();
        
        for(int i=0; i<TickersList.tickers.size(); i++)
        {
         
         try {
        
         JSONObject stockData = QuotesParser.getArrayData(i);
      
         String symbol = stockData.getString("symbol");
         String name = stockData.getString("name");
         String price = stockData.getString("price");
         String change = stockData.getString("change");
         String chg_percent = stockData.getString("chg_percent");
         String[] dat= { symbol,name,price,change,chg_percent };
         
         model.addRow(dat);
             
           	   
			} catch (Exception e) {
				
			}
         
          } 
        }
    
   
}