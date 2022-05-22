package Classes;

public class Page {
    // Attributen
    private int pid;
    private int pageNumber;
    private int frameNumber;

    // Default Constructor
    public Page() {
        pid = -1;
        pageNumber = -1;
        frameNumber = -1;
    }

    // Gewone Constructor
    public Page(final int frameNumber, final int pid, final int pageNumber) {
        this.frameNumber = frameNumber;
        this.pid = pid;
        this.pageNumber = pageNumber;
    }

    // Gewone Constructor
    public Page(final int pid, final int pageNumber) {
        this.pid = pid;
        this.pageNumber = pageNumber;
    }

    // Copy Constructor
    public Page(final Page page) {
        this.pid = page.pid;
        this.pageNumber = page.pageNumber;
        this.frameNumber = page.frameNumber;
    }

    // Getters
    public int getFrameNumber() {
        return frameNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPid() {
        return pid;
    }

    // Setters
    public void setFrameNumber(final int frameNumber) {
        this.frameNumber = frameNumber;
    }

    public void setPageNumber(final int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPid(final int pid) {
        this.pid = pid;
    }

    // toString
    public String toString() {
        return "Page{ frameNumber=" + frameNumber + ", pageNumber=" + pageNumber + ", pid=" + pid + " }";
    }
}
