package component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

/**
 * 咖啡机类，用于模拟咖啡机的真实运行。
 *
 * 基本属性：
 * - powerOn：是否开机
 * - coffeeBeanGrindLevel：咖啡豆研磨设置，取值范围：0-11
 * （0表示最粗磨，11表示最细磨，研磨消耗时间假定为 2n+10 s, 其中n表示研磨程度）
 * - drinkVolume：饮料量设置，取值范围：0-2（0表示低 160 ml ，1表示中 200 ml，2表示高 240 ml）
 * - aromaIntensity：香味浓度，取值范围：0-2（0表示最温和，2表示最浓郁）
 * - temperature：饮料温度，取值范围：0-2（0表示正常，2表示最大）
 *
 *
 * 全局属性：
 * - waterVolume：水量，取值范围：0-1800，单位ml (0表示无水，1800表示满水)
 * - coffeeBeanVolume：咖啡机的咖啡豆总量，取值范围：0-275，单位 g (0表示无咖啡豆，275表示满咖啡豆)
 * - milkVolume：奶量，取值范围：0-400，单位 ml (0表示无奶，400表示满奶)
 * - cupVolume：杯子容量，取值范围：0-250 (0表示无杯子，250表示满杯子)
 *
 * 功能：
 * - setPowerOn(boolean powerOn)：设置是否开机，true表示开机，false表示关机
 * - addWater(int volume)：加水，volume表示加水量，取值范围：0-1800，单位：ml，返回值表示实际加水量，
 * 如果加水量超过最大值，则返回最大值
 * - addCoffeeBean(int volume)：加咖啡豆，volume表示加咖啡豆量，取值范围：0-275，单位：g，返回值表示实际加咖啡豆量
 * - addMilk(int volume)：加奶，volume表示加奶量，取值范围：0-400，单位：ml，返回值表示实际加奶量
 * - addCup(int volume)：加杯子，volume表示加杯子量，取值范围：0-2，单位：个，0表示不加杯子，1表示加一个杯子，2表示加两个杯子
 * - setCoffeeType(int type)：设置咖啡类型，type表示咖啡类型，取值范围：1-3，1表示美式咖啡，2表示拿铁，3表示卡布奇诺
 * - brewCoffee()：开始冲泡咖啡，返回值表示冲泡是否成功，如果冲泡成功，则返回true，否则返回false,
 * 需要判断是否有足够的水、咖啡豆、奶和杯子 以及电源是否处于开启状态，根据不同的咖啡类型，消耗不同的水、咖啡豆、奶和杯子
 * - setCoffeeBeanGrindLevel(int level)：设置咖啡豆研磨程度，level表示咖啡豆研磨程度，取值范围：0-11, 消耗时间假定为 2n+10 s, 其中n表示研磨程度
 * - setDrinkVolume(int volume)：设置饮料量，volume表示饮料量，取值范围：0-2，0表示低 160 ml ，1表示中 200 ml，2表示高 240 ml
 * - setAromaIntensity(int intensity)：设置香味浓度，intensity表示香味浓度，取值范围：0-2，0表示最温和，2表示最浓郁
 * - setTemperature(int temperature)：设置饮料温度，temperature表示饮料温度，取值范围：0-2，0表示正常，2表示最大
 */

public class CoffeeMachine {
    private static final Logger LOGGER = LogManager.getLogger();
    // 系统状态属性
    private boolean waterReady;
    private boolean beanReady;
    private boolean milkReady;

    private boolean cupReady;
    private boolean thisTimeCoffeeReady;

    private boolean isWorking;
    private boolean isPowerOn;

    // 属性标准设定值
    private static final int MAX_WATER_VOLUME = 1800;
    private static final int MAX_COFFEE_BEAN_VOLUME = 250;
    private static final int MAX_MILK_VOLUME = 400;

    // 系统具体属性
    private int currentWaterVolume;
    private int currentBeanVolume;
    private int currentMilkVolume;

    // 固定资源消耗量
    // Type 1.美式        15g豆 240ml水 0ml奶
    // Type 2.拿铁        15g豆 40ml水 200ml奶
    // Type 3.卡布奇诺     15g豆 40ml水 100ml奶 100ml奶泡 (消耗40ml奶)

    public CoffeeMachine() {
        this.waterReady = false;
        this.beanReady = false;
        this.milkReady = false;
        this.cupReady = false;
        this.thisTimeCoffeeReady = false;
        this.isWorking = false;
        this.isPowerOn = false;
        this.currentWaterVolume = 0;
        this.currentBeanVolume = 0;
        this.currentMilkVolume = 0;
    }

