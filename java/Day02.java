import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Day02 {
    public static void main(String[] args) {
        processFile("example.txt", 1);
        processFile("input.txt", 1);

        processFile("example.txt", 2);
        processFile("input.txt", 2);
    }

    private static void processFile(String fileName, int part) {
        try {
            List<String> lines = Files.lines(Paths.get("resources/day02/" + fileName)).toList();

            int xPos = 0;
            int yPos = 0;
            int aim = 0;

            for (String line : lines) {
                int value = Integer.parseInt(line.replaceAll("[^0-9.]+", ""));
                if (part == 1) {
                    if (line.contains("up")) yPos-=value;
                    else if (line.contains("down")) yPos+=value;
                    else if (line.contains("forward")) xPos+=value;
                } else if (part == 2) {
                    if (line.contains("up")) aim -= value;
                    else if (line.contains("down")) aim += value;
                    else if (line.contains("forward")) {
                        xPos += value;
                        yPos += aim * value;
                    }
                }
            }

            System.out.println("PART " + part + " " + xPos + " * " + yPos + " = " + xPos * yPos);

        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
    }
}
