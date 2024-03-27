package System;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public class MsgSubscriber {
    private final static String TOPIC = "cm/post_control";
    private final static int QOS = 1;
    private final static String BROKER = "tcp://localhost:1883";
    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        try {
            MqttClient client = new MqttClient(BROKER, MqttClient.generateClientId());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(false);

            // 连接到MQTT代理服务器
            client.connect(connOpts);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
