package component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Device {
    private boolean state;
    private int version;
    private Lock lock = new ReentrantLock();

    public Device(boolean initialState, int initialVersion) {
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

class CommandHandler {
    public static void executeWriteCommand(Device device, boolean newState, int expectedVersion) {
        Lock deviceLock = device.getLock();
        deviceLock.lock(); // 获取设备级锁定
        try {
            // 读取设备版本号
            int currentVersion = device.getVersion();

            // 模拟设备崩溃
            if (Math.random() < 0.5) {
                throw new RuntimeException("Simulated device crash!");
            }

            // 检查版本号是否匹配
            if (currentVersion != expectedVersion) {
                System.out.println("Version conflict detected. Retrying command...");
                executeWriteCommand(device, newState, expectedVersion); // 重新执行写命令
                return;
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
            System.out.println("Exception occurred during write command: " + e.getMessage() + ". Retrying...");
            executeWriteCommand(device, newState, expectedVersion); // 重新执行写命令
        } finally {
            deviceLock.unlock(); // 释放设备级锁定
        }
    }

    public static boolean executeReadCommand(Device device) {
        Lock deviceLock = device.getLock();
        deviceLock.lock(); // 获取设备级锁定
        try {
            // 读取设备状态
            boolean currentState = device.getState();
            System.out.println("Read command executed successfully. Current state: " + currentState);
            return currentState;
        } finally {
            deviceLock.unlock(); // 释放设备级锁定
        }
    }
}

public class VersionControl {
    public static void main(String[] args) {
        Device device = new Device(false, 1);

        // 模拟并发执行写命令和读命令
        Thread writeCommand1 = new Thread(() -> CommandHandler.executeWriteCommand(device, true, device.getVersion()));
        Thread writeCommand2 = new Thread(() -> CommandHandler.executeWriteCommand(device, false, device.getVersion()));
        Thread readCommand = new Thread(() -> CommandHandler.executeReadCommand(device));

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

