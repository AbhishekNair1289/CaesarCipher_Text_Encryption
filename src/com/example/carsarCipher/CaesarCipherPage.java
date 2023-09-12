package com.example.carsarCipher;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.border.SoftBevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.border.BevelBorder;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class CaesarCipherPage extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField inputTextField;
	private JTextField shiftTextField;
	private JButton processButton;
	private JRadioButton encryptRadioButton;
	private JRadioButton decryptRadioButton;
	private JTextArea resultTextArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CaesarCipherPage frame = new CaesarCipherPage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CaesarCipherPage() {
		setAlwaysOnTop(true);
		setResizable(false);
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel inputLabel = new JLabel("Input Text : ");
		inputLabel.setHorizontalAlignment(SwingConstants.CENTER);
		inputLabel.setBounds(120, 30, 90, 20);
		contentPane.add(inputLabel);
		
		inputTextField = new JTextField();
		inputTextField.setBounds(230, 30, 100, 20);
		contentPane.add(inputTextField);
		inputTextField.setColumns(20);
		
		JLabel shiftLabel = new JLabel("Shift Value : ");
		shiftLabel.setBounds(350,30,75,20);
		contentPane.add(shiftLabel);
		
		shiftTextField = new JTextField();
		shiftTextField.setBounds(435,30,50,20);
		contentPane.add(shiftTextField);
		shiftTextField.setColumns(5);
		
		processButton = new JButton("Process");
		processButton.setBounds(505, 25, 100, 30);
		processButton.setBackground(new Color(153, 153, 255));
		contentPane.add(processButton);
		
		encryptRadioButton = new JRadioButton("Encrypt");
		encryptRadioButton.setBounds(230, 70, 70, 20);
		contentPane.add(encryptRadioButton);
		
		decryptRadioButton = new JRadioButton("Decrypt");
		decryptRadioButton.setBounds(350, 70, 70, 20);
		contentPane.add(decryptRadioButton);
		
		ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(encryptRadioButton);
        radioGroup.add(decryptRadioButton);
        encryptRadioButton.setSelected(true);
        
		resultTextArea = new JTextArea();
		resultTextArea.setBounds(260, 120, 200, 60);
		contentPane.add(resultTextArea);
		resultTextArea.setEditable(false);
		
		// Create a menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveMenuItem = new JMenuItem("Save Result");
        JMenuItem loadMenuItem = new JMenuItem("Load Text from File");
        
        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        
		
		JLabel resultLabel = new JLabel("Result : ");
		resultLabel.setBounds(188, 122, 62, 20);
		contentPane.add(resultLabel);
		
		processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//            	showMessage("Process Started");
                processText();
            }
        });
		
		saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveResultToFile();
            }
        });

        loadMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTextFromFile();
            }
        });
	}
	private void processText() {
        String inputText = inputTextField.getText();
        String shiftValueStr = shiftTextField.getText();
        int shiftValue;

        try {
            shiftValue = Integer.parseInt(shiftValueStr);
        } catch (NumberFormatException e) {
            showMessage1("Invalid shift value. Please enter an integer.");
            return;
        }

        String resultText = "";
        if (encryptRadioButton.isSelected()) {
            resultText = caesarCipher(inputText, shiftValue, "encrypt");
        } else if (decryptRadioButton.isSelected()) {
            resultText = caesarCipher(inputText, shiftValue, "decrypt");
        }

        resultTextArea.setText(resultText);
    }
	private String caesarCipher(String text, int shift, String mode) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            if (Character.isLetter(ch)) {
                char base = Character.isLowerCase(ch) ? 'a' : 'A';
                if (mode.equals("encrypt")) {
                    ch = (char) (base + (ch - base + shift) % 26);
                } else if (mode.equals("decrypt")) {
                    ch = (char) (base + (ch - base - shift + 26) % 26);
                }
            }

            result.append(ch);
        }

        return result.toString();
    }
	private void saveResultToFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            String fileName = fileChooser.getSelectedFile().getPath();
            try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
                writer.write(resultTextArea.getText());
                showMessage2("Result saved to " + fileName);
            } catch (IOException e) {
                showMessage1("Error saving the result to the file.");
            }
        }
    }

    private void loadTextFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            String fileName = fileChooser.getSelectedFile().getPath();
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                StringBuilder text = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    text.append(line).append("\n");
                }
                inputTextField.setText(text.toString().trim());
                showMessage2("Text loaded from " + fileName);
            } catch (IOException e) {
                showMessage1("Error loading text from the file.");
            }
        }
    }


	private void showMessage1(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
	private void showMessage2(String message) {
        JOptionPane.showMessageDialog(this, message, "Successfull", JOptionPane.INFORMATION_MESSAGE);
    }
	
}
