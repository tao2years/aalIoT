package System;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.reflect.TypeToken;
import graph.Edge;
import graph.Graph;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class MsgScheduler {
    private final static String TOPIC = "cm/control";
    private final static String Post_TOPIC = "cm/post_control";
    private final static String BROKER = "tcp://localhost:1883";
    private static final Logger LOGGER = LogManager.getLogger();
    private final static int QOS = 1;

    public static void main(String[] args) {
        try {
            MqttClient client = new MqttClient(BROKER, MqttClient.generateClientId());
            MqttConnectOptions connOpts = new MqttConnectOptions();

            connOpts.setCleanSession(false);

            // 连接到MQTT代理服务器
            client.connect(connOpts);

            CoffeeMachine_V1 coffeeMachine_v1 = new CoffeeMachine_V1();

            // 从文件中加载图
            String fileName = "src/main/java/System/DeviceModel/coffeeMachine.json";
            Graph<Object, Object> graph = loadGraphFromFile(fileName);

            AtomicReference<String> preState = new AtomicReference<>(getCurrentStateFromDeviceSystemState(coffeeMachine_v1.toSystemStateString(), graph));
            LOGGER.info("The init state is: " + preState);

            // 订阅指定主题的消息
            client.subscribe(TOPIC, QOS, (topic, message) -> {
                String receivedMessage = new String(message.getPayload());
                LOGGER.info("Receive msg: " + receivedMessage);
                JSONObject msg = JSON.parseObject(receivedMessage);
                String cmd = msg.getString("command");
                String ts = msg.getString("timestamp");
                LOGGER.info(coffeeMachine_v1.toSystemStateString());
                preState.set(processMessage(preState.get(), cmd, graph, client, coffeeMachine_v1));
                LOGGER.info("The current state is: " + preState.get());
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 处理消息
     * @param preState
     * @param cmd
     * @param graph
     * @param client
     */
    private static String processMessage(String preState, String cmd, Graph<Object, Object> graph, MqttClient client, CoffeeMachine_V1 cm){
//        LOGGER.info("PreState: " + preState + " Command: " + cmd);
//        if (preState.equals("null")){
//            preState = "S0";
//        }
        Map<String, ArrayList<Edge>> possibleExecutedAPIs = getApisBasedOnState(preState, graph);
        if (!possibleExecutedAPIs.keySet().contains(cmd)){
            LOGGER.info("The command is not supported in the current state.");
            return preState;
        }
//        LOGGER.info(preState + " " + cmd);
        switch (cmd) {
            case "turnOn":
                cm.turnOn();
                break;
            case "addWater":
                cm.addWater();
                break;
            case "addCoffeeBean":
                cm.addCoffeeBean();
                break;
            case "addMilk":
                cm.addMilk();
                break;
            case "placeCup":
                cm.placeCup();
                break;
            case "fetchCoffee":
                cm.fetchCoffee();
                break;
            case "brewCoffee_1":
                cm.brewCoffee(1);
                break;
            case "brewCoffee_2":
                cm.brewCoffee(2);
                break;
            case "brewCoffee_3":
                cm.brewCoffee(3);
                break;
        }
        String currentState = cm.toSystemStateString();
//        LOGGER.info("The current state is: " + currentState);
        preState = getCurrentStateFromDeviceSystemState(currentState, possibleExecutedAPIs.get(cmd));
//        LOGGER.info("The next state is: " + preState);
        return preState;
    }

    /**
     * 根据当前状态和执行的API获取下一个状态
     * @param currentState
     * @param edges
     * @return
     */
    private static String getCurrentStateFromDeviceSystemState(String currentState, ArrayList<Edge> edges) {
        boolean flag = false;
        String result;
//        LOGGER.info("Current state: " + currentState);
        for (Edge edge : edges) {
            LOGGER.info(edge);
            if (edge.getTarget().contains(currentState)) {
                Node node = Node.fromString(edge.getTarget());
                LOGGER.info(node);
                return node.getNodeId();
            }
        }
        return "null";
    }

    /**
     *
     * 根据当前状态获取下一个状态
     * @param currentState
     * @param graph
     * @return
     */
    public static String getCurrentStateFromDeviceSystemState(String currentState, Graph<Object, Object> graph){
        LOGGER.info("Current state: " + currentState);
        Collection<Edge> edges = graph.getEdges();
        for (Edge edge : edges){
            if (edge.getSource().contains(currentState)){
//                LOGGER.info("The edge is: " + edge);
                Node node = Node.fromString(edge.getSource());
//                LOGGER.info(node);
                return node.getNodeId();
            }
        }
        return "null";
    }


    /**
     * 根据当前状态获取可能执行的API
     * @param state
     * @param graph
     * @return
     */
    private static Map<String, ArrayList<Edge>> getApisBasedOnState(String state, Graph<Object, Object> graph){
        Collection<Edge> edges = graph.getEdges();
        Map<String, ArrayList<Edge>> result = new HashMap<>();
        for (Edge edge : edges){
            if (edge.getSource().contains(state)){
//                LOGGER.info("The edge is: " + edge);
                String name = edge.getName();
                if (result.containsKey(name)) {
                    result.get(name).add(edge);
                }else {
                    ArrayList<Edge> list = new ArrayList<>();
                    list.add(edge);
                    result.put(name, list);
                }
            }
        }
        return result;
    }


    /**
     *
     * @return
     */
    public static String CompareStateDifference (){

        return null;
    }

    public static Graph<Object, Object> loadGraphFromFile(String fileName){
        Graph<Object, Object> graph = new Graph<>(true,true,false);
        try (FileReader reader = new FileReader(fileName)) {
            Type collectionType = new TypeToken<Collection<Edge>>(){}.getType();
            Collection<Edge> edges = new Gson().fromJson(reader, collectionType);
            for (Edge edge : edges){
                graph.setEdge(edge);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }


}
