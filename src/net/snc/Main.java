package net.snc;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        JFrame frame = new JFrame("Scientific Notation Calculator");
        frame.setBounds(0, 0, 500, 270);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);

        JLabel titleLabel = new JLabel("Scientific Notation Calculator");
        titleLabel.setBounds(0, 5, 500, 35);
        titleLabel.setFont(new Font("plain", Font.PLAIN, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFocusable(false);

        JLabel instructionLabel = new JLabel("Input number/scientific notation:");
        instructionLabel.setBounds(0, 40, 500, 35);
        instructionLabel.setFont(new Font("plain", Font.PLAIN, 16));
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setFocusable(false);

        JTextField inputField = new JTextField();
        inputField.setBounds(105, 80, 275, 20);
        inputField.setBorder(BorderFactory.createEtchedBorder());

        ButtonGroup radioGroup = new ButtonGroup();

        JRadioButton notationButton = new JRadioButton("Scientific Notation to Number");
        notationButton.setBounds(65, 185, 170, 20);
        notationButton.setFocusable(false);

        JRadioButton numberButton = new JRadioButton("Number to Scientific Notation");
        numberButton.setBounds(235, 185, 170, 20);
        numberButton.setFocusable(false);

        radioGroup.add(notationButton);
        radioGroup.add(numberButton);

        notationButton.setSelected(true);

        JLabel answerLabel = new JLabel("Answer: ");
        answerLabel.setBounds(110, 155, 380, 35);
        answerLabel.setFont(new Font("plain", Font.PLAIN, 16));
        answerLabel.setFocusable(false);

        JCheckBox commasCheckBox = new JCheckBox("Commas");
        commasCheckBox.setBounds(195, 135, 75, 20);
        commasCheckBox.addActionListener((e) -> {
            if (notationButton.isSelected()) {
                if (commasCheckBox.isSelected()) {
                    answerLabel.setText("Answer: " + addCommas(answerLabel.getText().replaceAll(Pattern.quote("Answer: "), "")));
                } else {
                    answerLabel.setText("Answer: " + answerLabel.getText().replaceAll(Pattern.quote("Answer: "), "").replaceAll(Pattern.quote(","), ""));
                }
            }
        });
        commasCheckBox.setFocusable(false);

        JTextField maxDecimalsInput = new JTextField();
        maxDecimalsInput.setBounds(400, 210, 70, 20);
        maxDecimalsInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    if (numberButton.isSelected() && isValidNotation(answerLabel.getText().replaceAll(Pattern.quote("Answer: "), ""))) {
                        if (isNumber(inputField.getText())) {
                            String notation = toNotation(inputField.getText());
                            if (!maxDecimalsInput.getText().isEmpty()) {
                                if (isInteger(maxDecimalsInput.getText())) {
                                    int max = Integer.parseInt(maxDecimalsInput.getText());
                                    if ((max < 2)) {
                                        max = 2;
                                    }
                                    if (max + 2 <= notation.length() - 1) {
                                        notation = notation.split(Pattern.quote("e"))[0].substring(0, max + 2) + "e" + notation.split(Pattern.quote("e"))[1];
                                    }
                                }
                            }
                            answerLabel.setText("Answer: " + notation);
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        });
        maxDecimalsInput.setBorder(BorderFactory.createEtchedBorder());

        JLabel maxDecimalsLabel = new JLabel("Max Decimals:");
        maxDecimalsLabel.setBounds(285, 210, 100, 20);
        maxDecimalsLabel.setFont(new Font("plain", Font.PLAIN, 15));
        maxDecimalsLabel.setFocusable(false);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.setBounds(195, 110, 80, 20);
        calculateButton.addActionListener((e) -> {
            if (notationButton.isSelected()) {
                try {
                    if (isValidNotation(inputField.getText())) {
                        String text = "Answer: " + fromNotation(inputField.getText());
                        if (commasCheckBox.isSelected()) {
                            answerLabel.setText("Answer: " + addCommas(text.replaceAll(Pattern.quote("Answer: "), "")));
                        } else {
                            answerLabel.setText("Answer: " + text.replaceAll(Pattern.quote("Answer: "), "").replaceAll(Pattern.quote(","), ""));
                        }
                    } else {
                        answerLabel.setText("Answer: Invalid notation.");
                    }
                } catch (Exception ex) {
                    answerLabel.setText("Answer: Invalid notation.");
                }
            } else if (numberButton.isSelected()) {
                try {
                    if (isNumber(inputField.getText())) {
                        String notation = toNotation(inputField.getText());
                        if (!maxDecimalsInput.getText().isEmpty()) {
                            if (isInteger(maxDecimalsInput.getText())) {
                                int max = Integer.parseInt(maxDecimalsInput.getText());
                                if ((max < 2)) {
                                    max = 2;
                                }
                                if (max + 2 <= notation.length() - 1) {
                                    notation = notation.split(Pattern.quote("e"))[0].substring(0, max + 2) + "e" + notation.split(Pattern.quote("e"))[1];
                                }
                            }
                        }
                        answerLabel.setText("Answer: " + notation);
                    } else {
                        answerLabel.setText("Answer: Invalid number.");
                    }
                } catch (Exception ex) {
                    answerLabel.setText("Answer: Invalid number.");
                }
            }
        });
        calculateButton.setFocusable(false);

        JButton copyButton = new JButton("Copy Answer");
        copyButton.setBounds(285, 110, 110, 20);
        copyButton.addActionListener((e) -> {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(answerLabel.getText().replaceAll(Pattern.quote("Answer: "), "")), null);
            copyButton.setText("Copied!");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    copyButton.setText("Copy Answer");
                }
            }, 750L);
        });
        copyButton.setFocusable(false);

        frame.add(titleLabel);
        frame.add(instructionLabel);
        frame.add(inputField);
        frame.add(notationButton);
        frame.add(numberButton);
        frame.add(answerLabel);
        frame.add(calculateButton);
        frame.add(copyButton);
        frame.add(commasCheckBox);
        frame.add(maxDecimalsInput);
        frame.add(maxDecimalsLabel);

        frame.setVisible(true);
    }

    private static String toNotation(String number) {
        if (isNumber(number)) {
            StringBuilder temp = new StringBuilder(number);
            number = temp.toString().trim();
            for (int i = 0; i < number.length() - 1; i++) {
                if (number.charAt(i) == '0' && (number.length() >= i + 1 && number.charAt(i + 1) != '.')) {
                    temp.setCharAt(i, ' ');
                } else {
                    break;
                }
            }
            number = temp.toString().trim();

            boolean negative = number.charAt(0) == '0' && number.length() > 1 && number.charAt(1) == '.';

            if (!number.contains(".")) {
                String decimal = number.charAt(0) + "." + number.substring(1);
                String notation = decimal + "e+" + (decimal.length() - 2);
                while (true) {
                    if (notation.split(Pattern.quote("e"))[0].charAt(notation.split(Pattern.quote("e"))[0].length() - 1) == '0') {
                        notation = notation.split(Pattern.quote("e"))[0].substring(0, notation.split(Pattern.quote("e"))[0].length() - 1) + "e" + notation.split(Pattern.quote("e"))[1];
                    } else {
                        break;
                    }
                }
                if (notation.split(Pattern.quote("e"))[0].charAt(notation.split(Pattern.quote("e"))[0].length() - 1) == '.') {
                    return notation.split(Pattern.quote("e"))[0].substring(0, notation.split(Pattern.quote("e"))[0].length() - 1) + "e" + notation.split(Pattern.quote("e"))[1];
                } else {
                    return notation;
                }
            } else {
                if (!negative) {
                    String[] parts = number.split(Pattern.quote("."));
                    String partial = toNotation(parts[0] + parts[1]);
                    return partial.split(Pattern.quote("e"))[0] + "e+" + (Integer.parseInt(partial.split(Pattern.quote("e"))[1].substring(1)) - parts[1].length());
                } else {
                    int zeros = 0;
                    for (int i = 0; i < number.length(); i++) {
                        if (number.charAt(i) == '0' || number.charAt(i) == '.') {
                            if (number.charAt(i) == '0') {
                                zeros++;
                            }
                        } else {
                            break;
                        }
                    }
                    return toNotation(number.substring(zeros + 1)).split(Pattern.quote("e"))[0] + "e-" + zeros;
                }
            }
        }
        return null;
    }

    private static String addCommas(String number) {
        if (isNumber(number)) {
            String integer = number;
            if (number.contains(".")) {
                integer = number.split(Pattern.quote("."))[0];
            }

            int inc = 1;
            String newInteger = integer;
            for (int i = integer.length() - 1; i > 0; i--) {
                if (inc % 3 == 0) {
                    newInteger = newInteger.substring(0, i) + "," + newInteger.substring(i);
                }
                inc++;
            }

            if (number.contains(".")) {
                return newInteger + "." + number.split(Pattern.quote("."))[1];
            }
            return newInteger;
        } else {
            return number;
        }
    }

    private static String fromNotation(String notation) {
        notation = notation.toLowerCase();
        if (isValidNotation(notation)) {
            StringBuilder builder = new StringBuilder(notation.split(Pattern.quote("e"))[1]);
            builder.setCharAt(0, (notation.split(Pattern.quote("e"))[1].charAt(0) == '+') ? ' ' : '-');
            builder = new StringBuilder(builder.toString().trim());

            boolean negative = false;

            if (notation.split(Pattern.quote("e"))[0].charAt(0) == '-') {
                notation = notation.split(Pattern.quote("e"))[0].substring(1) + "e" + notation.split(Pattern.quote("e"))[1];
                negative = true;
            }

            if (!notation.split(Pattern.quote("e"))[0].contains(".")) {
                notation = notation.split(Pattern.quote("e"))[0] + ".0e" + notation.split(Pattern.quote("e"))[1];
            }

            int x = Integer.parseInt(builder.toString());
            String decimal = notation.split(Pattern.quote("e"))[0];
            if (x >= 0) {
                for (int i = 0; i < x; i++) {
                    String[] parts = decimal.split(Pattern.quote("."));
                    if (parts.length > 1) {
                        parts[0] = parts[0] + parts[1].charAt(0);
                        parts[1] = parts[1].substring(1);
                        decimal = parts[0] + "." + parts[1];
                    } else {
                        parts[0] = parts[0] + "0";
                        decimal = parts[0] + ".";
                    }
                }
            } else {
                for (int i = 0; i < Math.abs(x); i++) {
                    String[] parts = decimal.split(Pattern.quote("."));
                    if (parts[0].length() > 0) {
                        parts[1] = parts[0].charAt(parts[0].length() - 1) + parts[1];
                        parts[0] = parts[0].substring(0, parts[0].length() - 1);
                        decimal = parts[0] + "." + parts[1];
                    } else {
                        parts[1] = "0" + parts[1];
                        decimal = "." + parts[1];
                    }
                }
            }
            if (decimal.charAt(decimal.length() - 1) == '.') {
                decimal = decimal + "0";
            } else if (decimal.charAt(0) == '.') {
                decimal = "0" + decimal;
            }
            decimal = (negative ? "-" : "") + decimal;
            StringBuilder temp = new StringBuilder(decimal);
            for (int i = decimal.length() - 1; i > -1; i--) {
                if (decimal.charAt(i) == '0') {
                    temp.setCharAt(i, ' ');
                } else {
                    break;
                }
            }
            decimal = temp.toString();
            for (int i = 0; i < decimal.length() - 1; i++) {
                if (decimal.charAt(i) == '0' && (decimal.length() >= i + 1 && decimal.charAt(i + 1) != '.')) {
                    temp.setCharAt(i, ' ');
                } else {
                    break;
                }
            }
            String result = temp.toString().trim();
            if (result.length() - 2 >= 0) {
                return result + (result.charAt(result.length() - 1) == '.' ? "0" : "");
            } else {
                return result;
            }
        } else {
            return null;
        }
    }

    private static boolean isValidNotation(String notation) {
        if (notation.isEmpty()) {
            return false;
        }

        boolean a = (notation.contains("e-") || notation.contains("e+")) && isNumber(notation.split(Pattern.quote("e"))[0]);

        StringBuilder builder;
        if (a) {
            builder = new StringBuilder(notation.split(Pattern.quote("e"))[1]);
            builder.setCharAt(0, (notation.split(Pattern.quote("e"))[1].charAt(0) == '+') ? ' ' : '-');
            builder = new StringBuilder(builder.toString().trim());
            return isInteger(builder.toString());
        } else {
            return false;
        }
    }

    private static boolean isNumber(String string) {
        int i = 0;
        int j = 0;

        int inc = 0;
        for (char c : string.toCharArray()) {
            if (c == '.') {
                if ((string.length() >= inc + 2) && (string.length() >= inc - 1)) {
                    char c0 = string.charAt(inc + 1);
                    if (!(c0 == '0' || c0 == '1' || c0 == '2' || c0 == '3' || c0 == '4' || c0 == '5' || c0 == '6' || c0 == '7' || c0 == '8' || c0 == '9')) {
                        return false;
                    }

                    char c1 = string.charAt(inc - 1);
                    if (!(c1 == '0' || c1 == '1' || c1 == '2' || c1 == '3' || c1 == '4' || c1 == '5' || c1 == '6' || c1 == '7' || c1 == '8' || c1 == '9')) {
                        return false;
                    }
                } else {
                    return false;
                }
                i++;
            }

            if (c == '-') {
                if (inc != 0) {
                    return false;
                }
                j++;
            }

            if (!(c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' || c == '.' || c == '-')) {
                return false;
            }
            inc++;
        }

        return i <= 1 && j <= 1;
    }

    private static boolean isInteger(String string) {
        int i = 0;
        for (char c : string.toCharArray()) {
            if (!(c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' || (c == '-' && i == 0))) {
                return false;
            }
            i++;
        }
        return true;
    }
}
