public class Subtask extends Task {

    public int epicID;

    public Subtask(String name, String description, StatusType status, int epicID) {
        super(name, description, status);
        this.epicID = epicID;
    }
}
