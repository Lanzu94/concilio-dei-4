package client.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.awt.event.ActionEvent;

public class SelectGUICLI extends Observable{

	/*
	 * In this View the player chose at the beginning the connection mode and
	 * the graphics mode
	 */
	private JPanel contentPane;
	private String graphics, connection;
	private boolean selected=false;
	/**
	 * Create the frame.
	 */
	public SelectGUICLI() {
		final JFrame window = new JFrame();
		window.setTitle("The Concil");
		window.setVisible(true);
		window.setLocation(200, 200);
		window.setResizable(false);
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBounds(100, 100, 347, 163);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		window.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		ButtonGroup group1=new ButtonGroup();
		final JRadioButton radiobuttonGUI = new JRadioButton("GUI");
		radiobuttonGUI.setBounds(8, 26, 127, 25);
		contentPane.add(radiobuttonGUI);
		group1.add(radiobuttonGUI);
		final JRadioButton radiobuttonCLI = new JRadioButton("CLI");
		radiobuttonCLI.setBounds(8, 56, 127, 25);
		contentPane.add(radiobuttonCLI);
		group1.add(radiobuttonCLI);
		
		JLabel lblSelectGraphicsMode = new JLabel("Select Graphics mode");
		lblSelectGraphicsMode.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSelectGraphicsMode.setBounds(8, 0, 143, 16);
		contentPane.add(lblSelectGraphicsMode);
		
		ButtonGroup group2=new ButtonGroup();
		final JRadioButton radiobuttonSocket = new JRadioButton("Socket");
		radiobuttonSocket.setBounds(184, 56, 127, 25);
		contentPane.add(radiobuttonSocket);
		group2.add(radiobuttonSocket);
		final JRadioButton radiobuttonRmi = new JRadioButton("RMI");
		radiobuttonRmi.setBounds(184, 26, 127, 25);
		contentPane.add(radiobuttonRmi);
		group2.add(radiobuttonRmi);
		
		JLabel lblSelectConnectionType = new JLabel("Select Connection Type");
		lblSelectConnectionType.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSelectConnectionType.setBounds(184, 0, 182, 16);
		contentPane.add(lblSelectConnectionType);
		
		JButton btnNewButton = new JButton("PLAY");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if((radiobuttonGUI.isSelected() || radiobuttonCLI.isSelected()) && (radiobuttonSocket.isSelected() || radiobuttonRmi.isSelected()))
				{
					if(radiobuttonGUI.isSelected())
					{
						graphics="GUI";
					}
					if(radiobuttonCLI.isSelected())
					{
						graphics="CLI";
					}
					if(radiobuttonSocket.isSelected())
					{
						connection="Socket";
					}
					if(radiobuttonRmi.isSelected())
					{
						connection="RMI";
					}
					selected=true;
					setChanged();
				    notifyObservers();
				    window.hide();
				    
				}
				else 
				{
					JOptionPane.showMessageDialog(null, "Select the graphics and connection mode", "Attention", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNewButton.setBounds(0, 90, 345, 40);
		contentPane.add(btnNewButton);
		
		contentPane.revalidate();
		contentPane.repaint();
	}
	
	public String[] inizia()
	{
		if(selected==true)
		{
			String []chosen=new String[2];
			chosen[0]=graphics;
			chosen[1]=connection;
			return chosen;
		}
		return null;
	}
}