    public CoffeeMachine(boolean waterReady, boolean beanReady, boolean milkReady, boolean cupReady, boolean thisTimeCoffeeReady, boolean isWorking, boolean isPowerOn, int currentWaterVolume, int currentBeanVolume, int currentMilkVolume) {
        this.waterReady = waterReady;
        this.beanReady = beanReady;
        this.milkReady = milkReady;
        this.cupReady = cupReady;
        this.thisTimeCoffeeReady = thisTimeCoffeeReady;
        this.isWorking = isWorking;
        this.isPowerOn = isPowerOn;
        this.currentWaterVolume = currentWaterVolume;
        this.currentBeanVolume = currentBeanVolume;
        this.currentMilkVolume = currentMilkVolume;
    }

    public CoffeeMachine(Object waterReady, String beanReady, String milkReady, String cupReady, String thisTimeCoffeeReady, String isWorking, String isPowerOn, String currentWaterVolume, String currentBeanVolume, String currentMilkVolume) {
        this.waterReady = Boolean.parseBoolean(waterReady.toString());
        this.beanReady = Boolean.parseBoolean(beanReady);
        this.milkReady = Boolean.parseBoolean(milkReady);
        this.cupReady = Boolean.parseBoolean(cupReady);
        this.thisTimeCoffeeReady = Boolean.parseBoolean(thisTimeCoffeeReady);
        this.isWorking = Boolean.parseBoolean(isWorking);
        this.isPowerOn = Boolean.parseBoolean(isPowerOn);
        this.currentWaterVolume = Integer.parseInt(currentWaterVolume);
        this.currentBeanVolume = Integer.parseInt(currentBeanVolume);
        this.currentMilkVolume = Integer.parseInt(currentMilkVolume);
    }

    public boolean isWaterReady() {
        return waterReady;
    }

    public void setWaterReady(boolean waterReady) {
        this.waterReady = waterReady;
    }

    public boolean isBeanReady() {
        return beanReady;
    }

    public void setBeanReady(boolean beanReady) {
        this.beanReady = beanReady;
    }

    public boolean isMilkReady() {
        return milkReady;
    }

    public void setMilkReady(boolean milkReady) {
        this.milkReady = milkReady;
    }

    public boolean isCupReady() {
        return cupReady;
    }

    public void setCupReady(boolean cupReady) {
        this.cupReady = cupReady;
    }

    public boolean isThisTimeCoffeeReady() {
        return thisTimeCoffeeReady;
    }

    public void setThisTimeCoffeeReady(boolean thisTimeCoffeeReady) {
        this.thisTimeCoffeeReady = thisTimeCoffeeReady;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }

    public void setPowerOn(boolean powerOn) {
        isPowerOn = powerOn;
    }

    public boolean isPowerOn() {
        return isPowerOn;
    }

    public int getCurrentWaterVolume() {
        return currentWaterVolume;
    }

    public void setCurrentWaterVolume(int currentWaterVolume) {
        this.currentWaterVolume = currentWaterVolume;
    }

    public int getCurrentBeanVolume() {
        return currentBeanVolume;
    }

    public void setCurrentBeanVolume(int currentBeanVolume) {
        this.currentBeanVolume = currentBeanVolume;
    }

    public int getCurrentMilkVolume() {
        return currentMilkVolume;
    }

    public void setCurrentMilkVolume(int currentMilkVolume) {
        this.currentMilkVolume = currentMilkVolume;
    }

    public void turnOn() {
        this.isPowerOn = true;
    }

    public void turnOff() {
        this.isPowerOn = false;
    }

    public void addWater(){
        if (isPowerOn){
            int addWaterVolume = new Random().nextInt(MAX_WATER_VOLUME);
            currentWaterVolume = Math.min(currentWaterVolume + addWaterVolume, MAX_WATER_VOLUME);
//            LOGGER.info("当前水量：" + currentWaterVolume);
            if (currentWaterVolume > 250) {
                waterReady = true;
            }
        }
    }

    public void addCoffeeBean(){
        if (isPowerOn) {
            int addCoffeeBeanVolume = new Random().nextInt(MAX_COFFEE_BEAN_VOLUME);
            currentBeanVolume = Math.min(currentBeanVolume + addCoffeeBeanVolume, MAX_COFFEE_BEAN_VOLUME);
//            LOGGER.info("当前咖啡豆量：" + currentBeanVolume);
            if (currentBeanVolume > 15) {
                beanReady = true;
            }
        }
    }

