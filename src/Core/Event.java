package Core;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Представлява едно събитие от календара.
 * Съдържа информация за дата, време, име и допълнителна бележка.
 */
public class Event {

    /**
     * Датата, на която се провежда събитието.
     */
    private LocalDate date;

    /**
     * Начален час на събитието.
     */
    private LocalTime startTime;

    /**
     * Краен час на събитието.
     */
    private LocalTime endTime;

    /**
     * Име или заглавие на събитието.
     */
    private String name;

    /**
     * Допълнителна бележка или описание към събитието.
     */
    private String note;

    /**
     * Създава ново събитие с дадени параметри.
     *
     * @param date      датата на събитието
     * @param startTime начален час
     * @param endTime   краен час
     * @param name      заглавие или име на събитието
     * @param note      бележка или описание
     */
    public Event(LocalDate date, LocalTime startTime, LocalTime endTime, String name, String note) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.note = note;
    }

    // Гетъри
    /**
     * Връща датата на събитието.
     */
    public LocalDate getDate() { return date; }

    /**
     * Връща началния час на събитието.
     */
    public LocalTime getStartTime() { return startTime; }

    /**
     * Връща крайния час на събитието.
     */
    public LocalTime getEndTime() { return endTime; }

    /**
     * Връща името на събитието.
     */
    public String getName() { return name; }

    /**
     * Връща бележката към събитието.
     */
    public String getNote() { return note; }


    // Сетъри
    /**
     * Задава нова дата на събитието.
     */
    public void setDate(LocalDate date) { this.date = date; }

    /**
     * Задава нов начален час на събитието.
     */
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    /**
     * Задава нов краен час на събитието.
     */
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    /**
     * Задава ново име на събитието.
     */
    public void setName(String name) { this.name = name; }

    /**
     * Задава нова бележка към събитието.
     */
    public void setNote(String note) { this.note = note; }

    /**
     * Връща текстово представяне на събитието.
     *
     * @return низ в следния формат: дата начален_час–краен_час име : бележка
     */
    @Override
    public String toString() {
        return date + " " + startTime + "-" + endTime + " " + name + " : " + note;
    }
}
