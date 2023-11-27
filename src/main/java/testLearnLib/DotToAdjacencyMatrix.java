package testLearnLib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DotToAdjacencyMatrix {

    private static final Logger LOGGER = LogManager.getLogger();

    public DotToAdjacencyMatrix(){}

    /**
     * 将dot图转换为邻接矩阵
     *
     * @param dotGraph dot图
     * @return 邻接矩阵
     */
    public static String[][] dotToAdjacencyMatrix(String dotGraph) {
        Map<String, Integer> vertexMap = new HashMap<>();
        String[] lines = dotGraph.split("\n");

        // 解析dot图中的顶点
        int vertexCount = 0;
        for (String line : lines) {
            if (line.contains("[shape=")) {
                String vertex = line.split("\\[")[0].trim();
                vertexMap.put(vertex, vertexCount++);
            }
        }

        // 创建邻接矩阵并初始化为不可达的特殊字符串
        String[][] adjacencyMatrix = new String[vertexCount][vertexCount];
        String notReachable = "N/A";
        // 初始化邻接矩阵
        for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < vertexCount; j++) {
                adjacencyMatrix[i][j] = notReachable;
            }
        }

        // 解析dot图中的边信息
        for (String line : lines) {
            if (line.contains("->")) {
                String[] parts = line.trim().split("->");
                String sourceVertex = parts[0].trim();
                String targetVertex = parts[1].split("\\[")[0].trim();
                // s2 [label="pod[] / *"]; 提取" "中的内容
                int startIndex = parts[1].indexOf("\"");
                int endIndex = parts[1].indexOf("\"", startIndex + 1);
                if (startIndex != -1 && endIndex != -1) {
                    String label = parts[1].substring(startIndex+1, endIndex).trim();
                    int sourceIndex = vertexMap.get(sourceVertex);
                    int targetIndex = vertexMap.get(targetVertex);
                    if (adjacencyMatrix[sourceIndex][targetIndex] == "N/A"){
                        adjacencyMatrix[sourceIndex][targetIndex] = label;
                    } else {
                        adjacencyMatrix[sourceIndex][targetIndex] += ", " + label;
                    }
                }
            }
        }

        return adjacencyMatrix;
    }


    public static void main(String[] args) {
        String filePath = "src/main/java/testLearnLib/dotFile/CoffeeMachine.dot";

        String dotGraph = readDotFile(filePath);
        LOGGER.info(dotGraph);

        String[][] adjacencyMatrix = dotToAdjacencyMatrix(dotGraph);

        // 打印邻接矩阵
        printMatrix(adjacencyMatrix);

        // 遍历邻接矩阵，生成状态转换图
        Map<Integer, List<String>> transitions = traverseMatrix(adjacencyMatrix);

        printTransitionGraph(transitions);

    }

    /**
     * 读取dot文件
     *
     * @param filePath dot文件路径
     * @return dot文件内容
     */
    public static String readDotFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    /**
     * 打印邻接矩阵
     *
     * @param adjacencyMatrix 邻接矩阵
     */
    public static void printMatrix(String[][] adjacencyMatrix) {
        int size = adjacencyMatrix.length;

        // 计算每个单元格的最大宽度
        int maxCellWidth = calculateMaxCellWidth(adjacencyMatrix);

        // 打印列标
        System.out.print("          ");
        for (int i = 0; i < size; i++) {
            String label = String.format("| s%-" + maxCellWidth + "s", i);
            System.out.print(label);
        }
        System.out.println("|");

        // 打印分隔线
        System.out.print("---------+");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < maxCellWidth + 3; j++) {
                System.out.print("-");
            }
            System.out.print("+");
        }
        System.out.println();

        // 打印邻接矩阵内容
        for (int i = 0; i < size; i++) {
            System.out.printf("| s%-2d     ", i);

            for (int j = 0; j < size; j++) {
                String cell = adjacencyMatrix[i][j];
                System.out.printf("| %-" + maxCellWidth + "s ", cell);
            }

            System.out.println("|");

            // 打印分隔线
            System.out.print("---------+");
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < maxCellWidth + 3; k++) {
                    System.out.print("-");
                }
                System.out.print("+");
            }
            System.out.println();
        }
    }

    /**
     * 计算邻接矩阵中每个单元格的最大宽度
     *
     * @param matrix 邻接矩阵
     * @return 最大宽度
     */
    private static int calculateMaxCellWidth(String[][] matrix) {
        int maxWidth = 0;
        for (String[] row : matrix) {
            for (String cell : row) {
                int cellWidth = cell.length();
                if (cellWidth > maxWidth) {
                    maxWidth = cellWidth;
                }
            }
        }
        return maxWidth;
    }

    /**
     * 遍历邻接矩阵，获取状态迁移图
     *
     * @param adjacencyMatrix 邻接矩阵
     * @return 状态迁移图
     */
    public static Map<Integer, List<String>> traverseMatrix(String[][] adjacencyMatrix) {
        int size = adjacencyMatrix.length;

        // 记录已经遍历过的状态
        boolean[] visited = new boolean[size];
        visited[0] = true; // 初始状态 s0

        // 使用邻接表表示状态迁移图
        Map<Integer, List<String>> transitions = new HashMap<>();
        for (int i = 0; i < size; i++) {
            transitions.put(i, new ArrayList<>());
        }
        transitions.get(0).add("init");

        // 遍历首行，判断初始状态是否能到达其他节点
        String[] firstRow = adjacencyMatrix[0];
        for (int i = 1; i < size; i++) {
            String cell = firstRow[i];
            if (!cell.equals("N/A")) {
                transitions.get(i).add(cell.split(",")[0].trim().split("\\[")[0]); // 只取第一个字符串并去除前后空格
                visited[i] = true;
            }
        }

        // 遍历其他节点
        for (int i = 1; i < size; i++) {
            if (!visited[i]) {
                StringBuilder combinedCell = new StringBuilder();
                for (int j = 0; j < size; j++) {
                    if (visited[j] && !adjacencyMatrix[j][i].equals("N/A")) {
                        String cell = adjacencyMatrix[j][i];
                        if (!cell.equals("N/A")) {
                            if (combinedCell.length() > 0) {
                                combinedCell.append(", ");
                            }
                            combinedCell.append(cell.split(",")[0].trim().split("\\[")[0]); // 只取第一个字符串并去除前后空格
                        }
                    }
                }
                if (combinedCell.length() > 0) {
                    transitions.get(i).add(combinedCell.toString());
                    visited[i] = true;
                }
            }
        }

        return transitions;
    }

    /**
     * 打印状态迁移图
     *
     * @param transitions 状态迁移图
     */
    public static void printTransitionGraph(Map<Integer, List<String>> transitions) {
        // 输出状态迁移图
        System.out.println("\nTransition：");
        for (Map.Entry<Integer, List<String>> entry : transitions.entrySet()) {
            int state = entry.getKey();
            List<String> transitionList = entry.getValue();
            for (String transition : transitionList) {
                System.out.println("(s" + state + ", " + transition + ")");
            }
        }
    }
}