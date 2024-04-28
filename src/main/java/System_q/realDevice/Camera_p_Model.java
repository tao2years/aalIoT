package System_q.realDevice;

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

public class Camera_p_Model {
    private static final Logger LOGGER = LogManager.getLogger();
    private static Map<String, Set<String>> stateTransitions;

    public Camera_p_Model () {
        stateTransitions = new HashMap<>();
    }

    private static List<String> getAllPossibleAPIs() {
        List<String> apis = new ArrayList<>();
        apis.add("on");
        apis.add("off");
        apis.add("motion_record_on");
        apis.add("motion_record_off");
        apis.add("motion_record_stop");
        apis.add("light_on");
        apis.add("light_off");
        apis.add("full_color_on");
        apis.add("full_color_off");
        apis.add("flip_on");
        apis.add("flip_off");
        apis.add("improve_program_on");
        apis.add("improve_program_off");
        apis.add("wdr_on");
        apis.add("wdr_off");
        apis.add("watermark_on");
        apis.add("watermark_off");
        apis.add("night_mode_on");
        apis.add("night_mode_off");
        apis.add("night_mode_auto");
        apis.add("reset");
        return apis;
    }

    private static String executeApi(String api, ProcessBuilder pb) throws IOException, InterruptedException {
        switch (api) {
            case "on":
                return Yeelight_p.executePythonScript(pb, "src/main/python/Camera/on.py");
            case "off":
                return Yeelight_p.executePythonScript(pb, "src/main/python/Camera/off.py");
            case "motion_record_on":
                return Yeelight_p.executePythonScript(pb, "src/main/python/Camera/motion_record_on.py");
            case "motion_record_off":
                return Yeelight_p.executePythonScript(pb, "src/main/python/Camera/motion_record_off.py");
            case "motion_record_stop":
                return Yeelight_p.executePythonScript(pb, "src/main/python/Camera/motion_record_stop.py");
            case "light_on":
                return Yeelight_p.executePythonScript(pb, "src/main/python/Camera/light_on.py");
            case "light_off":
                return Yeelight_p.executePythonScript(pb, "src/main/python/Camera/light_off.py");
            case "full_color_on":
                return Yeelight_p.executePythonScript(pb, "src/main/python/Camera/full_color_on.py");
            case "full_color_off":
                return Yeelight_p.executePythonScript(pb, "src/main/python/Camera/full_color_off.py");
            case "flip_on":
                return Yeelight_p.executePythonScript(pb, "src/main/python/Camera/flip_on.py");
            case "flip_off":
                return Yeelight_p.executePythonScript(pb, "src/main/python/Camera/flip_off.py");
            case "improve_program_on":
                return Yeelight_p.executePythonScript(pb, "src/main/python/Camera/improve_program_on.py");
            case "improve_program_off":
                return Yeelight_p.executePythonScript(pb, "src/main/python/Camera/improve_program_off.py");
            case "wdr_on":
                return Yeelight_p.executePythonScript(pb, "src/main/python/Camera/wdr_on.py");
            case "wdr_off":
                return Yeelight_p.executePythonScript(pb, "src/main/python/Camera/wdr_off.py");
            case "watermark_on":
                return Yeelight_p.executePythonScript(pb, "src/main/python/Camera/watermark_on.py");
            case "watermark_off":
                return Yeelight_p.executePythonScript(pb, "src/main/python/Camera/watermark_off.py");
            case "night_mode_on":
                return Yeelight_p.executePythonScript(pb, "src/main/python/Camera/night_mode_on.py");
            case "night_mode_off":
                return Yeelight_p.executePythonScript(pb, "src/main/python/Camera/night_mode_off.py");
            case "night_mode_auto":
                return Yeelight_p.executePythonScript(pb, "src/main/python/Camera/night_mode_auto.py");
            case "reset":
                return Yeelight_p.executePythonScript(pb, "src/main/python/Camera/reset.py");
            default:
                return "Error";
        }
    }

