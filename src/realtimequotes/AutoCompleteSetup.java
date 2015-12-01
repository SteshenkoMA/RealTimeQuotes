package realtimequotes;


import java.awt.*;
import java.awt.event.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javax.swing.*;
import javax.swing.event.*;

// Данный класс используется для работы с JComboBox, который содержит в себе список тикеров из AutoComplete.showVariants 
// и появляется при вводе символов в JTextField symbol


public class AutoCompleteSetup {
    
    private static boolean isAdjusting(JComboBox cbInput) {
        if (cbInput.getClientProperty("is_adjusting") instanceof Boolean) {
            return (Boolean) cbInput.getClientProperty("is_adjusting");
        }
        return false;
    }

    private static void setAdjusting(JComboBox cbInput, boolean adjusting) {
        cbInput.putClientProperty("is_adjusting", adjusting);
    }

    public static void setupAutoComplete(final JTextField txtInput) {
        final DefaultComboBoxModel model = new DefaultComboBoxModel();
        final JComboBox cbInput = new JComboBox(model) {
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 0);
            }
        };
        setAdjusting(cbInput, false);
      
        cbInput.setSelectedItem(null);
        
        // Данный Listener при выборе тикера обрезает строчку, отбразывая полное название тикера, 
        // помещает полученное значение в txtInput
        
        cbInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isAdjusting(cbInput)) {
                    if (cbInput.getSelectedItem() != null) {
                        String st =cbInput.getSelectedItem().toString();
                        String selectedTicker = st.substring(0,st.indexOf(' '));
                        txtInput.setText(selectedTicker);
                     }
                }
            }
        });

        // Данный Listener отвечает за использовние клавиш Enter, Space, Esc, Up и Down
        // 1) Enter - помещает выбранный тикер в txtInput
        // 2) Ecs - закрывает всплывающее окно
        // 3) Space - выбирает первый тикер из списка
        // 4) Up и Down - позволяет перемещаться по списку тикеров, оставляя курсор и фокус на txtInput
        
        txtInput.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                setAdjusting(cbInput, true);
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (cbInput.isPopupVisible()) {
                        e.setKeyCode(KeyEvent.VK_ENTER);
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    e.setSource(cbInput);
                    cbInput.dispatchEvent(e);
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        String st =cbInput.getSelectedItem().toString();
                        String selectedTicker = st.substring(0,st.indexOf(' '));
                        txtInput.setText(selectedTicker);
                        cbInput.setPopupVisible(false);
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cbInput.setPopupVisible(false);
                }
                setAdjusting(cbInput, false);
            }
        });
        
        // Данный DocumentListener отслеживает изменения в txtInput
        // и при каждом изменении выполняет метод updateList
        
        txtInput.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                
                    updateList();
               
            }

            public void removeUpdate(DocumentEvent e) {
                
                    updateList();
             
            }

            public void changedUpdate(DocumentEvent e) {
               
                    updateList();
                
            }
            
            // Метод updateList:
            // 1) запускает на выполнение AutoComplete.showVariants 
            // 2) добавляет список тикеров в model класс JComboBox,
            //    которое появляется при вводе символов в JTextField stockSymbol 
            //    и предлагает список тикеров для выбора
            
            private void updateList()  {
                setAdjusting(cbInput, true);
                model.removeAllElements();
                
                  String input = txtInput.getText();
                  
                if (!input.isEmpty()) {
                         
                AutoComplete.showVariants(input);     
                
                JSONArray ra = AutoComplete.tickersList;
                        try {
                for(int i=0;i<ra.length();++i)
				{
					JSONObject item = ra.getJSONObject(i);
					String s = "";
					s+=item.getString("symbol") + " ";
					s+=item.getString("name");
					                                        
                                        model.addElement(s);
					
                                        
				}
                           	} catch (JSONException e) {
				
				e.printStackTrace();
			}
 
}
                
                cbInput.updateUI();
                cbInput.setPopupVisible(model.getSize() > 0);
                setAdjusting(cbInput, false);
            }
        });
       
        txtInput.setLayout(new BorderLayout());
        txtInput.add(cbInput, BorderLayout.SOUTH);
    }
       
}