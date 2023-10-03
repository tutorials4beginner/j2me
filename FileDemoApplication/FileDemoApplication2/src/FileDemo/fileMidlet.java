/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package FileDemo;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
//import javax.microedition.io.file.FileConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

import javax.microedition.io.Connector;
import java.util.Enumeration;
import javax.microedition.io.file.*;
//import javax.microedition.

/**
 * @author Administrator
 */
public class fileMidlet extends MIDlet implements CommandListener {
    
    private Form form;
    private Display display;
     private static final String MEGA_ROOT = "/";

    /* separator string as defined by FC specification */
    private static final String SEP_STR = "/";

    /* separator character as defined by FC specification */
    private static final char SEP = '/';
    private String currDirName;
    private List browser;
    private TextField nameInput;
    private Command view = new Command("View", Command.ITEM, 1);
    private Command creat = new Command("New", Command.ITEM, 2);
     private Command back=new Command("Go Up",Command.ITEM,2);
    //add delete file functionality
    //private Command delete = new Command("Delete", Command.ITEM, 3);
    private Command creatOK = new Command("OK", Command.OK, 1);
    private Command exit = new Command("Exit",Command.EXIT,1);
    private String msg="This is the first Demo File";
    public fileMidlet() {
        currDirName = MEGA_ROOT;
        form=new Form("File Creation form");
        display=Display.getDisplay(this);
    }
    public void startApp() {
         try {
            showCurrDir();
            
        } catch (SecurityException e) {
            Alert alert =
                new Alert("Error", "You are not authorized to access the restricted FileSystem", null,
                    AlertType.ERROR);
            alert.setTimeout(Alert.FOREVER);
            alert.addCommand(exit);
             alert.setCommandListener(this);
        display.setCurrent(alert);
    }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
    
   
     public void commandAction(Command c, Displayable s) {
        if (c == exit) {
            destroyApp(false);
            notifyDestroyed();
        } 
        else if (c == view) {
            List curr = (List)s;
            final String currFile = curr.getString(curr.getSelectedIndex());

             new Thread(new Runnable() {
               public void run() {
                   if(currFile.endsWith(SEP_STR))
                   {
                       if(currDirName.equals(MEGA_ROOT))
                       {
                           currDirName=currFile;
                       }
                       else
                       {
                        currDirName=currDirName+currFile;
                       }
                    showCurrDir();
                   }
                   else {
                   showFile(currFile);
                   }
               }
               }
           ).start();
        }
        else if (c == back) {
            if (currDirName.equals(MEGA_ROOT)) {
                // can not go up from MEGA_ROOT
                return;
            } else {
            // Go up one directory
            // TODO use setFileConnection when implemented
            int i = currDirName.lastIndexOf(SEP, currDirName.length() - 2);

            if (i != -1) {
                currDirName = currDirName.substring(0, i + 1);
            } else {
                currDirName = MEGA_ROOT;
            }
        } 
              form.deleteAll();
             new Thread(new Runnable() {
               public void run() {
                   
                    showCurrDir();
                   }
                   
               }
              
           ).start();
        }
        else if(c==creat) {
                  List curr = (List)s;
            final String currFolder = curr.getString(curr.getSelectedIndex());
                   if(currDirName.equals(MEGA_ROOT))
                       {
                           currDirName=currFolder;
                       }
                       else
                       {
                        currDirName=currDirName+currFolder;
                       }        
                   createFile();
           form.setCommandListener(this);
            display.setCurrent(form);
        }
        else if(c==creatOK) {
            final String fileName=nameInput.getString();
           new Thread(new Runnable() {
               public void run() {
                   CreateFile(fileName);
               }
               }
           ).start();
           form.setCommandListener(this);
           display.setCurrent(form);
        }
    }
     private void showCurrDir() {
        Enumeration e;
        FileConnection currDir = null;
        
        try {
            if (MEGA_ROOT.equals(currDirName)) {
                e = FileSystemRegistry.listRoots();
                browser = new List(currDirName, List.IMPLICIT);
            } else {
                currDir = (FileConnection)Connector.open("file://localhost/" + currDirName);
                e = currDir.list();
                browser = new List(currDirName, List.IMPLICIT);
                // not root - draw UP_DIRECTORY
               
            }

           Image dirIcon=Image.createImage("/icons/dir.png");
             Image fileIcon=Image.createImage("/icons/file.png");
            while (e.hasMoreElements()) {
                String fileName = (String)e.nextElement();

                if (fileName.charAt(fileName.length() - 1) == SEP) {
                    // This is directory
                    browser.append(fileName, dirIcon);
                } else {
                    // this is regular file
                    browser.append(fileName, fileIcon);
                    
                }
               }

            browser.setSelectCommand(view);

            //Do not allow creating files/directories beside root
            if (!MEGA_ROOT.equals(currDirName)) {
                browser.addCommand(creat);
                browser.addCommand(back);
                //browser.addCommand(delete);
            }

            if (currDir != null) {
                currDir.close();
            }
           // browser.addCommand(creat);
           browser.addCommand(exit);
            browser.setCommandListener(this);
            display.setCurrent(browser);
        } catch (IOException ioe) {
            browser.append(ioe.getMessage(), null);
            ioe.printStackTrace();
        }
    }
     void showFile(String fileName) {
        try {
            FileConnection fc =
                (FileConnection)Connector.open("file://localhost/" + currDirName + fileName);
            

            if (!fc.exists()) {
               fc.create(); 
            }
            
           
            int fileSize=(int)fc.fileSize();
            //StringBuffer sb=new StringBuffer(fileSize);
            byte[] b = new byte[fileSize];
             InputStream fis = fc.openInputStream();
            int bytesRead=fis.read(b);
            /*int bytesRead=0;
            while(bytesRead<fileSize && fis.read()!=-1)
            {
                int rem=fileSize-bytesRead;
            int actual = fis.read(b,bytesRead,rem);
            bytesRead+=actual;
            //sb.append(new String(b,bytesRead,b.length));
            }*/
            fis.close();
            fc.close();
            form.deleteAll();
            StringBuffer sb=new StringBuffer(fileSize);
            for(int i=0; i<b.length; i++)
            {
                char ch=(char) b[i];
                sb.append(ch);
            }
            if (bytesRead > 0) {
              form.append(sb.toString());
            }
            else {
               form.append("File is empty");  
            }
            form.removeCommand(view);
            form.removeCommand(creat);
            form.addCommand(back);
            form.addCommand(exit);
            form.setCommandListener(this);
            display.setCurrent(form);
            //display.setCurrent(viewer);
        } catch (IOException e) {
            Alert alert =
                new Alert("Error!",
                    "Can not access file " + fileName + " in directory " + currDirName +
                    "\nException: " + e.getMessage(), null, AlertType.ERROR);
            alert.setTimeout(Alert.FOREVER);
            display.setCurrent(alert);
        }
    }

void createFile() {
        //Form creator = new Form("New File");
        form.deleteAll();
        nameInput = new TextField("Enter Name", null, 256, TextField.ANY);
        //typeInput = new ChoiceGroup("Enter File Type", Choice.EXCLUSIVE, typeList, iconList);
        form.append(nameInput);
        //creator.append(typeInput);
        //creator.removeCommand(view)
        form.removeCommand(creat);
        form.addCommand(creatOK);
        form.addCommand(back);
        form.addCommand(exit);
        //creator.addCommand(back);
        //form.addCommand(exit);
        
    }
 private void CreateFile(String fileName )
    {
        try {
     FileConnection fconn = (FileConnection)Connector.open("file://localhost/" + currDirName + fileName);
     // If no exception is thrown, then the URI is valid, but the file may or may not exist.
     if (!fconn.exists())
         fconn.create();  // create the file if it doesn't exist
     OutputStream os=fconn.openOutputStream();
     os.write(msg.getBytes());
     os.flush();
     os.close();
     fconn.close();
     form.deleteAll();
     form.append("\n A new file is created with name:"+fileName);
     form.removeCommand(creatOK);
 }
 catch (IOException ioe) {
     form.append("\n "+ioe.getMessage());
 }
    }



}

