package Classes;

import java.util.ArrayList;
import java.util.List;

public class RAM {
    // Attributen
    private int aantalFrames;
    private List<Proces> procesList;
    private Page[] frameArray;
    final static int AANTAL_FRAMES_IN_RAM = 12;

    // Default Constructor
    public RAM() {
        aantalFrames = 0;
        procesList = new ArrayList<>();
        // 48 Kbyte en opgesplitst in 12 frames van 4 Kbyte
        frameArray = new Page[AANTAL_FRAMES_IN_RAM];
    }

    // Gewone Constructor
    public RAM(final int aantalFrames) {
        this.aantalFrames = aantalFrames;
        procesList = new ArrayList<>();
        // 48 Kbyte en opgesplitst in 12 frames van 4 Kbyte
        frameArray = new Page[AANTAL_FRAMES_IN_RAM];
    }

    // Copy Constructor
    public RAM(final RAM r) {
        this.aantalFrames = r.aantalFrames;
        this.procesList = r.procesList;
        this.frameArray = r.frameArray;
    }

    // Getters
    public int getAantalFrames() {
        return aantalFrames;
    }

    public List<Proces> getProcesList() {
        return procesList;
    }

    public Page[] getFrameArray() {
        return frameArray;
    }

    // Setters
    public void setAantalFrames(final int aantalFrames) {
        this.aantalFrames = aantalFrames;
    }

    public void setProcesList(final List<Proces> procesList) {
        this.procesList = procesList;
    }

    public void setFrameArray(final Page[] frameArray) {
        this.frameArray = frameArray;
    }

    // RAM modifiers
    public void addProces(final Proces p) {
        procesList.add(p);
    }

    public void removeProces(final Proces p) {
        procesList.remove(p);
    }

    public void setFrameArrayEntry(final int i, final Page p) {
        frameArray[i] = p;
    }

    public void resetFrameArrayEntry(final int i) {
        frameArray[i] = null;
    }

    // toString
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Proces p : procesList) {
            sb.append(p);
            sb.append(" ");
        }
        StringBuilder sb2 = new StringBuilder();
        for (Page page : frameArray) {
            sb2.append(page);
            sb2.append(" ");
        }
        return "RAM{ " + "Aantal Frames: " + aantalFrames
                + ", ProcesList: " + sb + ", FrameArray: " + sb2 + " }";
    }

}
