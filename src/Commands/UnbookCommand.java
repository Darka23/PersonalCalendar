package Commands;

import java.time.LocalDate;
import java.time.LocalTime;

import Core.CalendarContext;
import Core.MyCalendar;


/**
 * Команда за премахване на събитие от календара.
 */
public class UnbookCommand implements BaseCommand {
    private final CalendarContext context;

    /**
     * Създава команда за изтриване на събитие.
     *
     * @param context споделеният контекст на приложението
     */
    public UnbookCommand(CalendarContext context) {
        this.context = context;
    }

    /**
     * Изпълнява командата. Премахва събитие по дата и начален/краен час.
     *
     * @param args масив с 3 аргумента: дата, начален час, краен час
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: unbook <date (yyyy-MM-dd)> <start (hh:mm)> <end (hh:mm)>");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(args[0]);
            LocalTime start = LocalTime.parse(args[1]);
            LocalTime end = LocalTime.parse(args[2]);
            context.calendar.unbook(date, start, end);
            System.out.println("Successfully unbooked event.");
        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }
}
