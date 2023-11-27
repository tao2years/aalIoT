import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TraceMQTTSubscriber implements MqttCallback {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String LOG_FILE = "logs/application.log";
    private MqttClient client;
    private String topic;

    public TraceMQTTSubscriber(String broker, String clientId, String topic){
        try {
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
        String receivedMessage = new String(mqttMessage.getPayload());
        saveToLogFile(receivedMessage);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        LOGGER.info("Delivery complete! " + iMqttDeliveryToken.toString());
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


    public static void main(String[] args) throws MqttException {
        String broker = "tcp://localhost:1883";
        MqttClient client = new MqttClient(broker, "TraceMQTTHandler");
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        client.connect(connOpts);

        TraceMQTTSubscriber traceMQTTSubscriber = new TraceMQTTSubscriber(broker, client.getClientId(), "coffee/trace");

    }
}
