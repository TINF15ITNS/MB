# MB
A message broker implemented in Java according to our courses project guidelines

Bei Fehler in den englischen Kommentaren bitte korrigieren ... 

__Schaut euch bitte das Dokument Message.md an __

#!!!!!!!!!!Bitte lesen !!!!!!!!!!!!!!
Also ich hab mal den Grundaufbau der Klasse MessageServer angedeutet. Schaut euch die Klasse mal an. In den Methoden steht drinne, was sie mal machen sollen. Um genauer zu verstehen, was ich mir dabei gedacht habe bzw wie ich mir die ganze Sache im allgemeinen vorstelle, empfiehlt es sich mal in das Package Consumer reinzuschauen. Ich war da fleissig und hab schon mal den TCP-Teil ( ne mittlerweile alles) implementiert. Habs noch nicht getestet...
Bitte schaut mal drueber, ob das so ok ist und ob manche Desgin-Entscheidungen die ich getroffen habe in Ordnung ist. 
DANKE !!!

Also wenn der MessageServer implementiert wird, bitte nach den entsprechenden Methoden von Consumer schauen, was die verlangen.



#Offene Fragen: 

* man kann sowohl TCP als auch UDP Sockets an einen bestimmten lokalen Port binden (DatagramSocket udpSocket = new DatagramSocket( localPort ) ; und Socket s s.bind();) ... macht das Sinn ?
