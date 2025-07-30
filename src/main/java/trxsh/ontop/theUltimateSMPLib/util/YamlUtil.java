package trxsh.ontop.theUltimateSMPLib.util;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.inspector.TagInspector;
import org.yaml.snakeyaml.representer.Representer;

public class YamlUtil {
    public static String objectToYaml(Object data) {
        DumperOptions options = new DumperOptions();

        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);

        Representer representer = new Representer(options);
        representer.getPropertyUtils().setSkipMissingProperties(true);

        Yaml yaml = new Yaml(representer, options);
        return yaml.dump(data);
    }

    // this method is dangerous. it may allow for remote code execution as it allows every class to be instantiated. load YAML at your own risk.
    public static <T> T yamlToObject(String yamlString, Class<T> clazz) {
        try {
            LoaderOptions options = new LoaderOptions();

            options.setTagInspector(tag -> true); // this line right here is the dangerous one!

            Yaml yaml = new Yaml(new Constructor(clazz, options));
            return yaml.load(yamlString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
