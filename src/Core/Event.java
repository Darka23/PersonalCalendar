package Core;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event {

    // Дата на събитието (напр. 2025-05-13)
    private LocalDate date;

    // Начален час на събитието (напр. 14:00)
    private LocalTime startTime;

    // Краен час на събитието (напр. 15:00)
    private LocalTime endTime;

    // Име/заглавие на събитието (напр. "Среща с клиент")
    private String name;

    // Бележка или допълнителна информация
    private String note;

    public Event(LocalDate date, LocalTime startTime, LocalTime endTime, String name, String note) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.note = note;
    }

    // Гетъри
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public String getName() { return name; }
    public String getNote() { return note; }

    // Сетъри
    public void setDate(LocalDate date) { this.date = date; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public void setName(String name) { this.name = name; }
    public void setNote(String note) { this.note = note; }

    // toString – използва се при отпечатване в конзолата или логове
    @Override
    public String toString() {
        return date + " " + startTime + "-" + endTime + " " + name + " : " + note;
    }
}
