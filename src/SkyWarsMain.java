import javax.swing.*;

public class SkyWarsMain {

	public static void main(String[] args) {
		//Creating a new instance of the game and ensuring the GUI is created on
		//the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SkyWars.WelcomeFrame();
            }
        });
	}

}
