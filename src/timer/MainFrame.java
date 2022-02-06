package timer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.Timer;

/**
 *
 * @author: Dejan Smiljić; e-mail: dej4n.s@gmail.com
 *
 */
public class MainFrame extends JFrame {

    private SettingsPanel settingsPanel;
    private JPanel controlPanel;
    private JButton startButton;
    protected static JButton stopButton;
    protected static JLabel infoLabel;
    private JFrame colorfulWindow;
    private String presetTime;
    private int presetHours;
    private int presetMinutes;
    private int presetSeconds;
    private int presetMiliseconds;
    private LocalTime currentLocalTime;
    private int currentMiliseconds;
    private int finalMiliseconds;
    private long hours;
    private long minutes;
    private long seconds;
    private Component[] components;
    private Timer initialTimer;
    private SwingWorker workerThread;

//<editor-fold defaultstate="collapsed" desc="Private Inner Non-Static Class - Derived JFrame, instantiates through button event">
    private class ColorfulWindow extends JFrame {
        
        public ColorfulWindow() {
            
            initComponents();
        }
        
        private void initComponents() {
            
            this.setSize(250, 250);
            this.setLocation(850, 200);
            this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            this.setResizable(false);
            
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Private Inner Non-Static Class - ActionListener interface custom implementation; Target: JButton">
    private class ButtonCustomActionListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
            if ("start".equals(e.getActionCommand())) {
                System.out.println("button: start");//OBRISATI
                colorfulWindow = new ColorfulWindow();
                
                if (settingsPanel.buttonGroup.getSelection() == null) {
                    infoLabel.setText("Set the time");
                    settingsPanel.buttonGroup.setSelected(settingsPanel.radioBtnTime.getModel(), true);
                } else if (settingsPanel.radioBtnTime.isSelected()) {
                    //Input value check
                    if (settingsPanel.formattedTxtFieldTime.getValue() != null) {
                        //Saving entered value into String presetTime variable
                        presetTime = settingsPanel.formattedTxtFieldTime.getText();
                        //Parsing the value into hours
                        presetHours = Integer.parseInt(presetTime.split(":")[0]);
                        if (presetHours > 23) {
                            infoLabel.setText("Invalid Time Entry - Hours greater than 23");
                            settingsPanel.formattedTxtFieldTime.requestFocusInWindow();
                            if (stopButton.isEnabled()) {
                                stopButton.setEnabled(false);
                            }
                            return;
                        }
                        //Parsing the value into minutes
                        presetMinutes = Integer.parseInt(presetTime.split(":")[1]);
                        if (presetMinutes > 59) {
                            infoLabel.setText("Invalid Time Entry - Minutes greater than 59");
                            settingsPanel.formattedTxtFieldTime.requestFocusInWindow();
                            if (stopButton.isEnabled()) {
                                stopButton.setEnabled(false);
                            }
                            return;
                        }
                        //Parsing the value into seconds
                        presetSeconds = Integer.parseInt(presetTime.split(":")[2]);
                        if (presetSeconds > 59) {
                            infoLabel.setText("Invalid Time Entry - Seconds greater than 59");
                            settingsPanel.formattedTxtFieldTime.requestFocusInWindow();
                            if (stopButton.isEnabled()) {
                                stopButton.setEnabled(false);
                            }
                            return;
                        }
                        
                        System.out.println("preset time " + presetTime);//OBRISATI
                        //User input time to miliseconds conversion
                        presetMiliseconds = ((presetHours * 60 + presetMinutes) * 60 + presetSeconds) * 1000;
                        //Current time retrieval
                        currentLocalTime = LocalTime.now();
                        //Current time to miliseconds conversion
                        currentMiliseconds = ((currentLocalTime.getHour() * 60 + currentLocalTime.getMinute()) * 60 + currentLocalTime.getSecond()) * 1000;
                        //OBRISATI liniju ispod
                        System.out.println("current time " + currentLocalTime.getHour() + ":" + currentLocalTime.getMinute() + ":" + currentLocalTime.getSecond());
                        //Getting final value
                        finalMiliseconds = presetMiliseconds - currentMiliseconds;
                        if (finalMiliseconds < 0) {
                            finalMiliseconds = 24 * 60 * 60 * 1000 + finalMiliseconds;
                        }
                        //Miliseconds to human readable time conversion
                        hours = TimeUnit.MILLISECONDS.toHours(finalMiliseconds);
                        minutes = TimeUnit.MILLISECONDS.toMinutes(finalMiliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(finalMiliseconds));
                        seconds = TimeUnit.MILLISECONDS.toSeconds(finalMiliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(finalMiliseconds));
                        System.out.println("event in " + hours + ":" + minutes + ":" + seconds);//OBRISATI
                        //Setting up display text
                        if (hours == 0) {
                            if (minutes == 0) {
                                infoLabel.setText(String.format("Event will occur in:   %02d sec", seconds));
                            } else {
                                infoLabel.setText(String.format("Event will occur in:   %02dmin : %02dsec", minutes, seconds));
                            }
                        } else {
                            infoLabel.setText(String.format("Event will occur in:   %02dh : %02dmin : %02dsec", hours, minutes, seconds));
                        }
                        
                        //Timer setup
                        initialTimer = new Timer(finalMiliseconds, initialTimerActionListener);
                        initialTimer.setRepeats(false);
                        initialTimer.start();
                        
                        //Disabling components, enabling stopButton
                        stopButton.setEnabled(true);
                        enableComponents(settingsPanel, false);
                        startButton.setEnabled(false);
                        
                    } else {
                        infoLabel.setText("Set the time");
                        if (stopButton.isEnabled()) {
                            stopButton.setEnabled(false);
                        }
                    }
                    
                }//IF settingsPanel.radioBtnTime.isSelected END
                else if (settingsPanel.radioBtnCountdown.isSelected()) {
                    if (settingsPanel.formattedTxtFieldCountdown.getValue() != null) {
                        
                        //Saving entered value into int presetSeconds variable
                        System.out.println(settingsPanel.formattedTxtFieldCountdown.getValue() + " sec");//OBRISATI
                        presetSeconds = ((Integer) settingsPanel.formattedTxtFieldCountdown.getValue()) * 1000;
                        System.out.println(presetSeconds + " ms");//OBRISATI
                        //Timer setup
                        initialTimer = new Timer(presetSeconds, initialTimerActionListener);
                        initialTimer.setRepeats(false);
                        initialTimer.start();
                        
                        //Disabling components, enabling stopButton
                        enableComponents(settingsPanel, false);
                        startButton.setEnabled(false);
                        stopButton.setEnabled(true);
                        
                        infoLabel.setText("Twiddling thumbs ... ");
                        
                    } else {
                        infoLabel.setText("Set the time");
                        if (stopButton.isEnabled()) {
                            stopButton.setEnabled(false);
                        }
                        
                    }
                }//IF settingsPanel.radioBtnCountdown.isSelected END
                
                //IF startButton END; IF stopButton BEGIN
            } else if ("stop".equals(e.getActionCommand())) {
                
                System.out.println("button: stop");//OBRISATI
                if (initialTimer.isRunning()) {
                    initialTimer.stop();
                    colorfulWindow.dispose();
                    System.out.println("timer halted");//OBRISATI
                    System.out.println("timer running - " + initialTimer.isRunning());//OBRISATI
                } else if (!workerThread.isCancelled()) {
                    System.out.println("worker thread is running");//OBRISATI
                    workerThread.cancel(true);
                    colorfulWindow.dispose();
                    System.out.println("worker thread canceled - " + workerThread.isCancelled());//OBRISATI
                }
                //Enabling components, disabling stopButton
                stopButton.setEnabled(false);
                enableComponents(settingsPanel, true);
                settingsPanel.buttonGroup.clearSelection();
                settingsPanel.radioBtnTime.requestFocusInWindow();
                startButton.setEnabled(true);
                
                infoLabel.setText("Tidying up brushes ...");
            }
        }
        
    }
//</editor-fold>

