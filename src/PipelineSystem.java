import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PipelineSystem {
    public String process(String input) {
        return new Pipeline<String, String>()
                .addFilter(new InputFilter())
                .addFilter(new ShiftFilter())
                .addFilter(new SortFilter())
                .addFilter(new OutputFilter())
                .process(input);
    }
}

class Pipeline<I, O> {
    private List<Filter<?, ?>> filters = new ArrayList<>();

    public <NewO> Pipeline<I, NewO> addFilter(Filter<O, NewO> filter) {
        filters.add(filter);
        return (Pipeline<I, NewO>) this;
    }

    public O process(I input) {
        Object current = input;
        for (Filter filter : filters) {
            current = filter.process(current);
        }
        return (O) current;
    }
}

interface Filter<I, O> {
    O process(I input);
}

class InputFilter implements Filter<String, List<String>> {
    public List<String> process(String input) {
        return Arrays.asList(input.split("\n"));
    }
}

class ShiftFilter implements Filter<List<String>, List<String>> {
    public List<String> process(List<String> lines) {
        List<String> shifts = new ArrayList<>();
        for (String line : lines) {
            shifts.addAll(getAllShifts(line));
        }
        return shifts;
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
}

class SortFilter implements Filter<List<String>, List<String>> {
    public List<String> process(List<String> shifts) {
        Collections.sort(shifts);
        return shifts;
    }
}

class OutputFilter implements Filter<List<String>, String> {
    public String process(List<String> sortedShifts) {
        return String.join("\n", sortedShifts);
    }
}