    public void addMilk(){
        if (isPowerOn) {
            int addMilkVolume = new Random().nextInt(MAX_MILK_VOLUME);
            currentMilkVolume = Math.min(currentMilkVolume + addMilkVolume, MAX_MILK_VOLUME);
//            LOGGER.info("当前牛奶量：" + currentMilkVolume);
            if (currentMilkVolume > 200) {
                milkReady = true;
            }
        }
    }

    public void placeCup(){
        this.cupReady = true;
    }

    public String brewCoffee(int coffeeType){
        if (!isPowerOn){
            return "请先开机";
        }
        if (!cupReady){
            return "请先放杯子";
        }
        if (thisTimeCoffeeReady){
            return "请先取走上一杯咖啡";
        }
        // Type 1.美式        15g豆 240ml水 0ml奶
        // Type 2.拿铁        15g豆 40ml水 200ml奶
        // Type 3.卡布奇诺     15g豆 40ml水 100ml奶 100ml奶泡 (消耗40ml奶)
        switch (coffeeType){
            case 1:
                if (currentWaterVolume < 240){
                    this.waterReady = false;
                    return "请先加水";
                }
                if (currentBeanVolume < 15){
                    this.beanReady = false;
                    return "请先加咖啡豆";
                }
                break;
            case 2:
                if (currentWaterVolume < 40){
                    this.waterReady = false;
                    return "请先加水";
                }
                if (currentBeanVolume < 15){
                    this.beanReady = false;
                    return "请先加咖啡豆";
                }
                if (currentMilkVolume < 200){
                    this.milkReady = false;
                    return "请先加牛奶";
                }
                break;
            case 3:
                if (currentWaterVolume < 40){
                    this.waterReady = false;
                    return "请先加水";
                }
                if (currentBeanVolume < 15){
                    this.beanReady = false;
                    return "请先加咖啡豆";
                }
                if (currentMilkVolume < 140){
                    this.milkReady = false;
                    return "请先加牛奶";
                }
                break;
            default:
                return "请输入正确的咖啡类型";
        }
        makingCoffee(coffeeType);
        this.thisTimeCoffeeReady = true;
        return "Success";
    }

    private void makingCoffee(int coffeeType){
        this.isWorking = true;
//        this.waterReady = true;
//        this.milkReady = true;
//        this.beanReady = true;
        switch (coffeeType){
            case 1:
                currentWaterVolume -= 240;
                currentBeanVolume -= 15;
//                this.milkReady = false;
                break;
            case 2:
                currentWaterVolume -= 40;
                currentBeanVolume -= 15;
                currentMilkVolume -= 200;
                break;
            case 3:
                currentWaterVolume -= 40;
                currentBeanVolume -= 15;
                currentMilkVolume -= 140;
                break;
        }
//        wait(5);
        if (currentBeanVolume < 15){
            this.beanReady = false;
        } else {
            this.beanReady = true;
        }

        if (currentMilkVolume < 200){
            this.milkReady = false;
        } else {
            this.milkReady = true;
        }

        if (currentWaterVolume < 250){
            this.waterReady = false;
        } else {
            this.waterReady = true;
        }

        this.isWorking = false;
    }

    public void fetchCoffee(){
        if (this.thisTimeCoffeeReady){
            this.thisTimeCoffeeReady = false;
        }
        this.cupReady = false;
    }


    public String toString() {
        return "CoffeeMachine{" +
                "'waterReady':'" + waterReady + '\'' +
                ", 'beanReady':'" + beanReady + '\'' +
                ", 'milkReady':'" + milkReady + '\'' +
                ", 'cupReady':'" + cupReady + '\'' +
                ", 'thisTimeCoffeeReady':'" + thisTimeCoffeeReady + '\'' +
                ", 'isWorking':'" + isWorking + '\'' +
                ", 'isPowerOn':'" + isPowerOn + '\'' +
                ", 'currentWaterVolume':'" + currentWaterVolume + '\'' +
                ", 'currentBeanVolume':'" + currentBeanVolume + '\'' +
                ", 'currentMilkVolume':'" + currentMilkVolume + '\'' +
                '}';
    }

    public static CoffeeMachine fromString(String input) {
        JSONObject json = JSON.parseObject(input.replace("CoffeeMachine", ""));
        return new CoffeeMachine(json.get("waterReady"), json.get("beanReady").toString(), json.get("milkReady").toString(), json.get("cupReady").toString(), json.get("thisTimeCoffeeReady").toString(), json.get("isWorking").toString(), json.get("isPowerOn").toString(), json.get("currentWaterVolume").toString(), json.get("currentBeanVolume").toString(), json.get("currentMilkVolume").toString());
    }

