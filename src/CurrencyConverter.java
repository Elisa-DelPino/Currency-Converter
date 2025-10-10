import java.awt.EventQueue;
import java.awt.Graphics2D;
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
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.imageio.ImageIO;
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
	JTextField textFieldCurrency = new JTextField(10);
	JPanel textSelectedCurrency = new JPanel();
	JLabel textCurrency = new JLabel (", 00 " + selectedItem);
	
	
	
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
		
		URL url = getClass().getResource("images/icon.png");
		
		ImageIcon img = new ImageIcon(url);
		Image imgLogo = img.getImage();
		setIconImage(imgLogo);
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 150, 400, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout (0, 1, 5, 5));
		
		JPanel panelChoiceCurrency = new JPanel();
		panelChoiceCurrency.setLayout(new GridLayout (1, 0, 5, 5));
		contentPane.add(panelChoiceCurrency); 
		
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
				    onTextChanged();
				} else {
				    onTextChanged();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				onTextChanged();
			}
			
			
		});
		
		JComboBox<String> comboList = new JComboBox<>(ArrayCurrency);
		comboList.setMaximumSize(new Dimension(Integer.MAX_VALUE, comboList.getPreferredSize().height));
		panelChoiceCurrency.add(comboList);
		
		textSelectedCurrency.setBackground(Color.LIGHT_GRAY);
		panelChoiceCurrency.add(textSelectedCurrency);
		
		textSelectedCurrency.add(textCurrency);
		textSelectedCurrency.setLayout(new GridBagLayout());
		
		comboList.addActionListener(e -> 
		{
			selectedItem = (String) comboList.getSelectedItem();
			
			onTextChanged();
			RefreshCurrency();
		});
		
		RefreshCurrency();

	}
	
	public void RefreshCurrency() {
	    // Crée la liste des devises sans la devise source
	    List<String> listCurrency = new ArrayList<>(Arrays.asList(ArrayCurrency));
	    listCurrency.remove(selectedItem);

	    // Supprime tous les panels sauf le premier
	    Component[] components = contentPane.getComponents();
	    for (int i = components.length - 1; i >= 1; i--) {
	        contentPane.remove(components[i]);
	    }

	    contentPane.revalidate();
	    contentPane.repaint();

	    // Ajoute les nouveaux panels pour chaque devise cible
	    for (int i = 0; i < listCurrency.size(); i++) {
	        JPanel panelCurrency = new JPanel();
	        panelCurrency.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	        panelCurrency.setLayout(new GridLayout(1, 0, 5, 5));
	        contentPane.add(panelCurrency);

	        try {
				panelCurrency.add(ResizeImage(listCurrency.get(i)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        JLabel labelCurrency = new JLabel(listCurrency.get(i));
	        labelCurrency.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
	        labelCurrency.setForeground(Color.BLACK);
	        panelCurrency.add(labelCurrency);

	        JPanel panelResult = new JPanel();
	        panelResult.setBackground(Color.LIGHT_GRAY);

	        // Centrage du label dans panelResult
	        panelResult.setLayout(new GridBagLayout());

	        double resultConvertTextField = Request(doubleTextField, selectedItem, listCurrency.get(i));
	        DecimalFormat df = new DecimalFormat("#, ###.00");
			String doubleTextFieldFormate = df.format(resultConvertTextField);
	        JLabel labelResult = new JLabel(doubleTextFieldFormate + " " + listCurrency.get(i));

	        panelResult.add(labelResult);
	        panelCurrency.add(panelResult);
	        
	        
	    }

	    // Valider les changements
	    contentPane.revalidate();
	    contentPane.repaint();

	    // Redimensionner la fenêtre pour que tout rentre
	    // on appelle pack() pour ajuster la taille selon le contenu
	    this.pack();

	    // Optionnel : pour que la fenêtre reste centrée à l’écran
	    //this.setLocationRelativeTo(null);
	}

	
	
	public JLabel ResizeImage(String imageCurrency) throws IOException
	{
		
		// Charger Image 
		URL url = getClass().getResource("/images/" + imageCurrency + ".png");
		if (url == null) {
		    System.err.println("Image non trouvée : /images/" + imageCurrency + ".png");
		    return new JLabel();  // ou un label vide ou une image par défaut
		} else {
		    ImageIcon imageIcon = new ImageIcon(url);
		    return new JLabel(imageIcon);
		}

		
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
	
	public void onTextChanged() {
		
		String texte = textFieldCurrency.getText();
		
		if (textFieldCurrency.getText() == null || textFieldCurrency.getText().trim().isEmpty()) {
			
			texte = ("0.0");
		}
		
		
		doubleTextField = Double.parseDouble(texte);
		
		DecimalFormat df = new DecimalFormat("#, ###.00");
		String doubleTextFieldFormate = df.format(doubleTextField);
		
		textSelectedCurrency.remove(textCurrency);
	    textCurrency = new JLabel (doubleTextFieldFormate + " " + selectedItem );
	    textSelectedCurrency.add(textCurrency);
		
		RefreshCurrency(); 
	}

}
