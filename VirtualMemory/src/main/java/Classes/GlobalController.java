package Classes;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;


public class GlobalController {
    private InstructieController instructionAdministrator;
    private int time;
    private XMLReader xmlReader;
    private Instructie huidigeInstructie;
    private Queue<Instructie> instructieQueue;
    private ArrayList<Instructie> voltooideInstructies;
    private Proces huidigProces;
    private ArrayList<Proces> procesLijst;
    private ArrayList<Integer> address;
    final int LENGTE_OFFSET = 12;
    final int LENGTE_ADRES = 16;
    final int AANTAL_FRAMES_RAM = 12;

    public GlobalController() {
        time = 0;
        xmlReader = new XMLReader();
        huidigeInstructie = null;
        instructieQueue = new PriorityQueue<>();
        voltooideInstructies = new ArrayList<>();
        huidigProces = null;
        procesLijst = new ArrayList<>();
        address = null;
        instructionAdministrator = new InstructieController(new RAM(AANTAL_FRAMES_RAM));
    }

    public int getTime() {
        return time;
    }

    public XMLReader getXmlReader() {
        return xmlReader;
    }

    public Proces getHuidigeProces() {
        return huidigProces;
    }

    public Instructie getHuidigeInstructie() {
        return huidigeInstructie;
    }

    public ArrayList<Proces> getProcesLijst() {
        return procesLijst;
    }

    public ArrayList<Integer> getAddress() {
        return address;
    }

    public Queue<Instructie> getInstructieQueue() {
        return instructieQueue;
    }

    public ArrayList<Instructie> getVoltooideInstructies() {
        return voltooideInstructies;
    }

    public Page[] getFrames() {
        return instructionAdministrator.getramController().getFramesRAM();
    }

    public void setHuidigeProces(final int pid) {
        for (Proces p : procesLijst) {
            if (pid == p.getPid()) {
                huidigProces = p;
            }
        }
    }

    public void vulInstructieQueue(final String file) throws ParserConfigurationException, IOException, SAXException {
        instructieQueue = new LinkedList<>();
        instructieQueue = xmlReader.readXML(file);
    }

    public ArrayList<Integer> vertaalAdres(final int virtueelAdres) {
        // Deze functie returned [Framenummer, Offset]
        ArrayList<Integer> result = new ArrayList<>();
        // Het virtueel adres wordt omgezet in binair formaat
        // De virtuele adresruimte is 64 (2^16) kByte groot, we gebruiken dus 16-bit adressen.
        // Aangezien de functie zo weinig mogelijk bits gebruikt om het decimaal getal weer te geven als binair getal
        // We moeten nog nullen toevoegen tot we een 16-bit adres hebben
        String addressB = Integer.toString(virtueelAdres, 2);
        System.out.println("AddressB: " + addressB);
        int aantalExtraNullenNodig = LENGTE_ADRES - addressB.length();
        // 4096 = 2^12
        // Er zijn 16 pages die het virtueel geheugen kan gebruiken. Het pagenummer wordt in de 4 MSB geencodeerd.
        String temp = "0".repeat(Math.max(0, aantalExtraNullenNodig)) + addressB;
        // Pagenummer in de 4 MSB
        String pageNummer = temp.substring(0, temp.length() - LENGTE_OFFSET);
        // Offset in de 12 LSB
        String offset = temp.substring(temp.length() - LENGTE_OFFSET);
        // [FrameNummer, Offset]
        int[] address = new int[]{Integer.parseInt(pageNummer, 2), Integer.parseInt(offset, 2)};
        for (int j : address) {
            result.add(j);
        }
        //System.out.println("vertaalAddress - [Framenummer, Offset]" + result);
        return result;
    }

    public int berekenFysiekAdres(final int pageNummer, final int offset) {
        // Het pagenummer wordt opgezocht in de page table om het framenummer te verkrijgen
        // We gebruiken geen Hierarchical Paging
        // PhysicalAddress = PTE[p].FrameAddress + Offset
        return huidigProces.getFrameNumber(pageNummer) * 4096 + offset;

    }

