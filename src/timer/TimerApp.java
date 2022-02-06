package timer;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 *
 * @author: Dejan Smiljić; e-mail: dej4n.s@gmail.com
 *
 */
public class TimerApp {

    private static int chosenOption;
    private static final String[] OPTIONS = {"Settings", "Close"};

    public static void main(String[] args) {

        //NimbusLookAndFeel nimbusLaF = new NimbusLookAndFeel();
        /*try {
        UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException ulafExc) {
        ulafExc.getStackTrace();
        }*/

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException multiExc) {
            System.out.println(multiExc);
        }

        chosenOption = JOptionPane.showOptionDialog(null, "     Proceed to settings...", "Timer Application", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, OPTIONS, OPTIONS[0]);
        //if (chosenOption == 1) {
        //System.exit(0); // Closes the dialog, code below has the same effect
        /*Frame dialogFrame = JOptionPane.getRootFrame();
          dialogFrame.dispatchEvent(new WindowEvent(dialogFrame, WindowEvent.WINDOW_CLOSING));*/
        //} else
        if (chosenOption == 0) {
            try {
                EventQueue.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {

                        MainFrame mainWindow = new MainFrame();
                        mainWindow.setVisible(true);
                    }
                });
            } catch (InterruptedException | InvocationTargetException multiExc) {
                System.out.println(multiExc);
            }
        }

    }// main() END

}
