import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;

public class CurrencyConverter extends JFrame 
{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	String[] ArrayCurrency = new String[] { "EUR", "USD", "CNY", "GBP", "JPY" };
	String selectedItem = ArrayCurrency[0];

	
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() 
			{
				try 
				{
					CurrencyConverter frame = new CurrencyConverter();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true); 
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}	

	public CurrencyConverter() 
	{
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout (0, 1, 5, 5));
		
		JPanel panelChoiceCurrency = new JPanel();
		panelChoiceCurrency.setLayout(new GridLayout (1, 0, 5, 5));
		contentPane.add(panelChoiceCurrency); 
		
		JTextField textFieldCurrency = new JTextField(50);
		textFieldCurrency.setMargin(new Insets ( 0, 10, 0, 0));
		panelChoiceCurrency.add(textFieldCurrency);
		
		JComboBox<String> comboList = new JComboBox<>(ArrayCurrency);
		comboList.setMaximumSize(new Dimension(Integer.MAX_VALUE, comboList.getPreferredSize().height));
		panelChoiceCurrency.add(comboList);
		
		comboList.addActionListener(e -> 
		{
			selectedItem = (String) comboList.getSelectedItem();
			System.out.println("select : " + selectedItem);
			
			RefreshCurrency();
		});
		
		RefreshCurrency();
	}
	
	public void RefreshCurrency()
	{
		// Crée la liste des devise et remove la selection du comboBox
		List<String> listCurrency = new ArrayList<>(Arrays.asList(ArrayCurrency));
		listCurrency.remove(selectedItem);
		System.out.println(listCurrency); 
		
		// Supprime tout les elements present dans le contentPane sauf le premier (tout les panelCurrency)
		Component[] components = contentPane.getComponents();
		for( int i = 1; i < components.length; i++)
		{
			contentPane.remove(components[i]);
		}
		
		contentPane.revalidate();
		contentPane.repaint(); 
		
		// Crée tout les panelCurrency en fonction de la liste actualisé
		for ( int i = 0; i < listCurrency.size(); i++)
		{
			JPanel panelCurrency = new JPanel();
			panelCurrency.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			panelCurrency.setLayout(new GridLayout (1, 0, 5, 5));
			contentPane.add(panelCurrency);
			
			// Ajoute l'image correspondante en utilisant la fontion ResizeImage
			panelCurrency.add(ResizeImage(listCurrency.get(i))); 
			
			JLabel labelCurrency = new JLabel(listCurrency.get(i)); 
			labelCurrency.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
			labelCurrency.setForeground(Color.BLACK);
			panelCurrency.add(labelCurrency); 
			
			JPanel panelResult = new JPanel();
			panelResult.setBackground(Color.LIGHT_GRAY);
			panelCurrency.add(panelResult); 
			
			JLabel labelResult = new JLabel ( "0 " + ArrayCurrency[i]);
			panelResult.add(labelResult); 
		}
	}
	
	
	public JLabel ResizeImage(String imageCurrency)
	{
		
		// Charger Image 
		ImageIcon imageIcon = new ImageIcon("src/images/" + imageCurrency + ".png" );
		Image image = imageIcon.getImage();
		
		// Redimensionner Image
		
		int labelWidth = 50;
		int labelHeight = 50;
		Image resizedImage = image.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
		ImageIcon resizedIcon = new ImageIcon(resizedImage);
		
		JLabel imageLabel = new JLabel(resizedIcon);
		
		return imageLabel; 
		
	}
		
		
		
		
	
	
	

}
