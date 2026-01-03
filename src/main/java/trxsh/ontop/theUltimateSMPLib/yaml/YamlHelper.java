package trxsh.ontop.theUltimateSMPLib.yaml;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

/**
 * YAML helper.
 * converts objects to YAML, or from YAML to object.
 * May be dangerous.
 */
public class YamlHelper {
    /**
     * Converts any valid object datatype to YAML. (risky)
     * @param data
     * @return yaml string. should probably save it
     */
    public static String objectToYaml(Object data) {
        DumperOptions options = new DumperOptions();

        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);

        Representer representer = new Representer(options);
        representer.getPropertyUtils().setSkipMissingProperties(true);

        Yaml yaml = new Yaml(representer, options);
        return yaml.dump(data);
    }

    /**
     * this method is dangerous! it may allow for remote code execution as it allows every class to be instantiated.
     * load YAML at your own risk.
     * @param yamlString
     * @param clazz
     * @return object of type T
     * @param <T>
     */
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
