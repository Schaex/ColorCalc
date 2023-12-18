import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UI extends ColorCalcs {
    /**
     * Constants for easy access
     */
    static final int WIDTH = 1200;
    static final int HEIGHT = 650;
    static final Color STARTUP_COLOR = new Color(0, 0, 0);
    static final Color GRAY_BACKGROUND = new Color(238, 238, 238);

    /**
     * All UI elements
     */
    /**
     * Main window
     */
    static JFrame mainFrame = new JFrame("Color Calc");

    /**
     * Panels that contain all the elements
     */
    static JPanel inputPanel = new JPanel();
    static JPanel examplePanel = new JPanel();
    static JPanel optionsPanel = new JPanel();
    static JPanel colorPanel = new JPanel();
    static JPanel outputPanel = new JPanel();

    /**
     * Text input and output
     */
    static JTextField inputField = new JTextField(20);
    static JTextArea outputText = new JTextArea(6, 0);
    static final Font font = new Font(Font.MONOSPACED, Font.PLAIN, 14);

    /**
     * Option buttons
     */
    static JButton buttonColorChooser = new JButton("Color Chooser");
    static JButton buttonRGB = new JButton("from RGB");
    static JButton buttonHEX = new JButton("from HEX");
    static JButton buttonDEC = new JButton("from DEC");
    static JButton buttonHSBHSV = new JButton("from HSB/HSV");
    static JButton buttonHSL = new JButton("from HSL");
    static JButton buttonCMYK = new JButton("from CMYK");


    /**
     * Default constructor with all the important UI settings
     */
    public UI() {
        setInputLayout();
        setExampleLayout();
        setOptionsLayout();
        setOutputLayout();

        setMainLayout();

        setButtonActionListeners();


        createOutput(STARTUP_COLOR);

        mainFrame.setVisible(true);
    }

    /**
     * Helper methods to make the constructor less cluttered
     */

    /**
     * Sets a GroupLayout for the inputPanel
     */
    static void setInputLayout() {
        inputPanel.setSize(WIDTH * 8 / 10, 50);
        inputPanel.add(new JLabel("Input: "), BorderLayout.CENTER);
        inputPanel.add(inputField, BorderLayout.CENTER);
    }

    /**
     * Sets a GridLayout for the examplePanel
     */
    static void setExampleLayout() {
        examplePanel.setSize(WIDTH, 500);
        examplePanel.setBorder(new LineBorder(Color.BLACK, 2, true));

        examplePanel.setLayout(new GridBagLayout());

        examplePanel.add(exampleText("EXAMPLES"), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        examplePanel.add(exampleText("Color Space"), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        exampleLine(1, false, "RGB", "HEX", "DEC", "HSB/HSV", "HSL", "CMYK");
        examplePanel.add(exampleText("Definitions"), new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        exampleLine(2, false, "(Red, Green, Blue)", "hexadecimal 0xRRGGBB", "decimal number", "(Hue, Saturation, Brightness/Value)", "(Hue, Saturation, Lightness)", "(Cyan, Magenta, Yellow, black Key)");
        examplePanel.add(exampleText("Constraints"), new GridBagConstraints(0, 3, 1, 2, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        examplePanel.add(exampleText("R, G, B ∈ [0, 255]"), new GridBagConstraints(1, 3, 1, 2, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        examplePanel.add(exampleText("R, G, B ∈ [0, 9]∪[A, F]"), new GridBagConstraints(2, 3, 1, 2, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        exampleLine(3, false, "", "", "DEC = R * 65536 +", "H ∈ [0°, 360°)", "H ∈ [0°, 360°)", "");
        exampleLine(4, false, "", "", "256 * G + B", "S, B, V ∈ [0%, 100%]", "S, L ∈ [0%, 100%]", "");
        examplePanel.add(exampleText("C, M, Y, K ∈ [0.00, 1.00]"), new GridBagConstraints(6, 3, 1, 2, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        exampleLine(5, true, "(255, 255, 255)", "0xFFFFFF", "16777215", "(0, 0, 100)", "(0, 0, 100)", "(0.00, 0.00, 0.00, 0.00)");
        exampleLine(6, true, "(255, 0, 0)", "0xFF0000", "16711680", "(0, 100, 100)", "(0, 100, 50)", "(0.00, 1.00, 1.00, 0.00)");
        exampleLine(7, true, "(0, 255, 0)", "0x00FF00", "65280", "(120, 100, 100)", "(120, 100, 50)", "(1.00, 0.00, 1.00, 0.00)");
        exampleLine(8, true, "(0, 0, 255)", "0x0000FF", "255", "(240, 100, 100)", "(240, 100, 50)", "(1.00, 1.00, 0.00, 0.00)");
        exampleLine(9, true, "(153, 153, 153)", "0x999999", "10066329", "(0, 0, 60)", "(0, 0, 60)", "(0.00, 0.00, 0.00, 0.40)");
    }

    static void exampleLine(int line, boolean hasColorBox, String... strings) {
        if (hasColorBox) {
            examplePanel.add(exampleColorBox(RGBtupleToColor(strings[0])), new GridBagConstraints(0, line, 1, 1, 0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        }
        for (int i = 0; i < strings.length; i++) {
            examplePanel.add(exampleText(strings[i]), new GridBagConstraints(i + 1, line, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        }
    }

    static JPanel exampleColorBox(Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setPreferredSize(new Dimension(15, 15));
        panel.setBorder(new LineBorder(Color.BLACK, 1));

        return panel;
    }

    static JTextField exampleText(String text) {
        JTextField textField = new JTextField(text);
        textField.setBackground(GRAY_BACKGROUND);
        textField.setBorder(new EmptyBorder(0, 0, 0, 0));
        textField.setEditable(false);

        return textField;
    }

    /**
     * Sets a GroupLayout for the optionsPanel
     */
    static void setOptionsLayout() {
        optionsPanel.setSize(WIDTH * 2 / 10, 260);

        optionsPanel.setLayout(new GridBagLayout());

        optionsPanel.add(buttonColorChooser, new GridBagConstraints(0, 0, 2, 1, 1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 10), 0, 0));
        optionsPanel.add(buttonRGB, new GridBagConstraints(0, 1, 1, 1, 1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 5), 0, 0));
        optionsPanel.add(buttonHEX, new GridBagConstraints(0, 2, 1, 1, 1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 5), 0, 0));
        optionsPanel.add(buttonDEC, new GridBagConstraints(0, 3, 1, 1, 1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 5), 0, 0));
        optionsPanel.add(buttonHSBHSV, new GridBagConstraints(1, 1, 1, 1, 1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 10), 0, 0));
        optionsPanel.add(buttonHSL, new GridBagConstraints(1, 2, 1, 1, 1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 10), 0, 0));
        optionsPanel.add(buttonCMYK, new GridBagConstraints(1, 3, 1, 1, 1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 10), 0, 0));
    }

    /**
     * Sets a FlowLayout for the outputPanel
     */
    static void setOutputLayout() {
        outputPanel.setPreferredSize(new Dimension(WIDTH, 50));
        outputPanel.setBorder(new LineBorder(Color.BLACK, 2, true));

        colorPanel.setPreferredSize(new Dimension(80, 80));
        colorPanel.setBorder(new LineBorder(Color.BLACK, 2));

        outputText.setFont(font);
        outputText.setBackground(GRAY_BACKGROUND);
        outputText.setBorder(new EmptyBorder(0, 10, 0, 0));
        outputText.setEditable(false);

        FlowLayout outputLayout = new FlowLayout();
        outputLayout.setAlignment(FlowLayout.LEFT);
        outputPanel.setLayout(outputLayout);
        outputPanel.add(colorPanel);
        outputPanel.add(outputText);
    }

    /**
     * Sets the dimensions and properties of the mainFrame
     * Then sets a GroupLayout for the mainPanel and puts it to display into the mainFrame
     */
    static void setMainLayout() {
        mainFrame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        mainFrame.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        mainFrame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        mainFrame.setSize(WIDTH, HEIGHT);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.setLayout(new GridBagLayout());

        mainFrame.add(inputPanel, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        mainFrame.add(optionsPanel, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        mainFrame.add(examplePanel, new GridBagConstraints(0, 1, 2, 1, 2, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        mainFrame.add(outputPanel, new GridBagConstraints(0, 2, 2, 1, 2, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    }


    /**
     * Determines the behaviour of the buttons
     */
    static void setButtonActionListeners() {
        buttonColorChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(null, "Color Chooser", null);
                createOutput(color);
            }
        });
        buttonRGB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = RGBtupleToColor(inputField.getText());
                createOutput(color);
            }
        });
        buttonHEX.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = HEXtoColor(inputField.getText());
                createOutput(color);
            }
        });
        buttonDEC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = DECtoColor(inputField.getText());
                createOutput(color);
            }
        });
        buttonHSBHSV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = HSBHSVtupleToColor(inputField.getText());
                createOutput(color);
            }
        });
        buttonHSL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = HSLtupleToColor(inputField.getText());
                createOutput(color);
            }
        });
        buttonCMYK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = CMYKtupleToColor(inputField.getText());
                createOutput(color);
            }
        });
    }

    /**
     * Writes to the outputPanel
     */
    static void createOutput(Color color) {
        colorPanel.setBackground(color);
        outputText.setText(OutputValues(color));
    }

    public static String OutputValues(Color color) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ColorToRGB(color));
        stringBuilder.append(ColorToHEX(color));
        stringBuilder.append(ColorToDEC(color));
        stringBuilder.append(ColorToHSBHSV(color));
        stringBuilder.append(ColorToHSL(color));
        stringBuilder.append(ColorToCMYK(color));
        return stringBuilder.toString();
    }
}