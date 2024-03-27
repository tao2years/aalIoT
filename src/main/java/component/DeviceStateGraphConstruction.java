package component;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static component.YamlParser.parseDeviceAPIs;

/**
 * (1) 输入相关
 *     - 设备api配置文件 .yaml
 *     - 被测的设备对象 or 类
 * (2) 输出相关
 *     - 设备状态图
 *        - 节点, 设备状态集合
 *        - 边, 触发节点状态转换的api集合
 * (3) 构造逻辑
 *    - 读取设备api配置文件, 解析所有存在的api, 并构造 api列表, 需要处理不同的api类型与参数
 *    - 读取设备对象, 获取所有的属性, 并利用Any进行替代, 作为设备状态图的初始状态
 *    - 在初始节点上, 依次调用所有api, 并记录每个api调用后的设备状态, 作为下一个节点的初始状态
 *    - 在下一节点上, 依次调用所有api, 并记录每个api调用后的设备状态, 作为下一个节点的初始状态
 *    - 重复上述过程, 确保所有状态节点上的api都被调用过
 *    - 重复去重, 得到最终的设备状态迁移图
 */
public class DeviceStateGraphConstruction {

    public static void main(String[] args) {
        String yamlFilePath = "src/main/resources/deviceAPIs.yaml";
        List<YamlParser.DeviceAPI> allPossibleAPIs = getAllPossibleAPIs(yamlFilePath);
        printAllPossibleAPIs(allPossibleAPIs);
    }

    /**
     * 打印解析结果
     * @param deviceAPIs
     */
    private static void printAllPossibleAPIs(List<YamlParser.DeviceAPI> deviceAPIs) {
        // 打印解析结果
        for (YamlParser.DeviceAPI deviceAPI : deviceAPIs) {
            System.out.println("API Name: " + deviceAPI.getName());
            System.out.println("Parameters:");
            for (YamlParser.Parameter parameter : deviceAPI.getParameters()) {
                System.out.println("  Parameter Name: " + parameter.getName());
                System.out.println("  Parameter Type: " + parameter.getType());
                if (parameter instanceof YamlParser.EnumParameter) {
                    YamlParser.EnumParameter enumParameter = (YamlParser.EnumParameter) parameter;
                    System.out.println("  Enum Values: " + enumParameter.getValues());
                } else {
                    System.out.println("  Range: " + parameter.getMinValue() + " - " + parameter.getMaxValue());
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    /**
     * 读取设备api配置文件, 解析所有存在的api, 并构造 api列表, 需要处理不同的api类型与参数
     * @param yamlFilePath
     * @return
     */
    private static List<YamlParser.DeviceAPI> getAllPossibleAPIs(String yamlFilePath) {
        try (FileInputStream fis = new FileInputStream(yamlFilePath)) {
            Yaml yaml = new Yaml();
            Map<String, List<Map<String, Object>>> data = yaml.load(fis);
            List<YamlParser.DeviceAPI> deviceAPIs = parseDeviceAPIs(data);
            return deviceAPIs;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<String> constructApiLists(YamlParser.DeviceAPI deviceAPI) {
        return null;
    }

}
