package Core;

import java.util.*;
import java.time.*;

/**
 * Основен клас, представляващ личен календар.
 * <p>
 * Поддържа събития, почивни дни, справки, търсене на свободно време
 * и логика за обединяване с друг календар.
 */
public class MyCalendar {

    /**
     * Списък с всички събития в календара.
     */
    private List<Event> events = new ArrayList<>();

    /**
     * Множество от дати, отбелязани като почивни.
     */
    private Set<LocalDate> holidays = new HashSet<>();

    /**
     * Добавя ново събитие към календара.
     *
     * @param date  дата на събитието
     * @param start начален час
     * @param end   краен час
     * @param name  име на събитието
     * @param note  бележка
     */
    public void book(LocalDate date, LocalTime start, LocalTime end, String name, String note) {
        events.add(new Event(date, start, end, name, note));
    }

    /**
     * Премахва събитие от календара по дата и начален/краен час.
     *
     * @param date  дата на събитието
     * @param start начален час
     * @param end   краен час
     */
    public void unbook(LocalDate date, LocalTime start, LocalTime end) {
        events.removeIf(e -> e.getDate().equals(date) &&
                e.getStartTime().equals(start) &&
                e.getEndTime().equals(end));
    }

    /**
     * Връща всички събития за дадена дата, подредени по начален час.
     *
     * @param date датата, за която се търсят събития
     * @return списък със събитията за деня
     */
    public List<Event> agenda(LocalDate date) {
        List<Event> result = new ArrayList<>();
        for (Event e : events) {
            if (e.getDate().equals(date)) {
                result.add(e);
            }
        }
        result.sort(Comparator.comparing(Event::getStartTime));
        return result;
    }

    /**
     * Променя конкретно поле на съществуващо събитие.
     *
     * @param date      дата на събитието
     * @param start     начален час
     * @param end       краен час
     * @param field     полето за промяна: date, starttime, endtime, name, note
     * @param newValue  нова стойност
     */
    public void change(LocalDate date, LocalTime start, LocalTime end, String field, String newValue) {
        for (Event e : events) {
            if (e.getDate().equals(date) &&
                    e.getStartTime().equals(start) &&
                    e.getEndTime().equals(end)) {

                switch (field.toLowerCase()) {
                    case "date" -> e.setDate(LocalDate.parse(newValue));
                    case "starttime" -> e.setStartTime(LocalTime.parse(newValue));
                    case "endtime" -> e.setEndTime(LocalTime.parse(newValue));
                    case "name" -> e.setName(newValue);
                    case "note" -> e.setNote(newValue);
                }
                break;
            }
        }
    }

    /**
     * Търси събития, съдържащи ключова дума в името или бележката.
     *
     * @param keyword дума за търсене
     * @return списък с намерените събития
     */
    public List<Event> find(String keyword) {
        List<Event> result = new ArrayList<>();
        for (Event e : events) {
            if (e.getName().contains(keyword) || e.getNote().contains(keyword)) {
                result.add(e);
            }
        }
        return result;
    }

    /**
     * Отбелязва дадена дата като почивен ден.
     *
     * @param date датата за отбелязване
     */
    public void holiday(LocalDate date) {
        holidays.add(date);
    }

