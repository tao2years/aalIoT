import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceGraph {
    private Map<String, Integer> nodeMap;
    private List<DeviceState> states;
    private List<Transition> transitions;

    public DeviceGraph() {
        nodeMap = new HashMap<>();
        states = new ArrayList<>();
        transitions = new ArrayList<>();
    }

    public void addState(DeviceState state) {
        states.add(state);
        nodeMap.put(state.getLabel(), states.size() - 1);
    }

    public void addTransition(DeviceState fromState, DeviceState toState, String api) {
        int fromNode = nodeMap.get(fromState.getLabel());
        int toNode = nodeMap.get(toState.getLabel());
        transitions.add(new Transition(fromNode, toNode, api));
    }

    public void printGraph() {
        for (int i = 0; i < states.size(); i++) {
            DeviceState state = states.get(i);
            System.out.println("State: " + state.getLabel());
            System.out.println("APIs: " + getAPIsForState(i));
            System.out.println();
        }
    }

    private List<String> getAPIsForState(int node) {
        List<String> apis = new ArrayList<>();
        for (Transition transition : transitions) {
            if (transition.getFromNode() == node) {
                apis.add(transition.getApi());
            }
        }
        return apis;
    }

    private static class DeviceState {
        private String label;

        public DeviceState(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private static class Transition {
        private int fromNode;
        private int toNode;
        private String api;

        public Transition(int fromNode, int toNode, String api) {
            this.fromNode = fromNode;
            this.toNode = toNode;
            this.api = api;
        }

        public int getFromNode() {
            return fromNode;
        }

        public int getToNode() {
            return toNode;
        }

        public String getApi() {
            return api;
        }
    }

    public static void main(String[] args) {
        DeviceGraph graph = new DeviceGraph();

        DeviceState initialState = new DeviceState("S0");
        graph.addState(initialState);

        DeviceState state1 = new DeviceState("S1");
        graph.addState(state1);

        graph.addTransition(initialState, state1, "on()");

        graph.printGraph();
    }
}