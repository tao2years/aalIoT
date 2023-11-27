
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import sun.applet.Main;
import testLearnLib.DotToAdjacencyMatrix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Scheduler {

    public static String DOTFILE = "src/main/java/testLearnLib/dotFile/CoffeeMachine.dot";
    private static final Logger LOGGER = LogManager.getLogger();
    private final static int QOS = 1;
    private MqttClient client;
    private final static String BROKER = "tcp://localhost:1883";
    private final static String TRACETTOPIC = "coffee/trace";
    private final static String LOG_FILE = "logs/application.log";
    private static Map<LocalDateTime, SingleOperation> opTrace = new HashMap<>();

    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();

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

        // Print Maps
        printMapInfo(deviceBehaviorModel, "deviceBehaviorModel");
        printMapInfo(nodeState, "nodeState");


        // Listen to TOPIC coffee/trace to get operation trace
        scheduler.establishMQTT();
        scheduler.startListeningTraceInfo();

    }


    public static void printMapInfo (Map map, String mapName){
        LOGGER.info("Map: " + mapName + " size: " + map.size());
        for (Object key : map.keySet()){
            System.out.println(key + " : " + map.get(key));
        }
    }

    // Establish MQTT connection
    private void establishMQTT (){
        try {
            this.client =  new MqttClient(BROKER, MqttClient.generateClientId());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(false);
            this.client.connect(connOpts);
            LOGGER.info("MQTT connection established.");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // Close MQTT connection
    private void closeMQTT (){
        try {
            this.client.disconnect();
            this.client.close();
            LOGGER.info("MQTT connection closed.");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // Listen Operation Trace Info.
    private void listenTraceInfo (){
        try {
            this.client.subscribe(TRACETTOPIC, QOS, (topic, message) -> {
                String receivedMessage = new String(message.getPayload());
                saveToLogFile(receivedMessage);
//                SingleOperation op = SingleOperation.fromJsonString(receivedMessage);
//                LOGGER.info(receivedMessage);
//                assert op != null;
//                opTrace.put(op.getTimestamp(), op);
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // Start listening operation trace info.
    private void startListeningTraceInfo(){
        if (client != null) {
            LOGGER.info("Start listening operation trace info.");
            Thread thread = new Thread(()->{
                while (true){
                    this.listenTraceInfo();
                }
            });
            thread.start();
        }else {
            LOGGER.info("MQTT connection is not established.");
        }
    }

    // Save operation trace info to log file.
    private void saveToLogFile(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(message);
            writer.newLine();
            writer.flush(); // Force flush the buffer to write data to the file in real-time.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}





