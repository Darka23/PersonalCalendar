package Commands;

import Core.CalendarContext;
import Core.MyCalendar;

/**
 * Команда за затваряне на текущо отворения календар и изчистване на състоянието.
 */
public class CloseCommand implements BaseCommand {
    private final CalendarContext context;

    /**
     * Създава команда за затваряне на файл.
     *
     * @param context контекстът на приложението
     */
    public CloseCommand(CalendarContext context) {
        this.context = context;
    }

    /**
     * Затваря текущия файл и нулира състоянието на календара.
     *
     * @param args не се използват
     */
    @Override
    public void execute(String[] args) {
        if (!context.isOpen) {
            System.out.println("Няма отворен файл.");
            return;
        }

        context.calendar = new MyCalendar();
        context.openedFile = null;
        context.isOpen = false;
        System.out.println("Successfully closed.");
    }
}
