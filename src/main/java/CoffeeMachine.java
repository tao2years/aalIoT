import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CoffeeMachine {
    private String deviceId;
    private boolean waterReady;
    private boolean podReady;
    private static final Logger LOGGER = LogManager.getLogger();

    public CoffeeMachine(){}

    public CoffeeMachine(String deviceId){
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isWaterReady() {
        return waterReady;
    }

    public void setWaterReady(boolean waterReady) {
        this.waterReady = waterReady;
    }

    public boolean isPodReady() {
        return podReady;
    }

    public void setPodReady(boolean podReady) {
        this.podReady = podReady;
    }

    public void water() {
        setWaterReady(true);
        LOGGER.info("Adding water to the coffee machine.");
    }

    public void pod() {
        setPodReady(true);
        LOGGER.info("Inserting coffee pod into the machine.");
    }

    public void button() {
        if (isPodReady() && isWaterReady())
            LOGGER.info("Pressing the brew button.");
        else
            LOGGER.info("Error");
    }

    public void clean() {
        setPodReady(false);
        setWaterReady(false);
        LOGGER.info("Cleaning the coffee machine.");
    }

    public JSONObject getStatus() {
        JSONObject jsonStatus = new JSONObject();
        jsonStatus.put("waterReady", waterReady);
        jsonStatus.put("podReady", podReady);
        return jsonStatus;
    }
}