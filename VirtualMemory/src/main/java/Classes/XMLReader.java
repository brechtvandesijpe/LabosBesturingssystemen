package Classes;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class XMLReader {
    public XMLReader() {
    }

    public Queue<Instructie> readXML(String str) throws ParserConfigurationException, IOException, SAXException {
        File f = null;
        Queue<Instructie> instructies = new LinkedList<>();
        switch (str) {
            case "20000 instructies 20 processen" -> f = new File("Instructions_20000_20.xml");
            case "20000 instructies 4 processen" -> f = new File("Instructions_20000_4.xml");
            case "20 instructies 3 processen" -> f = new File("Instructions_30_3.xml");
            default -> System.out.println("readXML Failed");
        }

        if (f == null) {
            System.out.println("File niet gevonden.");
        }

        DocumentBuilderFactory docfactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuild = docfactory.newDocumentBuilder();
        Document document = docBuild.parse(f);
        document.getDocumentElement().normalize();
        NodeList nodeList = document.getElementsByTagName("instruction");
        for (int j = 0; j < nodeList.getLength(); j++) {
            Node n = nodeList.item(j);
            Instructie instructie = new Instructie();
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) n;
                instructie.setPid(Integer.parseInt(e.getElementsByTagName("processID").item(0).getTextContent()));
                instructie.setOperation(e.getElementsByTagName("operation").item(0).getTextContent());
                instructie.setAddress(Integer.parseInt(e.getElementsByTagName("address").item(0).getTextContent()));
                instructies.add(instructie);
            }
        }
        return instructies;
    }
}
