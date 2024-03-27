package System;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.time.LocalDateTime;
import java.util.Scanner;

public class MsgPublisher {
    private final static String TOPIC = "cm/control";
    private final static int QOS = 1;
    private final static String BROKER = "tcp://localhost:1883";
    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        try {
            // 创建MQTT客户端
            MqttClient client = new MqttClient(BROKER, MqttClient.generateClientId());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // 连接到MQTT代理服务器
            client.connect(connOpts);

            // 从用户获取命令输入
            Scanner scanner = new Scanner(System.in);
            while (true) {
                LOGGER.info("Enter the command (or 'q' to quit): ");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("q")) {
                    break;
                }


                // 构造消息
                String message = constructMessage(input);
                MqttMessage mqttMessage = new MqttMessage(message.getBytes());
                mqttMessage.setQos(QOS);

                // 发布消息到指定主题
                client.publish(TOPIC, mqttMessage);

                LOGGER.info("Message published: " + message);
            }

            // 断开连接
            client.disconnect();



        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private static String constructMessage(String input) {
        // 根据输入构造MQTT消息
        // 例如：{"command": "water", "timestamp": "2022-01-01T12:34:56"}
//        String ts = LocalDateTime.now().toString();
        String ts = "temp";
        return "{\"command\": \"" + input + "\", \"timestamp\": \"" + ts + "\"}";
    }
}
