package Core;

import Commands.*;

import java.util.*;

/**
 * Основен управляващ клас на приложението „Личен Календар“.
 * Отговаря за:
 *  Инициализация и регистриране на всички команди
 *  Четене и интерпретиране на потребителски вход
 *  Изпълнение на съответните команди
 */
public class CalendarManager {

    /**
     * Колекция от регистрирани команди, достъпни чрез ключ по име.
     */
    private final Map<String, BaseCommand> commands = new HashMap<>();

    /**
     * Четец за вход от потребителя.
     */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Общият контекст на приложението, съдържащ състоянието на календара и файла.
     */
    private final CalendarContext context = new CalendarContext();

    /**
     * Конструктор, който инициализира всички команди в системата.
     */
    public CalendarManager() {
        initializeCommands();
    }

    /**
     * Регистрира всички налични команди и ги асоциира с техните ключови думи.
     */
    private void initializeCommands() {
        // Основни команди
        commands.put("open", new OpenCommand(context));
        commands.put("close", new CloseCommand(context));
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

    /**
     * Стартира главния цикъл на приложението.
     * Приема потребителски команди от конзолата и ги изпълнява, ако са валидни.
     */
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
