package Commands;

import Core.CalendarContext;
import Core.MyCalendar;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Команда за отваряне на файл с календар.
 * <p>Ако файлът не съществува, се създава нов празен файл и празен календар.</p>
 */
public class OpenCommand implements BaseCommand {
    private final CalendarContext context;

    /**
     * Създава команда за отваряне или създаване на календарен файл.
     *
     * @param context контекстът, в който се съхранява текущото състояние
     */
    public OpenCommand(CalendarContext context) {
        this.context = context;
    }

    /**
     * Зарежда календар от подадения файл. Ако файлът не съществува, създава нов.
     *
     * @param args масив с 1 аргумент – път до файл
     */
    @Override
    public void execute(String[] args) {
        if (context.isOpen) {
            System.out.println("Моля, първо затворете текущия файл с командата 'close'.");
            return;
        }

        if (args.length < 1) {
            System.out.println("Моля, укажете файл за отваряне.");
            return;
        }

        String path = args[0];
        File file = new File(path);

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                context.calendar = new MyCalendar();
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 5) {
                        LocalDate date = LocalDate.parse(parts[0]);
                        LocalTime start = LocalTime.parse(parts[1]);
                        LocalTime end = LocalTime.parse(parts[2]);
                        String name = parts[3];
                        String note = parts[4];
                        context.calendar.book(date, start, end, name, note);
                    }
                }
                context.openedFile = path;
                context.isOpen = true;
                System.out.println("Successfully opened " + path);
            } catch (Exception e) {
                System.out.println("Error opening file: " + e.getMessage());
                System.exit(1);
            }
        } else {
            try {
                file.createNewFile();
                context.calendar = new MyCalendar();
                context.openedFile = path;
                context.isOpen = true;
                System.out.println("File did not exist. Created new file: " + path);
            } catch (IOException e) {
                System.out.println("Cannot create file: " + e.getMessage());
            }
        }
    }
}
