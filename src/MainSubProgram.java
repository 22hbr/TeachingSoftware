import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainSubProgram {
    public static String process(String input) {
        String[] lines = input.split("\n");
        List<String> shifts = new ArrayList<>();

        for (String line : lines) {
            shifts.addAll(processLine(line));
        }

        Collections.sort(shifts);
        return String.join("\n", shifts);
    }

    private static List<String> processLine(String line) {
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