    public void voerEenInstructieUit(final String s) throws ParserConfigurationException, IOException, SAXException {
        // Als er nog geen instructies in de queue zitten -> queue vullen
        if (instructieQueue.isEmpty()) {
            vulInstructieQueue(s);
        }

        // Retrievet de head van de queue, de instructie wordt niet met peek() niet verwijderd
        if (instructieQueue.peek() != null) {
            // Retrievet de head van de queue en verwijdert deze instructie van de queue
            huidigeInstructie = instructieQueue.poll();

            String operatie = huidigeInstructie.getOperation();
            switch (operatie) {
                // Start Instructie
                case "Start" -> {
                    address = null;
                    huidigProces = new Proces(huidigeInstructie.getPid());
                    procesLijst.add(huidigProces);
                    instructionAdministrator.startInstructie(huidigProces);
                }
                // Write Instructie
                case "Write" -> {
                    address = vertaalAdres(huidigeInstructie.getAddress());
                    setHuidigeProces(huidigeInstructie.getPid());
                    instructionAdministrator.writeInstructie(time, huidigeInstructie.getPid(), address);
                }
                // Read Instructie
                case "Read" -> {
                    address = vertaalAdres(huidigeInstructie.getAddress());
                    setHuidigeProces(huidigeInstructie.getPid());
                    instructionAdministrator.readInstructie(time, huidigeInstructie.getPid(), address);
                }
                // Terminate Instructie
                case "Terminate" -> {
                    System.out.println("Proces met pid " + huidigeInstructie.getPid() + " : Terminate instructie");
                    setHuidigeProces(huidigeInstructie.getPid());
                    address = null;
                    instructionAdministrator.terminate(huidigeInstructie.getPid());
                }
                default -> {
                }
            }
            time++;
            // Wanneer de instructie voltooid is wordt deze toegevoegd aan de voltooideInstructies ArrayList
            voltooideInstructies.add(huidigeInstructie);
        }
    }

    public void voerHeleQueueUit(final String s) throws ParserConfigurationException, IOException, SAXException {
        // Als er nog geen instructies in de queue zitten -> queue vullen
        if (instructieQueue.isEmpty()) {
            vulInstructieQueue(s);
        }
        // Retrievet de head van de queue, de instructie wordt niet met peek() niet verwijderd
        // Zolang de queue niet leeg is blijf je in de while-loop
        while (!(instructieQueue.peek() == null)) {
            // Retrievet de head van de queue en verwijdert deze instructie van de queue
            huidigeInstructie = instructieQueue.poll();

            String operatie = huidigeInstructie.getOperation();
            switch (operatie) {
                // Start Instructie
                case "Start" -> {
                    address = null;
                    huidigProces = new Proces(huidigeInstructie.getPid());
                    procesLijst.add(huidigProces);
                    instructionAdministrator.startInstructie(huidigProces);
                }
                // Write Instructie
                case "Write" -> {
                    address = vertaalAdres(huidigeInstructie.getAddress());
                    setHuidigeProces(huidigeInstructie.getPid());
                    instructionAdministrator.writeInstructie(time, huidigeInstructie.getPid(), address);
                }
                // Read Instructie
                case "Read" -> {
                    address = vertaalAdres(huidigeInstructie.getAddress());
                    setHuidigeProces(huidigeInstructie.getPid());
                    instructionAdministrator.readInstructie(time, huidigeInstructie.getPid(), address);
                }
                // Terminate Instructie
                case "Terminate" -> {
                    setHuidigeProces(huidigeInstructie.getPid());
                    address = null;
                    instructionAdministrator.terminate(huidigeInstructie.getPid());
                }
                default -> {
                }
            }
            time++;
            // Wanneer de instructie voltooid is wordt deze toegevoegd aan de voltooideInstructies ArrayList
            voltooideInstructies.add(huidigeInstructie);
        }
    }

    // nodig voor in GUIController-klasse
    public void setup() {
        System.out.println("Setup");
        time = 0;
        xmlReader = new XMLReader();
        huidigeInstructie = null;
        instructieQueue = new PriorityQueue<>();
        voltooideInstructies = new ArrayList<>();
        huidigProces = null;
        procesLijst = new ArrayList<>();
        address = null;
        instructionAdministrator = new InstructieController(new RAM(AANTAL_FRAMES_RAM));
    }
}
