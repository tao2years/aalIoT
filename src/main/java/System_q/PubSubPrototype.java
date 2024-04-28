package System_q;

import graph.Edge;
import graph.Graph;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import System.Node;

import System.CoffeeMachine_V1;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PubSubPrototype {
    private static final int WINDOW_SIZE = 1000; // Window size in milliseconds
    private static BlockingQueue<String> queue = new LinkedBlockingQueue<>(); // Queue for storing messages
    private static final Logger LOGGER = LogManager.getLogger();

    // Publish message to the queue
    public static void publish(String message) {
        try {
            queue.put(message);
            System.out.println("Published: " + message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Subscribe and execute messages immediately
    public static void subscribeAndExecute() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter messages to publish (enter 'q' to quit):");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("q")) {
                break;
            }
            publish(input);
        }
    }

    public static int generateRandomNumber(int min, int max) {
        // 创建Random对象
        Random random = new Random();

        // 生成指定范围内的随机数
        int randomNumber = random.nextInt(max - min + 1) + min;

        return randomNumber;
    }

    // Execute messages in the queue within the specified window time
    public static void executeWindowMessages() {
        long windowStartTime = System.currentTimeMillis();
        StringBuilder windowMessages = new StringBuilder();

        // 从文件中加载图
        String fileName = "src/main/java/System/DeviceModel/coffeeMachine.json";
        Graph<Object, Object> graph = Util.loadGraphFromFile(fileName);
        CoffeeMachine_V1 coffeeMachine_v1 = new CoffeeMachine_V1();
        AtomicReference<String> preState = new AtomicReference<>(Util.getCurrentStateFromDeviceSystemState(coffeeMachine_v1.toSystemStateString(), graph));

        LOGGER.info("The init state is: " + preState);

        Collection<Edge> edges = graph.getEdges();
        for (Edge edge : edges){
            String source = edge.getSource();
        }


        Set<List<String>> paths =findAllPaths(edges, "S0", "S1");

        // 遍历并打印每条路径
        for (List<String> path : paths) {
            System.out.println(path);
        }

        while (true) {
            try {
                String message = queue.take();
                windowMessages.append(message).append("\n");

                long currentTime = System.currentTimeMillis();
                // If window time has elapsed, execute messages in the window and reset the window
                if (currentTime - windowStartTime > WINDOW_SIZE) {
                    System.out.println("Window Messages:");
                    System.out.println(windowMessages.toString());

                    // Handler msg
                    for (String ori_cmd : windowMessages.toString().split(",")){
                        String cmd = ori_cmd.trim();
                        // Compare digital state and physical state
                        String physical_state = Util.getCurrentStateFromDeviceSystemState(coffeeMachine_v1.toSystemStateString(), graph);
                        double randomNumber = Math.random();
                        // 如果随机数大于0.8，则扰动执行结果
                        if (randomNumber > 0.8) {
                            LOGGER.info("[Error] Disturb the physical state");
                            int state = generateRandomNumber(0, 24);
                            physical_state = "S" + state;
                        }

                        if (preState.get().equals(physical_state)){
                            LOGGER.info("The physical state is the same as the digital state");
                            preState.set(Util.processMessage(preState.get(), cmd, graph, coffeeMachine_v1));
                            LOGGER.info("The current state is: " + preState.get());
                        }else{
                            // Inconsistent state, calculate the actions needed to be done
                            LOGGER.info("The physical state is different from the digital state");
                            Set<List<String>> _paths =findAllPaths(edges, physical_state, preState.get());
                            LOGGER.info("The paths are: " + _paths);
                        }
                    }


                    // Delete executed cmd and update time
                    windowMessages = new StringBuilder();
                    windowStartTime = currentTime;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // 找到所有从nodeA到nodeB的路径
    public static Set<List<String>> findAllPaths(Collection<Edge> edges, String nodeA, String nodeB) {
        // 构建图的邻接表
        Map<String, List<Edge>> graph = new HashMap<>();
        for (Edge edge : edges) {
            String source = edge.getSource();
            String nodeId = Node.fromString(source).getNodeId();
            graph.computeIfAbsent(nodeId, k -> new ArrayList<>()).add(edge);
        }

        // 使用DFS找到所有路径
//        List<List<String>> paths = new ArrayList<>();
        Set<List<String>> paths = new HashSet<>();
        findPathsDFS(graph, nodeA, nodeB, new ArrayList<>(), paths, new HashSet<>(), 0);
        return paths;
    }

    private static void findPathsDFS(Map<String, List<Edge>> graph, String current, String end, List<String> currentPath, Set<List<String>> paths, Set<String> visited, int depth) {
        if (depth > 3) { // 确保路径长度小于等于3
            return;
        }
        visited.add(current);

        if (current.equals(end)) {
            paths.add(new ArrayList<>(currentPath));
            visited.remove(current); // 回溯
            return;
        }

        List<Edge> edges = graph.get(current);
        if (edges != null) {
            for (Edge edge : edges) {
                String nextNodeId = getNodeIdFromNode(edge.getTarget()); // 假设getTarget返回Node对象
                if (!visited.contains(nextNodeId)) {
                    currentPath.add(edge.getName()); // 添加边的名字到当前路径
                    findPathsDFS(graph, nextNodeId, end, currentPath, paths, visited, depth + 1);
                    currentPath.remove(currentPath.size() - 1); // 回溯移除边的名字
                }
            }
        }

        visited.remove(current); // 回溯
    }

    private static void findPathsDFS(Map<String, List<Edge>> graph, String current, String end, List<String> currentPath, List<List<String>> paths, List<String> visited, Map<String, String> edgeNames) {
        visited.add(current);

        if (current.equals(end)) {
            paths.add(new ArrayList<>(currentPath));
            visited.remove(current); // 回溯
            return;
        }

        List<Edge> edges = graph.get(current);
        if (edges != null) {
            for (Edge edge : edges) {

                if (!visited.contains(getNodeIdFromNode(edge.getTarget()))) {
                    currentPath.add(edge.getName()); // 添加边的名字到当前路径
                    findPathsDFS(graph, getNodeIdFromNode(edge.getTarget()), end, currentPath, paths, visited, edgeNames);
                    currentPath.remove(currentPath.size() - 1); // 回溯移除边的名字
                }
            }
        }

        visited.remove(current); // 回溯
    }

    public static String getNodeIdFromNode(String content){
        return Node.fromString(content).getNodeId();
    }


    public static void main(String[] args) {
        // Start subscriber thread
        Thread subscriberThread = new Thread(PubSubPrototype::subscribeAndExecute);
        subscriberThread.start();

        // Start window messages execution thread
        Thread windowMessagesThread = new Thread(PubSubPrototype::executeWindowMessages);
        windowMessagesThread.start();
    }
}
