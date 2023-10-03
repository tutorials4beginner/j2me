/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package socketDemo;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.io.IOException;

/**
 * @author Administrator
 */
public class networkMidlet extends MIDlet implements CommandListener {
   private Display display;
    public static Form form;
    private Command exitCommand;
    private Command backCommand;
    private Command startServer;
    private Command startClient;
    private Command stopServer;
    private final String addr="socket://localhost:5000";
    private final long portNo=5000;
    private Server s;
    private Client cl;
            private StringBuffer sb = new StringBuffer();
    public networkMidlet() {
        display=Display.getDisplay(this);
        form=new Form("Socket Demo");
        exitCommand=new Command("Exit",Command.EXIT,1);
        backCommand=new Command("Back",Command.BACK,1);
        startServer=new Command("start Server",Command.ITEM,1);
        startClient=new Command("start Client",Command.ITEM,2);
        stopServer=new Command("stop Server",Command.ITEM,2);
    }
    public void startApp() {
        form.append("Test the Socket Demo....");
        form.addCommand(startServer);
        form.addCommand(startClient);
        form.addCommand(exitCommand);
        form.setCommandListener(this);
        display.setCurrent(form);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
    public void commandAction(Command c, Displayable d) {
        if(c==exitCommand) {
            if(Server.getserverresumed()) {
                s.stopserver();
            }
            destroyApp(false);
             notifyDestroyed();
        }
        else if(c==startServer) {
            form.deleteAll();
            new Thread(new Runnable() {
                public void run() {
                    if(Server.getserverresumed()) {
                        form.append("\n Server is listening at Port"+portNo);
                    }
                    else {
                    s=new Server(portNo);
                    try {
                    s.startConnection();
                    }catch(IOException e) {
                        form.append("\n"+e.getMessage());
                    }
                  }
                  }
            }
            ).start();
            form.addCommand(stopServer);
                    form.removeCommand(startServer);
                    form.removeCommand(startClient);
                    form.setCommandListener(this);
                    display.setCurrent(form);
        } else if(c==startClient) {
            form.deleteAll();
            new Thread(new Runnable() {
                public void run() {
                    
                    cl=new Client(addr);
                    try {
                    cl.runClient();
                    }catch(IOException e) {
                        form.append("\n"+e.getMessage());
                    }
                  }
                  
            }
            ).start();
            form.addCommand(backCommand);
                    form.removeCommand(startServer);
                    form.removeCommand(startClient);
                    form.setCommandListener(this);
                    display.setCurrent(form);
        } else if(c==backCommand) {
            form.deleteAll();
            form.removeCommand(backCommand);
            form.append("Test the Socket Demo....");
            form.addCommand(startServer);
            form.addCommand(startClient);
             form.setCommandListener(this);
             display.setCurrent(form);
        }
        else if(c==stopServer) {
            if(Server.getserverresumed()) {
                s.stopserver();
            }
            form.deleteAll();
            form.removeCommand(stopServer);
            
            form.append("Test the Socket Demo....");
            
            form.addCommand(startServer);
            form.addCommand(startClient);
             form.setCommandListener(this);
             display.setCurrent(form);
        }
        
    }

}
