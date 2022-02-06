package timer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author: Dejan Smiljić; e-mail: dej4n.s@gmail.com
 *
 */
public class SettingsPanel extends JPanel {

    private Border border;
    private RadioButtonItemListener radioItemListener;
    protected JRadioButton radioBtnTime;
    protected JRadioButton radioBtnCountdown;
    protected ButtonGroup buttonGroup;
    protected JFormattedTextField formattedTxtFieldTime;
    protected JFormattedTextField formattedTxtFieldCountdown;
    private NumberFormat numberFormat;
    private CustomNumberFormatter numberFormatter;
    private JButton pickColorButton;
    private final String BTN_TEXT = "<html><center>Choose<br />color</center></html>";
    private JLabel colorLabel;
    private final String LABEL_INI_TXT = "<html><center>No color<br />selected</center></html>";
    private JColorChooser colorPicker;
    protected Color chosenColor = null;
    protected JComboBox<Integer> jcBoxAlternation;
    private JLabel comboBoxLabel;

//<editor-fold defaultstate="collapsed" desc="Private Inner Non-static Class - ItemListener interface custom implementation; Target: JRadioButton">
    private class RadioButtonItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {

            JRadioButton button = (JRadioButton) e.getSource();
            MainFrame.infoLabel.setText("Planning time ...");

            if (MainFrame.stopButton.isEnabled()) {
                MainFrame.stopButton.setEnabled(false);
            }
            if (button == radioBtnTime) {
                
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    formattedTxtFieldTime.setEnabled(true);
                    formattedTxtFieldTime.requestFocusInWindow();
                } else if (state == ItemEvent.DESELECTED) {
                    formattedTxtFieldTime.setValue(null);
                    formattedTxtFieldTime.setEnabled(false);
                }
            } else if (button == radioBtnCountdown) {
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    formattedTxtFieldCountdown.setEnabled(true);
                    formattedTxtFieldCountdown.requestFocusInWindow();
                } else if (state == ItemEvent.DESELECTED) {
                    formattedTxtFieldCountdown.setValue(null);
                    formattedTxtFieldCountdown.setEnabled(false);
                }
            }
        }

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Custom Implementation of the NumberFormatter class - Overrides stringToValue() method ">
    private class CustomNumberFormatter extends NumberFormatter {

        public CustomNumberFormatter(NumberFormat format) {
            super(format);
        }

        @Override
        public void setFormat(Format format) {
            super.setFormat(format);
        }

        @Override
        public Object stringToValue(String text) throws ParseException {
            //Allows empty String in parent component (formattedTxtFieldCountdown)
            if (text.isEmpty()) {
                return null;
            }
            return super.stringToValue(text);
        }

    }
//</editor-fold>

    public SettingsPanel() {

        initComponents();

    }

    private void initComponents() {

        this.setPreferredSize(new Dimension(400, 170));
        this.setLayout(null);

        border = BorderFactory.createTitledBorder(border, "Settings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Coutirer New", Font.ITALIC, 11));
        this.setBorder(border);

        /*Creating ButtonGroup & JRadioButton components; 
        Adding JRadioButton components to the ButtonGroup & the container;*/
        radioBtnTime = new JRadioButton("On time : ");
        radioBtnTime.setBounds(12, 25, 100, 18);
        radioBtnCountdown = new JRadioButton("Countdown (seconds) : ");
        radioBtnCountdown.setBounds(12, 55, 158, 18);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(radioBtnTime);
        buttonGroup.add(radioBtnCountdown);
        this.add(radioBtnTime);
        this.add(radioBtnCountdown);

        //Instantiating & assigning ItemListener to existing JRadioButtons;
        radioItemListener = new RadioButtonItemListener();
        radioBtnTime.addItemListener(radioItemListener);
        radioBtnCountdown.addItemListener(radioItemListener);

        //Creating JFormattedTextField component formattedTxtFieldTime; Adding component to the panel;
        try {
            formattedTxtFieldTime = new JFormattedTextField(new MaskFormatter("##:##:##"));

        } catch (ParseException pExc) {
            System.out.println(pExc);
        }
        /*To do: prevent invalid entries, eg. 44:66:77
        JTextField and DocumentFilter classes should be used.*/
        formattedTxtFieldTime.setBounds(182, 23, 60, 22);
        formattedTxtFieldTime.setEnabled(false);
        this.add(formattedTxtFieldTime);

        //Creating JFormattedTextField component formattedTxtFieldCountdown; Adding component to the panel;
        //Custom format [numberFormatter] prevents faulty entries
        numberFormat = NumberFormat.getInstance();
        numberFormatter = new CustomNumberFormatter(numberFormat);
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setMinimum(1);
        numberFormatter.setMaximum(59);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setCommitsOnValidEdit(true);
        formattedTxtFieldCountdown = new JFormattedTextField(numberFormatter);

        formattedTxtFieldCountdown.setBounds(182, 53, 60, 22);
        formattedTxtFieldCountdown.setEnabled(false);
        this.add(formattedTxtFieldCountdown);

        //Creating JButton and JLabel components; Adding components to the panel;
        pickColorButton = new JButton();
        pickColorButton.setText(BTN_TEXT);
        pickColorButton.setBounds(20, 105, 78, 42);
        //pickColorButton.setEnabled(false);
        this.add(pickColorButton);

        colorLabel = new JLabel();
        colorLabel.setText(LABEL_INI_TXT);
        colorLabel.setBounds(118, 104, 100, 44);
        this.add(colorLabel);

        pickColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.infoLabel.setText("Openning paint buckets ...");
                colorPicker = new JColorChooser();
                JDialog dialog = JColorChooser.createDialog(null, "Color menu", true, colorPicker, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        chosenColor = colorPicker.getColor();
                        int red = chosenColor.getRed();
                        int green = chosenColor.getGreen();
                        int blue = chosenColor.getBlue();
                        colorLabel.setText("<html>R: " + red + "<br />G: " + green + "<br />B: " + blue + "</html>");
                        colorLabel.setForeground(chosenColor);
                        pickColorButton.setForeground(chosenColor);

                    }
                }, null);//.createDialog() END

                dialog.setVisible(true);

            }//actionPerformed() END

        });//.addActionListener() END

        //Creating JCombBox component; Adding ItemListener to the component; Adding component to the panel;
        //Creating JLabel component associated to JComboBox
        comboBoxLabel = new JLabel("Speed");
        comboBoxLabel.setBounds(200, 103, 50, 20);
        this.add(comboBoxLabel);

        jcBoxAlternation = new JComboBox<>();
        jcBoxAlternation.setModel(new DefaultComboBoxModel<>(new Integer[]{1, 2, 3, 4, 5}));
        jcBoxAlternation.setSelectedIndex(2);
        jcBoxAlternation.setBounds(198, 124, 40, 20);
        this.add(jcBoxAlternation);

    }//initComponents() END

}
