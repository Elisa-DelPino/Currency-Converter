import java.awt.EventQueue;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.BorderLayout;
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
	
	JLabel imageSelectedCurrency = new JLabel ();
	Double doubleTextField = 0.0; 
	
	RequestApi api = new RequestApi();

	
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
		super("CURRENCY CONVERTER"); 
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 150, 400, 400);
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
		
		// Empeche l'ecriture de lettre dans le JtextField
		textFieldCurrency.addKeyListener((KeyListener) new KeyAdapter() {
		    @Override
		    public void keyTyped(KeyEvent e) {
		        char c = e.getKeyChar();
		        // Si ce n’est pas un chiffre, on ignore
		        if (!Character.isDigit(c) && c != '.') {  // autoriser aussi le point “.” si tu veux des décimales
		            e.consume();  // empêche le caractère d’être inséré
		        }
		    }
		});
		
		
		// Ecoute les entrées utilisateurs du JtextField
		textFieldCurrency.getDocument().addDocumentListener((DocumentListener) new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				onTextChanged();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				
				// Verifie que le JtextField n'est pas null avant d'apeller onTextChanged
				if (textFieldCurrency.getText() == null || textFieldCurrency.getText().trim().isEmpty()) {
				    doubleTextField = 0.0; 
				    RefreshCurrency();
				} else {
				    onTextChanged();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				onTextChanged();
			}
			
			private void onTextChanged() {
				String texte = textFieldCurrency.getText();
				doubleTextField = Double.parseDouble(texte);
				RefreshCurrency(); 
			}
			
		});
		
		JComboBox<String> comboList = new JComboBox<>(ArrayCurrency);
		comboList.setMaximumSize(new Dimension(Integer.MAX_VALUE, comboList.getPreferredSize().height));
		panelChoiceCurrency.add(comboList);
		
		imageSelectedCurrency = ResizeImage(selectedItem); 
		panelChoiceCurrency.add(imageSelectedCurrency); 
		
		comboList.addActionListener(e -> 
		{
			selectedItem = (String) comboList.getSelectedItem();
			panelChoiceCurrency.remove(imageSelectedCurrency);
			imageSelectedCurrency = ResizeImage(selectedItem);
			panelChoiceCurrency.add(imageSelectedCurrency); 
			
			
			RefreshCurrency();
		});
		
		RefreshCurrency();

	}
	
	public void RefreshCurrency()
	{
		// Crée la liste des devise et remove la selection du comboBox
		List<String> listCurrency = new ArrayList<>(Arrays.asList(ArrayCurrency));
		listCurrency.remove(selectedItem); 
		
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
			
			panelResult.setLayout(new GridBagLayout()); 
			
			
			double resultConvertTextField = Request(doubleTextField, selectedItem, listCurrency.get(i));
			JLabel labelResult = new JLabel ( resultConvertTextField + " " +  listCurrency.get(i));
			
			
			panelResult.add(labelResult); 
			panelCurrency.add(panelResult);
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
	
	public Double Request(Double amount, String from, String to)
	{
        // On veut convertir le montant
        Double converted = api.convertAmount(amount, from, to);
        if (converted != null) {
            double arrondi = Math.round(converted * 100.0) / 100.0;
            return arrondi; 
        } else {
            System.out.println("Erreur lors de la conversion.");
            return 0.0;
        }
	}

}
