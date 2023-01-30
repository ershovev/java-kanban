public class Task {
    protected Integer id;
    protected String name;
    protected String description;
    protected StatusType status;

    public Task(String name, String description, StatusType status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }
}
