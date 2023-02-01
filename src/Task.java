public class Task {
    protected Integer id;
    protected String name;
    protected String description;
    protected StatusType status;

    protected Task(String name, String description, StatusType status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }
}
