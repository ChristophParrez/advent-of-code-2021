import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day03 {

    static List<String> lines;
    static int fileColumns;

    public static void main(String[] args) {
        processFilePartOne("example.txt");
        processFilePartOne("input.txt");

        processFilePartTwo("example.txt");
        processFilePartTwo("input.txt");
    }

    private static void readFile(String fileName) {
        try {
            lines = Files.lines(Paths.get("resources/day03/" + fileName)).toList();
            fileColumns = lines.stream().mapToInt(String::length).min().orElseThrow();
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
    }

    private static void processFilePartOne(String fileName) {

        readFile(fileName);

        StringBuilder gammaRate = new StringBuilder();
        StringBuilder epsilonRate = new StringBuilder();

        for (int i = 0; i < fileColumns; i++) {
            int dominantBit = findDominantBitInColumn(lines, i);
            gammaRate.append(dominantBit == 0 ? "0" : "1");
            epsilonRate.append(dominantBit == 0 ? "1" : "0");
        }

        int powerConsumption = convertToDecimal(gammaRate.toString()) * convertToDecimal(epsilonRate.toString());
        System.out.println("Power Consumption : " + powerConsumption);
    }

    private static void processFilePartTwo(String fileName) {

        readFile(fileName);

        String oxyBinary = eliminateNumbersForPartTwo(false);
        int oxyDecimal = convertToDecimal(oxyBinary);
        System.out.println("Oxygen: " + oxyBinary + " (" + oxyDecimal + ")");

        String co2Binary = eliminateNumbersForPartTwo(true);
        int co2Decimal = convertToDecimal(co2Binary);
        System.out.println("CO2: " + co2Binary + " (" + co2Decimal + ")");

        System.out.println("Life Support: " + oxyDecimal * co2Decimal + "\n");
    }

    private static String eliminateNumbersForPartTwo(boolean keepLessCommonBit) {
        List<String> numbersForOxygen = new ArrayList<>(lines);
        while (numbersForOxygen.size() > 1) {
            for (int column = 0; column < fileColumns; column++) {
                if (numbersForOxygen.size() == 1) break;
                int columnPosition = column;
                int mostCommonBitInColumn = findDominantBitInColumn(numbersForOxygen, columnPosition);
                int lessCommonBitInColumn = mostCommonBitInColumn == 0 ? 1 : 0;
                int bitToKeep = keepLessCommonBit ? lessCommonBitInColumn : mostCommonBitInColumn;
                numbersForOxygen = numbersForOxygen.stream()
                        .filter(number -> String.valueOf(number.charAt(columnPosition)).equals(String.valueOf(bitToKeep)))
                        .toList();
            }
        }
        return numbersForOxygen.get(0);
    }

    private static int convertToDecimal(String stringBuilder) {
        return Integer.parseInt(stringBuilder.toString(), 2);
    }

    private static int findDominantBitInColumn(List<String> numbers, int column) {
        long zerosInColumn = numbers.stream().filter(s -> s.charAt(column) == '0').count();
        long onesInColumn = numbers.size() - zerosInColumn;
        return zerosInColumn > onesInColumn ? 0 : 1;
    }
}
