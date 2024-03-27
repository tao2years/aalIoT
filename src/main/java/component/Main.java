package component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class _Device {
    private boolean state;
    private int version;
    private Lock lock = new ReentrantLock();

    public _Device(boolean initialState, int initialVersion) {
        this.state = initialState;
        this.version = initialVersion;
    }

    public boolean getState() {
        return state;
    }

    public int getVersion() {
        return version;
    }

    public void setState(boolean newState) {
        this.state = newState;
    }

    public void setVersion(int newVersion) {
        this.version = newVersion;
    }

    public Lock getLock() {
        return lock;
    }
}

class _CommandHandler {
    public static void executeWriteCommand(_Device device, boolean newState) {
        Lock deviceLock = device.getLock();
        deviceLock.lock(); // 获取设备级锁定
        try {
            // 读取设备状态和版本号
            int currentVersion = device.getVersion();

            // 模拟设备崩溃
            if (Math.random() < 0.5) {
                throw new RuntimeException("Simulated device crash!");
            }

            // 模拟延迟操作
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 执行写操作
            device.setState(newState);
            device.setVersion(currentVersion + 1);

            System.out.println("Write command executed successfully. New state: " + newState + ", New version: " + (currentVersion + 1));
        } catch (RuntimeException e) {
            System.out.println("Exception occurred during write command: " + newState + " - " + e.getMessage() + ". Retrying...");
            executeWriteCommand(device, newState); // 重新执行写命令
        } finally {
            deviceLock.unlock(); // 释放设备级锁定
        }
    }

    public static boolean executeReadCommand(_Device device) {
        Lock deviceLock = device.getLock();
        deviceLock.lock(); // 获取设备级锁定
        try {
            // 读取设备状态
            boolean currentState = device.getState();
            System.out.println("Read command executed successfully. Current state: " + currentState + "Current version: " + device.getVersion());
            return currentState;
        } finally {
            deviceLock.unlock(); // 释放设备级锁定
        }
    }
}

public class Main {
    public static void main(String[] args) {
        _Device device = new _Device(false, 1);

        // 模拟并发执行写命令
        Thread writeCommand1 = new Thread(() -> _CommandHandler.executeWriteCommand(device, true));
        Thread writeCommand2 = new Thread(() -> _CommandHandler.executeWriteCommand(device, false));

        // 模拟读命令
        Thread readCommand = new Thread(() -> _CommandHandler.executeReadCommand(device));

        writeCommand1.start();
        writeCommand2.start();
        readCommand.start();

        // 等待线程执行完毕
        try {
            writeCommand1.join();
            writeCommand2.join();
            readCommand.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 检查设备状态和版本是否正确更新
        System.out.println("Final state: " + device.getState() + ", Final version: " + device.getVersion());
    }
}