    private static String resetToCurrentState(CameraStatus currentStatus, String api, ProcessBuilder pb) throws IOException, InterruptedException {
        executeApi("reset", pb);
        LOGGER.info("Reset to -> " + currentStatus);
        executeApi("on", pb);
        String motion_record = currentStatus.getMotion_record();
        if (motion_record.equals("on")) {
            executeApi("motion_record_on", pb);
        } else if (motion_record.equals("off")) {
            executeApi("motion_record_off", pb);
        }else if (motion_record.equals("stop")) {
            executeApi("motion_record_stop", pb);
        }
        String light = currentStatus.getLight();
        if (light.equals("on")) {
            executeApi("light_on", pb);
        } else if (light.equals("off")) {
            executeApi("light_off", pb);
        }
        String full_color = currentStatus.getFull_color();
        if (full_color.equals("on")) {
            executeApi("full_color_on", pb);
        } else if (full_color.equals("off")) {
            executeApi("full_color_off", pb);
        }
        String flip = currentStatus.getFlip();
        if (flip.equals("on")) {
            executeApi("flip_on", pb);
        } else if (flip.equals("off")) {
            executeApi("flip_off", pb);
        }
        String improve_program = currentStatus.getImprove_program();
        if (improve_program.equals("on")) {
            executeApi("improve_program_on", pb);
        } else if (improve_program.equals("off")) {
            executeApi("improve_program_off", pb);
        }
        String wdr = currentStatus.getWdr();
        if (wdr.equals("on")) {
            executeApi("wdr_on", pb);
        } else if (wdr.equals("off")) {
            executeApi("wdr_off", pb);
        }
        String watermark = currentStatus.getWatermark();
        if (watermark.equals("on")) {
            executeApi("watermark_on", pb);
        } else if (watermark.equals("off")) {
            executeApi("watermark_off", pb);
        }
        int night_mode = currentStatus.getNight_mode();
        if (night_mode == 2) {
            executeApi("night_mode_on", pb);
        } else if (night_mode == 1) {
            executeApi("night_mode_off", pb);
        } else if (night_mode == 0) {
            executeApi("night_mode_auto", pb);
        }
        String power = currentStatus.getPower();
        if (power.equals("off")) {
            executeApi("off", pb);
        }
        return executeApi(api, pb);
    }

    private static void generateGraph(Set<Edge> edges, ProcessBuilder pb) throws IOException, InterruptedException {
        CameraStatus device = CameraStatus.parse(executeApi("reset", pb));
        String initState = device.toString();
        String initSystemState = device.toSystemString();

        if (stateTransitions == null) {
            LOGGER.error("Error: stateTransitions is null.");
            return;
        }
        stateTransitions.put(initSystemState, new HashSet<>());
        exploreState(initState, initSystemState, edges, pb);
    }

    private static void exploreState(String state, String systemState, Set<Edge> edges, ProcessBuilder pb) throws IOException, InterruptedException {
        List<String> apis = getAllPossibleAPIs();
//        YeelightStatus illegalState = new YeelightStatus(-1, false, -1,-1,-1);
        for (String api : apis) {
            CameraStatus currentState = CameraStatus.fromString(state);
            CameraStatus nextState = CameraStatus.parse(resetToCurrentState(currentState, api, pb));
            if (!(nextState == null)){
                LOGGER.info("Current state: " + currentState.toString() + ", API: " + api + ", Next state: " + nextState.toString());
                String nextSystemState = nextState.toSystemString();
                Edge edge = new Edge(systemState, nextSystemState, api);
                edges.add(edge);

                if (!stateTransitions.containsKey(nextSystemState)) {
                    stateTransitions.put(nextSystemState, new HashSet<>());
                    exploreState(nextState.toString(), nextSystemState, edges, pb);
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

    public static void main(String[] args) throws IOException, InterruptedException {
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

        ProcessBuilder pb = new ProcessBuilder();
        pb.redirectErrorStream(true);

        int iteration = 0;
        for (int i = 0; i < 1; i++) {
            LOGGER.info("Iteration: " + iteration++);
            Set<Edge> _temp = new HashSet<>();
            Camera_p_Model test = new Camera_p_Model();
            test.generateGraph(_temp, pb);
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


        String dotFile = "src/main/java/System_q/realDevice/camera_output.dot";
        generateDotFile(dotSet, nodeMap.size(), dotFile);

        LOGGER.info("=========== Graph Show ==============");
        Collection<graph.Edge> edge = userGraph.getEdges();
//        edge.forEach(temp->{
//            LOGGER.info(temp);
//        });
        LOGGER.info(edge.size());
        // save userGraph to file
        String fileName = "src/main/java/System_q/model_p/Camera.json";
        saveEdgesToFile(edge, fileName);

    }



}
