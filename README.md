# MB
A message broker implemented in Java according to our courses project guidelines

Bei Fehler in den englischen Kommentaren bitte korrigieren ... 


Anmerkungen: 













Komplexe Enums Folie 153 und 154

HashMap f�r Producer (und Konsumer) kapseln we auf Folie 98 JCF ?

Thema Output/InputStreams:
Im Code 
while (rein.ready()) {
		s = rein.readLine();
		// System.out.println(s);
		raus.println(s);
	}
	F�hrt die Zeile System.out.println(s); dazu, dass s NICHT mehr zur�ckgesendet wird ??????!!!!!!!!