/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package UIDemo;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * @author Administrator
 */
public class FirstMidlet extends MIDlet implements CommandListener {
    
    private Form form;
    private ChoiceGroup types;
    private static final String[] typeStrings =
        { "Alarm", "Confirmation", "Error", "Info", "Warning" };
    private boolean midletPaused;
    private Command exitCommand; 
    private Command stopCommand;
     private Command startCommand;
     private Command showCommand;
     AlertType[] alertTypes={
                AlertType.ALARM, AlertType.CONFIRMATION, AlertType.ERROR, AlertType.INFO,
                AlertType.WARNING
            };
    private Display display;
    public FirstMidlet() {
    midletPaused=true;
      form=new Form("Alert options");
      exitCommand = new Command("Exit", Command.EXIT, 1);
      stopCommand = new Command("Stop Midlet", Command.STOP, 1);
      startCommand = new Command("Start Midlet", Command.ITEM, 2);
      showCommand=new Command("Show Alert", Command.ITEM, 2);
    }


    public void startApp() {
        display=Display.getDisplay(this);
        //form.deleteAll();
        if(midletPaused) {
            initialize();
        }
        form.addCommand(showCommand);
        form.addCommand(exitCommand);
        form.addCommand(stopCommand);
        display.setCurrent(form);
        midletPaused=false;

    }

    public void pauseApp() {
        midletPaused=true;
        form.removeCommand(showCommand);
        form.removeCommand(stopCommand);
        form.deleteAll();
        form.append("Midlet is paused");
        form.addCommand(startCommand);
        form.setCommandListener(this);
        display.setCurrent(form);

    }

    public void destroyApp(boolean unconditional) {
    }
    private void initialize() {
        types=new ChoiceGroup("Type", ChoiceGroup.EXCLUSIVE, typeStrings, null);
       form.append("This is the first Midlet");
        form.append(types);
        form.setCommandListener(this);
      }
public void commandAction(Command c, Displayable s) {
        if (c == exitCommand) {
            destroyApp(false);
            notifyDestroyed();
        }
        else if(c == stopCommand) {
            pauseApp();
        }
        else if(c==startCommand) {
            form.removeCommand(startCommand);
            form.deleteAll();
            startApp();
        }
         else if(c==showCommand) {
             int typeIndex = types.getSelectedIndex();
                Alert alert = new Alert("Alert");
                alert.setType(alertTypes[typeIndex]);
                alert.setString(typeStrings[typeIndex] + " Alert, Running "); 
                //alert.setTimeout(2000);
                display.setCurrent(alert);
         }
      }

}