    public String toSystemStateString() {
        return "CoffeeMachine{" +
                "'waterReady':'" + waterReady + '\'' +
                ", 'beanReady':'" + beanReady + '\'' +
                ", 'milkReady':'" + milkReady + '\'' +
                ", 'cupReady':'" + cupReady + '\'' +
                ", 'thisTimeCoffeeReady':'" + thisTimeCoffeeReady + '\'' +
                ", 'isWorking':'" + isWorking + '\'' +
                ", 'isPowerOn':'" + isPowerOn + '\'' +
                '}';
    }

    public JSONObject getJsonState() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("waterReady", waterReady);
        jsonObject.put("beanReady", beanReady);
        jsonObject.put("milkReady", milkReady);
        jsonObject.put("cupReady", cupReady);
        jsonObject.put("thisTimeCoffeeReady", thisTimeCoffeeReady);
        jsonObject.put("isWorking", isWorking);
        jsonObject.put("isPowerOn", isPowerOn);
        jsonObject.put("currentWaterVolume", currentWaterVolume);
        jsonObject.put("currentBeanVolume", currentBeanVolume);
        jsonObject.put("currentMilkVolume", currentMilkVolume);
        return jsonObject;
    }

    private void wait(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 测试开机和关机功能
        CoffeeMachine coffeeMachine = new CoffeeMachine();
        coffeeMachine.turnOn();
        // 验证开机后的状态
        assert coffeeMachine.isPowerOn();

        coffeeMachine.turnOff();
        // 验证关机后的状态
        assert !coffeeMachine.isPowerOn();

        // 测试加水功能
        coffeeMachine.turnOn();

        coffeeMachine.addWater();
        // 验证当前水容量是否小于等于最大水容量
        assert coffeeMachine.getCurrentWaterVolume() <= CoffeeMachine.MAX_WATER_VOLUME;

        // 连续加水两次，验证当前水容量是否小于等于最大水容量
        coffeeMachine.addWater();
        assert coffeeMachine.getCurrentWaterVolume() <= CoffeeMachine.MAX_WATER_VOLUME;

        // 测试加咖啡豆功能
        coffeeMachine.addCoffeeBean();
        // 验证当前咖啡豆容量是否小于等于最大咖啡豆容量
        assert coffeeMachine.getCurrentBeanVolume() <= CoffeeMachine.MAX_COFFEE_BEAN_VOLUME;

        // 连续加咖啡豆两次，验证当前咖啡豆容量是否小于等于最大咖啡豆容量
        coffeeMachine.addCoffeeBean();
        assert coffeeMachine.getCurrentBeanVolume() <= CoffeeMachine.MAX_COFFEE_BEAN_VOLUME;

        // 测试加牛奶功能
        coffeeMachine.addMilk();
        // 验证当前牛奶容量是否小于等于最大牛奶容量
        assert coffeeMachine.getCurrentMilkVolume() <= CoffeeMachine.MAX_MILK_VOLUME;

        // 连续加牛奶两次，验证当前牛奶容量是否小于等于最大牛奶容量
        coffeeMachine.addMilk();
        assert coffeeMachine.getCurrentMilkVolume() <= CoffeeMachine.MAX_MILK_VOLUME;

        // 测试放杯子功能
        coffeeMachine.placeCup();
        // 验证杯子准备状态是否为true
        assert coffeeMachine.isCupReady() == true;

        // 测试冲泡咖啡功能
        coffeeMachine.brewCoffee(1);
        // 验证返回结果是否为"Success"
        assert coffeeMachine.isThisTimeCoffeeReady() == true;
        // 验证当前水容量是否减少240
        assert coffeeMachine.getCurrentWaterVolume() == 1800 - 240;
        // 验证当前咖啡豆容量是否减少15
        assert coffeeMachine.getCurrentBeanVolume() == 250 - 15;

        coffeeMachine.brewCoffee(2);
        coffeeMachine.brewCoffee(3);
        // 验证返回结果是否为"Success"
        assert coffeeMachine.isThisTimeCoffeeReady() == true;
        // 验证当前水容量是否减少40 x 2
        assert coffeeMachine.getCurrentWaterVolume() == 1800 - 240 - 40 * 2;
        // 验证当前咖啡豆容量是否减少15 x 2
        assert coffeeMachine.getCurrentBeanVolume() == 250 - 15 * 2;
        // 验证当前牛奶容量是否减少200 + 140
        assert coffeeMachine.getCurrentMilkVolume() == 400 - 200 - 140;

        System.out.println("All tests passed!");
    }

}
