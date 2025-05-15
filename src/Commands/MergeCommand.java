package Commands;

import Core.CalendarContext;
import Core.MyCalendar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Команда за обединяване на текущия календар с календар от файл.
 */
public class MergeCommand implements BaseCommand {
    private final CalendarContext context;

    /**
     * Създава команда за сливане на календари.
     *
     * @param context контекстът, съдържащ основния календар
     */
    public MergeCommand(CalendarContext context) {
        this.context = context;
    }

    /**
     * Зарежда втори календар от подадения файл и го слива с текущия,
     * като при нужда иска потвърждение от потребителя при конфликт.
     *
     * @param args масив с 1 аргумент – път до файл с друг календар
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: merge <file>");
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

            context.calendar.merge(other);
            System.out.println("Merge completed.");
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
