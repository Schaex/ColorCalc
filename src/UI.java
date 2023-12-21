import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class UI extends ColorCalc {
    /**
     * Constants for easy access.
     */
    static final int WIDTH = 1200;
    static final int HEIGHT = 690;
    static final Color STARTUP_COLOR = Color.BLACK;
    static final Color GRAY_BACKGROUND = new Color(238, 238, 238);

    /**
     * All UI elements.

     * Main window.
     */
    static final JFrame mainFrame = new JFrame("Color Calc");

    /**
     * Panels that contain all the elements.
     */
    static final JPanel inputPanel = new JPanel(new GridBagLayout());
    static final JPanel examplePanel = new JPanel();
    static final JPanel optionsPanel = new JPanel(new GridBagLayout());
    static final JPanel colorPanel = new JPanel();
    static final JPanel outputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    /**
     * Text input and output.
     */
    static final JLabel inputLabel = new JLabel("Input: ");
    static final JTextField inputField = new JTextField(20);
    static final JTextArea outputText = new JTextArea(6, 0);
    static final Font font = new Font(Font.MONOSPACED, Font.PLAIN, 14);
    static final LineBorder normalBorder = new LineBorder(Color.BLACK);
    static final LineBorder errorBorder = new LineBorder(Color.RED, 2);

    /**
     * Option buttons.
     */
    static final JButton buttonColorChooser = new JButton("Color Chooser");
    static final JButton buttonRGB = new JButton("from RGB");
    static final JButton buttonHEX = new JButton("from HEX");
    static final JButton buttonDEC = new JButton("from DEC");
    static final JButton buttonHSBHSV = new JButton("from HSB/HSV");
    static final JButton buttonHSL = new JButton("from HSL");
    static final JButton buttonCMYK = new JButton("from CMYK");


    /**
     * Default constructor with all the important UI settings.
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
     * Helper methods to make the constructor less cluttered.

     * Configures the GridBagLayout for the inputPanel.
     */
    static void setInputLayout() {
        inputPanel.setSize(WIDTH * 4 / 5, 50);

        inputPanel.add(Box.createGlue(), new GridBagConstraints(0, 0, 4, 1, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        inputPanel.add(Box.createGlue(), new GridBagConstraints(0, 2, 4, 1, 1, 1, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        inputPanel.add(Box.createGlue(), new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
        inputPanel.add(Box.createGlue(), new GridBagConstraints(3, 1, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
        inputPanel.add(inputLabel, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        inputPanel.add(inputField, new GridBagConstraints(2, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        inputField.setBorder(normalBorder);
    }

    /**
     * Sets a GridLayout for the examplePanel.
     */
    static void setExampleLayout() {
        examplePanel.setSize(WIDTH, 500);
        examplePanel.setBorder(new LineBorder(Color.BLACK, 2, true));

        examplePanel.setLayout(new GridBagLayout());

        examplePanel.add(new JLabel("<html><b><u>EXAMPLES</u></b></html>"), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        examplePanel.add(new JLabel("<html><b>Color Space</b></html>"), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        exampleLine(1, false, "RGB", "HEX", "DEC", "HSB/HSV", "HSL", "CMYK");
        examplePanel.add(new JLabel("<html><b>Definitions</b></html>"), new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        exampleLine(2, false, "(Red, Green, Blue)", "hexadecimal 0xRRGGBB", "decimal number", "(Hue, Saturation, Brightness/Value)", "(Hue, Saturation, Lightness)", "(Cyan, Magenta, Yellow, black Key)");
        examplePanel.add(new JLabel("<html><b>Constraints</b></html>"), new GridBagConstraints(0, 3, 1, 2, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        examplePanel.add(exampleText("R, G, B ∈ [0, 255]"), new GridBagConstraints(1, 3, 1, 2, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        examplePanel.add(exampleText("R, G, B ∈ [0, 9]∪[A, F]"), new GridBagConstraints(2, 3, 1, 2, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        exampleLine(3, false, "", "", "DEC = R * 65536 +", "H ∈ [0°, 360°)", "H ∈ [0°, 360°)");
        exampleLine(4, false, "", "", "256 * G + B", "S, B, V ∈ [0%, 100%]", "S, L ∈ [0%, 100%]");
        examplePanel.add(exampleText("C, M, Y, K ∈ [0.00, 1.00]"), new GridBagConstraints(6, 3, 1, 2, 1.0, 2.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        exampleLine(5, true, "(255, 255, 255)", "0xFFFFFF", "16777215", "(0, 0, 100)", "(0, 0, 100)", "(0.00, 0.00, 0.00, 0.00)");
        exampleLine(6, true, "(255, 0, 0)", "0xFF0000", "16711680", "(0, 100, 100)", "(0, 100, 50)", "(0.00, 1.00, 1.00, 0.00)");
        exampleLine(7, true, "(0, 255, 0)", "0x00FF00", "65280", "(120, 100, 100)", "(120, 100, 50)", "(1.00, 0.00, 1.00, 0.00)");
        exampleLine(8, true, "(0, 0, 255)", "0x0000FF", "255", "(240, 100, 100)", "(240, 100, 50)", "(1.00, 1.00, 0.00, 0.00)");
        exampleLine(9, true, "(153, 153, 153)", "0x999999", "10066329", "(0, 0, 60)", "(0, 0, 60)", "(0.00, 0.00, 0.00, 0.40)");
    }

    /**
     * 3 helper methods that make building the UI easier.
     */
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
        textField.setBorder(new EmptyBorder(2, 0, 5, 0));
        textField.setEditable(false);

        return textField;
    }

    /**
     * Sets up a GridBagLayout for the optionsPanel.
     */
    static void setOptionsLayout() {
        optionsPanel.setSize(WIDTH / 5, 260);

        optionsPanel.add(buttonColorChooser, new GridBagConstraints(0, 0, 2, 1, 1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 10), 0, 0));
        optionsPanel.add(buttonRGB, new GridBagConstraints(0, 1, 1, 1, 1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 5), 0, 0));
        optionsPanel.add(buttonHEX, new GridBagConstraints(0, 2, 1, 1, 1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 5), 0, 0));
        optionsPanel.add(buttonDEC, new GridBagConstraints(0, 3, 1, 1, 1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 5), 0, 0));
        optionsPanel.add(buttonHSBHSV, new GridBagConstraints(1, 1, 1, 1, 1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 10), 0, 0));
        optionsPanel.add(buttonHSL, new GridBagConstraints(1, 2, 1, 1, 1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 10), 0, 0));
        optionsPanel.add(buttonCMYK, new GridBagConstraints(1, 3, 1, 1, 1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 10), 0, 0));
    }

    /**
     * Sets up the FlowLayout for the outputPanel.
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

        outputPanel.add(colorPanel);
        outputPanel.add(outputText);
    }

    /**
     * Sets the dimensions and properties of the mainFrame.
     * Then sets a GroupLayout for the mainPanel and puts it to display into the mainFrame.
     */
    static void setMainLayout() {
        mainFrame.setSize(WIDTH, HEIGHT);
        mainFrame.setResizable(false);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.setLayout(new GridBagLayout());

        mainFrame.add(inputPanel, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        mainFrame.add(optionsPanel, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        mainFrame.add(examplePanel, new GridBagConstraints(0, 1, 2, 1, 2, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        mainFrame.add(outputPanel, new GridBagConstraints(0, 2, 2, 1, 2, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    }


    /**
     * Sets up the behaviour of the buttons.
     */
    static void setButtonActionListeners() {
        buttonColorChooser.addActionListener(e -> {
            Color color = JColorChooser.showDialog(null, "Color Chooser", Color.BLACK);

            if (color == null) {
                return;
            }

            createOutput(color);
        });
        buttonRGB.addActionListener(e -> {
            try {
                Color color = RGBtupleToColor(inputField.getText());
                createOutput(color);
            } catch (Exception exe) {
                setError();
            }
        });
        buttonHEX.addActionListener(e -> {
            try {
                Color color = HEXtoColor(inputField.getText());
                createOutput(color);
            } catch (Exception exe) {
                setError();
            }
        });
        buttonDEC.addActionListener(e -> {
            try {
                Color color = DECtoColor(inputField.getText());
                createOutput(color);
            } catch (Exception exe) {
                setError();
            }
        });
        buttonHSBHSV.addActionListener(e -> {
            try {
                Color color = HSBHSVtupleToColor(inputField.getText());
                createOutput(color);
            } catch (Exception exe) {
                setError();
            }
        });
        buttonHSL.addActionListener(e -> {
            try {
                Color color = HSLtupleToColor(inputField.getText());
                createOutput(color);
            } catch (Exception exe) {
                setError();
            }
        });
        buttonCMYK.addActionListener(e -> {
            try {
                Color color = CMYKtupleToColor(inputField.getText());
                createOutput(color);
            } catch (Exception exe) {
                setError();
            }
        });
    }

    /**
     * Writes to the outputPanel.
     */
    static void createOutput(Color color) {
        if (color != null) {
            replenishError();
            colorPanel.setBackground(color);
            outputText.setText(OutputValues(color));
        } else {
            setError();
        }

    }

    public static String OutputValues(Color color) {
        return ColorToRGB(color) +
                ColorToHEX(color) +
                ColorToDEC(color) +
                ColorToHSBHSV(color) +
                ColorToHSL(color) +
                ColorToCMYK(color);
    }

    /**
     * Signals to the user when they did an illegal input.
     * Reverts after an acceptable input.
     */
    public static void setError() {
        inputField.setBorder(errorBorder);
        inputLabel.setText("<html><font color='red'><b>Input: </b></font></html>");
    }

    public static void replenishError() {
        inputField.setBorder(normalBorder);
        inputLabel.setText("Input: ");
    }
}