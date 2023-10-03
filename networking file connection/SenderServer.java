/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package networking;

/**
 *
 * @author subhankar
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.ServerSocketConnection;
import javax.microedition.io.SocketConnection;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

public class SenderServer extends Thread implements CommandListener {
    private FileMidlet flmd;
    private Command conct;
    private Command ext;
    private Display display;
    private StringItem si;
    private Form fm;
    private int port;
    private ServerSocketConnection scn = null;
    //private ;
    private FileConnection fcn1;
    private InputStream is;
    private InputStream fis;
    private OutputStream os;
    Thread t;
    
    public SenderServer(FileMidlet fmd, int p) {
        flmd = fmd;
        port = p;
        display = Display.getDisplay(flmd);
        conct = new Command("Create", Command.ITEM, 1);
        ext = new Command("Exit", Command.EXIT, 1);
        si = new StringItem("Status: ", "Server Socket Open ");
        fm = new Form("FileServer");
        fm.append(si);
        fm.addCommand(ext);
        fm.addCommand(conct);
        fm.setCommandListener(this);
        display.setCurrent(fm);
    }
    
    public void run(){
        
        try {
            scn = (ServerSocketConnection) Connector.open("socket://:" + String.valueOf(port));            
            si.setText("Server Socket has been created");
            String s1 = "N";
            do{
                try{
                    SocketConnection sc = (SocketConnection) scn.acceptAndOpen();
                    is = sc.openInputStream();
                    os = sc.openOutputStream();
                    
                    byte[] b = new byte[20];
                    is.read(b);
                    int c = 0;
                    s1 = "";
                    for(int i=0; i<20; i++){
                        c = b[i];
                        s1 += String.valueOf((char) c);
                    }
                    s1 = s1.trim();
                    if(!s1.equals("exist"))
                    {
                        fcn1 = (FileConnection)Connector.open("file:///root1/" + s1);
                    
                        if(!fcn1.exists()){
                            try{
                                fm.append("\nNoSuchFile");
                                os.write("0".getBytes());
                                //is.read(b);
                            }
                            catch(Exception e){
                                fm.append("\n"+e.getMessage());
                            }
                            //fcn1.create();
                            //fcn1.close();
                        }
                        else{
                            fis = fcn1.openInputStream();
                            long fileSize1 = fcn1.fileSize();
                            if(fileSize1 == 0)
                                fileSize1 = 1;
                            byte[] b1 = new byte[(int) fileSize1];
                            
                            //int fl = (int)fileSize1;
                            try{
                                String fl = String.valueOf(fileSize1);
                                os.write(fl.getBytes());
                                //is.read(b);
                            }
                            catch(Exception e){
                                fm.append("\n"+e.getMessage());
                            }
                            fis.read(b1);

                            try{
                                os.write(b1);
                            }
                            catch(Exception e){
                                fm.append("\n"+e.getMessage());
                            }                            

                            fis.close();


                        }
                        fcn1.close();
                    }
                    
                    os.flush();
                    os.close();
                    is.close();
                    sc.close();
                }
                catch(Exception e){
                    fm.append("\n"+e.getMessage());
                }
            }while(!s1.equals("exit"));
        } catch (IOException ex) {
            si.setText(ex.getMessage());
        }
    }
    
    public void commandAction(Command c, Displayable dp){
        if(c == conct){
            t = new Thread(this);
            t.start();
            fm.removeCommand(conct);
        }
        else
            if(c == ext){
                try{
                    if(scn != null){
                        //fm.append("\n"+scn.toString());
                        scn.close();
                    }
                    scn = null;
                }
                catch(Exception ex){
                    si.setText(ex.getMessage());
                } 
                flmd.destroyApp(true);
                flmd.notifyDestroyed();
            }            
    }
    
}