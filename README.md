# MB
A message broker implemented in Java according to our courses project guidelines

Bei Fehler in den englischen Kommentaren bitte korrigieren ... 


#!!!!!!!!!!Bitte lesen !!!!!!!!!!!!!!
Also ich hab mal den Grundaufbau der Klasse MessageServer angedeutet. Schaut euch die Klasse mal an, in den Methoden steht drinne, was sie mal machen sollen. Um genauer zu verstehen, was ich mir dabei gedacht habe, bzw wie ich mir die ganze Sache im allgemeinen vorstelle, empfiehlt es sich mal in das Package Consumer reinzuschauen. Ich war da fleissig und hab schon mal den TCP_Teil, also Registrierung und Einschreiben fÃ¼r Producer, implementiert. Habs noch nicht getestet...
Bitte schaut mal drÃ¼ber, ob das so ok ist. Ob manche Desgin-Entscheidungen die ich getroffen habe in Ordnung sind. 
DANKE !!!








#Offene Fragen: 

* soll der Server bei der UDP-Kommunikation Antworten senden bzw erhalten? Also der Konsument sendet ne Deregistrierungmessage an den Server und wartet dann auf ne Antwort? Oder einfach nur einmal ne Message?
* ich hab die Messages jetzt mit diesem XML-En/Decoder versendet. Aber eigentlich ists völlig wurscht, ob wir das auch als Objekt versenden und Serializable benutzen ...
	weiß net, ob dass vlt sogar schöner wäre, da wir für UDP die Message ja net per XML-Format (oder doch?) verschicken können, sondern das auch serialisieren
	Denn: Für UDP Nachrichten müssen DatagrammPackets erstellt werden und die übermitteln Objekte, indem wir das Objekt zu einem String umwandeln und darauf 	getBytes() aufrufen ... dabei das geht auch kompliziert iwie anders siehe Skript Folie 183 
	
	
HALT !!! DAS GEHT SO NICHT ... man kann ja schlecht eraten wie groß der Antwort-String von dem Server ist. Ich glaub ich muss mal nach der komplizierten Variante schauen
	
ok das hab ich hinbekommen ... Aber die Frage bleibt offen !!!
	
* man kann sowohl TCP als auch UDP Sockets an einen bestimmten lokalen Port binden (DatagramSocket udpSocket = new DatagramSocket( localPort ) ; und Socket s s.bind();) ... macht das Sinn ?











Sry sind paar Notizen fÃ¼r mich :)
Komplexe Enums Folie 153 und 154

HashMap fÃ¼r Producer (und Konsumer) kapseln we auf Folie 98 JCF ?

Thema Output/InputStreams:
Im Code 
while (rein.ready()) {
		s = rein.readLine();
		// System.out.println(s);
		raus.println(s);
	}
	FÃ¼hrt die Zeile System.out.println(s); dazu, dass s NICHT mehr zurÃ¼ckgesendet wird ??????!!!!!!!!
