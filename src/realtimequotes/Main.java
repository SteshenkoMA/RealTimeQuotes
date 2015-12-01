package realtimequotes;

import javax.swing.JFrame;

public class Main {
    
     public static void main(String[] args)
    {
       
        GUI f = new GUI();
                
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(400, 288);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        
        TableUpdater autoupdate;
        autoupdate = new TableUpdater();
        autoupdate.startTask();
        
    }
}
