package Commands;

import java.time.LocalDate;
import java.time.LocalTime;

import Core.CalendarContext;

/**
 * Команда за добавяне на ново събитие в календара.
 */
public class BookCommand implements BaseCommand {
    private final CalendarContext context;

    /**
     * Конструктор, който инициализира командата с контекст.
     *
     * @param context контекст, съдържащ календара и състоянието на приложението
     */
    public BookCommand(CalendarContext context) {
        this.context = context;
    }

    /**
     * Изпълнява командата за добавяне на събитие.
     * Добавя събитие в календара, ако аргументите са валидни.
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 5) {
            System.out.println("Usage: book <date (yyyy-MM-dd)> <start (hh:mm)> <end (hh:mm)> <name> <note>");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(args[0]);
            LocalTime start = LocalTime.parse(args[1]);
            LocalTime end = LocalTime.parse(args[2]);
            String name = args[3];
            String note = args[4];
            context.calendar.book(date, start, end, name, note);
            System.out.println("Successfully booked event.");
        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }
}
