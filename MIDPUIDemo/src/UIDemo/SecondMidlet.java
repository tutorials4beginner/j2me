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
public class SecondMidlet extends MIDlet implements CommandListener {
    private Command exitCommand = new Command("Exit", Command.EXIT, 1);
    private Command CMD_BACK = new Command("Back", Command.BACK, 1);
    private Command showList = new Command("show List", Command.ITEM, 2);
     private Display display;
    private boolean firstTime;
    private Form mainForm;
    private List mainList;
    private ChoiceGroup choices;
            private int[] listtypes={Choice.EXCLUSIVE, Choice.IMPLICIT, Choice.MULTIPLE };
    String[] listOptions={ "Exclusive", "Implicit", "Multiple" };
    String[] stringArray = { "Option A", "Option B", "Option C", "Option D" };
    Image[] imageArray=null;

/** Creates a new instance of SecondMidlet */
    public SecondMidlet() {
        firstTime = true;
        mainForm = new Form("Text Field");
        display=Display.getDisplay(this);
        
    }
    public void startApp() {
        if (firstTime) {
             try {
                // load the duke image to place in the image array
                Image icon = Image.createImage("/midp/uidemo/Icon.png");

                // these are the images and strings for the choices.
                imageArray = new Image[] { icon, icon, icon };
            } catch (java.io.IOException err) {
                // ignore the image loading failure the application can recover.
            }
             choices=new ChoiceGroup("Pop-Up", ChoiceGroup.POPUP, listOptions, imageArray);
             //choices.setItemCommandListener(this);
             

            mainForm.append(" Hello !!!This demo contains only text fields and ChoiceGroup");

            mainForm.append(new TextField("Name", "", 15, TextField.ANY));
            mainForm.append(new TextField("E-Mail", "", 15, TextField.EMAILADDR));
            mainForm.append(new TextField("Number", "", 15, TextField.NUMERIC));
           
            mainForm.append(new TextField("Phone", "", 15, TextField.PHONENUMBER));
            mainForm.append(new TextField("Password", "", 15, TextField.PASSWORD));
            mainForm.append(new TextField("URL", "", 15, TextField.URL));
            mainForm.append(choices);
            mainForm.addCommand(showList);
            mainForm.addCommand(exitCommand);
            mainForm.setCommandListener(this);
            firstTime = false;
        }

        display.setCurrent(mainForm);

    }
    public void commandAction(Command c, Displayable s) {
        if (c == exitCommand) {
            destroyApp(false);
            notifyDestroyed();
        }
        else if(c == showList) {
             try {
                // load the duke image to place in the image array
                Image duke = Image.createImage("/midp/uidemo/Duke.png");
                // these are the images and strings for the choices.
                imageArray = new Image[] { duke, duke, duke, duke };
            } catch (java.io.IOException err) {
                // ignore the image loading failure the application can recover.
            }
           int i=choices.getSelectedIndex();
            
           mainList = new List(listOptions[i], listtypes[i], stringArray, imageArray);
            mainList.addCommand(CMD_BACK);
            mainList.setCommandListener(this);
            display.setCurrent(mainList);

        }
        else if(c == CMD_BACK)
        {
            display.setCurrent(mainForm);
        }
    }


    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
