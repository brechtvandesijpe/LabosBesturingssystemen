package Classes;

public class TableEntry {
    // Attributen
    private int frameNumber;
    private int pageNumber;
    private boolean presentBit;
    private boolean modifyBit;
    private int lastAccessed;

    // Default Constructor
    public TableEntry() {
        frameNumber = 0;
        lastAccessed = -1;  //dummy
        presentBit = false;
        modifyBit = false;
    }

    // Gewone Constructor
    public TableEntry(final int frameNumber, final int pageNumber, final boolean presentBit, final boolean modifyBit, final int lastAccessed) {
        this.frameNumber = frameNumber;
        this.pageNumber = pageNumber;
        this.presentBit = presentBit;
        this.modifyBit = modifyBit;
        this.lastAccessed = lastAccessed;
    }

    // Copy Constructor
    public TableEntry(final TableEntry te) {
        this.frameNumber = te.frameNumber;
        this.pageNumber = te.pageNumber;
        this.presentBit = te.presentBit;
        this.modifyBit = te.modifyBit;
        this.lastAccessed = te.lastAccessed;
    }

    // Getters
    public int getFrameNumber() {
        return frameNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public boolean getPresentBit() {
        return presentBit;
    }

    public boolean getModifyBit() {
        return modifyBit;
    }

    public int getLastAccessed() {
        return lastAccessed;
    }

    public boolean isPresent() {
        return presentBit;
    }

    public boolean isModified() {
        return modifyBit;
    }

    // Setters
    public void setFrameNumber(final int frameNumber) {
        this.frameNumber = frameNumber;
    }

    public void setPageNumber(final int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPresentBit(final boolean presentBit) {
        this.presentBit = presentBit;
    }

    public void setModifyBit(final boolean modifyBit) {
        this.modifyBit = modifyBit;
    }

    public void setLastAccessed(final int lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    // toString
    public String toString() {
        return "TableEntry{ frameNumber=" + frameNumber +
                ", pageNumber=" + pageNumber + ", presentBit=" + presentBit +
                ", modifyBit=" + modifyBit + ", lastAccessed=" + lastAccessed + "}";
    }
}
