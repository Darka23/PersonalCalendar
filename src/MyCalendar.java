import java.util.*;
import java.time.*;
import java.util.Scanner;

public class MyCalendar {

    // Списък с всички събития в календара
    private List<Event> events = new ArrayList<>();

    // Сет от дати, отбелязани като почивни
    private Set<LocalDate> holidays = new HashSet<>();

    // Добавя ново събитие към календара
    public void book(LocalDate date, LocalTime start, LocalTime end, String name, String note) {
        events.add(new Event(date, start, end, name, note));
    }

    //Премахва събитие по дата и час
    public void unbook(LocalDate date, LocalTime start, LocalTime end) {
        events.removeIf(e -> e.getDate().equals(date) &&
                e.getStartTime().equals(start) &&
                e.getEndTime().equals(end));
    }

    // Връща списък с всички събития за конкретна дата, подредени по час
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

    // Променя дадено поле на събитие
    public void change(LocalDate date, LocalTime start, LocalTime end, String field, String newValue) {
        for (Event e : events) {
            if (e.getDate().equals(date) &&
                    e.getStartTime().equals(start) &&
                    e.getEndTime().equals(end)) {

                switch (field.toLowerCase()) {
                    case "date":
                        e.setDate(LocalDate.parse(newValue));
                        break;
                    case "starttime":
                        e.setStartTime(LocalTime.parse(newValue));
                        break;
                    case "endtime":
                        e.setEndTime(LocalTime.parse(newValue));
                        break;
                    case "name":
                        e.setName(newValue);
                        break;
                    case "note":
                        e.setNote(newValue);
                        break;
                }
                break;
            }
        }
    }

    // Търси събития по ключова дума (в име или бележка)
    public List<Event> find(String keyword) {
        List<Event> result = new ArrayList<>();
        for (Event e : events) {
            if (e.getName().contains(keyword) || e.getNote().contains(keyword)) {
                result.add(e);
            }
        }
        return result;
    }

    // Маркира ден като почивен
    public void holiday(LocalDate date) {
        holidays.add(date);
    }

    // Изчислява колко часа заетост има за всеки ден от даден интервал
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

    // Търси първия свободен времеви слот между от и до с дадена продължителност
    public Optional<LocalDateTime[]> findslot(LocalDate fromDate, LocalDate toDate, Duration duration) {
        for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            if (holidays.contains(date)) continue;

            List<Event> dayEvents = agenda(date);
            dayEvents.sort(Comparator.comparing(Event::getStartTime));

            LocalTime lastEnd = LocalTime.of(8, 0); // работен ден от 08:00
            for (Event e : dayEvents) {
                if (Duration.between(lastEnd, e.getStartTime()).compareTo(duration) >= 0) {
                    return Optional.of(new LocalDateTime[]{
                            LocalDateTime.of(date, lastEnd),
                            LocalDateTime.of(date, lastEnd.plus(duration))
                    });
                }
                lastEnd = e.getEndTime();
            }
            // Проверка дали има време след последното събитие до края на деня (17:00)
            if (Duration.between(lastEnd, LocalTime.of(17, 0)).compareTo(duration) >= 0) {
                return Optional.of(new LocalDateTime[]{
                        LocalDateTime.of(date, lastEnd),
                        LocalDateTime.of(date, lastEnd.plus(duration))
                });
            }
        }
        return Optional.empty();
    }

    // Търси свободен слот между два календара
    public Optional<LocalDateTime[]> findslotwith(MyCalendar otherCalendar, LocalDate fromDate, LocalDate toDate, Duration duration) {
        for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            if (holidays.contains(date) || otherCalendar.holidays.contains(date)) continue;

            List<Event> dayEventsThis = agenda(date);
            List<Event> dayEventsOther = otherCalendar.agenda(date);

            List<Event> allEvents = new ArrayList<>();
            allEvents.addAll(dayEventsThis);
            allEvents.addAll(dayEventsOther);
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

    // Обединява текущия календар с друг, като пита потребителя при конфликт
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

    // Връща списъка с всички събития (за записване или справки)
    public List<Event> getEvents() {
        return events;
    }
}
