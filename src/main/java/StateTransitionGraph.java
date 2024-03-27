import java.util.*;

public class StateTransitionGraph {
    private static final int MAX_MODE = 3;

    private static class DeviceState {
        private String power;
        private String brightness;
        private String mode;

        public DeviceState(String power, String brightness, String mode) {
            this.power = power;
            this.brightness = brightness;
            this.mode = mode;
        }

        public String getPower() {
            return power;
        }

        public String getBrightness() {
            return brightness;
        }

        public String getMode() {
            return mode;
        }

        @Override
        public int hashCode() {
            return power.hashCode() + brightness.hashCode() + mode.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            DeviceState state = (DeviceState) obj;
            return power.equals(state.power) && brightness.equals(state.brightness) && mode.equals(state.mode);
        }
    }

    private static Map<DeviceState, List<String>> graph = new HashMap<>();

    public static void main(String[] args) {
        generateGraph();
        printGraph();
//        System.out.println(" \n\n ");
//        printAllGraph();
    }

    private static void generateGraph() {
        DeviceState initialState = new DeviceState("any", "any", "any");
        graph.put(initialState, new ArrayList<>());

        exploreState(initialState);
    }

    private static void exploreState(DeviceState state) {
        List<String> apis = getAllPossibleAPIs();

        for (String api : apis) {
            DeviceState nextState = performAPI(state, api);

            if (!graph.containsKey(nextState)) {
                graph.put(nextState, new ArrayList<>());
                exploreState(nextState);
            }

            graph.get(state).add(api);
        }
    }

    private static List<String> getAllPossibleAPIs() {
        List<String> apis = new ArrayList<>();
        apis.add("status");
        apis.add("on");
        apis.add("off");
        apis.add("brightness");
        for (int i = 1; i <= MAX_MODE; i++) {
            apis.add("changeMode_" + i);
        }

        return apis;
    }

    private static DeviceState performAPI(DeviceState state, String api) {
        String power = state.getPower();
        String brightness = state.getBrightness();
        String mode = state.getMode();

        if (api.equals("on")) {
            power = "on";
        } else if (api.equals("off")) {
            power = "off";
        } else if (api.startsWith("changeMode_")) {
            mode = api.substring(api.length() - 1);
        } else if (api.equals("brightness")) {
            brightness = "yes";
        }
        return new DeviceState(power, brightness, mode);
    }

    private static void printAllGraph() {
        System.out.println("All possible states: " + graph.size());
        for (Map.Entry<DeviceState, List<String>> entry : graph.entrySet()) {
            DeviceState state = entry.getKey();
            List<String> apis = entry.getValue();
            System.out.println("State: " + getStateLabel(state));
            System.out.println("APIs: " + apis);
            System.out.println();
        }
    }

    private static void printGraph() {
        System.out.println("digraph g {");
        int edgeCount = 0;
        int nodeCount = 0;
        Map<DeviceState, Integer> nodeMap = new HashMap<>();
        Map<DeviceState, Integer> transitionCountMap = new HashMap<>();
        Map<DeviceState, List<String>> transitionMap = new HashMap<>();
        Set<DeviceState> printedStates = new HashSet<>();

        // 遍历状态转换图中的每个状态
        for (Map.Entry<DeviceState, List<String>> entry : graph.entrySet()) {
            DeviceState state = entry.getKey();
            List<String> apis = entry.getValue();

            // 打印状态节点（如果状态已经打印过，则跳过）
            if (!printedStates.contains(state)) {
                String nodeLabel = "s" + nodeCount;
                nodeMap.put(state, nodeCount);
                System.out.println(nodeLabel + " [shape=\"circle\" label=\"" + getStateLabel(state) + "\"];");
                printedStates.add(state);
                nodeCount++;
            }

            // 遍历当前状态可执行的API
            for (String api : apis) {
                String edgeLabel = api;
                DeviceState nextState = performAPI(state, api);

                // 打印下一个状态节点（如果状态已经打印过，则跳过）
                if (!printedStates.contains(nextState)) {
                    int nextNodeCount = nodeCount;
                    nodeMap.put(nextState, nextNodeCount);
                    String targetNodeLabel = "s" + nextNodeCount;
                    System.out.println(targetNodeLabel + " [shape=\"circle\" label=\"" + getStateLabel(nextState) + "\"];");
                    printedStates.add(nextState);
                    nodeCount++;
                }

                // 打印状态之间的转换边
                int currentNodeCount = nodeMap.get(state);
                int nextNodeCount = nodeMap.get(nextState);
                String nodeLabel = "s" + currentNodeCount;
                String targetNodeLabel = "s" + nextNodeCount;
                System.out.println(nodeLabel + " -> " + targetNodeLabel + " [label=\"" + edgeLabel + "\"];");
                edgeCount++;

                // 更新状态的迁移边数和迁移边列表
                int transitionCount = transitionCountMap.getOrDefault(state, 0);
                transitionCountMap.put(state, transitionCount + 1);
                List<String> transitionList = transitionMap.getOrDefault(state, new ArrayList<>());
                transitionList.add(edgeLabel);
                transitionMap.put(state, transitionList);
            }
        }

        // 打印初始节点和起始边
        System.out.println("__start0 [label=\"\" shape=\"none\" width=\"0\" height=\"0\"];");
        System.out.println("__start0 -> s0;");
        System.out.println("}");

        // 统计边的数量和每个状态的迁移边数和迁移边列表
        System.out.println("Total edges: " + edgeCount);
        System.out.println("Total nodes: " + nodeCount);
    }

    private static int getStateNumber(DeviceState state) {
        return state.hashCode();
    }

    private static String getStateLabel(DeviceState state) {
        return "power: " + state.getPower() + ", brightness: " + state.getBrightness() + ", mode: " + state.getMode();
    }
}