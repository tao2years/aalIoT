package System_q;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 模拟洗衣机的类
 *
 * 这个类模拟了洗衣机的基本功能，包括开关机、
 * 开关门、注水、洗涤、漂洗和脱水等功能。
 */
public class WashingMachine {
    private boolean powerOn; // 洗衣机开关机状态
    private boolean doorOpen; // 洗衣机门是否打开
    private boolean waterIn; // 水是否注入
    private boolean washing; // 是否正在洗涤
    private boolean rinsing; // 是否正在漂洗
    private boolean spinning; // 是否正在脱水

    public WashingMachine() {
        this.powerOn = false;
        this.doorOpen = false;
        this.waterIn = false;
        this.washing = false;
        this.rinsing = false;
        this.spinning = false;
    }

    public WashingMachine(Boolean powerOn, Boolean doorOpen, Boolean waterIn, Boolean washing, Boolean rinsing, Boolean spinning) {
        this.powerOn = powerOn;
        this.doorOpen = doorOpen;
        this.waterIn = waterIn;
        this.washing = washing;
        this.rinsing = rinsing;
        this.spinning = spinning;
    }

    public String turnOn() {
        if (!powerOn) {
            powerOn = true;
            return "success";
        } else {
            return "skip";
        }
    }

    public String turnOff() {
        if (powerOn) {
            powerOn = false;
            return "success";
        } else {
            return "skip";
        }
    }

    public String openDoor() {
        if (!doorOpen) {
            doorOpen = true;
            return "success";
        } else {
            return "skip";
        }
    }

    public String closeDoor() {
        if (doorOpen) {
            doorOpen = false;
            return "success";
        } else {
            return "skip";
        }
    }

    public String fillWater() {
        if (powerOn && !waterIn) {
            waterIn = true;
            return "success";
        } else if (!powerOn) {
            return "skip";
        } else {
            return "skip";
        }
    }

    public String startWashing() {
        if (powerOn && !doorOpen && waterIn && !washing && !rinsing && !spinning) {
            washing = true;
            return "success";
        } else {
            return "skip";
        }
    }

    public String startRinsing() {
        if (powerOn && !doorOpen && waterIn && washing && !rinsing && !spinning) {
            rinsing = true;
            return "success";
        } else {
            return "skip";
        }
    }

    public String startSpinning() {
        if (powerOn && !doorOpen && waterIn && washing && rinsing && !spinning) {
            spinning = true;
            return "success";
        } else {
            return "skip";
        }
    }

    public String stop() {
        if (washing || rinsing || spinning) {
            washing = false;
            rinsing = false;
            spinning = false;
            return "success";
        } else {
            return "skip";
        }
    }

    @Override
    public String toString() {
        return "WashingMachine{" +
                "'powerOn':" + powerOn +
                ", 'doorOpen':" + doorOpen +
                ", 'waterIn':" + waterIn +
                ", 'washing':" + washing +
                ", 'rinsing':" + rinsing +
                ", 'spinning':" + spinning +
                "}";
    }

    public static WashingMachine fromString(String input) {
        JSONObject json = JSON.parseObject(input.replace("WashingMachine", ""));
        return new WashingMachine(json.getBoolean("powerOn"), json.getBoolean("doorOpen"), json.getBoolean("waterIn"), json.getBoolean("washing"), json.getBoolean("rinsing"), json.getBoolean("spinning"));
    }


    public static void main(String[] args) {
        WashingMachine washingMachine = new WashingMachine();
        washingMachine.turnOn();
        washingMachine.fillWater();
        washingMachine.startWashing();
        washingMachine.startRinsing();
        washingMachine.startSpinning();
        washingMachine.stop();
        washingMachine.turnOff();
        System.out.println(washingMachine.toString());
    }
}

