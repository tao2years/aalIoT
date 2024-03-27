package component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class YamlParser {
    public static void main(String[] args) {
        File file = new File("src/main/resources/deviceAPIs.yaml");

        try (FileInputStream fis = new FileInputStream(file)) {
            Yaml yaml = new Yaml();
            Map<String, List<Map<String, Object>>> data = yaml.load(fis);

            List<DeviceAPI> deviceAPIs = parseDeviceAPIs(data);

            // 打印解析结果
            for (DeviceAPI deviceAPI : deviceAPIs) {
                System.out.println("API Name: " + deviceAPI.getName());
                System.out.println("Parameters:");
                for (Parameter parameter : deviceAPI.getParameters()) {
                    System.out.println("  Parameter Name: " + parameter.getName());
                    System.out.println("  Parameter Type: " + parameter.getType());
                    if (parameter instanceof EnumParameter) {
                        EnumParameter enumParameter = (EnumParameter) parameter;
                        System.out.println("  Enum Values: " + enumParameter.getValues());
                    } else {
                        System.out.println("  Range: " + parameter.getMinValue() + " - " + parameter.getMaxValue());
                    }
                    System.out.println();
                }
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static List<DeviceAPI> parseDeviceAPIs(Map<String, List<Map<String, Object>>> data) {
        List<DeviceAPI> deviceAPIs = new ArrayList<>();

        List<Map<String, Object>> apiList = data.get("deviceAPIs");
        for (Map<String, Object> apiMap : apiList) {
            String name = (String) apiMap.get("name");
            List<Map<String, Object>> parametersList = (List<Map<String, Object>>) apiMap.get("parameters");

            List<Parameter> parameters = parseParameters(parametersList);

            DeviceAPI deviceAPI = new DeviceAPI(name, parameters);
            deviceAPIs.add(deviceAPI);
        }

        return deviceAPIs;
    }

    private static List<Parameter> parseParameters(List<Map<String, Object>> parametersList) {
        List<Parameter> parameters = new ArrayList<>();

        for (Map<String, Object> parameterMap : parametersList) {
            String paramName = (String) parameterMap.get("name");
            String paramType = (String) parameterMap.get("type");

            Parameter parameter;
            if ("int".equals(paramType) || "double".equals(paramType) || "boolean".equals(paramType) || "string".equals(paramType)) {
                parameter = new Parameter(paramName, paramType);
            } else if ("enum".equals(paramType)) {
                List<String> enumValues = (List<String>) parameterMap.get("values");
                parameter = new EnumParameter(paramName, enumValues);
            } else {
                throw new IllegalArgumentException("Invalid parameter type: " + paramType);
            }

            if (parameterMap.containsKey("range")) {
                Map<String, Object> rangeMap = (Map<String, Object>) parameterMap.get("range");
                parameter.setRange((Number) rangeMap.get("min"), (Number) rangeMap.get("max"));
            }

            parameters.add(parameter);
        }

        return parameters;
    }

    static class DeviceAPI {
        private String name;
        private List<Parameter> parameters;

        public DeviceAPI(String name, List<Parameter> parameters) {
            this.name = name;
            this.parameters = parameters;
        }

        public String getName() {
            return name;
        }

        public List<Parameter> getParameters() {
            return parameters;
        }
    }

    static class Parameter {
        private String name;
        private String type;
        private Number minValue;
        private Number maxValue;

        public Parameter(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public Number getMinValue() {
            return minValue;
        }

        public Number getMaxValue() {
            return maxValue;
        }

        public void setRange(Number minValue, Number maxValue) {
            this.minValue = minValue;
            this.maxValue = maxValue;
        }
    }

    static class EnumParameter extends Parameter {
        private List<String> values;

        public EnumParameter(String name, List<String> values) {
            super(name, "enum");
            this.values = values;
        }

        public List<String> getValues() {
            return values;
        }
    }
}