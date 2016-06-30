#Übersicht verschiedene Messages

Wollte mal kurz die Anforderungen an die Messages festhalten:

Typ 1: RegisterOnServer: 
  Nachricht __an__ Server: 
    consignorID: mitgesendete ID ist 0, da noch keine ConsumerID erzeugt wurde
    payload: Name des Konsumenten
  Nachricht __von__ Server:
      consignorID: _noch zu definieren (vlt ne serverspezifische ID?)_
      payload: die vom Server erzeugte Id, mit der der Consumer sich ab jetzt zu melden hat

Typ 2: RegisterOnProducer: 
  Nachricht __an__ Server:
    consignorID: ConsumerID
    payload: Namen der Produzenten mit einem ";" getrennt
  Nachricht __von__ Server:
    consignorID: _noch zu definieren (vlt ne serverspezifische ID?)_
    payload: "ok" und "cannotRegisterOnProducers" falls es warum auch immer nicht geklappt hat

Typ 3: Deregister: 
  Nachricht __an__ Server: 
    consignorID: ConsumerID
    payload: null
  Nachricht __von__ Server:
  _Gibts hier eine Antwort? Habe erstmal keine implementiert_

Typ 4: Message: 
  Nachricht __an__ Server: 
  _Solls ne Nachricht nach dem Motto: "Ok habs erhalten" geben? Oder sendet der Server einfach einmal?_
    
  Nachricht __von__ Server:
    consignorID:  _noch zu definieren (vlt ne serverspezifische ID?)_
    payload: in folgendem Format: "<Name des Producers>;<Inhalt der Meldung>"

Typ 5: getProducer: 
  Nachricht __an__ Server: 
    consignorID: ConsumerID
    payload:  null
  Nachricht __von__ Server:
    consignorID: _noch zu definieren (vlt ne serverspezifische ID?)_
    payload: Namen der Produzenten mit einem ";" getrennt ... __Wichtig: Ich würde sagen, die Namen der Produzenten müssen eindeutig sein, heißt, der Server muss mit der Antwort des Konsumenten in welcher die Namen stehen, ermitteln können, auf welche der Konsument sich einschreiben will. Hier auch noch ne ID für Produzenten zu erzeugen ergib tProbleme, da für die Message ja auf jeden Fall der Name mitgeschickt werden muss, um nachher ausgeben zu können, von wem die Nachricht kam ... Ansonsten m�sste in payload die Nummer zum r�ckmelden und er dazugeh�rige Name verschickt werden ... ginge auch__

      
      
#�nderungsvorschlag: 
Die Klasse Message h�lt anstatt eines Strings payload ein Objekt vom Typ eines Interfaces payload. Da wird dann im Konstruktor ein Objekt reingesteckt, was von dem interface erbt. Die implementierende Klasse hat dann die ben�tigten Attribute und das Interface ist nur so als Marker-interface gedacht. Ich glaub das ist ne gute Idee ... ich stecs grad mal in Github    

      
      
      
