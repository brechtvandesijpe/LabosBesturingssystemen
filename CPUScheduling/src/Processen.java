import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Processen {
    private List<Proces> processen;

    public Processen() {
        processen = new LinkedList<>();
    }

    public Processen(Processen p) {
        processen = new LinkedList<>();
        processen.addAll(p.processen);
    }

    public void voegToe(Proces p) {
        processen.add(p);
    }

    public void sorteerOpArrivalTime() {
        processen.sort(Proces::compareArrival);
    }

    public void sorteerOpServiceTime() {
        processen.sort(Proces::compareService);
    }

    public void scheduleFCFS() {
        this.sorteerOpArrivalTime();

        processen.get(0).run(processen.get(0).getArrivalTime(), -1);

        for (int i = 1; i < processen.size(); i++) {
            Proces huidige = processen.get(i);
            Proces vorige = processen.get(i - 1);

            huidige.run(Math.max(huidige.getArrivalTime(), vorige.getFinishedTime()), -1);
        }
    }

    public void scheduleSJF() {
        this.sorteerOpArrivalTime();

        int currentTime = 0;
        int indexLaatstVerwerkteProces = -1;
        boolean run = true;

        LinkedList<Proces> procesPool = new LinkedList<>();
        LinkedList<Proces> verwerkteProcessen = new LinkedList<>();

        while (run) {
            for (Proces p : processen) {
                if (p.getArrivalTime() <= currentTime) {
                    if (!procesPool.contains(p) && !verwerkteProcessen.contains(p)) {
                        procesPool.add(p);
                    }
                } else break;
            }
            if (procesPool.isEmpty()) {
                if (indexLaatstVerwerkteProces == processen.size() - 1) run = false;
                else currentTime = processen.get(indexLaatstVerwerkteProces + 1).getArrivalTime();
            } else {
                procesPool.sort(Proces::compareService);

                Proces p = procesPool.get(0);
                p.run(currentTime, -1);

                int index = processen.indexOf(p);
                if (index > indexLaatstVerwerkteProces) indexLaatstVerwerkteProces = index;

                currentTime = p.getFinishedTime();
                verwerkteProcessen.add(p);
                procesPool.remove(p);
            }
        }
    }

    public void scheduleSRT() {
        this.sorteerOpArrivalTime();

        int currentTime = 0;
        int indexLaatstVerwerkteProces = -1;
        boolean run = true;

        LinkedList<Proces> procesPool = new LinkedList<>();
        LinkedList<Proces> verwerkteProcessen = new LinkedList<>();

        while (run) {
            for (Proces p : processen) {
                if (p.getArrivalTime() <= currentTime) {
                    if (!procesPool.contains(p) && !verwerkteProcessen.contains(p)) {
                        procesPool.add(p);
                    }
                } else break;
            }

            if (procesPool.isEmpty()) {
                if (indexLaatstVerwerkteProces == processen.size() - 1) run = false;
                else currentTime = processen.get(indexLaatstVerwerkteProces + 1).getArrivalTime();
            } else {
                procesPool.sort(Proces::compareTodo);

                Proces p = procesPool.get(0);
                boolean afgerond = p.run(currentTime, 1);

                if (afgerond) {
                    int index = processen.indexOf(p);
                    if (index > indexLaatstVerwerkteProces) indexLaatstVerwerkteProces = index;
                    verwerkteProcessen.add(p);
                    procesPool.remove(p);
                }

                currentTime++;
            }
        }

    }

    public void scheduleHRRN() {
        this.sorteerOpArrivalTime();

        int currentTime = 0;
        int indexLaatstVerwerkteProces = -1;
        boolean run = true;

        LinkedList<Proces> procesPool = new LinkedList<>();
        LinkedList<Proces> verwerkteProcessen = new LinkedList<>();

        while (run) {
            for (Proces p : processen) {
                if (p.getArrivalTime() <= currentTime) {
                    if (!procesPool.contains(p) && !verwerkteProcessen.contains(p)) {
                        procesPool.add(p);
                    }
                } else break;
            }

            if (procesPool.isEmpty()) {
                if (indexLaatstVerwerkteProces == processen.size() - 1) run = false;
                else {
                    currentTime = processen.get(indexLaatstVerwerkteProces + 1).getArrivalTime();
                }
            } else {
                for (int i = 0; i < procesPool.size(); i++) procesPool.get(i).berekenResponseTime(currentTime);
                procesPool.sort(Proces::compareResponse);

                Proces p = procesPool.get(0);
                p.run(currentTime, -1);

                int index = processen.indexOf(p);
                if (index > indexLaatstVerwerkteProces) indexLaatstVerwerkteProces = index;

                currentTime = p.getFinishedTime();
                verwerkteProcessen.add(p);
                procesPool.remove(p);
            }
        }

        processen = verwerkteProcessen;
    }

    public void scheduleRR(int q) {
        this.sorteerOpArrivalTime();

        int currentTime = 0;
        int indexLaatstVerwerkteProces = -1;
        boolean run = true;

        LinkedList<Proces> procesPool = new LinkedList<>();
        LinkedList<Proces> verwerkteProcessen = new LinkedList<>();

        while (run) {
            for (Proces p : processen) {
                if (p.getArrivalTime() <= currentTime) {
                    if (!procesPool.contains(p) && !verwerkteProcessen.contains(p)) {
                        procesPool.add(p);
                    }
                } else break;
            }

            if (procesPool.isEmpty()) {
                if (indexLaatstVerwerkteProces == processen.size() - 1) run = false;
                else currentTime = processen.get(indexLaatstVerwerkteProces + 1).getArrivalTime();
            } else {
                Proces p = procesPool.poll();   // eerste element wordt uit de lijst genomen en verwijderd
                boolean afgerond = p.run(currentTime, q);

                int index = processen.indexOf(p);
                if (index > indexLaatstVerwerkteProces) indexLaatstVerwerkteProces = index;

                if (afgerond) {
                    verwerkteProcessen.add(p);
                    procesPool.remove(p);
                } else procesPool.addLast(p);
            }

            currentTime += q;
        }
    }

    private Proces scheduleRRFB(Processen proc, int currentTime, int q) {
        int aantalProcessen = processen.size();
        for (int i = 0; i < aantalProcessen; i++) {
            Proces p = processen.get(0);
            if (p.getArrivalTime() <= currentTime) {
                boolean afgerond = p.run(currentTime, q);

                if (!afgerond) {
                    processen.remove(0);
                    return p;
                } else {
                    proc.voegToe(p);
                    processen.remove(0);
                }
            }
        }
        return null;
    }

    public void scheduleMFBQ(int[] qWaarden) throws IOException {

        double[] grenzen = {0.0,
                0.2 * processen.size(),
                0.4 * processen.size(),
                0.6 * processen.size(),
                0.8 * processen.size(),
                processen.size()};

        this.sorteerOpServiceTime();
        List<Processen> queues = new LinkedList<>();

        for (int d = 1; d < grenzen.length; d++) {
            Processen queue = new Processen();
            for (int i = (int) grenzen[d - 1]; i < (int) grenzen[d]; i++) queue.voegToe(processen.get(i));
            queues.add(queue);
        }

        Processen verwerkteProcessen = new Processen();
        for (Processen queue : queues) queue.sorteerOpArrivalTime();

        Processen prio1 = queues.get(0);
        Processen prio2 = queues.get(1);
        Processen prio3 = queues.get(2);
        Processen prio4 = queues.get(3);
        Processen prio5 = queues.get(4);

        int currentTime = 0;

        while (verwerkteProcessen.processen.size() < processen.size()) {

            prio1.sorteerOpArrivalTime();
            prio2.sorteerOpArrivalTime();
            prio3.sorteerOpArrivalTime();
            prio4.sorteerOpArrivalTime();
            prio5.sorteerOpArrivalTime();

            if (!prio1.processen.isEmpty() && prio1.processen.get(0).getArrivalTime() <= currentTime) {
                Proces teDegraderen = prio1.scheduleRRFB(verwerkteProcessen, currentTime, qWaarden[0]);
                if (teDegraderen != null) prio2.voegToe(teDegraderen);
                currentTime += qWaarden[0];
            } else if (!prio2.processen.isEmpty() && prio2.processen.get(0).getArrivalTime() <= currentTime) {
                Proces teDegraderen = prio2.scheduleRRFB(verwerkteProcessen, currentTime, qWaarden[1]);
                if (teDegraderen != null) prio3.voegToe(teDegraderen);
                currentTime += qWaarden[1];
            } else if (!prio3.processen.isEmpty() && prio3.processen.get(0).getArrivalTime() <= currentTime) {
                Proces teDegraderen = prio3.scheduleRRFB(verwerkteProcessen, currentTime, qWaarden[2]);
                if (teDegraderen != null) prio4.voegToe(teDegraderen);
                currentTime += qWaarden[2];
            } else if (!prio4.processen.isEmpty() && prio4.processen.get(0).getArrivalTime() <= currentTime) {
                Proces teDegraderen = prio4.scheduleRRFB(verwerkteProcessen, currentTime, qWaarden[3]);
                if (teDegraderen != null) prio5.voegToe(teDegraderen);
                currentTime += qWaarden[3];
            } else if (!prio5.processen.isEmpty() && prio5.processen.get(0).getArrivalTime() <= currentTime) {
                Proces teDegraderen = prio5.scheduleRRFB(verwerkteProcessen, currentTime, qWaarden[4]);
                if (teDegraderen != null) prio5.voegToe(teDegraderen);
                currentTime += qWaarden[4];
            } else currentTime++;
        }

        verwerkteProcessen.sorteerOpServiceTime();
        verwerkteProcessen.export("mfbq2ServiceSort20k.csv");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Proces p : processen) {
            sb.append(p.getArrivalTime()).append("|").append(p.getServiceTime());
            sb.append(" ");
        }
        return sb.toString();
    }

    public String toCSVString() {
        StringBuilder sb = new StringBuilder();

        for (Proces p : processen) {
            sb.append(p.toCSVString());
            sb.append("\n");
        }

        return sb.toString();
    }

    public void export(String s) throws IOException {
        try {
            FileWriter fw = new FileWriter(s);
            PrintWriter pw = new PrintWriter(fw);

            pw.write(this.toCSVString());
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
