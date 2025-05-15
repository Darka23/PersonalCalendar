package Commands;

import Core.CalendarContext;
import Core.MyCalendar;

public class CloseCommand implements BaseCommand {
    private final CalendarContext context;

    public CloseCommand(CalendarContext context) {
        this.context = context;
    }

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
