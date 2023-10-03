/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package socketDemo;

//import javax.microedition.io.StreamConnectionNotifier;
import javax.microedition.io.Connector;
//import javax.microedition.io.StreamConnection;
import javax.microedition.io.ServerSocketConnection;
import javax.microedition.io.SocketConnection;
import java.io.*;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
import javax.microedition.lcdui.Form;
import socketDemo.networkMidlet;
import java.io.IOException;
import java.util.Date;
//import javax.microedition.io.


/**
 *
 * @author Administrator
 */
public class Server  {
    //networkMidlet midlet;
    private long port;
    private ServerSocketConnection scon;
    private SocketConnection con;
    //private ;
    
    private static boolean serverresumed=false;
    public Server(long port) {
        this.port=port;
        //this.serverresumed=true;
    }
    public static void setserverresumed(boolean x) {
        serverresumed=x;
    }
    public static boolean getserverresumed() {
       return serverresumed;
    }
    public void startConnection() throws IOException {
        serverresumed=true;
        try {
        scon=(ServerSocketConnection) Connector.open("socket://:"+this.port);
          networkMidlet.form.append("\n Waiting for new Connection......");
          do
          {
              //scon.
              
           con=(SocketConnection) scon.acceptAndOpen();
          new Thread(new Runnable() { 
              public void run(){
              StringBuffer sb=new StringBuffer();
            int ch;
            networkMidlet.form.append("\n Connection accepted :");
            try {
                //con.
           DataInputStream dis=con.openDataInputStream();
           DataOutputStream dos=con.openDataOutputStream();
           Date date=new Date(System.currentTimeMillis());
          /*String msg="Hello World";
          //char[] b=msg.toCharArray();
         byte[] msgBuffer=msg.getBytes();
          for(int i=0; i<msgBuffer.length; i++)
          {
          os.write((int) msgBuffer[i]);
          }
          os.write(-1);*/
           dos.writeUTF(date.toString());
          networkMidlet.form.append("\n Message sent :"+date.toString());
         while ((ch = dis.read()) != -1) {
              sb.append((char) ch);
           }

           networkMidlet.form.append("\n Message received from client :"+sb.toString());
          dis.close();
           dos.close();
           }catch(IOException ex) {
            networkMidlet.form.append("\n"+ex.getMessage());
        }      
              }
        }).start();
          }while(serverresumed);
        }catch(IOException ex) {
            networkMidlet.form.append("\n"+ex.getMessage());
        }finally {
            if(con!=null)
                con.close();
            if(scon!=null)
                scon.close();
        }             
                    }
    public void stopserver() {
        serverresumed=false;
        try {
           if(con!=null)
                con.close();
            if(scon!=null)
                scon.close(); 
        }catch(IOException ex) {
            networkMidlet.form.append("\n"+ex.getMessage());
        }
    }

}