    public MainFrame() {

        initComponents();
    }

    private void initComponents() {

        this.setTitle("Timer App");
        this.setSize(280, 280);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(20, 0));
        this.setResizable(false);

        settingsPanel = new SettingsPanel();
        this.add(settingsPanel, BorderLayout.PAGE_START);

        controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 35, 12));

        infoLabel = new JLabel("Let's do some pane painting!");

        startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(80, 28));
        startButton.setActionCommand("start");
        startButton.addActionListener(new ButtonCustomActionListener());

        stopButton = new JButton("Stop");
        stopButton.setPreferredSize(new Dimension(80, 28));
        stopButton.setActionCommand("stop");
        stopButton.setEnabled(false);
        stopButton.addActionListener(new ButtonCustomActionListener());

        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        controlPanel.add(infoLabel, FlowLayout.RIGHT);
        this.add(controlPanel, BorderLayout.CENTER);

    }//initComponents() END

    //Disables/Enables container's component set
    private void enableComponents(JComponent component, boolean state) {
        components = component.getComponents();
        for (Component c : components) {
            c.setEnabled(state);
        }
    }
    
    ActionListener initialTimerActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            startButton.setEnabled(false);
            
            //Instead of putting thread to sleep consider using Timer class for processing window color change
            workerThread = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    //OBRISATI liniju ispod
                    System.out.println(Thread.currentThread().getName() + "; id: " + Thread.currentThread().getId());

                    while (!isCancelled()) {
                        infoLabel.setText("Painting in process ...");
                        colorfulWindow.setVisible(true);
                        //colorfulWindow.getContentPane().setBackground(settingsPanel.chosenColor);

                        int flashSpeed = (Integer) settingsPanel.jcBoxAlternation.getSelectedItem();
                        switch (flashSpeed) {

                            case 1:
                                colorfulWindow.getContentPane().setBackground(settingsPanel.chosenColor);
                                Thread.sleep(1000);
                                colorfulWindow.getContentPane().setBackground(Color.WHITE);
                                Thread.sleep(1000);
                                break;
                            case 2:
                                colorfulWindow.getContentPane().setBackground(settingsPanel.chosenColor);
                                Thread.sleep(2000);
                                colorfulWindow.getContentPane().setBackground(Color.WHITE);
                                Thread.sleep(2000);
                                break;
                            case 3:
                                colorfulWindow.getContentPane().setBackground(settingsPanel.chosenColor);
                                Thread.sleep(3000);
                                colorfulWindow.getContentPane().setBackground(Color.WHITE);
                                Thread.sleep(3000);
                                break;
                            case 4:
                                colorfulWindow.getContentPane().setBackground(settingsPanel.chosenColor);
                                Thread.sleep(4000);
                                colorfulWindow.getContentPane().setBackground(Color.WHITE);
                                Thread.sleep(4000);
                                break;
                            case 5:
                                colorfulWindow.getContentPane().setBackground(settingsPanel.chosenColor);
                                Thread.sleep(5000);
                                colorfulWindow.getContentPane().setBackground(Color.WHITE);
                                Thread.sleep(5000);
                                break;
                        }
                    }//While() END
                    return null;
                }
            };//SwingWorker END
            workerThread.execute();
        }
    };
}
