# MB
A message broker implemented in Java according to our courses project guidelines

Bei Fehler in den englischen Kommentaren bitte korrigieren ... 


!!!!!!!!!!Bitte lesen !!!!!!!!!!!!!!
Also ich hab mal den Grundaufbau der Klasse MessageServer angedeutet. Schaut euch die Klasse mal an, in den Methoden steht drinne, was sie mal machen sollen. Um genauer zu verstehen, was ich mir dabei gedacht habe, bzw wie ich mir die ganze Sache im allgemeinen vorstelle, empfiehlt es sich mal in das Package Consumer reinzuschauen. Ich war da fleissig und hab schon mal den TCP_Teil, also Registrierung und Einschreiben für Producer, implementiert. Habs noch nicht getestet...
Bitte schaut mal drüber, ob das so ok ist. Ob manche Desgin-Entscheidungen die ich getroffen habe in Ordnung sind. 
DANKE !!!








Anmerkungen: 












Sry sind paar Notizen für mich :)
Komplexe Enums Folie 153 und 154

HashMap für Producer (und Konsumer) kapseln we auf Folie 98 JCF ?

Thema Output/InputStreams:
Im Code 
while (rein.ready()) {
		s = rein.readLine();
		// System.out.println(s);
		raus.println(s);
	}
	Führt die Zeile System.out.println(s); dazu, dass s NICHT mehr zurückgesendet wird ??????!!!!!!!!
