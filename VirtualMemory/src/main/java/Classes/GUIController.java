package Classes;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUIController {
    // Attributen
    private GlobalController globalController;
    private String set;

    // Drop-down menu
    @FXML private ChoiceBox<String> choiceBoxDataSet;

    // Tabel Page Table
    @FXML private TableView<TableEntry> tableViewpageTable;

    // Tabel RAM
    @FXML private TableView<Page> tableViewRam;

    // Tabel procesInformatie
    @FXML private TableView<Proces> tableViewProcesInformatie;

    // Kolommen Page Table
    @FXML private TableColumn<TableEntry, Boolean> tableColumnPresent;
    @FXML private TableColumn<TableEntry, Boolean> tableColumnModified;
    @FXML private TableColumn<TableEntry, Integer> tableColumnLastAccessed;
    @FXML private TableColumn<TableEntry, Integer> tableColumnFrame;
    @FXML private TableColumn<Page, Integer> tableColumnPage;

    // Kolommen RAM
    @FXML private TableColumn<Page, Integer> tableColumnFrame2;
    @FXML private TableColumn<Page, Boolean> tableColumnPage2;
    @FXML private TableColumn<Page, Integer> tableColumnPid;

    // Kolommen procesInformatie
    @FXML private TableColumn<Proces, Integer> tableColumnPid2;
    @FXML private TableColumn<Proces, Integer> tableColumnAantalWrites;
    @FXML private TableColumn<Proces, Integer> tableColumnAantalReads;

    // Informatie
    @FXML private Label labelSysteemKlok;
    @FXML private Label labelAantalPageIns;
    @FXML private Label labelAantalPageOuts;

    // Huidige Instructie
    @FXML private Label labelHuidigeInstructie;
    @FXML private Label labelHuidigFysiekAdres;
    @FXML private Label labelHuidigVirtueelAdres;
    @FXML private Label labelHuidigPid;
    @FXML private Label labelHuidigPageNummer;
    @FXML private Label labelHuidigeOffset;

    // Vorige Instructie
    @FXML private Label labelVorigeInstructie;
    @FXML private Label labelVorigFysiekAdres;
    @FXML private Label labelVorigVirtueelAdres;
    @FXML private Label labelVorigPid;
    @FXML private Label labelVorigPageNummer;
    @FXML private Label labelVorigeOffset;

    // Constructor gets called first, then the @FXML anotated fields are populated, then initialize() is called
    @FXML public void initialize() {
        globalController = new GlobalController();
        choiceBoxDataSet.getItems().removeAll(choiceBoxDataSet.getItems());
        choiceBoxDataSet.getItems().addAll("20000 instructies 20 processen", "20000 instructies 4 processen", "20 instructies 3 processen");
        // let op: exact zelfde naam nemen als propertyvaluefactory
        choiceBoxDataSet.getSelectionModel().select("20000 instructies 20 processen");
        // Page Table
        tableColumnFrame.setCellValueFactory(new PropertyValueFactory<>("frameNumber"));
        tableColumnPage.setCellValueFactory(new PropertyValueFactory<>("pageNumber"));
        tableColumnPresent.setCellValueFactory(new PropertyValueFactory<>("presentBit"));
        tableColumnModified.setCellValueFactory(new PropertyValueFactory<>("modifyBit"));
        tableColumnLastAccessed.setCellValueFactory(new PropertyValueFactory<>("lastAccessed"));
        // RAM
        tableColumnFrame2.setCellValueFactory(new PropertyValueFactory<>("frameNumber"));
        tableColumnPage2.setCellValueFactory(new PropertyValueFactory<>("pageNumber"));
        tableColumnPid.setCellValueFactory(new PropertyValueFactory<>("pid"));
        // Procesinformatie
        tableColumnPid2.setCellValueFactory(new PropertyValueFactory<>("pid"));
        tableColumnAantalWrites.setCellValueFactory(new PropertyValueFactory<>("aantalPageIns"));
        tableColumnAantalReads.setCellValueFactory(new PropertyValueFactory<>("aantalPageOuts"));
        setTables();
    }

    @FXML public void resetButtonPressed() {
        globalController.setup();
        labelSysteemKlok.setText(String.valueOf(globalController.getTime()));
        setPageInOutInformatie();
        setTables();
        // Alle labels resetten
        labelVorigeInstructie.setText("null");
        labelVorigeOffset.setText("null");
        labelVorigPageNummer.setText("null");
        labelVorigPid.setText("null");
        labelVorigVirtueelAdres.setText("null");
        labelVorigFysiekAdres.setText("null");
        labelHuidigeInstructie.setText("null");
        labelHuidigeOffset.setText("null");
        labelHuidigPageNummer.setText("null");
        labelHuidigPid.setText("null");
        labelHuidigVirtueelAdres.setText("null");
        labelHuidigFysiekAdres.setText("null");
    }

    @FXML public void execute1InstructieButtonPressed() throws ParserConfigurationException, IOException, SAXException {
        if (set == null || set.equals(choiceBoxDataSet.getValue())) {
            set = choiceBoxDataSet.getValue();
        } else {
            set = choiceBoxDataSet.getValue();
            globalController.setup();
        }
        globalController.voerEenInstructieUit(choiceBoxDataSet.getValue());
        labelSysteemKlok.setText(String.valueOf(globalController.getTime()));
        setTables();
        setInstructiePanes();
        setPageInOutInformatie();
    }

    @FXML public void executeAlleInstructiesButtonPressed() throws ParserConfigurationException, IOException, SAXException {
        globalController.voerHeleQueueUit(choiceBoxDataSet.getValue());
        labelSysteemKlok.setText(String.valueOf(globalController.getTime()));
        setTables();
        setInstructiePanes();
        setPageInOutInformatie();
        System.out.println("Alle instructies zijn uitgevoerd. Controller wordt gereset.");
        globalController.setup();
    }

    public void setInstructiePanes() {
        labelVorigVirtueelAdres.setText(labelHuidigVirtueelAdres.getText());
        labelVorigeInstructie.setText(labelHuidigeInstructie.getText());
        labelVorigPid.setText(labelHuidigPid.getText());
        labelVorigFysiekAdres.setText(labelHuidigFysiekAdres.getText());
        labelVorigeOffset.setText(labelHuidigeOffset.getText());
        labelVorigPageNummer.setText(labelHuidigPageNummer.getText());
        Instructie instructie = globalController.getHuidigeInstructie();
        labelHuidigVirtueelAdres.setText(String.valueOf(instructie.getAddress()));
        labelHuidigeInstructie.setText(instructie.getOperation());
        labelHuidigPid.setText(String.valueOf(instructie.getPid()));
        ArrayList<Integer> vertaaldAdres = globalController.vertaalAdres(instructie.getAddress());
        labelHuidigFysiekAdres.setText(String.valueOf(globalController.berekenFysiekAdres(vertaaldAdres.get(0), vertaaldAdres.get(1))));
        int page = vertaaldAdres.get(0);
        int offset = vertaaldAdres.get(1);
        labelHuidigeOffset.setText(String.valueOf(offset));
        labelHuidigPageNummer.setText(String.valueOf(page));
    }

    public void setTables() {
        //  Page Table Tabel
        for (int i = 0; i < tableViewpageTable.getItems().size(); i++) {
            // Alle items in de Page Table worden gecleared
            tableViewpageTable.getItems().clear();
        }
        Proces p = globalController.getHuidigeProces();
        if (p != null) {
            for (PageMetTableEntry pwte : p.getPageTable()) {
                if (pwte != null) {
                    // De tabel wordt gevuld met page table entries
                    TableEntry te = new TableEntry(pwte.getTableEntry().getFrameNumber(), pwte.getPage().getPageNumber(), pwte.getTableEntry().isPresent(), pwte.getTableEntry().isModified(), pwte.getTableEntry().getLastAccessed());
                    tableViewpageTable.getItems().add(te);
                }
            }
        }
        // RAM Tabel
        System.out.println("setTables RAM, aantal items in tableViewRam.getItems():" + tableViewRam.getItems().size());
        for (int i = 0; i < tableViewRam.getItems().size(); i++) {
            // Alle items in de RAM Table worden gecleared
            tableViewRam.getItems().clear();
        }

        Page[] pages = globalController.getFrames();
        for (int i = 0; i < pages.length; i++) {
            if (pages[i] != null) {
                // De tabel wordt gevuld met pages
                tableViewRam.getItems().add(new Page(i, pages[i].getPid(), pages[i].getPageNumber()));
            }
        }

        // Proces Informatie Tabel
        for (int i = 0; i < tableViewProcesInformatie.getItems().size(); i++) {
            // Alle items in de Proces Informatie Tables worden gecleared
            tableViewProcesInformatie.getItems().clear();
        }
        List<Proces> processen = globalController.getProcesLijst();
        for (Proces proces : processen) {
            if (proces != null) {
                // De tabel wordt gevuld met processen
                tableViewProcesInformatie.getItems().add(new Proces(proces.getPid(), proces.getAantalPageIns(), proces.getAantalPageOuts()));
            }
        }
    }

    public void setPageInOutInformatie() {
        int pageIn = 0;
        int pageOut = 0;

        for(Proces p : globalController.getProcesLijst()) {
            pageIn += p.getAantalPageIns();
            pageOut += p.getAantalPageOuts();
        }

        labelAantalPageIns.setText(String.valueOf(pageIn));
        labelAantalPageOuts.setText(String.valueOf(pageOut));
    }
}
