package Core;

/**
 * Клас, който съхранява текущото състояние на календара и приложението.
 * Използва се за споделяне на информация между всички команди.
 */
public class CalendarContext {

    /**
     * Пътят до в момента отворения файл, или {@code null}, ако няма отворен файл.
     */
    private String openedFile = null;

    /**
     * Флаг, указващ дали в момента има отворен файл.
     */
    private boolean isOpen = false;

    /**
     * Текущият календар, с който работи приложението.
     */
    private MyCalendar calendar = new MyCalendar();


    public String getOpenedFile() {
        return openedFile;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public MyCalendar getCalendar() {
        return calendar;
    }

    public void setOpenedFile(String openedFile) {
        this.openedFile = openedFile;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public void setCalendar(MyCalendar calendar) {
        this.calendar = calendar;
    }
}
