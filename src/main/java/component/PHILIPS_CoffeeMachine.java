package component;

/**
 * 1. 冲煮饮品
 *
 * 一般步骤
 * (1) 加水、放咖啡豆
 * (2) 打开开关，咖啡机加热，自动冲洗，等待所有指示灯都亮
 * (3) 把杯子放在出水口下方，上下滑动出水口调整出水口高度
 *
 * 饮品个性化定制
 * (1) 香味浓度，3种, 最温和、中等、最浓郁
 * (2) 饮料量 / 奶量, 3种 低、中、高
 *
 * 预先配置 可选
 * (1) 香味设置
 * (2) 饮料量 / 奶量设置
 * (3) 温度设置, 3种 正常、高、最大
 * (4) 咖啡豆研磨设置, 12档
 *
 * 中途干预：
 * (1) 中途可以点击停止/启动按钮
 */

import java.util.Objects;

/**
 * Philips咖啡机类，用于冲煮饮品。
 *
 * 属性：
 * - powerOn：是否开机
 * - waterAdded：是否加水
 * - coffeeBeansAdded：是否放入咖啡豆
 * - switchOn：是否打开开关
 * - heating：是否正在加热
 * - autoRinse：是否自动冲洗
 * - allLightsOn：是否所有指示灯都亮
 * - cupPlaced：是否放置杯子
 * - aromaIntensity：香味浓度，取值范围：0-2（0表示最温和，2表示最浓郁）
 * - drinkVolume：饮料量/奶量，取值范围：0-2（0表示低，2表示高）
 * - milkVolume：奶量，取值范围：0-2（0表示低，2表示高）
 * - temperature：温度，取值范围：0-2（0表示正常，2表示最大）
 * - coffeeBeanGrindLevel：咖啡豆研磨设置，取值范围：0-11（0表示最粗磨，11表示最细磨）
 *
 * 构造方法：
 * - PHILIPS_CoffeeMachine()：初始化所有属性为默认值
 *
 * 方法：
 * - setPowerOn(boolean powerOn)：设置是否开机
 * - isPowerOn()：获取是否开机
 * - setWaterAdded(boolean waterAdded)：设置是否加水
 * - isWaterAdded()：获取是否加水
 * - setCoffeeBeansAdded(boolean coffeeBeansAdded)：设置是否放入咖啡豆
 * - isCoffeeBeansAdded()：获取是否放入咖啡豆
 * - setSwitchOn(boolean switchOn)：设置是否打开开关
 * - isSwitchOn()：获取是否打开开关
 * - setHeating(boolean heating)：设置是否正在加热
 * - isHeating()：获取是否正在加热
 * - setAutoRinse(boolean autoRinse)：设置是否自动冲洗
 * - isAutoRinse()：获取是否自动冲洗
 * - setAllLightsOn(boolean allLightsOn)：设置是否所有指示灯都亮
 * - isAllLightsOn()：获取是否所有指示灯都亮
 * - setCupPlaced(boolean cupPlaced)：设置是否放置杯子
 * - isCupPlaced()：获取是否放置杯子
 * - setAromaIntensity(int aromaIntensity)：设置香味浓度
 * - getAromaIntensity()：获取香味浓度
 * - setDrinkVolume(int drinkVolume)：设置饮料量
 * - getDrinkVolume()：获取饮料量
 * - setMilkVolume(int milkVolume)：设置奶量
 * - getMilkVolume()：获取奶量
 * - setTemperature(int temperature)：设置温度
 * - getTemperature()：获取温度
 * - setCoffeeBeanGrindLevel(int coffeeBeanGrindLevel)：设置咖啡豆研磨设置
 * - getCoffeeBeanGrindLevel()：获取咖啡豆研磨设置
 * - brewBeverage()：冲泡饮品
 * - stopBrewing()：停止冲泡
 * - accessCurrentStatus()：获取当前状态
 */
