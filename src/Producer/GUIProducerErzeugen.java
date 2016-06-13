package Producer;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class GUIProducerErzeugen extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	JButton erzeugeProducerButton;
	ProducerErzeugen producerErzeugen;

	/*
	 * To Do:
	 * 
	 * Vielleicht die Sache mit ComponentListener und ActionListener mttels innerer oder anonoymer Klassen implmentieren
	 */

	public GUIProducerErzeugen(ProducerErzeugen p) {
		producerErzeugen = p;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 75);
		this.setTitle("Erzeugen von Produzenten");
		// x-Position: in Mitte der linken Fensterhälfte
		// y-Position: bei 0.05 der Bildschirmgröße
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 4) - (this.getWidth() / 2),
				(int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.05));
		this.setLayout(new BorderLayout());

		erzeugeProducerButton = new JButton("Erzeuge einen neuen Produzenten");
		this.add(erzeugeProducerButton, BorderLayout.CENTER);
		erzeugeProducerButton.addActionListener(this);
		erzeugeProducerButton.setSize(220, 70);

		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.equals(erzeugeProducerButton)) {
			producerErzeugen.setErzeugeProducer(true);
		}

	}
}
