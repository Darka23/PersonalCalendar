package Commands;

import Core.CalendarContext;
import Core.MyCalendar;
import Core.Event;

import java.util.List;

/**
 * Команда за търсене на събития по ключова дума.
 */
public class FindCommand implements BaseCommand {
    private final CalendarContext context;

    /**
     * Създава команда за търсене в календара.
     *
     * @param context контекстът, съдържащ календара
     */
    public FindCommand(CalendarContext context) {
        this.context = context;
    }

    /**
     * Търси събития, в чието име или бележка присъства ключовата дума.
     *
     * @param args масив с 1 или повече думи – ключова дума за търсене
     */
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
