public class Subtask extends Task {

    public int epicID;

    public Subtask(String name, String description, StatusType status, int epicID, int duration, String startTime) {
        super(name, description, status, duration, startTime);
        this.epicID = epicID;
    }
}
