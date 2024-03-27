package component;

/**
 洗衣机类，用于提供洗衣功能。
 属性：
 powerOn：是否开机，取值范围：true or false
 waterAdded：是否加水，取值范围：true or false
 detergentAdded：是否加洗涤剂，取值范围：true or false
 switchOn：是否开关打开，取值范围：true or false
 heating：是否加热，取值范围：true or false
 spinning：是否旋转，取值范围：true or false
 allLightsOn：是否所有灯都亮，取值范围：true or false
 washMode：洗涤模式（0-无模式，1-标准模式，2-快速模式，3-深层清洁模式）
 washTime：洗涤时间（单位：分钟）
 washIntensity：洗涤强度（1-低强度，2-中等强度，3-高强度）
 waterLevel：水位（1-低水位，2-中等水位，3-高水位）
 构造方法：
 WashingMachine()：初始化所有属性为默认值
 方法：
 setPowerOn(boolean powerOn)：设置是否开机
 isPowerOn()：获取是否开机
 setWaterAdded(boolean waterAdded)：设置是否加水
 isWaterAdded()：获取是否加水
 setDetergentAdded(boolean detergentAdded)：设置是否加洗涤剂
 isDetergentAdded()：获取是否加洗涤剂
 setSwitchOn(boolean switchOn)：设置是否开关打开
 isSwitchOn()：获取是否开关打开
 setHeating(boolean heating)：设置是否加热
 isHeating()：获取是否加热
 setSpinning(boolean spinning)：设置是否旋转
 isSpinning()：获取是否旋转
 setAllLightsOn(boolean allLightsOn)：设置是否所有灯都亮
 isAllLightsOn()：获取是否所有灯都亮
 setWashMode(int washMode)：设置洗涤模式
 getWashMode()：获取洗涤模式
 setWashTime(int washTime)：设置洗涤时间
 getWashTime()：获取洗涤时间
 setWashIntensity(int washIntensity)：设置洗涤强度
 getWashIntensity()：获取洗涤强度
 setWaterLevel(int waterLevel)：设置水位
 getWaterLevel()：获取水位
 startWashing()：开始洗衣流程
 pauseWashing()：暂停洗衣流程
 accessCurrentStatus()：获取当前状态
 */
public class WashingMachine {
    private boolean powerOn; // 是否开机
    private boolean waterAdded; // 是否加水
    private boolean detergentAdded; // 是否加洗涤剂
    private boolean switchOn; // 是否开关打开
    private boolean heating; // 是否加热
    private boolean spinning; // 是否旋转
    private boolean allLightsOn; // 是否所有灯都亮
    private int washMode; // 洗涤模式（0-无模式，1-标准模式，2-快速模式，3-深层清洁模式）
    private int washTime; // 洗涤时间（单位：分钟）
    private int washIntensity; // 洗涤强度（1-低强度，2-中等强度，3-高强度）
    private int waterLevel; // 水位（1-低水位，2-中等水位，3-高水位）

    public WashingMachine() {
        this.powerOn = false;
        this.waterAdded = false;
        this.detergentAdded = false;
        this.switchOn = false;
        this.heating = false;
        this.spinning = false;
        this.allLightsOn = false;
        this.washMode = 0;
        this.washTime = 0;
        this.washIntensity = 0;
        this.waterLevel = 0;
    }

    public void setPowerOn(boolean powerOn) {
        this.powerOn = powerOn;
    }

    public boolean isPowerOn() {
        return powerOn;
    }

    public void setWaterAdded(boolean waterAdded) {
        this.waterAdded = waterAdded;
    }

    public boolean isWaterAdded() {
        return waterAdded;
    }

    public void setDetergentAdded(boolean detergentAdded) {
        this.detergentAdded = detergentAdded;
    }

    public boolean isDetergentAdded() {
        return detergentAdded;
    }

    public void setSwitchOn(boolean switchOn) {
        this.switchOn = switchOn;
    }

    public boolean isSwitchOn() {
        return switchOn;
    }

    public void setHeating(boolean heating) {
        this.heating = heating;
    }

    public boolean isHeating() {
        return heating;
    }

    public void setSpinning(boolean spinning) {
        this.spinning = spinning;
    }

