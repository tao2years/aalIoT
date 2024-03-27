package component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Yeelight {
    private boolean power; // true-false
    private int brightness; // 1-100 percentage
    private int colorTemperature; // 1700-6500 K
    private int[] rgb; // 0-255, 0-255, 0-255, Not all 0
    private int mode; // 1-color 2-day

    public Yeelight(){}

    public void setPower(boolean power) {
        this.power = power;
    }

    public void setColorTemperature(int colorTemperature) {
        this.colorTemperature = colorTemperature;
    }

    public void setRgb(int[] rgb) {
        this.rgb = rgb;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isPower() {
        return power;
    }

    public int getBrightness() {
        return brightness;
    }

    public int getColorTemperature() {
        return colorTemperature;
    }

    public int[] getRgb() {
        return rgb;
    }

    public int getMode() {
        return mode;
    }

    public String turnOn() {
        setPower(true);
//        exePy("on");
        return "on";
    }

    public String turnOff(){
        setPower(false);
//        exePy("off");
        return "off";
    }

    public String setBrightness(int brightness){
        if (isPower()) {
            this.brightness = brightness;
        }
        return "Unknown";
    }

    public String setRbg(int[] rgb){
        if (isPower()) {
            this.rgb = rgb;
        }
        return "Unknown";
    }

    private void exePy(String cmd) {
        String path = "src/main/python/yeelight/";
        // 创建进程并执行命令
        try {
            if (cmd.equals("on")) {
                String pathOn = path + "on.py";
                String[] _cmd = {"python", pathOn};
                ProcessBuilder pb = new ProcessBuilder(_cmd);
                Process process = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                int exitCode = process.waitFor();
                System.out.println("Exit Code: " + exitCode);
            } else if(cmd.equals("off")){
                String pathOn = path + "on.py";
                String[] _cmd = {"python", pathOn};
                ProcessBuilder pb = new ProcessBuilder(_cmd);
                Process process = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                int exitCode = process.waitFor();
                System.out.println("Exit Code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
