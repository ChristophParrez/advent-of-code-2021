import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class Day01 {

    public static void main(String[] args) {
        processFile("example.txt", 1);
        processFile("numbers.txt", 1);
        processFile("example.txt", 3);
        processFile("numbers.txt", 3);
    }

    private static void processFile(String fileName, int windowLength) {
        try {
            Stream<String> lines = Files.lines(Paths.get("resources/day01/" + fileName));
            List<Long> numbers = lines.filter(line -> line.matches("[0-9]+")).map(Long::parseLong).toList();

            long counter = 0;
            for (int i = 0; i < numbers.size() - windowLength; i++) {
                if (numbers.get(i + windowLength) > numbers.get(i)) counter ++;
            }

            System.out.println("Result for " + fileName + " with window of " + windowLength + ": " + counter);
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
    }

}