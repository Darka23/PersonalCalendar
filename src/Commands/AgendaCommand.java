package Commands;

import Core.CalendarContext;
import Core.MyCalendar;
import Core.Event;

import java.time.LocalDate;
import java.util.List;

/**
 * Команда за извеждане на събитията за дадена дата.
 */
public class AgendaCommand implements BaseCommand {
    private final CalendarContext context;

    /**
     * Създава команда за извеждане на дневна програма.
     *
     * @param context контекстът с календара
     */
    public AgendaCommand(CalendarContext context) {
        this.context = context;
    }

    /**
     * Извежда списък със събития за избрана дата.
     *
     * @param args масив с 1 аргумент – дата (yyyy-MM-dd)
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: agenda <date (yyyy-MM-dd)>");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(args[0]);
            List<Event> events = context.getCalendar().agenda(date);
            if (events.isEmpty()) {
                System.out.println("No events for " + date);
            } else {
                for (Event e : events) {
                    System.out.println(e);
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid date format.");
        }
    }
}
