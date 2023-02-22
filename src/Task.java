import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    protected Integer id;
    protected String name;
    protected String description;
    protected StatusType status;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public Task(String name, String description, StatusType status, int duration, String startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime);
        this.endTime = this.startTime.plus(this.duration);
    }

    public Task(String name, String description, StatusType status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }
}
