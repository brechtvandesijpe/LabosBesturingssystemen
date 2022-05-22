import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        // Inladen XML-data in de lijst met processen

        Processen processen = new Processen();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(new File("processen20000.xml"));
            document.getDocumentElement().normalize();

            NodeList procesList = document.getElementsByTagName("process");

            for (int i = 0; i < procesList.getLength(); i++) {
                Node proces = procesList.item(i);

                if (proces.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) proces;
                    processen.voegToe(new Proces(Integer.parseInt(element.getElementsByTagName("pid").item(0).getTextContent()),
                                                 Integer.parseInt(element.getElementsByTagName("arrivaltime").item(0).getTextContent()),
                                                 Integer.parseInt(element.getElementsByTagName("servicetime").item(0).getTextContent())));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Processen processen2 = new Processen(processen);
        processen2.scheduleFCFS();
        processen2.export("fcfs.csv");

        processen2 = new Processen(processen);
        processen2.scheduleSJF();
        processen2.export("sjf.csv");

        processen2 = new Processen(processen);
        processen2.scheduleSRT();
        processen2.export("srt.csv");

        processen2 = new Processen(processen);
        processen2.scheduleHRRN();
        processen2.export("hrrn.csv");

        processen2 = new Processen(processen);
        processen2.scheduleRR(2);
        processen2.export("rrq2.csv");

        processen2 = new Processen(processen);
        processen2.scheduleRR(4);
        processen2.export("rrq2.csv");

        processen2 = new Processen(processen);
        processen2.scheduleRR(8);
        processen2.export("rrq8.csv");

        processen2 = new Processen(processen);
        int[] qWaarden = {16, 32, 64, 128, 512};
        processen2.scheduleMFBQ(qWaarden);
        processen2.export("mfbq1.csv");

        processen2 = new Processen(processen);
        qWaarden = new int[]{16, 32, 48, 64, 80};
        processen2.scheduleMFBQ(qWaarden);
        processen2.export("mfbq2.csv");

    }
}
