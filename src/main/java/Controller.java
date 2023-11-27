import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import testLearnLib.DotToAdjacencyMatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Controller {
    private final static String TOPIC = "coffee/control";
    private final static String RESULTTOPIC = "coffee/result";
    private final static int QOS = 1;
    private final static String BROKER = "tcp://localhost:1883";
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String filePath = "src/main/java/testLearnLib/dotFile/CoffeeMachine.dot";
    private static final Object lock = new Object();

    public static Map<Integer, String> getNodeState(Map<Integer, List<String>> transitions) {
        try {
            MqttClient client = new MqttClient(BROKER, MqttClient.generateClientId());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(false);

            // 连接到MQTT代理服务器
            client.connect(connOpts);

            ArrayList<String> resultList = new ArrayList<>();
            try {
                client.subscribe(RESULTTOPIC, (topic, message) -> {
                    String msg = new String(message.getPayload());
                    LOGGER.info("Received message: " + msg);
                    resultList.add(msg);
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }

//            String dotGraph = DotToAdjacencyMatrix.readDotFile(filePath);
//            String[][] adjacencyMatrix = DotToAdjacencyMatrix.dotToAdjacencyMatrix(dotGraph);
//            Map<Integer, List<String>> transitions = DotToAdjacencyMatrix.traverseMatrix(adjacencyMatrix);
            Map<Integer, Integer> transMapCount = exploreSystemState(transitions, client);

            int sum = calculateSum(transMapCount);
            LOGGER.info(sum);
            while (true){
                if (resultList.size() == sum){
                    LOGGER.info("All results received!");
                    break;
                }
            }
            // Process State
            Map<Integer, String> stateMap = processState(transMapCount, resultList);
//            for (Map.Entry<Integer, String> entry : stateMap.entrySet()) {
//                System.out.println("(s" + entry.getKey() + ", " + entry.getValue() + ")");
//            }

            // 断开连接
            client.disconnect();
            return stateMap;


        } catch (MqttException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static Map<Integer, String> processState(Map<Integer, Integer> inputMap, List<String> arrayList) {
        Map<Integer, String> resultMap = new HashMap<>();
        int index = 0;
        for (Map.Entry<Integer, Integer> entry : inputMap.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();

            String result = arrayList.get(index + value - 1);

            resultMap.put(key, result);
            index += value;
        }
        return resultMap;
    }


    public static Map<Integer, String> processStateFull(Map<Integer, Integer> inputMap, List<String> arrayList) {
        Map<Integer, String> resultMap = new HashMap<>();
        int index = 0;
        for (Map.Entry<Integer, Integer> entry : inputMap.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();

            StringBuilder sb = new StringBuilder();
            for (int i = index; i < index + value; i++) {
                sb.append(arrayList.get(i)).append(", ");
            }

            resultMap.put(key, sb.toString().trim());
            index += value;
        }
        return resultMap;
    }

    private static Integer calculateSum(Map<Integer, Integer> transMapCount){
        int sum = 0;
        for (Map.Entry<Integer, Integer> entry : transMapCount.entrySet()) {
            sum += entry.getValue();
        }
        return sum;
    }

    private static Map<Integer, Integer> exploreSystemState(Map<Integer, List<String>> transitions,
                                                                 MqttClient client) {
        for (Map.Entry<Integer, List<String>> entry : transitions.entrySet()) {
           int state = entry.getKey();
           if (state != 0) {
               List<String> temp = entry.getValue();
               String newValue = "init, " + temp.get(0);
               temp.set(0, newValue);
           }
        }

        Map<Integer, Integer> result = new HashMap<>();

        for (Map.Entry<Integer, List<String>> entry : transitions.entrySet()) {
            int state = entry.getKey();
            List<String> temp = entry.getValue();
            String[] commands = temp.get(0).split(",");
            LOGGER.info("State: " + state + ", Command: " + temp.get(0) + " || Length: " + commands.length);
            result.put(state, commands.length);
            for (String subCmd : commands){
                String message = constructMessage(subCmd.trim());
                MqttMessage mqttMessage = new MqttMessage(message.getBytes());
                mqttMessage.setQos(QOS);
                try {
                    client.publish(TOPIC, mqttMessage);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    private static String constructMessage(String input) {
        return "{\"command\": \"" + input + "\"}";
    }
}
