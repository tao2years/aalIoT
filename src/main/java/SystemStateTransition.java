import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemStateTransition {

    public static String findFinalState(String[][] adjacencyMatrix, List<String> cmdList) {
        Map<Integer, Integer> stateMap = generateStateMap(adjacencyMatrix);
        int currentState = 0; // 初始状态为0

        for (String cmd : cmdList) {
            int row = currentState;
            for (int col = 0; col < adjacencyMatrix[row].length; col++) {
                String[] actions = adjacencyMatrix[row][col].split(", ");
                for (String action : actions) {
                    if (action.startsWith(cmd)) {
                        currentState = col;
                        break;
                    }
                }
            }
        }

        return String.valueOf(currentState);
    }

    private static Map<Integer, Integer> generateStateMap(String[][] adjacencyMatrix) {
        Map<Integer, Integer> stateMap = new HashMap<>();
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            stateMap.put(i, i);
        }
        return stateMap;
    }

    private static List<String> calculateSupplyCmdList(String currentSystemState, String targetSystemState, String[][] adjacencyMatrix) {
        List<String> supplyCmdList = new ArrayList<>();
        int currentRow = Integer.parseInt(currentSystemState);
        int targetRow = Integer.parseInt(targetSystemState);

        while (currentRow != targetRow) {
            for (int col = 0; col < adjacencyMatrix[currentRow].length; col++) {
                if (col != currentRow) {
                    String[] actions = adjacencyMatrix[currentRow][col].split(", ");
                    for (String action : actions) {
                        if (!action.equals("N/A")) {
                            supplyCmdList.add(action.split(" / ")[0].replace("[]", ""));
                            currentRow = col;
                            break;
                        }
                    }
                }
            }
        }

        return supplyCmdList;
    }

    public static void main(String[] args) {
        String[][] adjacencyMatrix = {
                {"button[] / Error, clean[] / *", "pod[] / *", "N/A", "water[] / *"},
                {"clean[] / *", "pod[] / *, button[] / Error", "water[] / *", "N/A"},
                {"clean[] / *", "N/A", "water[] / *, pod[] / *, button[] / OK", "N/A"},
                {"clean[] / *", "N/A", "pod[] / *", "water[] / *, button[] / Error"}
        };

        String initialSystemState = "0";
        String targetSystemState = "2";

        List<String> supplyCmdList = calculateSupplyCmdList(initialSystemState, targetSystemState, adjacencyMatrix);
        System.out.println("Initial System State: " + initialSystemState);
        System.out.println("Target System State: " + targetSystemState);
        System.out.println("Supply Command List: " + supplyCmdList);
    }
}