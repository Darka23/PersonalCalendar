package Commands;

import Core.CalendarContext;
import Core.MyCalendar;

import java.time.LocalDate;
import java.util.Map;

/**
 * Команда за показване на заетост по дни в даден период.
 */
public class BusyDaysCommand implements BaseCommand {
    private final CalendarContext context;

    /**
     * Създава команда за справка по заетост.
     *
     * @param context контекстът с календара
     */
    public BusyDaysCommand(CalendarContext context) {
        this.context = context;
    }

    /**
     * Изчислява и показва часовете заетост за всеки ден между две дати.
     *
     * @param args масив с 2 аргумента – начален и краен ден (yyyy-MM-dd)
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: busydays <start date (yyyy-MM-dd)> <end date (yyyy-MM-dd)>");
            return;
        }

        try {
            LocalDate from = LocalDate.parse(args[0]);
            LocalDate to = LocalDate.parse(args[1]);
            Map<LocalDate, Long> stats = context.calendar.busydays(from, to);

            for (Map.Entry<LocalDate, Long> entry : stats.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue() + " часа");
            }
        } catch (Exception e) {
            System.out.println("Invalid date input.");
        }
    }
}
