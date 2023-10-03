/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package socketDemo;

import javax.microedition.io.*;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
import java.io.*;
import java.io.IOException;
import java.util.Date;


/**
 *
 * @author Administrator
 */
public class Client {
    private String url;
    private SocketConnection con;
    public Client(String url) {
        this.url=url;
    }
    public void runClient() throws IOException {
        try {
            StringBuffer sb=new StringBuffer();
            int ch;
        con=(SocketConnection) Connector.open(this.url);
        networkMidlet.form.append("\n Trying to connect to server ;"+url);
        DataInputStream dis=con.openDataInputStream();
        DataOutputStream dos=con.openDataOutputStream();
        String msg=dis.readUTF();
         /*while ((ch = is.read()) != -1) {
              sb.append((char) ch);
           }*/
         networkMidlet.form.append("\n Message received :"+msg);
         dos.write(msg.getBytes());
         dis.close();
         dos.close();
        }catch(IOException ex) {
            networkMidlet.form.append("\n"+ex.getMessage());
        }finally {
            
            if(con!=null)
                con.close();
        }
    }
}
