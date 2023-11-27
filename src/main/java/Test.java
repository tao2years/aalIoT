import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        Test test = new Test();

        String input = "{\"systemStatus\":{\"DeviceId\":\"Test001\",\"Property\":{\"podReady\":false,\"waterReady\":false}},\"command\":\"water\",\"timestamp\":\"2023-11-17T10:43:44.480\"}";

        JSONObject json = JSON.parseObject(input);
        String systemStatus = json.getString("systemStatus");
        String command = json.getString("command");
        String timestampStr = json.getString("timestamp");
        LocalDateTime timestamp = LocalDateTime.parse(timestampStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        System.out.println("systemStatus: " + systemStatus);
        System.out.println("command: " + command);
        System.out.println("timestamp: " + timestamp);
    }

    public String testTrace(){
        SingleOperation lastOp = new SingleOperation(LocalDateTime.now().toString(), "on", "off");
        SingleOperation currentOp = new SingleOperation(LocalDateTime.now().toString(), "off", "on");
        Trace trace = new Trace(lastOp, currentOp);
        LOGGER.info(trace.toString());
        return trace.toString();
    }
}
