package System_q;

import com.google.gson.Gson;
import component.Edge;
import graph.Graph;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import System.Node;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Gateway_model {
    private static final Logger LOGGER = LogManager.getLogger();
    private static Map<String, Set<String>> stateTransitions;

    public Gateway_model() {
        stateTransitions = new HashMap<>();
    }

    private static List<String> getAllPossibleAPIs() {
        List<String> apis = new ArrayList<>();
        apis.add("turnLightOn");
        apis.add("turnLightOff");
        apis.add("setLightBrightness");
        apis.add("turnAlarmOn");
        apis.add("turnAlarmOff");
        apis.add("addDevice");
        apis.add("removeDevice");
        return apis;
    }

    private static String executeApi(String api, Gateway gateway) {
        switch (api) {
            case "turnLightOn":
                return gateway.turnLightOn().equals("success") ? gateway.toString() : "skip";
            case "turnLightOff":
                return gateway.turnLightOff().equals("success") ? gateway.toString() : "skip";
            case "setLightBrightness":
                return gateway.setLightBrightness(50).equals("success") ? gateway.toString() : "skip";
            case "turnAlarmOn":
                return gateway.turnAlarmOn().equals("success") ? gateway.toString() : "skip";
            case "turnAlarmOff":
                return gateway.turnAlarmOff().equals("success") ? gateway.toString() : "skip";
            case "addDevice":
                return gateway.addDevice("device").equals("success") ? gateway.toString() : "skip";
            case "removeDevice":
                return gateway.removeDevice("device").equals("success") ? gateway.toString() : "skip";
            default:
                return gateway.toString();
        }
    }

    private static void generateGraph(Set<Edge> edges){
        Gateway device = new Gateway();
        String initState = device.toString();
        String initSystemState = device.toSystemString();
        stateTransitions.put(initSystemState, new HashSet<>());

//        LOGGER.info("Initial state: " + initState + " System state: " + initSystemState);
        exploreState(initState, initSystemState, edges);
    }

    private static void exploreState(String state, String systemState, Set<Edge> edges){
        List<String> apis = getAllPossibleAPIs();

        for (String api : apis) {
            Gateway currentDevice = Gateway.fromString(state);
            String nextState = executeApi(api, currentDevice);
            if (!nextState.equals("skip")){
                Gateway nextWM = Gateway.fromString(nextState);
                String nextSystemState = nextWM.toSystemString();
                Edge edge = new Edge(systemState, nextSystemState, api);
                edges.add(edge);

                if (!stateTransitions.containsKey(nextSystemState)) {
                    stateTransitions.put(nextSystemState, new HashSet<>());
                    exploreState(nextWM.toString(), nextSystemState, edges);
                }

                stateTransitions.get(systemState).add(api);

            }
        }
    }

    public static void generateDotFile (Set<Edge> dotSet, int numNodes, String filePath) {
        StringBuilder dotContent = new StringBuilder();
        dotContent.append("digraph g {\n\n");

//        boolean flag = false;
//
//        int temp = 0;
//        // 生成节点
//        for (int i = 0; i < numNodes; i++) {
//            dotContent.append("\ts")
//                    .append(i)
//                    .append(" [shape=\"circle\" label=\"")
//                    .append(i)
//                    .append("\"];\n");
//            temp++;
//        }
//
//        LOGGER.info(temp);
//
//        if (temp == numNodes){
//            flag = true;
//        }

//        if (flag) {
        for (Edge edge : dotSet) {
            dotContent.append("\t")
                    .append(edge.getSource())
                    .append(" -> ")
                    .append(edge.getTarget())
                    .append(" [label=\"")
                    .append(edge.getApi())
                    .append("\"];\n");
        }
//        }

//        dotContent.append("\n\t__start0 [label=\"\" shape=\"none\" width=\"0\" height=\"0\"];\n");
//        dotContent.append("\t__start0 -> s0;\n\n");

        dotContent.append("\n}\n");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(dotContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveEdgesToFile(Collection<graph.Edge> edges, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            new Gson().toJson(edges, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime(); // 获取开始时间
        Graph<Object, Object> userGraph = new Graph<>(true,true,false);
        Set<Edge> edges = new HashSet<>();
        int[] sizeHistory = new int[20];
        int historyIndex = 0;
        Set<String> nodesSet = new TreeSet<>();
        Set<String> nodeOrderSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        // 创建节点编号Map
        Map<String, String> nodeNumbers = new HashMap<>();
        int sumEdgeLast;

        // 最终Edges集合
        Set<Edge> final_Edges = new HashSet<>();

        int iteration = 0;
        for (int i = 0; i < 2; i++) {
            LOGGER.info("Iteration: " + iteration++);
            Set<Edge> _temp = new HashSet<>();
            Gateway_model test = new Gateway_model();
            test.generateGraph(_temp);
            edges.addAll(_temp);
        }

        for (Edge edge: edges){
            String source = edge.getSource();
            String target = edge.getTarget();
            nodesSet.add(source);
            nodesSet.add(target);
        }

        nodeOrderSet.addAll(nodesSet);

        // 统计节点数量
        int nodeCount = 0;
        for (String node : nodeOrderSet) {
            String nodeNumber = "S" + nodeCount;
            nodeNumbers.put(node, nodeNumber);
            nodeCount++;
        }

        // 输出所有节点的出边和API
        int sumEdge = 0;
        for (String node : nodeNumbers.keySet()) {
            String nodeNumber = nodeNumbers.get(node);
//                LOGGER.info("Node: " + nodeNumber);
//                LOGGER.info("State: " + node);
//
//                LOGGER.info("OutEdges:");
            Set<String> visitedApis = new HashSet<>();
            int temp = 0;
            for (Edge edge : edges) {
                if (edge.getSource().equals(node)) {
                    String target = edge.getTarget();
                    String api = edge.getApi();
                    String targetAndApi = target + " " + api;
                    if (!visitedApis.contains(targetAndApi)) {
//                            LOGGER.info("- " + nodeNumbers.get(target) + ", API = \"" + api + "\" + " + " " + target);
                        visitedApis.add(targetAndApi);
                        temp++;
                    }
                }
            }
//                System.out.println();
            sumEdge += temp;
        }

        // 输出节点总数
        LOGGER.info("Node Count: " + nodeCount);
        LOGGER.info("Sum Edge: " + sumEdge);

        sizeHistory[historyIndex] = sumEdge;
        historyIndex = (historyIndex + 1) % 20;
        LOGGER.info("[History]: " + Arrays.toString(sizeHistory));
        LOGGER.info("------------------------");
//            LOGGER.info("[Current]: " + edges.size());

        LOGGER.info("\n\n\n\n------------------------");
        LOGGER.info("[Finished generating graph]");
        LOGGER.info("[Total]: " + edges.size());

        // Construct Dot Graph
        Map<String, String> nodeMap = new HashMap<>();
        Set<String> _nodesSet = new TreeSet<>();
        Set<String> _nodeOrderSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        Set<Edge> dotSet = new HashSet<>();
        for (Edge edge : edges){
            String source = edge.getSource();
            String target = edge.getTarget();
            _nodesSet.add(source);
            _nodesSet.add(target);
        }

        _nodeOrderSet.addAll(nodesSet);

        // 统计节点数量
        int _nodeCount = 0;
        for (String node : _nodeOrderSet) {
            String nodeNumber = "S" + _nodeCount;
            nodeMap.put(node, nodeNumber);
            _nodeCount++;
        }

        for (String node : nodeMap.keySet()) {
            String nodeNumber = nodeMap.get(node);
            LOGGER.info("Node: " + nodeNumber);
            LOGGER.info("State: " + node);

            LOGGER.info("OutEdges:");
            Set<String> visitedApis = new HashSet<>();
            for (Edge edge : edges) {
                if (edge.getSource().equals(node)) {
                    String target = edge.getTarget();
                    String api = edge.getApi();
                    String targetAndApi = target + " " + api;
                    if (!visitedApis.contains(targetAndApi)) {
                        LOGGER.info("- " + nodeMap.get(target) + ", API = \"" + api + "\" + " + " " + target);
                        userGraph.setEdge(new Node(nodeNumber, node).toString(), new Node(nodeMap.get(target), target).toString(), api, api);
                        dotSet.add(new Edge(nodeNumber, nodeMap.get(target), api));
                        visitedApis.add(targetAndApi);
                    }
                }
            }
            System.out.println();
        }

        long endTime = System.nanoTime(); // 获取结束时间
        long elapsedTime = endTime - startTime; // 计算运行时间（纳秒）

        double seconds = (double) elapsedTime / 1000000000.0; // 转换为秒

        LOGGER.info("Program ran for " + seconds + " seconds.");

//        for (Edge edge : dotSet){
//            LOGGER.info(edge);
//        }

        LOGGER.info("Node Count: " + nodeCount + " Edge Count: " + dotSet.size());


        String dotFile = "src/main/java/System_q/gateway_output.dot";
        generateDotFile(dotSet, nodeMap.size(), dotFile);

        LOGGER.info("=========== Graph Show ==============");
        Collection<graph.Edge> edge = userGraph.getEdges();
//        edge.forEach(temp->{
//            LOGGER.info(temp);
//        });
        LOGGER.info(edge.size());
        // save userGraph to file
        String fileName = "src/main/java/System_q/model/Gateway.json";
        saveEdgesToFile(edge, fileName);

    }

}
