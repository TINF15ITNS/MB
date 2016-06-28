# MB
A message broker implemented in Java according to our courses project guidelines

Bei Fehler in den englischen Kommentaren bitte korrigieren ... 


#!!!!!!!!!!Bitte lesen !!!!!!!!!!!!!!
Also ich hab mal den Grundaufbau der Klasse MessageServer angedeutet. Schaut euch die Klasse mal an. In den Methoden steht drinne, was sie mal machen sollen. Um genauer zu verstehen, was ich mir dabei gedacht habe bzw wie ich mir die ganze Sache im allgemeinen vorstelle, empfiehlt es sich mal in das Package Consumer reinzuschauen. Ich war da fleissig und hab schon mal den TCP-Teil ( ne mittlerweile alles) implementiert. Habs noch nicht getestet...
Bitte schaut mal drueber, ob das so ok ist und ob manche Desgin-Entscheidungen die ich getroffen habe in Ordnung ist. 
DANKE !!!

Also wenn der MessageServer implementiert wird, bitte nach den entsprechenden Methoden von Consumer schauen, was die verlangen.








#Offene Fragen: 

* soll der Server bei der UDP-Kommunikation Antworten senden bzw erhalten? Also der Konsument sendet ne Deregistrierungmessage an den Server und wartet dann auf ne Antwort? Oder einfach nur einmal ne Message?
* ich hab die Messages jetzt mit diesem XML-En/Decoder versendet. Aber eigentlich ists völlig wurscht, ob wir das auch als Objekt versenden und Serializable benutzen ...	weiß net, ob dass vlt sogar schöner wäre, da wir für UDP die Message ja net per XML-Format (oder doch?) verschicken können, sondern das auch serialisieren
Denn: Für UDP Nachrichten müssen DatagramPackets erstellt werden und die brauchen nen byte-Array als Data und um das zu erhalten nutzt man ein ByteArrayOutputStream und der serialisiert das Objekt ... Dann ist alles einheitlich

* man kann sowohl TCP als auch UDP Sockets an einen bestimmten lokalen Port binden (DatagramSocket udpSocket = new DatagramSocket( localPort ) ; und Socket s s.bind();) ... macht das Sinn ?
