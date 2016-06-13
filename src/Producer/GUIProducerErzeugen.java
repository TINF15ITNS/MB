package Producer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUIProducerErzeugen extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	JButton initialisiereProducerErzeugenButton, beendeProducerErzeugenButton;
	ProducerErzeugen producerErzeugen;
	JPanel panel1, panel2;
	JTextField textfield;

	/*
	 * To Do:
	 * 
	 * Vielleicht die Sache mit ComponentListener und ActionListener mttels innerer oder anonoymer Klassen implmentieren
	 * 
	 * setFocusable()
	 */

	public GUIProducerErzeugen(ProducerErzeugen p) {
		producerErzeugen = p;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Erzeugen von Produzenten");
		this.setLayout(new BorderLayout());

		panel1 = new JPanel();
		panel1.setSize(new Dimension(400, 75));
		initialisiereProducerErzeugenButton = new JButton("Neuer Produzent");
		panel1.add(initialisiereProducerErzeugenButton, BorderLayout.CENTER);
		initialisiereProducerErzeugenButton.addActionListener(this);
		initialisiereProducerErzeugenButton.setSize(220, 70);
		panel1.setVisible(true);
		this.add(panel1, BorderLayout.CENTER);

		panel2 = new JPanel();
		panel2.setSize(new Dimension(400, 300));
		panel2.setLayout(null);
		beendeProducerErzeugenButton = new JButton("OK");
		beendeProducerErzeugenButton.setSize(75, 30);
		beendeProducerErzeugenButton.setLocation((int) ((panel2.getWidth() / 2) - (beendeProducerErzeugenButton.getWidth() / 2)),
				(int) ((panel2.getHeight() * 0.3) - (beendeProducerErzeugenButton.getHeight() / 2)));
		beendeProducerErzeugenButton.addActionListener(this);
		panel2.add(beendeProducerErzeugenButton);
		textfield = new JTextField("Name des Produzenten");
		textfield.setSize(300, 30);
		textfield.setLocation((int) ((panel2.getWidth() / 2) - (textfield.getWidth() / 2)), (int) ((panel2.getHeight() * 0.6) - (textfield.getHeight() / 2)));
		panel2.add(textfield);
		panel2.setVisible(false);
		this.add(panel2, BorderLayout.CENTER);

		// Default Große ist das, des Panel1
		this.setSize(panel1.getSize());
		// x-Position: in Mitte der linken Fensterhälfte
		// y-Position: bei 0.05 der Bildschirmgröße
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 4) - (this.getWidth() / 2),
				(int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.05));
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Betrete jetzt die actionPerformed()-Methode");
		if (e.getSource().equals(initialisiereProducerErzeugenButton)) {
			System.out.println("1");
			panel1.setVisible(false);
			this.setSize(panel2.getSize());
			panel2.setVisible(true);
		}
		if (e.getSource().equals(beendeProducerErzeugenButton)) {
			System.out.println("1");
			panel2.setVisible(false);
			this.setSize(panel1.getSize());
			panel1.setVisible(true);
			String s = textfield.getText();
			textfield.setText("Name des Produzenten");
			producerErzeugen.erzeugeNeuenProducer(s);
		}

	}
}
