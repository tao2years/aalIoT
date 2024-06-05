import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import org.eclipse.paho.client.mqttv3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Subscriber {
    private final static String TOPIC = "coffee/control";
    private final static String RESULTTOPIC = "coffee/result";
    private final static String TRACETTOPIC = "coffee/trace";
    private final static int QOS = 1;
    private final static String BROKER = "tcp://localhost:1883";
    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        // 创建MQTT客户端
        try {
            MqttClient client = new MqttClient(BROKER, MqttClient.generateClientId());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(false);

            // 连接到MQTT代理服务器
            client.connect(connOpts);

            // 创建咖啡机对象
            CoffeeMachine CM = new CoffeeMachine("Test001");

            // 创建DeviceTwin
            JSONObject deviceTwin = new JSONObject();
            deviceTwin.put("DeviceId", CM.getDeviceId());
            deviceTwin.put("Property", CM.getStatus());

            // 订阅指定主题的消息
            client.subscribe(TOPIC, QOS, (topic, message) -> {
                String receivedMessage = new String(message.getPayload());
                LOGGER.info("Receive msg: " + receivedMessage);
                // 解析msg, 提取command
                JSONObject msg = JSON.parseObject(receivedMessage);
                String cmd = msg.getString("command");
                String ts = msg.getString("timestamp");
                processMessage(cmd, ts, CM, deviceTwin, client);
            });

            // 等待人工退出
            LOGGER.info("Subscriber is listening for messages. Press 'q' to quit. Press 'twin' to get deviceTwin status");
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("q")) {
                    break;
                }else if (input.equalsIgnoreCase("twin")){
                    LOGGER.info("[DeviceTwin]: " + deviceTwin);
                }
            }

            // 断开连接
            client.disconnect();
            System.exit(0);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private static void processMessage(String message, String ts, CoffeeMachine CM, JSONObject deviceTwin, MqttClient client) throws MqttException {
        SingleOperation singleOp = new SingleOperation();
        JSONObject properties = deviceTwin.getJSONObject("Property");
        switch (message) {
            case "init":
            case "clean":
                try{
                CM.clean();
                client.publish(RESULTTOPIC, CM.getStatus().toJSONString().getBytes(), QOS, false);
                // Construct Single Operation Trace
                singleOp.setAll(ts, message, deviceTwin.toJSONString());
                client.publish(TRACETTOPIC, singleOp.toString().getBytes(), QOS, false);
                properties.put("waterReady", false);
                properties.put("podReady", false);}
                catch (Exception e){
                    LOGGER.error(e.getMessage());
                }
                break;
            case "water":
                try {
                    CM.water();
//                LOGGER.info(CM.getStatus()); // {"podReady":false,"waterReady":true}
                    client.publish(RESULTTOPIC, CM.getStatus().toJSONString().getBytes(), QOS, false);
                    // Construct Single Operation Trace
                    singleOp.setAll(ts, message, deviceTwin.toJSONString());
                    client.publish(TRACETTOPIC, singleOp.toString().getBytes(), QOS, false);
                    properties.put("waterReady", true);
                }catch (Exception e){
                    LOGGER.error(e.getMessage());
                }
                break;
            case "pod":
                try{
                    CM.pod();
                    client.publish(RESULTTOPIC, CM.getStatus().toJSONString().getBytes(), QOS, false);
                    // Construct Single Operation Trace
                    singleOp.setAll(ts, message, deviceTwin.toJSONString());
                    client.publish(TRACETTOPIC, singleOp.toString().getBytes(), QOS, false);
                    properties.put("podReady", true);
                }catch (Exception e){
                    LOGGER.error(e.getMessage());
                }
                break;
            case "button":
                try{
                    CM.button();
                    client.publish(RESULTTOPIC, CM.getStatus().toJSONString().getBytes(), QOS, false);
                    // Construct Single Operation Trace
                    singleOp.setAll(ts, message, deviceTwin.toJSONString());
                    client.publish(TRACETTOPIC, singleOp.toString().getBytes(), QOS, false);
                }catch (Exception e){
                    LOGGER.error(e.getMessage());
                }
                break;
            case "status":
                LOGGER.info(CM.getStatus().toJSONString());
                break;
            default:
                LOGGER.info("Invalid command: " + message);
                client.publish(RESULTTOPIC, "Invalid Command".getBytes(), QOS, false);
                break;
        }
    }
}