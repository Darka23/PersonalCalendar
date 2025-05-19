package Commands;

import Core.CalendarContext;
import Core.MyCalendar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.*;
import java.util.Optional;

/**
 * Команда за намиране на общ свободен времеви слот с друг календар.
 */
public class FindSlotWithCommand implements BaseCommand {
    private final CalendarContext context;

    /**
     * Създава команда за търсене на общ свободен интервал между два календара.
     *
     * @param context контекстът, съдържащ текущия календар
     */
    public FindSlotWithCommand(CalendarContext context) {
        this.context = context;
    }

    /**
     * Зарежда втори календар от файл и търси общ свободен интервал за среща.
     *
     * @param args масив с 4 аргумента – файл, начална дата, крайна дата, продължителност в минути
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage: findslotwith <file> <start date (yyyy-dd-mm)> <end date (yyyy-dd-mm)> <duration in minutes>");
            return;
        }

        String filePath = args[0];
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            MyCalendar other = new MyCalendar();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] e = line.split("\\|");
                if (e.length == 5) {
                    LocalDate date = LocalDate.parse(e[0]);
                    LocalTime start = LocalTime.parse(e[1]);
                    LocalTime end = LocalTime.parse(e[2]);
                    String name = e[3];
                    String note = e[4];
                    other.book(date, start, end, name, note);
                }
            }

            LocalDate from = LocalDate.parse(args[1]);
            LocalDate to = LocalDate.parse(args[2]);
            long minutes = Long.parseLong(args[3]);

            Optional<LocalDateTime[]> result = context.getCalendar().findslotwith(other, from, to, Duration.ofMinutes(minutes));
            if (result.isPresent()) {
                LocalDateTime[] times = result.get();
                System.out.println("Found mutual slot: " + " " + times[0] + " - " + " " + times[1]);
            } else {
                System.out.println("No common free slot found.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
