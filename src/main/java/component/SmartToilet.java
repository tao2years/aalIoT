package component;
/**
 * 智能马桶类，用于提供舒适的上厕所体验。
 *
 * 属性：
 * - seatTemperature：座椅温度，取值范围：整数
 * - waterTemperature：冲洗水温度，取值范围：整数
 * - waterPressure：冲洗水压力，取值范围：整数
 * - musicPlaying：是否正在播放音乐，取值范围：true or false
 * - lightOn：是否开启照明，取值范围：true or false
 * - autoLid：是否开启自动盖板，取值范围：true or false
 * - userDetected：是否检测到用户，取值范围：true or false
 * - seatHeatingEnabled：是否开启座圈加热，取值范围：true or false
 * - nozzleCleaningEnabled：是否开启清洁剂喷洒，取值范围：true or false
 * - dryingEnabled：是否开启风干功能，取值范围：true or false
 * - remoteControlEnabled：是否启用远程控制，取值范围：true or false
 *
 * 构造方法：
 * - SmartToilet()：初始化所有属性为默认值
 *
 * 方法：
 * - getSeatTemperature()：获取座椅温度
 * - setSeatTemperature(int seatTemperature)：设置座椅温度
 * - getWaterTemperature()：获取冲洗水温度
 * - setWaterTemperature(int waterTemperature)：设置冲洗水温度
 * - getWaterPressure()：获取冲洗水压力
 * - setWaterPressure(int waterPressure)：设置冲洗水压力
 * - isMusicPlaying()：获取是否正在播放音乐
 * - setMusicPlaying(boolean musicPlaying)：设置是否正在播放音乐
 * - isLightOn()：获取是否开启照明
 * - setLightOn(boolean lightOn)：设置是否开启照明
 * - isAutoLid()：获取是否开启自动盖板
 * - setAutoLid(boolean autoLid)：设置是否开启自动盖板
 * - isUserDetected()：获取是否检测到用户
 * - setUserDetected(boolean userDetected)：设置是否检测到用户
 * - isSeatHeatingEnabled()：获取是否开启座圈加热
 * - setSeatHeatingEnabled(boolean seatHeatingEnabled)：设置是否开启座圈加热
 * - isNozzleCleaningEnabled()：获取是否开启清洁剂喷洒
 * - setNozzleCleaningEnabled(boolean nozzleCleaningEnabled)：设置是否开启清洁剂喷洒
 * - isDryingEnabled()：获取是否开启风干功能
 * - setDryingEnabled(boolean dryingEnabled)：设置是否开启风干功能
 * - isRemoteControlEnabled()：获取是否启用远程控制
 * - setRemoteControlEnabled(boolean remoteControlEnabled)：设置是否启用远程控制
 * - playMusic()：播放音乐
 * - controlLight(boolean on)：控制照明
 * - autoLidSwitch()：自动开关盖板
 */
public class SmartToilet {
    private int seatTemperature; // 座椅温度
    private int waterTemperature; // 冲洗水温度
    private int waterPressure; // 冲洗水压力
    private boolean musicPlaying; // 是否正在播放音乐
    private boolean lightOn; // 是否开启照明
    private boolean autoLid; // 是否开启自动盖板
    private boolean userDetected; // 是否检测到用户
    private boolean seatHeatingEnabled; // 是否开启座圈加热
    private boolean nozzleCleaningEnabled; // 是否开启清洁剂喷洒
    private boolean dryingEnabled; // 是否开启风干功能
    private boolean remoteControlEnabled; // 是否启用远程控制

    // 构造函数
    public SmartToilet() {
        this.seatTemperature = 0;
        this.waterTemperature = 0;
        this.waterPressure = 0;
        this.musicPlaying = false;
        this.lightOn = false;
        this.autoLid = false;
        this.userDetected = false;
        this.seatHeatingEnabled = false;
        this.nozzleCleaningEnabled = false;
        this.dryingEnabled = false;
        this.remoteControlEnabled = false;
    }

    // Getters and Setters
    public int getSeatTemperature() {
        return seatTemperature;
    }

    public void setSeatTemperature(int seatTemperature) {
        this.seatTemperature = seatTemperature;
    }

    public int getWaterTemperature() {
        return waterTemperature;
    }

    public void setWaterTemperature(int waterTemperature) {
        this.waterTemperature = waterTemperature;
    }

    public int getWaterPressure() {
        return waterPressure;
    }

    public void setWaterPressure(int waterPressure) {
        this.waterPressure = waterPressure;
    }

    public boolean isMusicPlaying() {
        return musicPlaying;
    }

