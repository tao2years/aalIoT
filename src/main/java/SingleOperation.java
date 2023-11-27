import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SingleOperation {
    private String timestamp;
    private String command;
    private String systemStatus;
    private static final Logger LOGGER = LogManager.getLogger();

    public SingleOperation(String timestamp, String command, String systemStatus) {
        this.timestamp = timestamp;
        this.command = command;
        this.systemStatus = systemStatus;
    }

    public SingleOperation(){

    }

    public void setAll (String timestamp, String command, String systemStatus) {
        this.timestamp = timestamp;
        this.command = command;
        this.systemStatus = systemStatus;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(String systemStatus) {
        this.systemStatus = systemStatus;
    }

    @Override
    public String toString() {
        return "{" +
                "\"timestamp\":\"" + timestamp + "\"," +
                "\"command\":\"" + command + "\"," +
                "\"systemStatus\":" + systemStatus +
                "}";
    }

    public static SingleOperation fromJsonString (String input){
        LOGGER.info("[fromJsonString] input: " + input);
        if (input != null){
            LOGGER.info("input: " + input);
            try {
                JSONObject json = JSON.parseObject(input);
                String systemStatus = json.getString("systemStatus");
                String command = json.getString("command");
                String timestampStr = json.getString("timestamp");
//                LocalDateTime timestamp = LocalDateTime.parse(timestampStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                return new SingleOperation(timestampStr, command, systemStatus);
            }catch (Exception e){
                LOGGER.info("FromJsonString error: " + e.getMessage());
            }
        }else {
            return null;
        }
        return null;
    }

}
