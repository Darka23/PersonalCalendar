package Core;

/**
 * Клас, който съхранява текущото състояние на календара и приложението.
 * Използва се за споделяне на информация между всички команди.
 */
public class CalendarContext {

    /**
     * Пътят до в момента отворения файл, или {@code null}, ако няма отворен файл.
     */
    public String openedFile = null;

    /**
     * Флаг, указващ дали в момента има отворен файл.
     */
    public boolean isOpen = false;

    /**
     * Текущият календар, с който работи приложението.
     */
    public MyCalendar calendar = new MyCalendar();
}