    public void setMusicPlaying(boolean musicPlaying) {
        this.musicPlaying = musicPlaying;
    }

    public boolean isLightOn() {
        return lightOn;
    }

    public void setLightOn(boolean lightOn) {
        this.lightOn = lightOn;
    }

    public boolean isAutoLid() {
        return autoLid;
    }

    public void setAutoLid(boolean autoLid) {
        this.autoLid = autoLid;
    }

    public boolean isUserDetected() {
        return userDetected;
    }

    public void setUserDetected(boolean userDetected) {
        this.userDetected = userDetected;
    }

    public boolean isSeatHeatingEnabled() {
        return seatHeatingEnabled;
    }

    public void setSeatHeatingEnabled(boolean seatHeatingEnabled) {
        this.seatHeatingEnabled = seatHeatingEnabled;
    }

    public boolean isNozzleCleaningEnabled() {
        return nozzleCleaningEnabled;
    }

    public void setNozzleCleaningEnabled(boolean nozzleCleaningEnabled) {
        this.nozzleCleaningEnabled = nozzleCleaningEnabled;
    }

    public boolean isDryingEnabled() {
        return dryingEnabled;
    }

    public void setDryingEnabled(boolean dryingEnabled) {
        this.dryingEnabled = dryingEnabled;
    }

    public boolean isRemoteControlEnabled() {
        return remoteControlEnabled;
    }

    public void setRemoteControlEnabled(boolean remoteControlEnabled) {
        this.remoteControlEnabled = remoteControlEnabled;
    }

    // 播放音乐
    public void playMusic() {
        if (userDetected) {
            musicPlaying = true;
            // 播放音乐的逻辑
        }
    }

    // 控制照明
    public void controlLight(boolean on) {
        if (userDetected) {
            lightOn = on;
            // 控制照明的逻辑
        }
    }

    // 自动开关盖板
    public void autoLidSwitch() {
        if (userDetected) {
            autoLid = !autoLid;
            // 自动开关盖板的逻辑
        }
    }

    // 检测用户
    public void detectUser(boolean detected) {
        userDetected = detected;
        if (!userDetected){
            musicPlaying = false;
            lightOn = false;
            autoLid = false;
        }
        // 检测用户的逻辑
    }

    // 设置座圈加热
    public void setSeatHeating(boolean enabled) {
        seatHeatingEnabled = enabled;
        // 设置座圈加热的逻辑
    }

    // 喷洒清洁剂
    public void sprayCleaningAgent() {
        if (userDetected) {
            nozzleCleaningEnabled = true;
            // 喷洒清洁剂的逻辑
        }
    }

    // 启动风干
    public void enableDrying() {
        if (userDetected) {
            dryingEnabled = true;
            // 启动风干的逻辑
        }
    }

    // 远程控制
    public void enableRemoteControl() {
        remoteControlEnabled = true;
        // 启用远程控制的逻辑
    }

    // 温暖舒适功能依赖于座圈加热功能
    public void enableComfortMode() {
        if (seatHeatingEnabled) {
            // 温暖舒适的逻辑
        }
    }

    // 洁净卫生功能依赖于清洁剂喷洒和冲洗水功能
    public void enableCleanMode() {
        if (nozzleCleaningEnabled && waterPressure > 0) {
            // 洁净卫生的逻辑
        }
    }

    // 获取当前状态
    public String accessCurrentStatus() {
        StringBuilder status = new StringBuilder();
        status.append("座椅温度: ").append(seatTemperature).append("℃\n");
        status.append("冲洗水温度: ").append(waterTemperature).append("℃\n");
        status.append("冲洗水压力: ").append(waterPressure).append("psi\n");
        status.append("音乐播放状态: ").append(musicPlaying ? "开启" : "关闭").append("\n");
        status.append("照明状态: ").append(lightOn ? "开启" : "关闭").append("\n");
        status.append("自动盖板状态: ").append(autoLid ? "开启" : "关闭").append("\n");
        status.append("用户检测状态: ").append(userDetected ? "检测到用户" : "未检测到用户").append("\n");
        status.append("座圈加热状态: ").append(seatHeatingEnabled ? "开启" : "关闭").append("\n");
        status.append("清洁剂喷洒状态: ").append(nozzleCleaningEnabled ? "开启" : "关闭").append("\n");
        status.append("风干功能状态: ").append(dryingEnabled ? "开启" : "关闭").append("\n");
        status.append("远程控制状态: ").append(remoteControlEnabled ? "开启" : "关闭").append("\n");
        return status.toString();
    }
}