    public boolean isSpinning() {
        return spinning;
    }

    public void setAllLightsOn(boolean allLightsOn) {
        this.allLightsOn = allLightsOn;
    }

    public boolean isAllLightsOn() {
        return allLightsOn;
    }

    public void setWashMode(int washMode) {
        this.washMode = washMode;
    }

    public int getWashMode() {
        return washMode;
    }

    public void setWashTime(int washTime) {
        this.washTime = washTime;
    }

    public int getWashTime() {
        return washTime;
    }

    public void setWashIntensity(int washIntensity) {
        this.washIntensity = washIntensity;
    }

    public int getWashIntensity() {
        return washIntensity;
    }

    public void setWaterLevel(int waterLevel) {
        this.waterLevel = waterLevel;
    }

    public int getWaterLevel() {
        return waterLevel;
    }

    public void startWashing() {
        if (!powerOn) {
            System.out.println("Cannot start washing. Washing machine is not powered on.");
            return;
        }

        if (!waterAdded) {
            System.out.println("Cannot start washing. Water is not added.");
            return;
        }

        if (!detergentAdded) {
            System.out.println("Cannot start washing. Detergent is not added.");
            return;
        }

        if (!switchOn) {
            System.out.println("Cannot start washing. Switch is not turned on.");
            return;
        }

        if (!heating) {
            System.out.println("Cannot start washing. Machine is not heating.");
            return;
        }

        if (!spinning) {
            System.out.println("Cannot start washing. Machine is not spinning.");
            return;
        }

        if (!allLightsOn) {
            System.out.println("Cannot start washing. All lights are not on.");
            return;
        }

        if (washMode == 0) {
            System.out.println("Cannot start washing. Wash mode is not selected.");
            return;
        }

        if (washTime <= 0) {
            System.out.println("Cannot start washing. Wash time is not set.");
            return;
        }

        if (washIntensity <= 0) {
            System.out.println("Cannot start washing. Wash intensity is not set.");
            return;
        }

        if (waterLevel <= 0) {
            System.out.println("Cannot start washing. Water level is not set.");
            return;
        }

        System.out.println("Starting washing process...");
        // Code to start the washing process
    }

    public void pauseWashing() {
        if (!powerOn) {
            System.out.println("Cannot pause washing. Washing machine is not powered on.");
            return;
        }

        if (!waterAdded) {
            System.out.println("Cannot pause washing. Water is not added.");
            return;
        }

        if (!detergentAdded) {
            System.out.println("Cannot pause washing. Detergent is not added.");
            return;
        }

        if (!switchOn) {
            System.out.println("Cannot pause washing. Switch is not turned on.");
            return;
        }

        if (!heating) {
            System.out.println("Cannot pause washing. Machine is not heating.");
            return;
        }

        if (!spinning) {
            System.out.println("Cannot pause washing. Machine is not spinning.");
            return;
        }

        if (!allLightsOn) {
            System.out.println("Cannot pause washing. All lights are not on.");
            return;
        }

        if (washMode == 0) {
            System.out.println("Cannot pause washing. Wash mode is not selected.");
            return;
        }

        if (washTime <= 0) {
            System.out.println("Cannot pause washing. Wash time is not set.");
            return;
        }

        if (washIntensity <= 0) {
            System.out.println("Cannot pause washing. Wash intensity is not set.");
            return;
        }

        if (waterLevel <= 0) {
            System.out.println("Cannot pause washing. Water level is not set.");
            return;
        }

        System.out.println("Pausing washing process...");
        // Code to pause the washing process
    }

    public void accessCurrentStatus() {
        System.out.println("Current status:");
        System.out.println("Power On: " + powerOn);
        System.out.println("Water Added: " + waterAdded);
        System.out.println("Detergent Added: " + detergentAdded);
        System.out.println("Switch On: " + switchOn);
        System.out.println("Heating: " + heating);
        System.out.println("Spinning: " + spinning);
        System.out.println("All Lights On: " + allLightsOn);
        System.out.println("Wash Mode: " + washMode);
        System.out.println("Wash Time: " + washTime);
        System.out.println("Wash Intensity: " + washIntensity);
        System.out.println("Water Level: " + waterLevel);
    }
}