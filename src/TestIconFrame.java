import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class TestIconFrame extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public TestIconFrame() {
        URL url = getClass().getResource("/images/icon.png");
        System.out.println("url = " + url);
        if (url != null) {
            setIconImage(new ImageIcon(url).getImage());
        } else {
            System.err.println("Icon non chargée");
        }
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
        URL url2 = getClass().getResource("/images/icon.png");
        System.out.println("URL icône = " + url2);

    }
    public static void main(String[] args) {
        new TestIconFrame();
        
     

    }
}
