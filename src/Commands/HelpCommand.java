package Commands;

/**
 * Команда за извеждане на списък с всички поддържани команди.
 */
public class HelpCommand implements BaseCommand {

    /**
     * Извежда списък с наличните команди и тяхното предназначение.
     *
     * @param args не се използват
     */
    @Override
    public void execute(String[] args) {
        System.out.println("The following commands are supported:");
        System.out.println("open <file>     opens <file>");
        System.out.println("close           closes currently opened file");
        System.out.println("save            saves the currently open file");
        System.out.println("saveas <file>   saves the currently open file in <file>");
        System.out.println("help            prints this information");
        System.out.println("exit            exits the program");
        System.out.println("book, unbook, agenda, find, change, holiday, busydays, findslot, findslotwith, merge");
    }
}
