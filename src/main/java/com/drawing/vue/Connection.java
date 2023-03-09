package com.drawing.vue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.drawing.projet.Client;
import com.drawing.projet.Commun;

import static com.drawing.utils.Utils.IMAGE_PATH;

public class Connection implements ActionListener{

	private static final Logger logger = Logger.getLogger(Connection.class.getName());
	private JFrame frame;
	private JTextField UsernameTextField;
	private JPasswordField passwordField;
	private JButton btnOk;
	public static Client client;
    public static DrawingWindow drawingWindow;
    public static Connection window; 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new Connection();				    
					window.frame.setMaximumSize(new Dimension(350,105));
					window.frame.setMaximizedBounds(new Rectangle(50, 50, 350, 105));
					window.frame.setTitle("Connection");
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Connection() {
		this.initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 250, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.WEST);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{39, 114, 46, 4, 0};
		gbl_panel.rowHeights = new int[]{19, 25, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		//panel.setLayout(new FlowLayout());		
		
		JLabel lblLogin = new JLabel("Login");
		GridBagConstraints gbc_lblLogin = new GridBagConstraints();
		gbc_lblLogin.insets = new Insets(0, 0, 5, 5);
		gbc_lblLogin.gridx = 0;
		gbc_lblLogin.gridy = 1;
		panel.add(lblLogin, gbc_lblLogin);		
		
		UsernameTextField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.anchor = GridBagConstraints.NORTHWEST;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 1;
		panel.add(UsernameTextField, gbc_textField);
		UsernameTextField.setColumns(10);
		
		JLabel lblPsswd = new JLabel("psswd");
		GridBagConstraints gbc_lblPsswd = new GridBagConstraints();
		gbc_lblPsswd.insets = new Insets(0, 0, 5, 5);
		gbc_lblPsswd.gridx = 0;
		gbc_lblPsswd.gridy = 2;
		panel.add(lblPsswd, gbc_lblPsswd);
		
		passwordField = new JPasswordField();
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.anchor = GridBagConstraints.NORTH;
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.gridx = 1;
		gbc_passwordField.gridy = 2;
		panel.add(passwordField, gbc_passwordField);
		
		btnOk = new JButton("OK");
		btnOk.addActionListener(this);
		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.anchor = GridBagConstraints.NORTH;
		gbc_btnOk.insets = new Insets(0, 0, 0, 5);
		gbc_btnOk.gridx = 1;
		gbc_btnOk.gridy = 3;
		panel.add(btnOk, gbc_btnOk);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnOk) {
			logger.info("Password: " + Arrays.toString(this.passwordField.getPassword()));
			if(this.UsernameTextField.getText().equals("uc") && this.passwordField.getText().equals("uc")) {

			drawingWindow = new DrawingWindow();
			drawingWindow.setSize(new Dimension(800,600));
			drawingWindow.setVisible(true);
			window.frame.setVisible(false);
			Commun.m_connecte.addElement(drawingWindow);
			
			client = new Client(drawingWindow);
			DrawingWindow.client = client;
			client.connecter();
			}
			else {
				JOptionPane.showMessageDialog(null, "Login ou mot de pass incorrect",
					  "Accès refusé", JOptionPane.INFORMATION_MESSAGE,
					  		new ImageIcon(IMAGE_PATH + "erreur.jpg"));
      
			}
		}
	}
}