    /**
     * Изчислява часовете заетост за всеки ден от даден период.
     *
     * @param from начална дата
     * @param to   крайна дата
     * @return карта от дати към обща заетост (в часове)
     */
    public Map<LocalDate, Long> busydays(LocalDate from, LocalDate to) {
        Map<LocalDate, Long> busyHours = new HashMap<>();
        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            long totalMinutes = 0;
            for (Event e : events) {
                if (e.getDate().equals(date)) {
                    totalMinutes += Duration.between(e.getStartTime(), e.getEndTime()).toMinutes();
                }
            }
            busyHours.put(date, totalMinutes / 60); // в часове
        }
        return busyHours;
    }

    /**
     * Намира първия свободен времеви интервал в даден период.
     *
     * @param fromDate начална дата
     * @param toDate   крайна дата
     * @param duration минимална продължителност
     * @return свободен интервал или празен резултат, ако няма такъв
     */
    public Optional<LocalDateTime[]> findslot(LocalDate fromDate, LocalDate toDate, Duration duration) {
        for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            if (holidays.contains(date)) continue;

            List<Event> dayEvents = agenda(date);
            LocalTime lastEnd = LocalTime.of(8, 0); // Работен ден от 08:00

            for (Event e : dayEvents) {
                if (Duration.between(lastEnd, e.getStartTime()).compareTo(duration) >= 0) {
                    return Optional.of(new LocalDateTime[]{
                            LocalDateTime.of(date, lastEnd),
                            LocalDateTime.of(date, lastEnd.plus(duration))
                    });
                }
                lastEnd = e.getEndTime();
            }

            if (Duration.between(lastEnd, LocalTime.of(17, 0)).compareTo(duration) >= 0) {
                return Optional.of(new LocalDateTime[]{
                        LocalDateTime.of(date, lastEnd),
                        LocalDateTime.of(date, lastEnd.plus(duration))
                });
            }
        }
        return Optional.empty();
    }

    /**
     * Намира общ свободен интервал между текущия календар и друг.
     *
     * @param otherCalendar втори календар
     * @param fromDate      начален ден
     * @param toDate        краен ден
     * @param duration      желаната продължителност
     * @return общо свободно време, ако има такова
     */
    public Optional<LocalDateTime[]> findslotwith(MyCalendar otherCalendar, LocalDate fromDate, LocalDate toDate, Duration duration) {
        for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            if (holidays.contains(date) || otherCalendar.holidays.contains(date)) continue;

            List<Event> allEvents = new ArrayList<>();
            allEvents.addAll(agenda(date));
            allEvents.addAll(otherCalendar.agenda(date));
            allEvents.sort(Comparator.comparing(Event::getStartTime));

            LocalTime lastEnd = LocalTime.of(8, 0);
            for (Event e : allEvents) {
                if (Duration.between(lastEnd, e.getStartTime()).compareTo(duration) >= 0) {
                    return Optional.of(new LocalDateTime[]{
                            LocalDateTime.of(date, lastEnd),
                            LocalDateTime.of(date, lastEnd.plus(duration))
                    });
                }
                if (e.getEndTime().isAfter(lastEnd)) {
                    lastEnd = e.getEndTime();
                }
            }

            if (Duration.between(lastEnd, LocalTime.of(17, 0)).compareTo(duration) >= 0) {
                return Optional.of(new LocalDateTime[]{
                        LocalDateTime.of(date, lastEnd),
                        LocalDateTime.of(date, lastEnd.plus(duration))
                });
            }
        }
        return Optional.empty();
    }

    /**
     * Обединява текущия календар с друг, като при конфликт иска потвърждение от потребителя.
     *
     * @param otherCalendar календарът, който се обединява
     */
    public void merge(MyCalendar otherCalendar) {
        Scanner scanner = new Scanner(System.in);
        for (Event e : otherCalendar.events) {
            boolean conflict = false;
            for (Event existing : this.events) {
                if (existing.getDate().equals(e.getDate()) &&
                        !(existing.getEndTime().isBefore(e.getStartTime()) || existing.getStartTime().isAfter(e.getEndTime()))) {
                    conflict = true;
                    break;
                }
            }

            if (conflict) {
                System.out.println("Конфликт със събитие: " + e);
                System.out.print("Да добавя събитието на друга дата/час (yes/no)? ");
                String answer = scanner.nextLine();
                if (answer.equalsIgnoreCase("yes")) {
                    System.out.print("Нова дата (yyyy-MM-dd): ");
                    LocalDate newDate = LocalDate.parse(scanner.nextLine());
                    System.out.print("Нов начален час (HH:mm): ");
                    LocalTime newStart = LocalTime.parse(scanner.nextLine());
                    System.out.print("Нов краен час (HH:mm): ");
                    LocalTime newEnd = LocalTime.parse(scanner.nextLine());

                    events.add(new Event(newDate, newStart, newEnd, e.getName(), e.getNote()));
                }
            } else {
                events.add(e);
            }
        }
    }

    /**
     * Връща всички събития в календара.
     *
     * @return списък със събития
     */
    public List<Event> getEvents() {
        return events;
    }
}
