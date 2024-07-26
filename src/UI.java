import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UI extends JFrame {
    /**
     * Control variable to only allow a single instance of this class.
     */
    private static boolean initialized = false;

    /**
     * Constants for easy access.
     */
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 680;
    private static final String TITLE = "Color Calc";
    private static final Color STARTUP_COLOR = Color.BLACK;
    private static final Color GRAY_BACKGROUND = new Color(238, 238, 238);
    private static final Font NORMAL_FONT = new Font(Font.DIALOG, Font.PLAIN, 12);
    private static final Font MONOSPACED_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 14);
    private static final Insets INSETS = new Insets(5, 5, 5, 5);

    /**
     * UI elements.
     * <br>
     * <br>
     * Color output.
     */
    private final JPanel colorPanel = new JPanel();

    /**
     * Text input and output.
     */
    private final JLabel inputLabel = new JLabel("Input: ");
    private final JTextField inputField = new JTextField(20);
    private final LineBorder normalBorder = new LineBorder(Color.BLACK);
    private final LineBorder errorBorder = new LineBorder(Color.RED, 2);
    private final JLabel[][] outputLabels = new JLabel[6][];

    /**
     * Only public member of this class. Only allows a single instance to be loaded.
     */
    public static synchronized void init() {
        if (!initialized) {
            SwingUtilities.invokeLater(UI::new);
            initialized = true;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Default constructor with all the important UI settings.
     */
    private UI() {
        super(TITLE);

        setMainLayout();

        createOutput(STARTUP_COLOR);

        setVisible(true);
    }

    /**
     * Helper methods to make the constructor less cluttered.

     * Configures the GridBagLayout for the inputPanel.
     */
    private JPanel makeInputPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());

        panel.add(Box.createGlue(), new GridBagConstraints(0, 0, 4, 1, 1, 1, GridBagConstraints.NORTH,  GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        panel.add(Box.createGlue(), new GridBagConstraints(0, 2, 4, 1, 1, 1, GridBagConstraints.SOUTH,  GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        panel.add(Box.createGlue(), new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.WEST,   GridBagConstraints.VERTICAL, INSETS, 0, 0));
        panel.add(Box.createGlue(), new GridBagConstraints(3, 1, 1, 1, 1, 1, GridBagConstraints.EAST,   GridBagConstraints.VERTICAL, INSETS, 0, 0));
        panel.add(inputLabel,       new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.EAST,   GridBagConstraints.NONE, INSETS, 0, 0));
        panel.add(inputField,       new GridBagConstraints(2, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, INSETS, 0, 0));

        inputField.setBorder(normalBorder);

        return panel;
    }

    /**
     * Sets up a panel with all the buttons and their respective behavior.
     */
    private JPanel makeOptionsPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());

        panel.add(new JButton(new AbstractAction("Color Chooser") {
            @Override
            public void actionPerformed(ActionEvent e) {
                final Color color = JColorChooser.showDialog(null, "Color Chooser", Color.BLACK);

                if (color == null) {
                    return;
                }

                createOutput(color);
            }
        }), new GridBagConstraints(0, 0, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        panel.add(new JButton(new AbstractAction("from RGB") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    final Color color = ColorCalc.RGBtupleToColor(inputField.getText());
                    createOutput(color);
                } catch (Exception ex) {
                    setError();
                }
            }
        }),      new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        panel.add(new JButton(new AbstractAction("from HEX") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    final Color color = ColorCalc.HEXtoColor(inputField.getText());
                    createOutput(color);
                } catch (Exception ex) {
                    setError();
                }
            }
        }),      new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        panel.add(new JButton(new AbstractAction("from DEC") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    final Color color = ColorCalc.DECtoColor(inputField.getText());
                    createOutput(color);
                } catch (Exception ex) {
                    setError();
                }
            }
        }),      new GridBagConstraints(0, 3, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        panel.add(new JButton(new AbstractAction("from HSB/HSV") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    final Color color = ColorCalc.HSBHSVtupleToColor(inputField.getText());
                    createOutput(color);
                } catch (Exception ex) {
                    setError();
                }
            }
        }),  new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        panel.add(new JButton(new AbstractAction("from HSL") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    final Color color = ColorCalc.HSLtupleToColor(inputField.getText());
                    createOutput(color);
                } catch (Exception ex) {
                    setError();
                }
            }
        }),      new GridBagConstraints(1, 2, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        panel.add(new JButton(new AbstractAction("from CMYK") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    final Color color = ColorCalc.CMYKtupleToColor(inputField.getText());
                    createOutput(color);
                } catch (Exception ex) {
                    setError();
                }
            }
        }),     new GridBagConstraints(1, 3, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));

        return panel;
    }

    /**
     * Sets up a panes with examples.
     */
    private JPanel makeExampleLayout() {
        final JPanel panel = new JPanel(new GridBagLayout());

        panel.setBorder(new LineBorder(Color.BLACK, 2, true));

        panel.add(new JLabel("<html><b><u>EXAMPLES</u></b></html>"),   new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        panel.add(new JLabel("<html><b>Color Space</b></html>"),       new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        exampleLine(panel, 1, false, "RGB", "HEX", "DEC", "HSB/HSV", "HSL", "CMYK");
        panel.add(new JLabel("<html><b>Definitions</b></html>"),       new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        exampleLine(panel, 2, false, "(Red, Green, Blue)", "hexadecimal 0xRRGGBB", "decimal number", "(Hue, Saturation, Brightness/Value)", "(Hue, Saturation, Lightness)", "(Cyan, Magenta, Yellow, black Key)");
        panel.add(new JLabel("<html><b>Constraints</b></html>"),       new GridBagConstraints(0, 3, 1, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        panel.add(makeExampleText("R, G, B ∈ [0, 255]", false),        new GridBagConstraints(1, 3, 1, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        panel.add(makeExampleText("R, G, B ∈ [0, 9]∪[A, F]", false),   new GridBagConstraints(2, 3, 1, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        exampleLine(panel, 3, false, "", "", "DEC = R * 65536 +", "H ∈ [0°, 360°)", "H ∈ [0°, 360°)");
        exampleLine(panel, 4, false, "", "", "G * 256 + B", "S, B, V ∈ [0%, 100%]", "S, L ∈ [0%, 100%]");
        panel.add(makeExampleText("C, M, Y, K ∈ [0.00, 1.00]", false), new GridBagConstraints(6, 3, 1, 2, 1, 2, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        exampleLine(panel, 5, true, "(255, 255, 255)", "0xFFFFFF", "16777215", "(0, 0, 100)", "(0, 0, 100)", "(0.00, 0.00, 0.00, 0.00)");
        exampleLine(panel, 6, true, "(255, 0, 0)", "0xFF0000", "16711680", "(0, 100, 100)", "(0, 100, 50)", "(0.00, 1.00, 1.00, 0.00)");
        exampleLine(panel, 7, true, "(0, 255, 0)", "0x00FF00", "65280", "(120, 100, 100)", "(120, 100, 50)", "(1.00, 0.00, 1.00, 0.00)");
        exampleLine(panel, 8, true, "(0, 0, 255)", "0x0000FF", "255", "(240, 100, 100)", "(240, 100, 50)", "(1.00, 1.00, 0.00, 0.00)");
        exampleLine(panel, 9, true, "(153, 153, 153)", "0x999999", "10066329", "(0, 0, 60)", "(0, 0, 60)", "(0.00, 0.00, 0.00, 0.40)");

        return panel;
    }

    /**
     * 3 helper methods that make building the UI easier.
     */
    private void exampleLine(JPanel panel, int line, boolean hasColorBox, String... strings) {
        if (hasColorBox) {
            panel.add(makeColorBox(ColorCalc.RGBtupleToColor(strings[0])), new GridBagConstraints(0, line, 1, 1, 0, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, INSETS, 0, 0));
        }

        for (int i = 0; i < strings.length; i++) {
            panel.add(makeExampleText(strings[i], hasColorBox), new GridBagConstraints(i + 1, line, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        }
    }

    private JPanel makeColorBox(Color color) {
        final JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setPreferredSize(new Dimension(15, 15));
        panel.setBorder(new LineBorder(Color.BLACK, 1));

        return panel;
    }

    private JLabel makeExampleText(String text, boolean copyable) {
        final JLabel label = new JLabel(text);
        label.setFont(NORMAL_FONT);

        if (copyable) {
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    copyToClipboard(label.getText());
                }
            });
        }

        return label;
    }

    /**
     * Sets up the FlowLayout for the outputPanel.
     */
    private JPanel makeOutputPanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panel.setBorder(new LineBorder(Color.BLACK, 2, true));

        colorPanel.setPreferredSize(new Dimension(80, 80));
        colorPanel.setBorder(new LineBorder(Color.BLACK, 2));

        final JPanel textOutput = new JPanel(new GridBagLayout());
        final String[] titles = {"In RGB:", "In HEX:", "In DEC:", "In HSB/HSV:", "In HSL:", "In CMYK:"};
        final int[] outputCounts = {2, 3, 1, 3, 3, 3};
        final GridBagConstraints constraints = new GridBagConstraints(GridBagConstraints.RELATIVE, 0, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE, INSETS, 0, 0);

        for (int i = 0; i < titles.length; i++) {
            final JLabel title = new JLabel(titles[i]);
            title.setFont(MONOSPACED_FONT);
            textOutput.add(title, constraints);

            final JLabel[] labels = new JLabel[outputCounts[i]];
            outputLabels[i] = labels;

            for (int j = 0; j < outputCounts[i]; j++) {
                final JLabel label = new JLabel();
                label.setFont(MONOSPACED_FONT);
                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        copyToClipboard(label.getText());
                    }
                });

                labels[j] = label;
                textOutput.add(label, constraints);
            }

            constraints.gridy++;
        }

        textOutput.setBackground(GRAY_BACKGROUND);
        textOutput.setBorder(new EmptyBorder(0, 10, 0, 0));

        panel.add(colorPanel);
        panel.add(textOutput);

        return panel;
    }

    /**
     * Sets up the dimensions, properties and content of the window.
     */
    private void setMainLayout() {
        setSize(WIDTH, HEIGHT);
        setResizable(false);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));

        setLayout(new GridBagLayout());

        add(makeInputPanel(),    new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        add(makeOptionsPanel(),  new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        add(makeExampleLayout(), new GridBagConstraints(0, 1, 2, 1, 2, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
        add(makeOutputPanel(),   new GridBagConstraints(0, 2, 2, 1, 2, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
    }

    /**
     * Creates an output for the user, either by converting the color or by signaling a bad input.
     * @param color Color to be converted into all color spaces.
     */
    private void createOutput(Color color) {
        if (color != null) {
            color = ColorCalc.removeAlpha(color);
            replenishError();
            colorPanel.setBackground(color);
            OutputValues(color);
        } else {
            setError();
        }

    }

    /**
     * Writes to the output panel.
     */
    private void OutputValues(Color color) {
        final String[][] output = {
                ColorCalc.ColorToRGB(color),
                ColorCalc.ColorToHEX(color),
                ColorCalc.ColorToDEC(color),
                ColorCalc.ColorToHSBHSV(color),
                ColorCalc.ColorToHSL(color),
                ColorCalc.ColorToCMYK(color)
        };

        for (int i = 0; i < output.length; i++) {
            final String[] line = output[i];

            for (int j = 0; j < line.length; j++) {
                outputLabels[i][j].setText(line[j]);
            }
        }
    }

    /**
     * Signals to the user when they did an illegal input.
     */
    private void setError() {
        inputField.setBorder(errorBorder);
        inputLabel.setText("<html><font color='red'>Input: </font></html>");
    }

    /**
     * Reverts the red text and border to black.
     */
    private void replenishError() {
        inputField.setBorder(normalBorder);
        inputLabel.setText("Input: ");
    }

    /**
     * Copies to the system clipboard and signals to the user.
     * @param text Text to be copied.
     */
    private void copyToClipboard(String text) {
        final StringSelection selection = new StringSelection(text);

        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);

        setTitle(TITLE + " (copied to clipboard)");

        new Thread(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ignored) {
            } finally {
                setTitle(TITLE);
            }
        }).start();
    }
}