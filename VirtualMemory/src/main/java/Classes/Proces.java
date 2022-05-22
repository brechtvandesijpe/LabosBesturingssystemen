package Classes;

import java.util.ArrayList;

public class Proces {
    // Attributen
    private int pid;
    private int aantalPageIns;
    private int aantalPageOuts;
    private int lastAccessed;
    private ArrayList<PageMetTableEntry> pageTable;
    static final int MAX_VALUE = 0x7fffffff;

    // Default Constructor
    public Proces() {
    }

    // Gewone Constructor
    public Proces(final int pid, final int aantalPageIns, final int aantalReadsVanRAM, final int lastAccessed, final ArrayList<PageMetTableEntry> pageTable) {
        this.pid = pid;
        this.aantalPageIns = aantalPageIns;
        this.aantalPageOuts = aantalReadsVanRAM;
        this.lastAccessed = lastAccessed;
        this.pageTable = pageTable;
        for (int i = 0; i <= 15; i++) {
            pageTable.add(new PageMetTableEntry(new Page(pid, i), new TableEntry()));
        }
    }

    public Proces(final int pid, final int aantalPageIns, final int aantalReadsVanRAM) {
        this.pid = pid;
        this.aantalPageIns = aantalPageIns;
        this.aantalPageOuts = aantalReadsVanRAM;
    }

    // Gewone Constructor
    public Proces(final int pid) {
        this.pid = pid;
        pageTable = new ArrayList<>();
        aantalPageIns = 0;
        aantalPageOuts = 0;
        lastAccessed = -1; // dummy
        for (int i = 0; i <= 15; i++) {
            pageTable.add(new PageMetTableEntry(new Page(pid, i), new TableEntry()));
        }
    }

    // Copy Constructor
    public Proces(final Proces p) {
        this.pid = p.pid;
        this.aantalPageIns = p.aantalPageIns;
        this.aantalPageOuts = p.aantalPageOuts;
        this.lastAccessed = p.lastAccessed;
        this.pageTable = p.pageTable;
    }

    // Getters
    public int getPid() {
        return pid;
    }

    public ArrayList<PageMetTableEntry> getPageTable() {
        return pageTable;
    }

    public int getAantalPageIns() {
        return aantalPageIns;
    }

    public int getAantalPageOuts() {
        return aantalPageOuts;
    }

    public int getLastAccessed() {
        return lastAccessed;
    }

    public boolean getPresentBitPage(final ArrayList<Integer> address) {
        return pageTable.get(address.get(0)).getTableEntry().isPresent();
    }

    public int getFrameNumber(final int pageNummer) {
        for (PageMetTableEntry pwte : pageTable) {
            if (pwte.getPage().getPageNumber() == pageNummer) {
                return pwte.getTableEntry().getFrameNumber();
            }
        }
        return 0;
    }

    public Page getUnavaiblePage() {
        for (PageMetTableEntry pwte : pageTable) {
            if (!pwte.getTableEntry().isPresent()) {
//                System.out.println(pwte.getPage());
                return pwte.getPage();
            }
        }
        return null;
    }

    public int getAantalPagesInRAMProces() {
        int result = 0;
        for (PageMetTableEntry pwte : pageTable) {
            if (pwte.getTableEntry().isPresent()) {
                result += 1;
            }
        }
        return result;
    }

    public Page getLeastRecentlyUsedPage() {
        Page result = null;
        int minimum = MAX_VALUE;
        for (PageMetTableEntry pwte : pageTable) {
            if ((pwte.getTableEntry().getLastAccessed() < minimum) && (pwte.getTableEntry().isPresent())) {
                result = pwte.getPage();
                minimum = pwte.getTableEntry().getLastAccessed();
            }
        }
        return result;
    }

    // Setters
    public void setPid(final int pid) {
        this.pid = pid;
    }

    public void setPageTable(final ArrayList<PageMetTableEntry> pageTable) {
        this.pageTable = pageTable;
    }

    public void setAantalPageIns(final int aantalPageIns) {
        this.aantalPageIns = aantalPageIns;
    }

    public void setAantalPageOuts(final int aantalPageOuts) {
        this.aantalPageOuts = aantalPageOuts;
    }

    public void setLastAccessed(final int lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    // Proces modifiers
    public void incrementeerAantalPageIns() {
        aantalPageIns += 1;
    }

    public void incrementeerAantalPageOuts() {
        aantalPageOuts += 1;
    }

    public void incrementeerAantalPageIns(int i) {
        aantalPageIns += i;
    }

    public void incrementeerAantalPageOuts(int i) {
        aantalPageOuts += i;
    }

    public void updateProcesPageTable(final int frameNummer, final int pageNummer, final boolean presentBit, final boolean modifyBit) {
        pageTable.get(pageNummer).getTableEntry().setFrameNumber(frameNummer);
        pageTable.get(pageNummer).getTableEntry().setPresentBit(presentBit);
        pageTable.get(pageNummer).getTableEntry().setModifyBit(modifyBit);
    }

    // toString
    public String toString() {
        return "Proces { PID: " + pid + ", # Page ins: " + aantalPageIns +
                ", # Page Outs: " + aantalPageOuts + ", last accessed: " + lastAccessed + " }";
    }

}
