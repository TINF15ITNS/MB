package Producer;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class GUIProducer extends JFrame implements ComponentListener, ActionListener {
	private static final long serialVersionUID = 1L;
	/**
	 * Wird ein neues Fenster erzeugt und es existiert schon eins, dann soll es ein wenig unter der Default-Location erzeugt werden. Dazu wird diese
	 * Klassenvariable benötigt
	 */
	static int anzahlFramesProuzenten = 0;
	/**
	 * Fenster, in welchem der "Mitteilung erzeugen" - Button gedrückt werden kann.
	 */
	JPanel fensterMitteilungErzeugen;
	/**
	 * Fenster, in welchem die benötigten Informationen zum Abschicken einer Push-Meldung abgfragt werden.
	 */
	JPanel fensterInformationenMitteilung;
	/**
	 * Durch Drücken dieses Buttons wird der Prozess "Erzeugen einer Push-Nachricht" angestoßen.
	 */
	JButton mitteilungErzeugen;
	/**
	 * Wurden alle Daten eingegeben, kann der Prozess "Erzeugen einer Push-Nachricht" mit diesem Button abgeschlossen werden.
	 */
	JButton abschickenMitteilung;
	JLabel ueberschriftFensterInformationenMitteilung;
	JTextField eingabeInformationenPushNachricht;

	/**
	 * Konstruktor erzeugt JFrame und die beiden benötigten Panels mit Inhalt
	 * 
	 * @param name
	 *            Erwartet den Namen des Produzenten
	 */
	public GUIProducer(String name) {
		// Fenster erzeugen
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 400);
		this.setTitle(name);
		// x-Position: in Mitte der linken Fensterhälfte
		// y-Position: bei 0.2 der Bildschirmgröße + Versatz bei mehreren Fenstern
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 4) - (this.getWidth() / 2),
				(int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.15) + anzahlFramesProuzenten * 20);
		// dadruch, dass wir dem Frame und den folgenden Panels kein Layout zuordnen, können wir alle Inhalte nach belieben "Freihand" eintragen
		this.setLayout(null);
		this.addComponentListener(this);

		// Panel für Zustand 1
		fensterMitteilungErzeugen = new JPanel();
		fensterMitteilungErzeugen.setLayout(null);
		this.add(fensterMitteilungErzeugen);

		mitteilungErzeugen = new JButton();
		mitteilungErzeugen.setText("Mitteilung erzeugen");
		mitteilungErzeugen.addActionListener(this);
		fensterMitteilungErzeugen.add(mitteilungErzeugen);
		mitteilungErzeugen.setMinimumSize(new Dimension(200, 50));
		mitteilungErzeugen.setMaximumSize(new Dimension(300, 75));
		// Methode leistet alle Groessenanpassungen
		fensterinhaltFenster1Anpassen();
		fensterMitteilungErzeugen.setVisible(true);

		// Panel für Zustand 2
		fensterInformationenMitteilung = new JPanel();
		fensterInformationenMitteilung.setLayout(null);
		this.add(fensterInformationenMitteilung);

		abschickenMitteilung = new JButton();
		abschickenMitteilung.setText("Nachricht abschicken?");
		abschickenMitteilung.addActionListener(this);
		fensterInformationenMitteilung.add(abschickenMitteilung);

		ueberschriftFensterInformationenMitteilung = new JLabel("Text der Mitteilung: ");
		fensterInformationenMitteilung.add(ueberschriftFensterInformationenMitteilung);

		eingabeInformationenPushNachricht = new JTextField();
		fensterInformationenMitteilung.add(eingabeInformationenPushNachricht);
		fensterinhaltFenster2Anpassen();

		fensterInformationenMitteilung.setVisible(false);

		this.setVisible(true);
	}

	/**
	 * Stellt die Größen und Poisitionen des ersten Panels ein. Wird durch den Konstruktor und nach manueller Größenänderung des JFrames aufgerufen
	 */
	void fensterinhaltFenster1Anpassen() {
		fensterMitteilungErzeugen.setSize(this.getWidth(), this.getHeight());
		// klappt nicht , warum auch immer -> Problemlösung auf später verschoben
		/*
		 * Dimension d = new Dimension((int) (fensterMitteilungErzeugen.getWidth() * 0.5), (int) (fensterMitteilungErzeugen.getHeight() * 0.125)); if
		 * (d.getWidth() < mitteilungErzeugen.getWidth() && d.getHeight() < mitteilungErzeugen.getHeight()) { System.out.println("Bin im kleiner-Fall");
		 * mitteilungErzeugen.setSize(mitteilungErzeugen.getMinimumSize()); } else if (d.getWidth() > mitteilungErzeugen.getWidth() || d.getHeight() >
		 * mitteilungErzeugen.getHeight()) { mitteilungErzeugen.setSize(mitteilungErzeugen.getMaximumSize()); System.out.println("Bin im groesser-Fall"); } else
		 * { System.out.println("Bin im Normal-Fall"); mitteilungErzeugen.setSize(d); }
		 */
		mitteilungErzeugen.setSize((int) (fensterMitteilungErzeugen.getWidth() * 0.5), (int) (fensterMitteilungErzeugen.getHeight() * 0.125));
		// Mitte des Fensters
		mitteilungErzeugen.setLocation((int) ((fensterMitteilungErzeugen.getWidth() / 2) - (mitteilungErzeugen.getWidth() / 2)),
				(int) ((fensterMitteilungErzeugen.getHeight() / 2) - (mitteilungErzeugen.getHeight() / 2)));

	}

	/**
	 * Stellt die Größen und Poisitionen des zweiten Panels ein. Wird durch den Konstruktor und nach manueller Größenänderung des JFrames aufgerufen
	 */
	void fensterinhaltFenster2Anpassen() {
		fensterInformationenMitteilung.setSize(this.getWidth(), this.getHeight());

		ueberschriftFensterInformationenMitteilung.setSize((int) (fensterInformationenMitteilung.getWidth() * 0.3),
				(int) (fensterInformationenMitteilung.getHeight() * 0.1));
		ueberschriftFensterInformationenMitteilung.setLocation(
				(int) ((fensterInformationenMitteilung.getWidth() / 2) - (ueberschriftFensterInformationenMitteilung.getWidth() / 2)),
				(int) ((fensterInformationenMitteilung.getHeight() * 0.25) - (ueberschriftFensterInformationenMitteilung.getHeight() / 2)));
		eingabeInformationenPushNachricht.setSize((int) (fensterInformationenMitteilung.getWidth() * 0.3),
				(int) (fensterInformationenMitteilung.getHeight() * 0.1));
		eingabeInformationenPushNachricht.setLocation((int) ((fensterMitteilungErzeugen.getWidth() / 2) - (eingabeInformationenPushNachricht.getWidth() / 2)),
				(int) ((fensterMitteilungErzeugen.getHeight() * 0.5) - (eingabeInformationenPushNachricht.getHeight() / 2)));

		abschickenMitteilung.setSize((int) (fensterInformationenMitteilung.getWidth() * 0.3), (int) (fensterInformationenMitteilung.getHeight() * 0.1));
		abschickenMitteilung.setLocation((int) ((fensterMitteilungErzeugen.getWidth() / 2) - (eingabeInformationenPushNachricht.getWidth() / 2)),
				(int) ((fensterMitteilungErzeugen.getHeight() * 0.75) - (eingabeInformationenPushNachricht.getHeight() / 2)));
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// Aktuallisiere Locations und Größen der Fensterinhalte
		fensterinhaltFenster1Anpassen();
		fensterMitteilungErzeugen.repaint();
		fensterinhaltFenster2Anpassen();
		fensterInformationenMitteilung.repaint();

	}

	@Override
	public void componentShown(ComponentEvent e) {
		fensterinhaltFenster1Anpassen();
		fensterMitteilungErzeugen.repaint();
		fensterinhaltFenster2Anpassen();
		fensterInformationenMitteilung.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource().equals(mitteilungErzeugen)) {
			fensterMitteilungErzeugen.setVisible(false);
			fensterInformationenMitteilung.setVisible(true);
		}
		if (arg0.getSource().equals(abschickenMitteilung)) {
			String eingabe = eingabeInformationenPushNachricht.getText();
			// sende hier an messageServer

			fensterMitteilungErzeugen.setVisible(true);
			fensterInformationenMitteilung.setVisible(false);
		}

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}
}
