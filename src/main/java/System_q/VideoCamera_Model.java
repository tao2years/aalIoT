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

public class VideoCamera_Model {
    private static final Logger LOGGER = LogManager.getLogger();
    private static Map<String, Set<String>> stateTransitions;

    public VideoCamera_Model() {
        stateTransitions = new HashMap<>();
    }

    private static List<String> getAllPossibleAPIs() {
        List<String> apis = new ArrayList<>();
        apis.add("turnOn");
        apis.add("turnOff");
        apis.add("turnOnMotionRecord");
        apis.add("turnOffMotionRecord");
        apis.add("turnOnLight");
        apis.add("turnOffLight");
        apis.add("turnOnFullColor");
        apis.add("turnOffFullColor");
        apis.add("turnOnFlip");
        apis.add("turnOffFlip");
        apis.add("turnOnImproveProgram");
        apis.add("turnOffImproveProgram");
        apis.add("turnOnWdr");
        apis.add("turnOffWdr");
        apis.add("turnOnTrack");
        apis.add("turnOffTrack");
        apis.add("turnOffWatermark");
        apis.add("setMaxClient");
        apis.add("setNightMode");
        apis.add("setMiniLevel");
        return apis;
    }

    private static String executeApi(String api, VideoCamera videoCamera){
        switch (api) {
            case "turnOn":
                return videoCamera.turnOn().equals("success") ? videoCamera.toString() : "skip";
            case "turnOff":
                return videoCamera.turnOff().equals("success") ? videoCamera.toString() : "skip";
            case "turnOnMotionRecord":
                return videoCamera.turnOnMotionRecord().equals("success") ? videoCamera.toString() : "skip";
            case "turnOffMotionRecord":
                return videoCamera.turnOffMotionRecord().equals("success") ? videoCamera.toString() : "skip";
            case "turnOnLight":
                return videoCamera.turnOnLight().equals("success") ? videoCamera.toString() : "skip";
            case "turnOffLight":
                return videoCamera.turnOffLight().equals("success") ? videoCamera.toString() : "skip";
            case "turnOnFullColor":
                return videoCamera.turnOnFullColor().equals("success") ? videoCamera.toString() : "skip";
            case "turnOffFullColor":
                return videoCamera.turnOffFullColor().equals("success") ? videoCamera.toString() : "skip";
            case "turnOnFlip":
                return videoCamera.turnOnFlip().equals("success") ? videoCamera.toString() : "skip";
            case "turnOffFlip":
                return videoCamera.turnOffFlip().equals("success") ? videoCamera.toString() : "skip";
            case "turnOnImproveProgram":
                return videoCamera.turnOnImproveProgram().equals("success") ? videoCamera.toString() : "skip";
            case "turnOffImproveProgram":
                return videoCamera.turnOffImproveProgram().equals("success") ? videoCamera.toString() : "skip";
            case "turnOnWdr":
                return videoCamera.turnOnWdr().equals("success") ? videoCamera.toString() : "skip";
            case "turnOffWdr":
                return videoCamera.turnOffWdr().equals("success") ? videoCamera.toString() : "skip";
            case "turnOnTrack":
                return videoCamera.turnOnTrack().equals("success") ? videoCamera.toString() : "skip";
            case "turnOffTrack":
                return videoCamera.turnOffTrack().equals("success") ? videoCamera.toString() : "skip";
            case "turnOffWatermark":
                return videoCamera.turnOffWatermark().equals("success") ? videoCamera.toString() : "skip";
            case "setMaxClient":
                return videoCamera.setMaxClient(2).equals("success") ? videoCamera.toString() : "skip";
            case "setNightMode":
                return videoCamera.setNightMode(2).equals("success") ? videoCamera.toString() : "skip";
            case "setMiniLevel":
                return videoCamera.setMiniLevel(2).equals("success") ? videoCamera.toString() : "skip";
            default:
                return videoCamera.toString();
        }
    }

    private static void generateGraph(Set<Edge> edges){
        VideoCamera device = new VideoCamera();
        String initState = device.toString();
        String initSystemState = device.toSystemString();
        stateTransitions.put(initSystemState, new HashSet<>());

//        LOGGER.info("Initial state: " + initState + " System state: " + initSystemState);
        exploreState(initState, initSystemState, edges);
    }

    private static void exploreState(String state, String systemState, Set<Edge> edges){
        List<String> apis = getAllPossibleAPIs();

        for (String api : apis) {
            VideoCamera currentDevice = VideoCamera.fromString(state);
            String nextState = executeApi(api, currentDevice);
            if (!nextState.equals("skip")){
                VideoCamera nextWM = VideoCamera.fromString(nextState);
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
            VideoCamera_Model test = new VideoCamera_Model();
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


        String dotFile = "src/main/java/System_q/videoCamera_output.dot";
        generateDotFile(dotSet, nodeMap.size(), dotFile);

        LOGGER.info("=========== Graph Show ==============");
        Collection<graph.Edge> edge = userGraph.getEdges();
//        edge.forEach(temp->{
//            LOGGER.info(temp);
//        });
        LOGGER.info(edge.size());
        // save userGraph to file
        String fileName = "src/main/java/System_q/model/videoCamera.json";
        saveEdgesToFile(edge, fileName);

    }


}
