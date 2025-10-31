package cn.kmdckj.epersonnelarchivegenerator.engine.config;

import cn.kmdckj.epersonnelarchivegenerator.engine.layout.LayoutField;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 字段过滤器 - 过滤空值或不应显示的字段
 */
@Component
public class FieldFilter {

    private final FieldConfigRegistry configRegistry;

    public FieldFilter(FieldConfigRegistry configRegistry) {
        this.configRegistry = configRegistry;
    }

    /**
     * 过滤字段数据,返回应该显示的字段列表
     *
     * @param fieldData 字段键值对数据
     * @return 过滤后的LayoutField列表
     */
    public List<LayoutField> filterAndConvert(Map<String, String> fieldData) {
        List<LayoutField> fields = new ArrayList<>();

        for (Map.Entry<String, String> entry : fieldData.entrySet()) {
            String fieldKey = entry.getKey();
            String value = entry.getValue();

            FieldConfig config = configRegistry.getConfig(fieldKey);
            if (config == null) {
                continue;  // 没有配置的字段跳过
            }

            // 判断是否应该显示
            if (!config.shouldDisplay(value)) {
                continue;  // 不显示空值字段(除非是必填)
            }

            // 创建LayoutField
            LayoutField field = new LayoutField();
            field.setFieldKey(fieldKey);
            field.setLabel(config.getLabel());
            field.setValue(value != null ? value : "");
            field.setWidthPercent(config.getWidthPercent());

            // 判断是否为长文本
            if (config.isLongText(value)) {
                field.setForceFullWidth(true);
                field.setWidthPercent(100.0);
                field.setType(LayoutField.FieldType.LONG_TEXT);
            }

            fields.add(field);
        }

        return fields;
    }

    /**
     * 过滤并按配置顺序排序
     */
    public List<LayoutField> filterAndConvertOrdered(
            Map<String, String> fieldData,
            FieldConfigRegistry.FieldGroup group) {

        List<LayoutField> fields = new ArrayList<>();
        List<FieldConfig> configs = configRegistry.getConfigsByGroup(group);

        for (FieldConfig config : configs) {
            String fieldKey = config.getFieldKey();
            String value = fieldData.get(fieldKey);

            // 判断是否应该显示
            if (!config.shouldDisplay(value)) {
                continue;
            }

            // 创建LayoutField
            LayoutField field = new LayoutField();
            field.setFieldKey(fieldKey);
            field.setLabel(config.getLabel());
            field.setValue(value != null ? value : "");
            field.setWidthPercent(config.getWidthPercent());

            // 判断是否为长文本
            if (config.isLongText(value)) {
                field.setForceFullWidth(true);
                field.setWidthPercent(100.0);
                field.setType(LayoutField.FieldType.LONG_TEXT);
            }

            fields.add(field);
        }

        return fields;
    }
}
