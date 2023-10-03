/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package networking;

import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

/**
 *
 * @author subhankar
 */
public class ReceiverClient extends Thread implements CommandListener {
    private FileMidlet flmd;
    private Command send;
    private Command ext;
    private Display display;
    private StringItem si;
    private TextField fileName;
    private Form fm;
    private int port;
    SocketConnection sc1;
    private FileConnection fconn;
    //private ;
    private InputStream is1;
    private OutputStream os1;
    private OutputStream fos;
    Thread th;
    
    public ReceiverClient(FileMidlet fd, int pt){
        flmd = fd;
        port = pt;
        display = Display.getDisplay(flmd);
        send = new Command("Receive", Command.ITEM, 1);
        ext = new Command("Exit", Command.EXIT, 1);
        fileName = new TextField("File Name: ", "newfile.txt", 20, TextField.ANY);
        fm = new Form("Client");
        fm.append(fileName);
        fm.addCommand(send);
        fm.addCommand(ext);
        fm.setCommandListener(this);
        display.setCurrent(fm);
    }
    
    public void run(){
         try{
             String name = fileName.getString();
             sc1 = (SocketConnection) Connector.open("socket://localhost:"+port);
             is1 = sc1.openInputStream();
             os1 = sc1.openOutputStream();
             fconn = (FileConnection) Connector.open("file:///root1/" + name);
                          
             if(!fconn.exists()){
                 try{
                     os1.write(name.getBytes());
                 }
                 catch(Exception e){
                     fm.append("\n Doesnot abble to write the name");
                 }
                 
                 byte[] b1 = new byte[12];
                 try{
                     is1.read(b1);
                 }
                 catch(Exception e){
                     fm.append("\n Doesnot abble to read file size");
                 }
                 int c = 0;
                 String s1 = "";
                 for(int j=0; j<12; j++) {
                     c = b1[j];
                     if( c>47 && c<58){
                         s1 += String.valueOf((char) c);
                     }
                     else
                         break;
                 }
                 int i = 0;
                 try{
                         i = Integer.parseInt(s1);
                     }
                     catch(Exception e){
                         fm.append("\n" + e.getMessage());
                     }
                 if(i == 0)
                     fm.append("\nNo Such File Exists!");
                 else{
                     byte[] b = new byte[i];
                     try{
                         is1.read(b);
                     }
                     catch(Exception e){
                         fm.append("\n Doesnot abble to read the file");
                     }
                     
                     fconn.create();
                     fos = fconn.openOutputStream();
                     fos.write(b);                         
                     fos.close();                     
                 }                     
             }
             else
                 os1.write("exist".getBytes());
             
             fconn.close();
             os1.flush();
             os1.close();
             is1.close();
             sc1.close();
         }
         catch(Exception ex){
             fm.append("\n" + ex.getMessage());
         }
         
    }
    
    public void commandAction(Command c, Displayable dp){
        if(c == ext){
            flmd.destroyApp(true);
            flmd.notifyDestroyed();
        }
        else
            if(c == send){
                th = new Thread(this);
                th.start();
            }
    }

}