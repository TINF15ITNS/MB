package Producer;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class GUIProducerErzeugen extends JFrame implements ComponentListener, ActionListener {
	JButton erzeugeProducer;

	public GUIProducerErzeugen() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(250, 75);
		this.setTitle("Erzeugen von Produzenten");
		// x-Position: in Mitte der linken Fensterhälfte
		// y-Position: bei 0.05 der Bildschirmgröße
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 4) - (this.getWidth() / 2),
				(int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.05));
		this.setLayout(new BorderLayout());
		this.addComponentListener(this);

		erzeugeProducer = new JButton("Erzeuge einen neuen Produzenten");
		this.add(erzeugeProducer, BorderLayout.CENTER);

		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.equals(erzeugeProducer)) {

		}

	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

}
