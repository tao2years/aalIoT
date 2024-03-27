package component;
/**

 空调类，用于提供制冷和制热功能。
 属性：
 powerOn：是否开机，取值范围：true or false
 coolMode：是否制冷模式，取值范围：true or false
 heatMode：是否制热模式，取值范围：true or false
 temperature：温度（单位：摄氏度）
 fanSpeed：风速
 构造方法：
 AirConditioner()：初始化所有属性为默认值
 方法：
 setPowerOn(boolean powerOn)：设置是否开机
 isPowerOn()：获取是否开机
 setCoolMode(boolean coolMode)：设置是否制冷模式
 isCoolMode()：获取是否制冷模式
 setHeatMode(boolean heatMode)：设置是否制热模式
 isHeatMode()：获取是否制热模式
 setTemperature(int temperature)：设置温度
 getTemperature()：获取温度
 setFanSpeed(int fanSpeed)：设置风速
 getFanSpeed()：获取风速
 turnOn()：开启空调
 turnOff()：关闭空调
 cool()：制冷
 heat()：制热
 setTemperatureAndFanSpeed(int temperature, int fanSpeed)：设置温度和风速
 accessCurrentStatus()：获取当前状态
 */
public class AirConditioner {
    private boolean powerOn;
    private boolean coolMode;
    private boolean heatMode;
    private int temperature;
    private int fanSpeed;

    public AirConditioner() {
        this.powerOn = false;
        this.coolMode = false;
        this.heatMode = false;
        this.temperature = 25;
        this.fanSpeed = 1;
    }

    // Getter and Setter methods

    public void setPowerOn(boolean powerOn) {
        this.powerOn = powerOn;
    }

    public boolean isPowerOn() {
        return powerOn;
    }

    public void setCoolMode(boolean coolMode) {
        this.coolMode = coolMode;
        if (coolMode) {
            this.heatMode = false; // Cannot be in both cool and heat mode at the same time
        }
    }

    public boolean isCoolMode() {
        return coolMode;
    }

    public void setHeatMode(boolean heatMode) {
        this.heatMode = heatMode;
        if (heatMode) {
            this.coolMode = false; // Cannot be in both heat and cool mode at the same time
        }
    }

    public boolean isHeatMode() {
        return heatMode;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setFanSpeed(int fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    public int getFanSpeed() {
        return fanSpeed;
    }

    // Basic functionality methods

    public void turnOn() {
        powerOn = true;
        System.out.println("Air conditioner turned on");
        // Add logic to turn on the air conditioner
    }

    public void turnOff() {
        powerOn = false;
        System.out.println("Air conditioner turned off");
        // Add logic to turn off the air conditioner
    }

    public void cool() {
        if (powerOn && coolMode) {
            System.out.println("Cooling mode activated");
            // Add logic for cooling
        } else {
            System.out.println("Cannot activate cooling mode");
        }
    }

    public void heat() {
        if (powerOn && heatMode) {
            System.out.println("Heating mode activated");
            // Add logic for heating
        } else {
            System.out.println("Cannot activate heating mode");
        }
    }

    public void setTemperatureAndFanSpeed(int temperature, int fanSpeed) {
        if (powerOn) {
            this.temperature = temperature;
            this.fanSpeed = fanSpeed;
            System.out.println("Temperature set to " + temperature + "°C");
            System.out.println("Fan speed set to " + fanSpeed);
            // Add logic to set temperature and fan speed
        } else {
            System.out.println("Cannot set temperature and fan speed. Air conditioner is turned off");
        }
    }

    // Method to access current device status

    public void accessCurrentStatus() {
        System.out.println("Current status:");
        System.out.println("Power On: " + powerOn);
        System.out.println("Cool Mode: " + coolMode);
        System.out.println("Heat Mode: " + heatMode);
        System.out.println("Temperature: " + temperature + "°C");
        System.out.println("Fan Speed: " + fanSpeed);
    }
}
