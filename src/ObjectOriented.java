import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ObjectOriented {
    public String process(String input) {
        return new KWICSystem(input).process();
    }
}

class KWICSystem {
    private List<String> lines;

    public KWICSystem(String input) {
        this.lines = Arrays.asList(input.split("\n"));
    }

    public String process() {
        List<String> shifts = new ArrayList<>();
        for (String line : lines) {
            shifts.addAll(new LineShifter(line).getAllShifts());
        }
        Collections.sort(shifts);
        return String.join("\n", shifts);
    }
}

class LineShifter {
    private String[] words;

    public LineShifter(String line) {
        this.words = line.split("\\s+");
    }

    public List<String> getAllShifts() {
        List<String> shifts = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            shifts.add(getShift(i));
        }
        return shifts;
    }

    private String getShift(int start) {
        StringBuilder shift = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            shift.append(words[(start + i) % words.length]).append(" ");
        }
        return shift.toString().trim();
    }
}