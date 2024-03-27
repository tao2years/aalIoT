package component;
/**

 加湿器类，用于提供调节湿度的功能。
 属性：
 humiditySetting：湿度设定值（单位：%）
 currentHumidity：当前湿度（单位：%）
 mode：工作模式（选项："Auto"，"Manual"）
 waterTankCapacity：水箱容量（单位：毫升）
 isPowerOn：加湿器电源状态（开启/关闭）
 构造方法：
 Humidifier(int humiditySetting, int waterTankCapacity)：
 初始化湿度设定值和水箱容量，其他属性为默认值
 方法：
 setHumidity(int humidity)：设置湿度设定值
 getCurrentHumidity()：获取当前湿度
 setMode(String mode)：设置工作模式
 addWater(int waterAmount)：加水
 powerOn()：开启加湿器
 powerOff()：关闭加湿器
 startHumidifier()：启动加湿器
 stopHumidifier()：停止加湿器
 */
public class Humidifier {
    private int humiditySetting; // Humidity setting in percentage (Range: 0-100)
    private int currentHumidity; // Current humidity level in percentage (Range: 0-100)
    private String mode; // Mode of operation (Options: "Auto", "Manual")
    private int waterTankCapacity; // Water tank capacity in milliliters

    private boolean isPowerOn; // Power status of the humidifier

    public Humidifier(int humiditySetting, int waterTankCapacity) {
        this.humiditySetting = humiditySetting;
        this.waterTankCapacity = waterTankCapacity;
        this.currentHumidity = 0; // Initially, humidity is set to 0
        this.mode = "Auto"; // Default mode is Auto
        this.isPowerOn = false; // Initially, humidifier is powered off
    }

    public void setHumidity(int humidity) {
        if (isPowerOn) {
            if (humidity >= 0 && humidity <= 100) {
                humiditySetting = humidity;
                System.out.println("Humidity setting is now: " + humiditySetting + "%");
            } else {
                System.out.println("Invalid humidity value. Please enter a value between 0 and 100.");
            }
        } else {
            System.out.println("Humidifier is powered off. Please power on the humidifier first.");
        }
    }

    public int getCurrentHumidity() {
        return currentHumidity;
    }

    public void setMode(String mode) {
        if (isPowerOn) {
            if (mode.equals("Auto") || mode.equals("Manual")) {
                this.mode = mode;
                System.out.println("Mode is now set to: " + mode);
            } else {
                System.out.println("Invalid mode. Please select either 'Auto' or 'Manual'.");
            }
        } else {
            System.out.println("Humidifier is powered off. Please power on the humidifier first.");
        }
    }

    public void addWater(int waterAmount) {
        if (isPowerOn) {
            if (waterAmount > 0) {
                if (waterTankCapacity - waterAmount >= 0) {
                    waterTankCapacity -= waterAmount;
                    System.out.println("Added " + waterAmount + "ml of water to the tank.");
                } else {
                    System.out.println("Water tank capacity exceeded. Please add a lower amount of water.");
                }
            } else {
                System.out.println("Invalid water amount. Please enter a positive value.");
            }
        } else {
            System.out.println("Humidifier is powered off. Please power on the humidifier first.");
        }
    }

    public void powerOn() {
        isPowerOn = true;
        System.out.println("Humidifier powered on.");
    }

    public void powerOff() {
        isPowerOn = false;
        System.out.println("Humidifier powered off.");
    }

    public void startHumidifier() {
        if (isPowerOn) {
            if (mode.equals("Manual")) {
                System.out.println("Humidifier started manually. Humidity will not be automatically adjusted.");
            } else {
                System.out.println("Humidifier started in auto mode. Humidity will be adjusted to " + humiditySetting + "%.");
            }
        } else {
            System.out.println("Humidifier is powered off. Please power on the humidifier first.");
        }
    }

    public void stopHumidifier() {
        if (isPowerOn) {
            System.out.println("Humidifier stopped.");
        } else {
            System.out.println("Humidifier is powered off. Please power on the humidifier first.");
        }
    }
}
