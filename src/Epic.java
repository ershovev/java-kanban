import java.util.ArrayList;

public class Epic extends Task {

    public ArrayList<Integer> tasks = new ArrayList<>();   // список для хранения айдишников тасков которые относятся к эпику

    public Epic(int id, String name, String description, String status) {
        super(id, name, description, status);
    }
}