public class PHILIPS_CoffeeMachine {
    private boolean powerOn;
    private boolean waterAdded;
    private boolean coffeeBeansAdded;
    private boolean switchOn;
    private boolean heating;
    private boolean autoRinse;
    private boolean allLightsOn;
    private boolean cupPlaced;
    private int aromaIntensity;
    private int drinkVolume;
    private int milkVolume;
    private int temperature;
    private int coffeeBeanGrindLevel;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PHILIPS_CoffeeMachine)) return false;
        PHILIPS_CoffeeMachine that = (PHILIPS_CoffeeMachine) o;
        return isPowerOn() == that.isPowerOn() && isWaterAdded() == that.isWaterAdded() && isCoffeeBeansAdded() == that.isCoffeeBeansAdded() && isSwitchOn() == that.isSwitchOn() && isHeating() == that.isHeating() && isAutoRinse() == that.isAutoRinse() && isAllLightsOn() == that.isAllLightsOn() && isCupPlaced() == that.isCupPlaced() && getAromaIntensity() == that.getAromaIntensity() && getDrinkVolume() == that.getDrinkVolume() && getMilkVolume() == that.getMilkVolume() && getTemperature() == that.getTemperature() && getCoffeeBeanGrindLevel() == that.getCoffeeBeanGrindLevel();
    }

    @Override
    public int hashCode() {
        return Objects.hash(isPowerOn(), isWaterAdded(), isCoffeeBeansAdded(), isSwitchOn(), isHeating(), isAutoRinse(), isAllLightsOn(), isCupPlaced(), getAromaIntensity(), getDrinkVolume(), getMilkVolume(), getTemperature(), getCoffeeBeanGrindLevel());
    }

    public PHILIPS_CoffeeMachine() {
        this.powerOn = false;
        this.waterAdded = false;
        this.coffeeBeansAdded = false;
        this.switchOn = false;
        this.heating = false;
        this.autoRinse = false;
        this.allLightsOn = false;
        this.cupPlaced = false;
        this.aromaIntensity = 0;
        this.drinkVolume = 0;
        this.milkVolume = 0;
        this.temperature = 0;
        this.coffeeBeanGrindLevel = 0;
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

    public void setCoffeeBeansAdded(boolean coffeeBeansAdded) {
        this.coffeeBeansAdded = coffeeBeansAdded;
    }

    public boolean isCoffeeBeansAdded() {
        return coffeeBeansAdded;
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

    public void setAutoRinse(boolean autoRinse) {
        this.autoRinse = autoRinse;
    }

    public boolean isAutoRinse() {
        return autoRinse;
    }

    public void setAllLightsOn(boolean allLightsOn) {
        this.allLightsOn = allLightsOn;
    }

    public boolean isAllLightsOn() {
        return allLightsOn;
    }

    public void setCupPlaced(boolean cupPlaced) {
        this.cupPlaced = cupPlaced;
    }

    public boolean isCupPlaced() {
        return cupPlaced;
    }

    public void setAromaIntensity(int aromaIntensity) {
        this.aromaIntensity = aromaIntensity;
    }

    public int getAromaIntensity() {
        return aromaIntensity;
    }

    public void setDrinkVolume(int drinkVolume) {
        this.drinkVolume = drinkVolume;
    }

    public int getDrinkVolume() {
        return drinkVolume;
    }

    public void setMilkVolume(int milkVolume) {
        this.milkVolume = milkVolume;
    }

    public int getMilkVolume() {
        return milkVolume;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setCoffeeBeanGrindLevel(int coffeeBeanGrindLevel) {
        this.coffeeBeanGrindLevel = coffeeBeanGrindLevel;
    }

    public int getCoffeeBeanGrindLevel() {
        return coffeeBeanGrindLevel;
    }

    public void brewBeverage() {
        if (powerOn && waterAdded && coffeeBeansAdded && switchOn && heating && autoRinse && allLightsOn && cupPlaced) {
            System.out.println("Brewing beverage...");
        } else {
            System.out.println("Cannot brew beverage. Check if all requirements are met.");
        }
    }

    public void stopBrewing() {
        System.out.println("Brewing stopped.");
    }

    public void accessCurrentStatus() {
        System.out.println("Current status:");
        System.out.println("Power On: " + powerOn);
        System.out.println("Water Added: " + waterAdded);
        System.out.println("Coffee Beans Added: " + coffeeBeansAdded);
        System.out.println("Switch On: " + switchOn);
        System.out.println("Heating: " + heating);
        System.out.println("Auto Rinse: " + autoRinse);
        System.out.println("All Lights On: " + allLightsOn);
        System.out.println("Cup Placed: " + cupPlaced);
        System.out.println("Aroma Intensity: " + aromaIntensity);
        System.out.println("Drink Volume: " + drinkVolume);
        System.out.println("Milk Volume: " + milkVolume);
        System.out.println("Temperature: " + temperature);
        System.out.println("Coffee Bean Grind Level: " + coffeeBeanGrindLevel);
    }
}