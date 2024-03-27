package component;

/**
 冰箱类，用于提供冷藏功能。
 属性：
 powerOn：是否开机，取值范围：true or false
 temperature：温度（单位：摄氏度）
 humidity：湿度（单位：%）
 cooling：是否制冷，取值范围：true or false
 iceMaking：是否制冰，取值范围：true or false
 构造方法：
 Refrigerator()：初始化所有属性为默认值
 方法：
 setPowerOn(boolean powerOn)：设置是否开机
 isPowerOn()：获取是否开机
 setTemperature(int temperature)：设置温度
 getTemperature()：获取温度
 setHumidity(int humidity)：设置湿度
 getHumidity()：获取湿度
 setCooling(boolean cooling)：设置是否制冷
 isCooling()：获取是否制冷
 setIceMaking(boolean iceMaking)：设置是否制冰
 isIceMaking()：获取是否制冰
 accessCurrentStatus()：获取当前状态
 */

public class Refrigerator {
    private boolean powerOn; // 是否开机
    private int temperature; // 温度（单位：摄氏度）
    private int humidity; // 湿度（单位：%）
    private boolean cooling; // 是否制冷
    private boolean iceMaking; // 是否制冰

    public Refrigerator() {
        this.powerOn = false;
        this.temperature = 0;
        this.humidity = 0;
        this.cooling = false;
        this.iceMaking = false;
    }

    public void setPowerOn(boolean powerOn) {
        this.powerOn = powerOn;
    }

    public boolean isPowerOn() {
        return powerOn;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setCooling(boolean cooling) {
        this.cooling = cooling;
    }

    public boolean isCooling() {
        return cooling;
    }

    public void setIceMaking(boolean iceMaking) {
        this.iceMaking = iceMaking;
    }

    public boolean isIceMaking() {
        return iceMaking;
    }

    public void accessCurrentStatus() {
        System.out.println("Current status:");
        System.out.println("Power On: " + powerOn);
        System.out.println("Temperature: " + temperature + "°C");
        System.out.println("Humidity: " + humidity + "%");
        System.out.println("Cooling: " + cooling);
        System.out.println("Ice Making: " + iceMaking);
    }
}