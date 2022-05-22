
public class Proces {

    private final int pid, arrivalTime, serviceTime;
    private double waitingTime, turnAroundTime, normTurnAroundTime, responseTime;
    private int todo, finishedTime;

    public Proces(int p, int a, int s) {
        if (p == -1 || a == -1 || s == -1) throw new NullPointerException();
        else {
            pid = p;
            arrivalTime = a;
            serviceTime = s;
            todo = serviceTime;
            finishedTime = 0;
            waitingTime = 0;
            turnAroundTime = 0;
            normTurnAroundTime = 0.0;
        }
    }

    public void schrijf() {
        System.out.println(pid + " | " + arrivalTime + " | " + serviceTime + " | " + finishedTime + " | " + waitingTime);
    }

    public int getFinishedTime() { return finishedTime; }

    public int getArrivalTime() { return arrivalTime; }

    public int getServiceTime() { return serviceTime; }

    public int getPid() { return pid; }

    public double getTurnAroundTime() { return turnAroundTime; }

    public double getNormTurnAroundTime() { return normTurnAroundTime; }

    public double getWaitingTime() { return waitingTime; }

    public double getResponseTime() { return responseTime; }

    public void berekenResponseTime(int currentTime) { responseTime = (currentTime - arrivalTime + serviceTime) / (double) serviceTime; }

    public boolean run(int startingTime, int limiet) {
        int runningTime;

        if (limiet == -1) runningTime = serviceTime;
        else runningTime = limiet;

        if (todo - runningTime <= 0) {
            finishedTime = startingTime + todo;
            todo = 0;
            turnAroundTime = finishedTime - arrivalTime;
            normTurnAroundTime = turnAroundTime / (double) serviceTime;
            waitingTime = turnAroundTime - serviceTime;
            return true;   // proces afgerond
        } else {
            todo -= runningTime;
            return false;   // proces nog niet afgerond
        }
    }

    public int comparePID(Proces p) {
        if (pid == p.pid) return 0;
        else if (pid < p.pid) return -1;
        else return 1;
    }

    public int compareArrival(Proces p) {
        if (arrivalTime == p.arrivalTime) return 0;
        else if (arrivalTime < p.arrivalTime) return -1;
        else return 1;
    }

    public int compareService(Proces p) {
        if (serviceTime == p.serviceTime) return 0;
        else if (serviceTime < p.serviceTime) return -1;
        else return 1;
    }

    public int compareWaiting(Proces p) {
        if (waitingTime == p.waitingTime) return 0;
        else if (waitingTime < p.waitingTime) return -1;
        else return 1;
    }

    public int compareTodo(Proces p) {
        if (todo == p.todo) return 0;
        else if (todo < p.todo) return -1;
        else return 1;
    }

    public int compareResponse(Proces p) {
        if (responseTime == p.responseTime) return 0;
        else if (responseTime < p.responseTime) return 1;
        else return -1;
    }

    public boolean startVoorEinde(Proces p) {
        return arrivalTime < p.finishedTime;
    }

    public String toCSVString() {
        return pid + "," + arrivalTime + "," + serviceTime + "," + waitingTime + "," + turnAroundTime + "," + normTurnAroundTime;
    }
}
