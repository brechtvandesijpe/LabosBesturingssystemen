package Classes;

public class PageMetTableEntry {
    // Attributen
    private TableEntry te;
    private Page p;

    // Default Constructor
    public PageMetTableEntry() {
    }

    // Gewone Constructor
    public PageMetTableEntry(final Page p, final TableEntry te) {
        this.p = p;
        this.te = te;
    }

    // Copy Constructor
    public PageMetTableEntry(final PageMetTableEntry pwte) {
        this.te = pwte.te;
        this.p = pwte.p;
    }

    // Getters
    public TableEntry getTableEntry() {
        return te;
    }

    public Page getPage() {
        return p;
    }

    // Setters
    public void setPage(final Page page) {
        this.p = page;
    }

    public void setTableEntry(final TableEntry t) {
        this.te = t;
    }

    // toString
    public String toString() {
        return "PageWithTableEntry{ TableEntry{" + te + "} " + ", Page{" + p + "}}";
    }
}






