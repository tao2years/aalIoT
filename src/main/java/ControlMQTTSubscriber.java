import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;
import testLearnLib.DotToAdjacencyMatrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControlMQTTSubscriber implements MqttCallback {
    private static final Logger LOGGER = LogManager.getLogger();
    private final static String LOG_FILE = "logs/application.log";
    private MqttClient client;
    private String topic;
    private static final String DOTFILE = "src/main/java/testLearnLib/dotFile/CoffeeMachine.dot";

    private static String[][] adjacencyMatrix;
    private static Map<Integer, List<String>> deviceBehaviorModel;
    private static Map<Integer, String> nodeState;

    public ControlMQTTSubscriber(String broker, String clientId, String topic, String[][] adjacencyMatrix, Map<Integer, List<String>> deviceBehaviorModel, Map<Integer, String> nodeState) {
        try {
            this.adjacencyMatrix = adjacencyMatrix;
            this.deviceBehaviorModel = deviceBehaviorModel;
            this.nodeState = nodeState;
            this.client = new MqttClient(broker, clientId);
            this.topic = topic;

            client.setCallback(this);
            client.connect();
            client.subscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        LOGGER.info("Connection lost!" + throwable.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        LOGGER.info("Message arrived! Topic: " + topic + ", Message: " + mqttMessage.toString());
        List<String> Lines = readLines(LOG_FILE);
        analyzeLines(Lines, mqttMessage.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        LOGGER.info("Delivery complete! " + iMqttDeliveryToken.toString());
    }

    private boolean isValidMessage(MqttMessage mqttMessage){
        LOGGER.info("Validate message! " + mqttMessage.toString());
        List<String> Lines = readLines(LOG_FILE);
        analyzeLines(Lines, mqttMessage.toString());
        return false;
    }

    private void publishTransitionMessage(MqttMessage mqttMessage){
        LOGGER.info("Publish transition message!");
    }

    private void processMessage( MqttMessage mqttMessage){
        LOGGER.info("Process message!");
    }

    private static List<String> readLines(String filePath) {
        List<String> Lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    Lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Lines;
    }

    public static String findFinalState(String[][] adjacencyMatrix, List<String> cmdList) {
        Map<Integer, Integer> stateMap = generateStateMap(adjacencyMatrix);
        int currentState = 0; // 初始状态为0

        for (String cmd : cmdList) {
            int row = currentState;
            for (int col = 0; col < adjacencyMatrix[row].length; col++) {
                String[] actions = adjacencyMatrix[row][col].split(", ");
                for (String action : actions) {
                    if (action.startsWith(cmd)) {
                        currentState = col;
                        break;
                    }
                }
            }
        }

        return String.valueOf(currentState);
    }

    private static Map<Integer, Integer> generateStateMap(String[][] adjacencyMatrix) {
        Map<Integer, Integer> stateMap = new HashMap<>();
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            stateMap.put(i, i);
        }
        return stateMap;
    }

    private static void analyzeLines(List<String> lines, String mqttMessage) {
        LOGGER.info("Analyze lines!");
        int size = lines.size();
        if (lines.get(size-1).contains(mqttMessage)){
            lines.remove(size-1);
        }
        if (lines.size() < 2){
            executeDevice(mqttMessage);
        }else{
            String targetSystemState = executeSystemState(lines);
            LOGGER.info(targetSystemState);
            String currentSystemState = getCurrentSystemState();
            if (!targetSystemState.equals(currentSystemState)){
                List<String> supplyCmdList = calculateSupplyCmdList(currentSystemState, targetSystemState, adjacencyMatrix);
            }
        }
    }


    private static void executeDevice(String mqttMessage) {
        
    }

    private static List<String> calculateSupplyCmdList(String currentSystemState, String targetSystemState, String[][] adjacencyMatrix){
        List<String> supplyCmdList = new ArrayList<>();
        int currentRow = Integer.parseInt(currentSystemState);
        int targetRow = Integer.parseInt(targetSystemState);

        while (currentRow != targetRow) {
            for (int col = 0; col < adjacencyMatrix[currentRow].length; col++) {
                if (col != currentRow) {
                    String[] actions = adjacencyMatrix[currentRow][col].split(", ");
                    for (String action : actions) {
                        if (!action.equals("N/A")) {

                            supplyCmdList.add(action.split(" / ")[0]);
                            currentRow = col;
                            break;
                        }
                    }
                }
            }
        }

        return supplyCmdList;
    }

    private static String executeSystemState(List<String> lines){
        List<String> cmdList = new ArrayList<>();
        for (String line : lines){
            JSONObject msg = JSON.parseObject(line);
            String cmd = msg.getString("command");
            LOGGER.info("Command: " + cmd);
            cmdList.add(cmd);
        }
        String finalState = findFinalState(adjacencyMatrix, cmdList);
        return finalState;
    }

    private static String getCurrentSystemState(){
        return null;
    }

    public static void main(String[] args) throws MqttException {

    }
}
