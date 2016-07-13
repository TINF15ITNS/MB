# MB
A message broker implemented in Java according to our courses project guidelines

# ToDo
* alle Downcast mit instanceof überprüfen oder auf MessageType?
* überlegen oob man da nicht was mit Generics machen kann

* Interfaceentkopplung
* try-with-ressouces

* was passiert wenn Server nicht da ist, TimeoutExceptions abfangen und ausgeben



#Offene Fragen: 

* man kann sowohl TCP als auch UDP Sockets an einen bestimmten lokalen Port binden (DatagramSocket udpSocket = new DatagramSocket( localPort ) ; und Socket s s.bind();) ... macht das Sinn ?
