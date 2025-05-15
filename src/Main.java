import Core.CalendarManager;

/**
 * Главен клас, който стартира приложението „Личен Календар“.
 */
public class Main {

    /**
     * Главна входна точка на програмата.
     * Създава и стартира {@link CalendarManager}, който поема управлението.
     *
     * @param args аргументи от командния ред (не се използват)
     */
    public static void main(String[] args) {

        // Създава основния мениджър на календара
        CalendarManager manager = new CalendarManager();

        // Стартира програмата и влиза в команден цикъл
        manager.run();
    }
}