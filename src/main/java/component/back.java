//import java.util.*;
//
//class DeviceState {
//    String power;
//    int brightness;
//    int mode;
//
//    public DeviceState(String power, int brightness, int mode) {
//        this.power = power;
//        this.brightness = brightness;
//        this.mode = mode;
//    }
//
//    // 重写equals和hashCode方法，用于比较设备状态是否相同
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null || getClass() != obj.getClass()) {
//            return false;
//        }
//        DeviceState other = (DeviceState) obj;
//        return power.equals(other.power) && brightness == other.brightness && mode == other.mode;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(power, brightness, mode);
//    }
//}
//
//public class DeviceStateTransitionGraph {
//    // API调用的参数取值范围
//    static final String[] POWER_VALUES = {"on", "off"};
//    static final int[] BRIGHTNESS_VALUES = {1, 99};
//    static final int[] MODE_VALUES = {1, 2, 3};
//
//    // API调用的方法名
//    static final String[] API_NAMES = {"on", "off", "setBrightness", "changeMode_1", "changeMode_2", "changeMode_3", "status"};
//
//    public static void main(String[] args) {
//        // 初始化初始状态
//        DeviceState initialState = new DeviceState("any", -1, -1);
//
//        // 创建状态迁移图的队列和集合
//        Queue<DeviceState> queue = new LinkedList<>();
//        Set<DeviceState> visited = new HashSet<>();
//
//        // 将初始状态加入队列和集合
//        queue.offer(initialState);
//        visited.add(initialState);
//
//        // 创建状态迁移图的邻接表
//        Map<DeviceState, List<String>> graph = new HashMap<>();
//
//        // 广度优先搜索
//        while (!queue.isEmpty()) {
//            DeviceState currentState = queue.poll();
//            List<String> currentAPIs = graph.getOrDefault(currentState, new ArrayList<>());
//
//            // 遍历所有可能的API调用
//            for (String api : API_NAMES) {
//                // 调用API并获取新的状态
//                DeviceState newState = performAPI(currentState, api);
//
//                // 将当前API加入当前状态的API列表
//                currentAPIs.add(api);
//
//                // 如果新状态没有被访问过，则加入队列和集合，并更新状态迁移图
//                if (!visited.contains(newState)) {
//                    queue.offer(newState);
//                    visited.add(newState);
//                    graph.put(newState, new ArrayList<>());
//                }
//
//                // 更新当前状态的边信息
//                graph.get(currentState).add(api);
//            }
//        }
//
//        // 输出状态迁移图
//        System.out.println("digraph g {");
//        for (Map.Entry<DeviceState, List<String>> entry : graph.entrySet()) {
//            DeviceState state = entry.getKey();
//            List<String> apis = entry.getValue();
//            String nodeLabel = "s" + getStateNumber(state);
//            System.out.println(nodeLabel + " [shape=\"circle\" label=\"" + getStateLabel(state) + "\"];");
//            for (String api : apis) {
//                String edgeLabel = api + "[] / *";
//                String targetNodeLabel = "s" + getStateNumber(performAPI(state, api));
//                System.out.println(nodeLabel + " -> " + targetNodeLabel + " [label=\"" + edgeLabel + "\"];");
//            }
//        }
//        System.out.println("__start0 [label=\"\" shape=\"none\" width=\"0\" height=\"0\"];");
//        System.out.println("__start0 -> s0;");
//        System.out.println("}");
//    }
//
//    // 模拟调用API并返回新状态
//    static DeviceState performAPI(DeviceState currentState, String api) {
//        if (api.equals("on")) {
//            return new DeviceState("on", currentState.brightness, currentState.mode);
//        } else if (api.equals("off")) {
//            return new DeviceState("off", currentState.brightness, currentState.mode);
//        } else if (api.equals("setBrightness")) {
//            return new DeviceState(currentState.power, getRandomValue(BRIGHTNESS_VALUES), currentState.mode);
//        } else if (api.equals("changeMode_1")) {
//            return new DeviceState(currentState.power, currentState.brightness, 1);
//        } else if (api.equals("changeMode_2")) {
//            return new DeviceState(currentState.power, currentState.brightness, 2);
//        } else if (api.equals("changeMode_3")) {
//            return new DeviceState(currentState.power, currentState.brightness, 3);
//        } else if (api.equals("status")) {
//            return currentState;
//        }
//        return currentState;
//    }
//
//    // 获取状态的编号
//    static int getStateNumber(DeviceState state) {
//        return state.power.equals("on") ? 1 : 0;
//    }
//
//    // 获取状态的标签
//    static String getStateLabel(DeviceState state) {
//        String powerLabel = state.power.equals("on") ? "ON" : "OFF";
//        String brightnessLabel = state.brightness == -1 ? "*" : String.valueOf(state.brightness);
//        String modeLabel = state.mode == -1 ? "*" : String.valueOf(state.mode);
//        return powerLabel + " / " + brightnessLabel + " / " + modeLabel;
//    }
//
//    // 获取指定范围内的随机整数
//    static int getRandomValue(int[] values) {
//        Random random = new Random();
//        int index = random.nextInt(values.length);
//        return values[index];
//    }
//}