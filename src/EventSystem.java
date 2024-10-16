import java.util.*;

public class EventSystem {
    private EventBus eventBus;
    private OutputHandler outputHandler;

    public EventSystem() {
        this.eventBus = new EventBus();
        this.outputHandler = new OutputHandler();
        eventBus.register(new InputHandler(eventBus));
        eventBus.register(new ShiftHandler(eventBus));
        eventBus.register(new SortHandler(eventBus));
        eventBus.register(outputHandler);
    }

    public String process(String input) {
        eventBus.post(new InputEvent(input));
        return outputHandler.getResult();
    }
}

class EventBus {
    private Map<Class<?>, List<EventHandler>> handlers = new HashMap<>();

    public void register(EventHandler handler) {
        handlers.computeIfAbsent(handler.getEventType(), k -> new ArrayList<>()).add(handler);
    }

    public void post(Object event) {
        List<EventHandler> eventHandlers = handlers.get(event.getClass());
        if (eventHandlers != null) {
            for (EventHandler handler : eventHandlers) {
                handler.handle(event);
            }
        }
    }
}

interface EventHandler {
    void handle(Object event);
    Class<?> getEventType();
}

class InputEvent {
    String input;
    InputEvent(String input) { this.input = input; }
}

class LinesEvent {
    List<String> lines;
    LinesEvent(List<String> lines) { this.lines = lines; }
}

class ShiftsEvent {
    List<String> shifts;
    ShiftsEvent(List<String> shifts) { this.shifts = shifts; }
}

class SortedShiftsEvent {
    List<String> sortedShifts;
    SortedShiftsEvent(List<String> sortedShifts) { this.sortedShifts = sortedShifts; }
}

class InputHandler implements EventHandler {
    private EventBus eventBus;

    public InputHandler(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void handle(Object event) {
        if (event instanceof InputEvent) {
            String input = ((InputEvent) event).input;
            List<String> lines = Arrays.asList(input.split("\n"));
            eventBus.post(new LinesEvent(lines));
        }
    }

    public Class<?> getEventType() { return InputEvent.class; }
}

class ShiftHandler implements EventHandler {
    private EventBus eventBus;

    public ShiftHandler(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void handle(Object event) {
        if (event instanceof LinesEvent) {
            List<String> lines = ((LinesEvent) event).lines;
            List<String> shifts = new ArrayList<>();
            for (String line : lines) {
                shifts.addAll(getAllShifts(line));
            }
            eventBus.post(new ShiftsEvent(shifts));
        }
    }

    private List<String> getAllShifts(String line) {
        List<String> shifts = new ArrayList<>();
        String[] words = line.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            StringBuilder shift = new StringBuilder();
            for (int j = 0; j < words.length; j++) {
                shift.append(words[(i + j) % words.length]).append(" ");
            }
            shifts.add(shift.toString().trim());
        }
        return shifts;
    }

    public Class<?> getEventType() { return LinesEvent.class; }
}

class SortHandler implements EventHandler {
    private EventBus eventBus;

    public SortHandler(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void handle(Object event) {
        if (event instanceof ShiftsEvent) {
            List<String> shifts = ((ShiftsEvent) event).shifts;
            Collections.sort(shifts);
            eventBus.post(new SortedShiftsEvent(shifts));
        }
    }

    public Class<?> getEventType() { return ShiftsEvent.class; }
}

class OutputHandler implements EventHandler {
    private String result;

    public void handle(Object event) {
        if (event instanceof SortedShiftsEvent) {
            List<String> sortedShifts = ((SortedShiftsEvent) event).sortedShifts;
            result = String.join("\n", sortedShifts);
        }
    }

    public String getResult() {
        return result;
    }

    public Class<?> getEventType() { return SortedShiftsEvent.class; }
}