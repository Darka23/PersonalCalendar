package Commands;

import Core.CalendarContext;
import Core.MyCalendar;
import Core.Event;

import java.util.List;

public class FindCommand implements BaseCommand {
    private final CalendarContext context;

    public FindCommand(CalendarContext context) {
        this.context = context;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: find <keyword>");
            return;
        }

        List<Event> found = context.calendar.find(String.join(" ", args).trim());
        if (found.isEmpty()) {
            System.out.println("No events found.");
        } else {
            for (Event e : found) {
                System.out.println(e);
            }
        }
    }
}
