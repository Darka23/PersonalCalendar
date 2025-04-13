import java.time.LocalDate;
import java.time.LocalTime;

public class Event {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String name;
    private String note;

    public Event(LocalDate date, LocalTime startTime, LocalTime endTime, String name, String note) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.note = note;
    }

    public LocalDate getDate() { return date; }

    public LocalTime getStartTime() { return startTime; }

    public LocalTime getEndTime() { return endTime; }

    public String getName() { return name; }

    public String getNote() { return note; }

    public void setDate(LocalDate date) { this.date = date; }

    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public void setName(String name) { this.name = name; }

    public void setNote(String note) { this.note = note; }

    @Override
    public String toString() {
        return date + " " + startTime + "-" + endTime + " " + name + " : " + note;
    }
}
