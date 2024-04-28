package System_q.realDevice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class CameraStatus {
    private static final Logger LOGGER = LogManager.getLogger();

    private String power;
    private String motion_record;
    private String light;
    private String full_color;
    private String flip;
    private String improve_program;
    private String wdr;
    private String watermark;
    private int night_mode;

    public CameraStatus() {
    }

    public CameraStatus(String power, String motion_record, String light, String full_color, String flip, String improve_program, String wdr, String watermark, int night_mode) {
        this.power = power;
        this.motion_record = motion_record;
        this.light = light;
        this.full_color = full_color;
        this.flip = flip;
        this.improve_program = improve_program;
        this.wdr = wdr;
        this.watermark = watermark;
        this.night_mode = night_mode;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getMotion_record() {
        return motion_record;
    }

    public void setMotion_record(String motion_record) {
        this.motion_record = motion_record;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getFull_color() {
        return full_color;
    }

    public void setFull_color(String full_color) {
        this.full_color = full_color;
    }

    public String getFlip() {
        return flip;
    }

    public void setFlip(String flip) {
        this.flip = flip;
    }

    public String getImprove_program() {
        return improve_program;
    }

    public void setImprove_program(String improve_program) {
        this.improve_program = improve_program;
    }

    public String getWdr() {
        return wdr;
    }

    public void setWdr(String wdr) {
        this.wdr = wdr;
    }

    public String getWatermark() {
        return watermark;
    }

    public void setWatermark(String watermark) {
        this.watermark = watermark;
    }

    public int getNight_mode() {
        return night_mode;
    }

    public void setNight_mode(int night_mode) {
        this.night_mode = night_mode;
    }

    @Override
    public String toString() {
        return "Camera{" +
                "'power':'" + power +
                "', 'motion_record':'" + motion_record +
                "', 'light':'" + light +
                "', 'full_color':'" + full_color +
                "', 'flip':'" + flip +
                "', 'improve_program':'" + improve_program +
                "', 'wdr':'" + wdr +
                "', 'watermark':'" + watermark +
                "', 'night_mode':'" + night_mode +
                "'}";
    }

    public String toSystemString() {
        return "Camera{" +
                "'power':'" + power +
                "', 'motion_record':'" + motion_record +
                "', 'light':'" + light +
                "', 'full_color':'" + full_color +
                "', 'flip':'" + flip +
                "', 'improve_program':'" + improve_program +
                "', 'wdr':'" + wdr +
                "', 'watermark':'" + watermark +
                "', 'night_mode':'" + night_mode +
                "'}";
    }

    public static CameraStatus fromString(String state) {
        JSONObject json = JSON.parseObject(state.replace("Camera", ""));
        return new CameraStatus(json.getString("power"), json.getString("motion_record"), json.getString("light"), json.getString("full_color"), json.getString("flip"), json.getString("improve_program"), json.getString("wdr"), json.getString("watermark"), json.getInteger("night_mode"));
    }

    public static CameraStatus parse(String logLine) {
        try {
            JSONObject json = JSON.parseObject(logLine);
            return new CameraStatus(json.getString("power"), json.getString("motion_record"), json.getString("light"), json.getString("full_color"), json.getString("flip"), json.getString("improve_program"), json.getString("wdr"), json.getString("watermark"), json.getInteger("night_mode"));
        }catch (Exception e){
            LOGGER.error("Error parsing CameraStatus: " + logLine);
        }
        return null;
    }

    public static void main(String[] args) {
        String testLine = "{\"power\": \"off\", \"motion_record\": \"stop\", \"light\": \"on\", \"full_color\": \"on\", \"flip\": \"on\", \"improve_program\": \"on\", \"wdr\": \"off\", \"track\": \"off\", \"sdcard_status\": \"0\", \"watermark\": \"on\", \"max_client\": \"0\", \"night_mode\": \"2\", \"mini_level\": \"1\"}\n";
        CameraStatus cameraStatus = parse(testLine);
        String test2String = cameraStatus.toString();
        LOGGER.info(test2String);
        CameraStatus cameraStatus2 = fromString(test2String);
        LOGGER.info(cameraStatus2.toString());
    }
}
