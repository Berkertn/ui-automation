package ui.helpers.yaml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.testng.Assert;

import java.io.InputStream;

import static ui.utils.LogUtil.LOG;

public class ConfigReader {

    private static JsonNode config;

    public static String getConfig(String key) {
        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream("config.yaml")) {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            config = mapper.readTree(inputStream);
        } catch (Exception e) {
            LOG.error(e);
            Assert.fail("Error reading config file");
        }

        String[] keys = key.split("\\.");
        JsonNode currentNode = config;
        for (String k : keys) {
            currentNode = currentNode.path(k);
        }

        return currentNode.isMissingNode() ? null : currentNode.asText();
    }
}