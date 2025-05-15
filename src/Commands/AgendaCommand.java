package Commands;

import Core.CalendarContext;
import Core.MyCalendar;
import Core.Event;

import java.time.LocalDate;
import java.util.List;

public class AgendaCommand implements BaseCommand {
    private final CalendarContext context;

    public AgendaCommand(CalendarContext context) {
        this.context = context;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: agenda <date (yyyy-dd-mm)>");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(args[0]);
            List<Event> events = context.calendar.agenda(date);
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
