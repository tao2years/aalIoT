import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import testLearnLib.DotToAdjacencyMatrix;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static testLearnLib.DotToAdjacencyMatrix.printMatrix;

public class CoreSubscriber {
    private static String DOTFILE = "src/main/java/testLearnLib/dotFile/CoffeeMachine.dot";
    private static final Logger LOGGER = LogManager.getLogger();
    private final static int QOS = 1;
    private final static String BROKER = "tcp://localhost:1883";
    private final static String CONTROL_TOPIC = "coffee/control";
    private final static String TRACE_TOPIC = "coffee/trace";
    private final static String LOG_FILE = "logs/application.log";

    public static void printMapInfo (Map map, String mapName){
        LOGGER.info("Map: " + mapName + " size: " + map.size());
        for (Object key : map.keySet()){
            System.out.println(key + " : " + map.get(key));
        }
    }

    public static void main(String[] args) {
        try {
            MqttClient traceClient = new MqttClient(BROKER, MqttClient.generateClientId());
            MqttConnectOptions traceConnOpts = new MqttConnectOptions();
            traceConnOpts.setCleanSession(true);
            traceClient.connect(traceConnOpts);

            TraceMQTTSubscriber traceSubscriber = new TraceMQTTSubscriber(BROKER, traceClient.getClientId(), TRACE_TOPIC);

            MqttClient controlClient = new MqttClient(BROKER, MqttClient.generateClientId());
            MqttConnectOptions controlConnOpts = new MqttConnectOptions();
            controlConnOpts.setCleanSession(true);
            controlClient.connect(controlConnOpts);

            // Generate device behavior model from dot file
            String dotGraph = DotToAdjacencyMatrix.readDotFile(DOTFILE);
            String[][] adjacencyMatrix = DotToAdjacencyMatrix.dotToAdjacencyMatrix(dotGraph);
            Map<Integer, List<String>> deviceBehaviorModel = DotToAdjacencyMatrix.traverseMatrix(adjacencyMatrix);

            // Drive system to explore all node states
//        Map<Integer, String> nodeState = Controller.getNodeState(deviceBehaviorModel);
            // Assume that the system has been explored and the node state has been obtained
            Map<Integer, String> nodeState = new HashMap<>();
            nodeState.put(0, "{\"podReady\":false,\"waterReady\":false}");
            nodeState.put(1, "{\"podReady\":true,\"waterReady\":false}");
            nodeState.put(2, "{\"podReady\":true,\"waterReady\":true}");
            nodeState.put(3, "{\"podReady\":false,\"waterReady\":true}");

            ControlMQTTSubscriber controlSubscriber = new ControlMQTTSubscriber(BROKER, controlClient.getClientId(), CONTROL_TOPIC, adjacencyMatrix, deviceBehaviorModel, nodeState);

            printMapInfo(deviceBehaviorModel, "deviceBehaviorModel");
            printMapInfo(nodeState, "nodeState");
            printMatrix(adjacencyMatrix);

        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
}

