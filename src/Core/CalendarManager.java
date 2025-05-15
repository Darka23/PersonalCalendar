package Core;

import Commands.*;

import java.util.*;

public class CalendarManager {

    private final Map<String, BaseCommand> commands = new HashMap<>();
    private final Scanner scanner = new Scanner(System.in);
    private final CalendarContext context = new CalendarContext();

    public CalendarManager() {
        initializeCommands();
    }

    private void initializeCommands() {
        // Основни команди
        commands.put("open", new OpenCommand(context));       // ако имаш логика за отваряне
        commands.put("close", new CloseCommand(context));     // ако имаш логика за затваряне
        commands.put("save", new SaveCommand(context));
        commands.put("saveas", new SaveAsCommand(context));
        commands.put("help", new HelpCommand());
        commands.put("exit", new ExitCommand());

        // Календарни команди
        commands.put("book", new BookCommand(context));
        commands.put("unbook", new UnbookCommand(context));
        commands.put("agenda", new AgendaCommand(context));
        commands.put("find", new FindCommand(context));
        commands.put("change", new ChangeCommand(context));
        commands.put("holiday", new HolidayCommand(context));
        commands.put("busydays", new BusyDaysCommand(context));
        commands.put("findslot", new FindSlotCommand(context));
        commands.put("findslotwith", new FindSlotWithCommand(context));
        commands.put("merge", new MergeCommand(context));
    }

    public void run() {
        System.out.println("Добре дошли в Личен Календар!");
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split("\\s+");
            String command = parts[0].toLowerCase();
            String[] args = Arrays.copyOfRange(parts, 1, parts.length);

            BaseCommand cmd = commands.get(command);
            if (cmd != null) {
                try {
                    cmd.execute(args);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                System.out.println("Unknown command. Type 'help' to see available commands.");
            }
        }
    }
}
