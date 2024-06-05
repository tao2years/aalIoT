package System_q.realDevice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Yeelight_p {
    private static final Logger LOGGER = LogManager.getLogger();

    public static String turnOn(ProcessBuilder pb) throws IOException, InterruptedException {
        String lightOn = "src/main/python/yeelight/on.py";
        pb.command("python" ,lightOn);
        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        String result = "";
        while ((line = reader.readLine()) != null) {
            result += line + "\n";
        }
        reader.close();
        process.waitFor();
        int exitCode = process.exitValue();
        if (exitCode != 0) {
            return "Error";
        }
        return result;
    }

    public static String turnOff(ProcessBuilder pb) throws IOException, InterruptedException {
        String lightOn = "src/main/python/yeelight/off.py";
        pb.command("python" ,lightOn);
        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        String result = "";
        while ((line = reader.readLine()) != null) {
            result += line + "\n";
        }
        reader.close();
        process.waitFor();
        int exitCode = process.exitValue();
        if (exitCode != 0) {
            return "Error";
        }
        return result;
    }

    public static String setRGB(ProcessBuilder pb, int red, int green, int blue) throws IOException, InterruptedException {
        String lightOn = "src/main/python/yeelight/setRGB.py ";

        List<String> command = new ArrayList<>();
        command.add("python");
        command.add(lightOn);
        command.add(String.valueOf(red));
        command.add(String.valueOf(green));
        command.add(String.valueOf(blue));

        pb.command(command.toArray(new String[0]));


        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        String result = "";
        while ((line = reader.readLine()) != null) {
            result += line + "\n";
        }
        reader.close();
        process.waitFor();
        int exitCode = process.exitValue();
        if (exitCode != 0) {
            return "Error";
        }
        return result;
    }

    public static String setBrightness(ProcessBuilder pb, int brightness) throws IOException, InterruptedException {
        String lightOn = "src/main/python/yeelight/setBrightness.py ";

        pb.command("python" ,lightOn, String.valueOf(brightness));

        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        String result = "";
        while ((line = reader.readLine()) != null) {
            result += line + "\n";
        }
        LOGGER.info(result);
        reader.close();
        process.waitFor();
        int exitCode = process.exitValue();
        if (exitCode != 0) {
            return "Error";
        }
        return result;
    }

    public static String executePythonScript(ProcessBuilder pb, String scriptPath, String... args) throws IOException, InterruptedException {
        List<String> command = new ArrayList<>();
        command.add("python");
        command.add(scriptPath);
        command.addAll(Arrays.asList(args));

        pb.command(command.toArray(new String[0]));
//        LOGGER.info(pb.environment());

        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line).append("\n");
        }
        reader.close();
        process.waitFor();
        int exitCode = process.exitValue();
        if (exitCode != 0) {
            return "Error";
        }
        return result.toString();
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder();
        pb.redirectErrorStream(true);

        // 调用 turnOn 方法
        String result1 = executePythonScript(pb, "src/main/python/yeelight/on.py");
        LOGGER.info(result1);

        String result3 = executePythonScript(pb, "src/main/python/yeelight/setRGB.py", "255", "0", "0");
        LOGGER.info(result3);

        String result4 = executePythonScript(pb, "src/main/python/yeelight/setBrightness.py", "100");
        LOGGER.info(result4);

        String result2 = executePythonScript(pb, "src/main/python/yeelight/off.py");
        LOGGER.info(result2);

    }


}
