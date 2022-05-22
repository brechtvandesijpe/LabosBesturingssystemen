package Classes;

public class Instructie {
    // Attributen
    private int pid;
    private String operation;
    private int address;

    // Default Constructor
    public Instructie() {
    }

    // Gewone Constructor
    public Instructie(final int pid, final String operation, final int address) {
        this.pid = pid;
        this.operation = operation;
        this.address = address;
    }

    // Getters
    public int getPid() {
        return pid;
    }

    public String getOperation() {
        return operation;
    }

    public int getAddress() {
        return address;
    }

    // Setters
    public void setPid(final int pid) {
        this.pid = pid;
    }

    public void setOperation(final String operation) {
        this.operation = operation;
    }

    public void setAddress(final int address) {
        this.address = address;
    }

    // toString
    public String toString() {
        return "Instruction: { pid=" + pid
                + ", operation=" + operation + ", address=" + address + " }";
    }

}

