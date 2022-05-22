package Classes;

import java.util.ArrayList;


public class InstructieController {
    // Attributen
    private RAMController ramController;

    // Default Constructor
    public InstructieController() {
    }

    // Gewone Constructor
    public InstructieController(final RAM ram) {
        ramController = new RAMController(ram);
    }

    // Copy Constructor
    public InstructieController(final InstructieController ic) {
        this.ramController = ic.ramController;
    }

    // Getters
    public RAMController getramController() {
        return ramController;
    }

    // Setters
    public void setramController(final RAMController ramController) {
        this.ramController = ramController;
    }

    // de 4 soorten instructies
    public void startInstructie(final Proces p) {
        // Het proces wordt toegevoegd aan RAM
        // De eventuele herverdeling van RAM gebeurt in de addProces functie
        ramController.addProces(p);
        int aantalPagesInRAMVanProces = p.getAantalPagesInRAMProces();

        p.incrementeerAantalPageIns(aantalPagesInRAMVanProces);
        if(p.getAantalPagesInRAMProces() != 12){
            p.incrementeerAantalPageOuts(aantalPagesInRAMVanProces);
        }

    }

    public void terminate(final int pid) {
        // Het proces wordt verwijderd uit RAM
        int aantalPagesInRAMVanProces = getramController().getProces(pid).getAantalPagesInRAMProces();
        // Alle pages van dat proces worden eruit geswapped

        ramController.getProces(pid).incrementeerAantalPageOuts(aantalPagesInRAMVanProces);
        if(ramController.getProces(pid).getAantalPagesInRAMProces() != 12){
            ramController.getProces(pid).incrementeerAantalPageIns(aantalPagesInRAMVanProces);
        }
        ramController.removeProces(pid);
        // Herverdeling RAM
        ramController.updateFramesRAM();
    }

    public void writeInstructie(final int time, final int pid, final ArrayList<Integer> address) {
        Proces p = ramController.getProces(pid);
        if (p == null) {
            // Proces zit nog niet in RAM
            p = new Proces(pid);

            ramController.addProces(p);
        }
        // Update de access time naar de huidige systeemclock tijd als de page in RAM zit
        if (p.getPresentBitPage(address)) {
            ramController.updateAccessTime(time, p, address);
        }
        // Als de page nog niet in de RAM zit
        else {
            // Pagina zoeken die we gaan evicten
            Page oudePagina = p.getLeastRecentlyUsedPage();
            int oudeFrameNummer = p.getPageTable().get(oudePagina.getPageNumber()).getTableEntry().getFrameNumber();
            // Swap de nieuwe pagina in de RAM op de plek van de oude pagina
            ramController.swapPage(p.getPageTable().get(address.get(0)).getPage(), oudeFrameNummer);
            int pageNummer = p.getPageTable().get(address.get(0)).getPage().getPageNumber();
            // Update de page table van het proces
            p.updateProcesPageTable(oudeFrameNummer, pageNummer, true, true);
            // Update de access time naar de huidige systeemclock tijd
            p.getPageTable().get(address.get(0)).getTableEntry().setLastAccessed(time);
        }
        p.setLastAccessed(time);
    }

    public void readInstructie(final int time, final int pid, final ArrayList<Integer> address) {
        Proces p = ramController.getProces(pid);
        if (p == null) {
            // Proces zit nog niet in RAM
            p = new Proces(pid);
            ramController.addProces(p);
        }
        // Update de access time naar de huidige systeemclock tijd als de page in RAM zit
        if (p.getPresentBitPage(address)) {
            ramController.updateAccessTime(time, p, address);
        } else {
            // Pagina zoeken die we gaan evicten
            Page oudePagina = p.getLeastRecentlyUsedPage();
            int oudeFrameNummer = p.getPageTable().get(oudePagina.getPageNumber()).getTableEntry().getFrameNumber();
            ramController.swapPage(p.getPageTable().get(address.get(0)).getPage(), oudeFrameNummer);
            int pageNummer = p.getPageTable().get(address.get(0)).getPage().getPageNumber();
            p.updateProcesPageTable(oudeFrameNummer, pageNummer, true, false);
            p.getPageTable().get(address.get(0)).getTableEntry().setLastAccessed(time);
        }
        p.setLastAccessed(time);
    }

}
