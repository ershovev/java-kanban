public class Subtask extends Task {

    public int epicID;

    public Subtask(int id, String name, String description, String status, int epicID) {
        super(id, name, description, status);
        this.epicID = epicID;
    }


}
