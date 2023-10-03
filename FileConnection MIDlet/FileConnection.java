import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.io.*;
import javax.microedition.io.*;

public class FileConnection extends MIDlet implements CommandListener {

private Command exit;
private Command start;
private Display display;
private Form form;

public FileConnection() {
display = Display.getDisplay(this);
exit = new Command("Exit", Command.EXIT, 1);
start = new Command("Start", Command.EXIT, 1);
form = new Form("Write To File");
form.addCommand(exit);
form.addCommand(start);
form.addCommandListener(this);
}

public void startApp() throws MIDletStateChangeException {
 display.setCurrent(form);
}

public void pauseApp() {
}

public void destroyApp(boolean uncond){
}

public void commandAction(Command command, Displayable displayable){
	if(command == exit){
	 destroyApp(false);
	 notifyDestroyed();
	}
	else if(command == start){
	 try{
		OutputConnection connection = (OutputConnection) 
		Connector.open("file:///c:/myfile.txt;append=true", Connector.WRITE);
		OutputStream out = connection.openOutputStream();
		PrintStream output = new PrintStream(out);
		output.println("This is a test.");
		out.close();
		connection.close();
		Alert alert = new Alert("Completed", "Data Written",null,null);
		alert.setTimeout(Alert.FOREVER);
		alert.setType(AlertType.ERROR);
		display.setCurrent(alert);
	 }//end try
	 catch(ConnectionNotFoundException error){
		Alert alert = new Alert("Error", "Cannot access file",null,null);
		alert.setTimeout(Alert.FOREVER);
		alert.setType(AlertType.ERROR);
		display.setCurrent(alert);
	 }//end catch

	 catch(IOException error){
		Alert alert = new Alert("Error", error.toString(),null,null);
		alert.setTimeout(Alert.FOREVER);
		alert.setType(AlertType.ERROR);
		display.setCurrent(alert);
	 }//end catch

	}//end elseif
}//end commandAction()


}//end class