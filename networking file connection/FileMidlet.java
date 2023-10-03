/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package networking;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;



/**
 * @author subhankar
 */
public class FileMidlet extends MIDlet implements CommandListener{
    
    private static final String SERVER = "FileSender";
    private static final String CLIENT = "Client";
    private static final String[] names = { SERVER, CLIENT };
    private Display disp;
    private Form f;
    private ChoiceGroup cg;
    private TextField portField;
    private Command exit;
    private Command ok;
    private ReceiverClient receiver;
    private SenderServer sender;
    
    public FileMidlet(){
        disp = Display.getDisplay(this);
        exit = new Command("Exit", Command.EXIT, 1);
        ok = new Command("Ok", Command.ITEM, 1);
        f = new Form("Sender_Receiver");
        cg = new ChoiceGroup("Please select one", Choice.EXCLUSIVE, names, null);
        portField = new TextField("Port No: ", "5000", 5, TextField.NUMERIC);
        f.append(cg);
        f.append(portField);
        f.addCommand(exit);
        f.addCommand(ok);
        f.setCommandListener(this);
    }
    
    public void startApp() {
        disp.setCurrent(f);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
    
    public void commandAction(Command c, Displayable d){
        if(c == exit){
            destroyApp(false);
	    notifyDestroyed();
        }
        else
            if(c == ok){
                String name = cg.getString(cg.getSelectedIndex());
                
                if (name.equals(CLIENT)) {
                    receiver = new ReceiverClient(this, Integer.parseInt(portField.getString()));
                } else {
                    sender = new SenderServer(this, Integer.parseInt(portField.getString()));
                }
            }
    }
}
