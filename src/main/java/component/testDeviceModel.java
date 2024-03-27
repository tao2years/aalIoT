package component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.transform.Source;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class testDeviceModel {
    private static final Logger LOGGER = LogManager.getLogger();
    private static Map<String, Set<String>> stateTransitions;
//    private static CoffeeMachine device;
    private static final int MAX_MODE = 20;
//    private static Set<Edge> edges;

    public testDeviceModel() {
        stateTransitions = new HashMap<>();
//        edges = new HashSet<>();
//        device = new CoffeeMachine();
    }

    private static List<String> getAllPossibleAPIs() {
        List<String> apis = new ArrayList<>();
        apis.add("turnOn");
//        apis.add("turnOff");
        apis.add("addWater");
        apis.add("addCoffeeBean");
        apis.add("addMilk");
        apis.add("placeCup");
        apis.add("fetchCoffee");
        for (int i = 1; i <= 3; i++) {
            apis.add("brewCoffee_" + i);
        }

        return apis;
    }

    private static String executeApi(String api, CoffeeMachine device) {
        switch (api) {
            case "turnOn":
                device.turnOn();
                return device.toString();
            case "turnOff":
                device.turnOff();
                return device.toString();
            case "addWater":
                device.addWater();
//                LOGGER.info(device.toString());
                return device.toString();
            case "addCoffeeBean":
                device.addCoffeeBean();
//                LOGGER.info(device.toString());
                return device.toString();
            case "addMilk":
                device.addMilk();
//                LOGGER.info(device.toString());
                return device.toString();
            case "placeCup":
                device.placeCup();
                return device.toString();
            case "brewCoffee_1":
                String result= device.brewCoffee(1);
                String time = "skip"; // Add to edge
                if (result.equals("Success"))
                    return device.toString();
                else
                    return "skip";
            case "brewCoffee_2":
                result= device.brewCoffee(2);
                time = "skip"; // Add to edge
                if (result.equals("Success"))
                    return device.toString();
                else
                    return "skip";
            case "brewCoffee_3":
                result = device.brewCoffee(3);
                time = "skip"; // Add to edge
                if (result.equals("Success"))
                    return device.toString();
                else
                    return "skip";
            case "fetchCoffee":
                device.fetchCoffee();
                return device.toString();
            default:
                return device.toString();
        }
    }

    private static void generateGraph(Set<Edge> edges) {
        CoffeeMachine device = new CoffeeMachine();
        String initState = device.toString();
        String initSystemState = device.toSystemStateString();
        stateTransitions.put(initSystemState, new HashSet<>());

        exploreState(initState, initSystemState, edges);
    }

    private static void exploreState(String state, String systemState, Set<Edge> edges) {
        List<String> apis = getAllPossibleAPIs();

        for (String api : apis) {
//            LOGGER.info("------------------------");
//            LOGGER.info(api);
//            LOGGER.info("Current state: " + state);
            CoffeeMachine currentCM = CoffeeMachine.fromString(state);
            String nextState = executeApi(api, currentCM);
            if (!nextState.equals("skip")){
                CoffeeMachine nextCM = CoffeeMachine.fromString(nextState);
                String nextSystemState = nextCM.toSystemStateString();
                Edge edge = new Edge(systemState, nextSystemState, api);
//            LOGGER.info(edge);
                edges.add(edge);

                int temp = 0;
                while (systemState.equals(nextSystemState)) {
//                LOGGER.info("\n" + systemState + "\n" + nextSystemState);
                    if (state.equals(nextState)) {
//                    LOGGER.info("Api: " + api);
//                    LOGGER.info("Current: " + state);
//                    LOGGER.info("Next: " + nextState);
//                    LOGGER.info("State is the same, breaking.");
                        break;
                    }else {
                        String source = nextSystemState;
                        String _nextState = executeApi(api, nextCM);
                        nextCM = CoffeeMachine.fromString(_nextState);
                        String target = nextCM.toSystemStateString();
                        Edge _edge = new Edge(source, target, api);
                        edges.add(_edge);
                        temp++;
                        if (temp>MAX_MODE) {
//                        LOGGER.info("[MAX] State is the same, breaking.");
                            break;
                        }
                    }
                }

                if (!stateTransitions.containsKey(nextSystemState)) {
                    stateTransitions.put(nextSystemState, new HashSet<>());
                    exploreState(nextCM.toString(), nextSystemState, edges);
                }

                stateTransitions.get(systemState).add(api);
            }
        }
    }

    public static void main(String[] args) {
        Set<Edge> edges = new HashSet<>();
//        Set<Integer> sizeHistory = new LinkedHashSet<>();
        int[] sizeHistory = new int[20];
        int historyIndex = 0;
        Set<String> nodesSet = new TreeSet<>();
        Set<String> nodeOrderSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        // 创建节点编号Map
        Map<String, String> nodeNumbers = new HashMap<>();
        int sumEdgeLast;

        // 最终Edges集合
        Set<Edge> final_Edges = new HashSet<>();

        for (int i = 0; i < 100; i++) {
            Set<Edge> _temp = new HashSet<>();
            testDeviceModel test = new testDeviceModel();
            test.generateGraph(_temp);
            edges.addAll(_temp);
        }

        for (int i = 101; i < 9999; i++) {
            LOGGER.info("Iteration: " + i);
            Set<Edge> _temp = new HashSet<>();
            testDeviceModel test = new testDeviceModel();
            test.generateGraph(_temp);
            edges.addAll(_temp);

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

            if (checkSizeConsistency(sizeHistory)) {
                break;
            }
        }


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
                        dotSet.add(new Edge(nodeNumber, nodeMap.get(target), api));
                        visitedApis.add(targetAndApi);
                    }
                }
            }
            System.out.println();
        }

        for (Edge edge : dotSet){
            LOGGER.info(edge);
        }

        LOGGER.info(dotSet.size());

        String dotFile = "src/main/java/component/output.dot";
        generateDotFile(dotSet, nodeMap.size(), dotFile);


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




    private static boolean checkSizeConsistency(int[] sizeHistory) {
        if (sizeHistory[0] == 0) {
            return false; // 初始状态，不一致
        }

        int size = sizeHistory[0];
        for (int i = 1; i < sizeHistory.length; i++) {
            if (sizeHistory[i] != size) {
                return false; // 不一致
            }
        }

        return true; // 一致
    }
}
