import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day04 {

    public static void main(String[] args) {
        processFilePartOne("example.txt");
        processFilePartOne("input.txt");
    }

    private static List<String> readFile(String fileName) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.lines(Paths.get("resources/day04/" + fileName)).collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
        return lines;
    }

    private static void processFilePartOne(String fileName) {
        List<String> fileLines = readFile(fileName);

        int[] numbersToDraw = Stream.of(fileLines.get(0).split(",")).mapToInt(value -> Integer.parseInt(value)).toArray();

        fileLines.remove(0);
        fileLines = fileLines.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());

        List<Board> boards = new ArrayList<>();
        for (int i = 0; i < fileLines.size(); i += 5) {
            boards.add(new Board());
            for (int row = 0; row < 5; row++) {
                String[] numbersInLine = fileLines.get((boards.size() - 1) * 5 + row).trim().split("\\s+");
                for (int col = 0; col < 5; col++) {
                    boards.get(boards.size() - 1).setNumber(row, col, Integer.parseInt(numbersInLine[col]));
                }
            }
        }

        List<Board> winners = new ArrayList<>();
        List<Integer> drawnNumbers = new ArrayList<>();
        for (Integer numberDrawn : numbersToDraw) {
            drawnNumbers.add(numberDrawn);
            for (Board board : boards) {
                if (winners.contains(board)) continue;
                if (winners.size() == boards.size()) break;
                if (board.isWinner(drawnNumbers)) {
                    board.setWinningNumber(numberDrawn);
                    board.setSumOfAllUnmarkedNumbers(drawnNumbers);
                    winners.add(new Board(board));
                }
            }
        }

        if (winners.size() == 0) return;
        System.out.println("The first winner is board: \n\n" + winners.get(0));
        System.out.println("The last winner is board: \n\n" + winners.get(winners.size() - 1));
    }
}

class Board {

    static int boardCounter = 0;
    int boardId;
    List<BoardItem> boardItems;
    Integer winningNumber;
    long sumOfAllUnmarkedNumbers;

    public Board() {
        boardCounter++;
        this.boardId = boardCounter;
        this.boardItems = new ArrayList<>();
        this.winningNumber = null;
        this.sumOfAllUnmarkedNumbers = 0;
    }

    public Board(Board board) {
        this.boardId = board.boardId;
        this.boardItems = board.boardItems;
        this.winningNumber = board.winningNumber;
        this.sumOfAllUnmarkedNumbers = board.sumOfAllUnmarkedNumbers;
    }

    public void setNumber(int row, int col, int val) {
        this.boardItems.add(new BoardItem(row, col, val));
    }

    public boolean isWinner(List<Integer> drawnNumbers) {
        for (int row = 0; row < 5; row++) {
            int finalRow = row;
            List<Integer> numbersInRow = boardItems.stream().filter(boardItem -> boardItem.row == finalRow).map(boardItem -> boardItem.value).toList();
            long matchesInRow = numbersInRow.stream().filter(s -> drawnNumbers.stream().anyMatch(drawn -> drawn.equals(s))).count();
            if (matchesInRow == 5) return true;

        }
        for (int col = 0; col < 5; col++) {
            int finalCol = col;
            List<Integer> numbersInCol = boardItems.stream().filter(boardItem -> boardItem.col == finalCol).map(boardItem -> boardItem.value).toList();
            long matchesInRow = numbersInCol.stream().filter(s -> drawnNumbers.stream().anyMatch(drawn -> drawn.equals(s))).count();
            if (matchesInRow == 5) return true;
        }
        return false;
    }

    public void setWinningNumber(Integer winningNumber) {
        this.winningNumber = Integer.valueOf(winningNumber);
    }

    public void setSumOfAllUnmarkedNumbers(List<Integer> drawnNumbers) {
        this.sumOfAllUnmarkedNumbers = this.boardItems.stream()
                // .flatMap(Arrays::stream)
                .filter(number -> !drawnNumbers.contains(number.value))
                .mapToInt(number -> number.value)
                .sum();
    }

    @Override
    public String toString() {
        String matrix = "**** BOARD " + boardId + " ****\n";
        if (this.winningNumber != null) {
            matrix += "\nSum of all unmarked numbers: " + sumOfAllUnmarkedNumbers + " | Final number: " + this.winningNumber + " | Result: " + this.sumOfAllUnmarkedNumbers * this.winningNumber + "\n";
        }
        return matrix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return boardId == board.boardId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardId);
    }
}

class BoardItem {
    int row;
    int col;
    int value;

    public BoardItem(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }
}