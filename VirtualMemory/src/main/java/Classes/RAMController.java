package Classes;

import java.util.ArrayList;


public class RAMController {

    // Attributen
    private RAM ram;
    static final int MAX_VAL = 0x7fffffff;
    static final int MAX_AANTAL_PROCESSEN_IN_RAM = 4;

    // Default Constructor
    public RAMController() {
    }

    // Gewone Constructor
    public RAMController(final RAM ram) {
        this.ram = ram;
    }

    // Copy Constructor
    public RAMController(final RAMController r) {
        this.ram = r.ram;
    }

    // Getters
    public RAM getRam() {
        return ram;
    }

    public Proces getProces(final Page page) {
        for (Proces proces : ram.getProcesList()) {
            if (proces.getPid() == page.getPid()) {
                return proces;
            }
        }
        return null;
    }

    public Proces getProces(final int pid) {
        for (Proces proces : ram.getProcesList()) {
            if (proces.getPid() == pid) {
                return proces;
            }
        }
        return null;
    }

    public Page[] getFramesRAM() {
        if (ram == null) {
            return null;
        } else {
            return ram.getFrameArray();
        }
    }

    public Proces getProcesWithLeastTime() {
        int pid = -1;
        int minimum = MAX_VAL;
        // Alle processen in de RAM overlopen en het proces met de laagste lastAccessed bijhouden
        for (Proces proces : ram.getProcesList()) {
            // Als het proces langer niet gebruikt is
            // dan het huidige minimum
            // wordt het proces het nieuwe minimum
            if (proces.getLastAccessed() < minimum) {
                pid = proces.getPid();
                minimum = proces.getLastAccessed();
            }
        }
        // het langst niet-gebruikte proces wordt gereturned
        return getProces(pid);
    }

    public void addProces(final Proces p) {
        // in RAM maximaal 4 processen toegelaten voor deze opdracht
        System.out.println("ProcesList Size: " + ram.getProcesList().size());
        if (ram.getProcesList().size() >= MAX_AANTAL_PROCESSEN_IN_RAM) {
            System.out.println(getProcesWithLeastTime().getPid() + " verwijderd uit RAM.");
            removeProces(getProcesWithLeastTime().getPid());
        }
        ram.addProces(p);
        updateFramesRAM();
    }

    public void removeProces(final int pid) {
        Proces p = new Proces(pid);
        for (Proces proces : ram.getProcesList()) {
            if (pid == proces.getPid()) {
                p = proces;
            }
        }

        for (int i = 0; i < ram.getFrameArray().length; i++) {
            System.out.println("Aantal frames: " + ram.getAantalFrames());
            System.out.println("i: " + i + " " + ram.getFrameArray()[i]);
            if (ram.getFrameArray()[i].getPid() == p.getPid()) {
                System.out.println(ram.getFrameArray()[i] + " wordt verwijderd...");
                removePage(ram.getFrameArray()[i]);
                System.out.println("verwijderen succesvol");
            }
        }
        ram.removeProces(p);
    }

    public void updateFramesRAM() {
        // Elk proces moet even veel frames in de RAM bezitten
        // Zolang ze niet eerlijk verdeeld zijn blijf je in de while-loop
        if (ram.getProcesList().size() > 0) {
            // Anders division by zero
            int div = ram.getAantalFrames() / ram.getProcesList().size();
            for (Proces p : ram.getProcesList()) {
                // Zolang er geen gelijke verdeling is
                while (!(p.getAantalPagesInRAMProces() == div)) {
                    // Als het proces teveel pagina's heeft moet de langst niet-gebruikte verwijderd worden
                    if (div < p.getAantalPagesInRAMProces()) {
                        removePage(p.getLeastRecentlyUsedPage());
                    } else {
                        Page temp = p.getUnavaiblePage();
                        System.out.println("Unavailable page: " + p.getUnavaiblePage());
                        int i = 0;
                        boolean emptyFrameGevonden = false;
                        while (!emptyFrameGevonden) {
                            // Als de inhoud van de index null is, dan is de frame leeg
                            if (ram.getFrameArray()[i] == null) {
                                emptyFrameGevonden = true;
                            } else {
                                i++;
                            }
                        }
                        ram.setFrameArrayEntry(i, temp);
                        // Vergeet niet present bit op true te zetten
                        p.updateProcesPageTable(i, temp.getPageNumber(), true, false);
                    }
                }
            }
        }
    }

    public void updateAccessTime(final int time, final Proces p, final ArrayList<Integer> address) {
        // Set het lastAccessed attribuut op het huidige tijdstip van de systeemclock
        p.getPageTable().get(address.get(0)).getTableEntry().setLastAccessed(time);
        // Set de modified bit want de page is gemodified
        p.getPageTable().get(address.get(0)).getTableEntry().setModifyBit(true);
    }

    public void removePage(final Page p) {
        Proces pr = getProces(p);
        int frameNummer = pr.getPageTable().get(p.getPageNumber()).getTableEntry().getFrameNumber();
        // Access en dirty bit resetten
        pr.updateProcesPageTable(frameNummer, p.getPageNumber(), false, false);
        // Frame resetten
        ram.resetFrameArrayEntry(frameNummer);
    }

    public void swapPage(final Page p, final int oudeFrameNummer) {
        // Neem de pagina die je uit de RAM wilt swappen
        Page oudePagina = ram.getFrameArray()[oudeFrameNummer];
        // Als er een pagina zat op die framenummer
        if (oudePagina != null) {
            // Update de page table van het proces van de oude pagina en incrementeer het aantal writes naar RAM
            // Present en modifybit resetten
            getProces(oudePagina).updateProcesPageTable(0, oudePagina.getPageNumber(), false, false);
            // Incrementeer het aantal page outs
            getProces(oudePagina).incrementeerAantalPageIns();
        }
        Proces nieuwePaginaProces = getProces(p);
        // Update de page table van het proces van de nieuwe pagina en incrementeer het aantal reads van RAM
        // De nieuwe pagina wordt in de frame van het oude proces geplaatst, de presentbit van de nieuwe pagina moet geset worden
        nieuwePaginaProces.updateProcesPageTable(oudeFrameNummer, p.getPageNumber(), true, false);
        // Incrementeer het aantal page ins
        ram.setFrameArrayEntry(oudeFrameNummer, p);
        nieuwePaginaProces.incrementeerAantalPageOuts();
    }
}
