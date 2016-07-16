#Notizen und Ideen zum Dokumentationsdokument

__Alles Vorschläge von mir, wir ich die Sache angehen würde:__

* Er möchte ja eine Übersicht, über unsere Projekt bekommen, als würde ich schon nochmal die Idee mit dem UML-Diagramm rauskramen. Ich würde mal anfangen, ein Diagramm pro Package zu erstllen. Die geben halt schon mal nen schnellen Überblick über alle Code-Files und sind bestimmt ein Alleinstellungsmerkmal im Vergleich zu den anderen Projektlösungen.

* Denke es sollte in der Dokumentation deutlch rauskommen, dass das "Herz" des Projektes das Message-Package ist. 

* Grober Aufbau:
		* Es gibt die 3 Anwendungen Producer, Consumer und MessageServer (nur kurz beschreiben, was sie können, sozusagen die Option aus der CLI erkläern. Implementatiosnweise später)
		* Zur Kommunikation verwenden sie das Package Message -> MessageTypes = Kommuniktationsströme erläutern (siehe Bild in WhatsApp-Gruppe; vlt sogar Digitalisieren?). Hier dann auch erläutern, wie das mit den Payloads funktioniert, vlt auf ein zwei Besonderheiten in den Payloads drauf eingehen (?, wie genau soll das werden?)
		* Dann die Besonderheiten in der Implementierung der Producer-, Consumer- und MessageServer-Klassen (wie haben wir das mit dem Multithreading gelöst. Welche Datenstrukturen (siehe Aufgabenstellung). Die Dreiteilung erläutern (Producer, ProucerIF, ProducerCLI)
