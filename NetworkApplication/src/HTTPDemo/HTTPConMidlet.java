/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package HTTPDemo;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.io.*;
import java.io.IOException;
import java.io.InputStream;

//import java.lang.StringBuffer;

/**
 * @author Administrator
 */
public class HTTPConMidlet extends MIDlet implements CommandListener  {
    private Display display;
    private Form form;
    private Command exitCommand;
    private Command goCommand;
    private final String URL="http://localhost:8080/servlets-examples/servlet/dateServlet";
    private StringBuffer sb = new StringBuffer();
    public HTTPConMidlet()
    {
        form=new Form("Show http Connection");
        exitCommand = exitCommand = new Command("Exit", Command.EXIT, 1);
        goCommand = new Command("Go", Command.ITEM, 2);
    }
    public void startApp() {
        display=Display.getDisplay(this);
        form.append("Go to "+URL);
        form.addCommand(exitCommand);
        form.addCommand(goCommand);
        
        form.setCommandListener(this);
        display.setCurrent(form);
        
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        
    }
    public void commandAction(Command c, Displayable s) {
        if (c == exitCommand) {
            destroyApp(false);
            notifyDestroyed();
        } 
        else if(c == goCommand) {
            //form.deleteAll();
           Runnable httpCon=new httpConnection();
           new Thread(httpCon).start();
            //form.append(sb.toString());
            form.removeCommand(goCommand);
            form.setCommandListener(this);
            display.setCurrent(form);
        }
    }
  
   class httpConnection implements Runnable {
      //private String conurl=null;
       public httpConnection()
       {
           //conurl=str;
          }
      public void run(){
           try {
                getViaHttpConnection(URL);
                               
                }catch(Exception ex)
            {
               form.append(ex.getMessage()); 
            }
       }
      private void getViaHttpConnection(String url) throws IOException {
HttpConnection c=null ;
// = null;
int rc;
try {
c = (HttpConnection)Connector.open(url);

// Getting the response code will open the connection,
// send the request, and read the HTTP response headers.
// The headers are stored until requested.
rc = c.getResponseCode();
if (rc != HttpConnection.HTTP_OK) {
throw new IOException("HTTP response code: " + rc);
}
InputStream is = c.openInputStream();
// Get the ContentType
String type = c.getType();
form.deleteAll();
form.append("Content type is:"+type+"\n");
// Get the length and process the data
int len = (int)c.getLength();
//form.
sb=new StringBuffer(len);
if (len > 1) {
int actual = 0;
int bytesread = 0 ;
byte[] data = new byte[len];
while ((bytesread != len) && (actual != -1)) {
actual = is.read(data, bytesread, len - bytesread);
bytesread += actual;
}
form.append("Bytes Read :"+bytesread+"\n");
for(int i=0;i<data.length;i++)
{
        sb.append((char) data[i]);
}
} else {
int ch;
while ((ch = is.read()) != -1) {
     sb.append((char) ch);
}
}
is.close();
form.append(sb.toString());
} catch (ClassCastException e) {
throw new IllegalArgumentException("Not an HTTP URL");
} finally {

if (c != null)
c.close();
}
}
       
   }
}

