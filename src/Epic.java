import java.util.ArrayList;

public class Epic extends Task {

    protected Epic(String name, String description, StatusType status) {
        super(name, description, status);
    }

    protected ArrayList<Integer> tasks = new ArrayList<>();   // список для хранения айдишников тасков которые относятся к эпику
}
