package cn.kmdckj.epersonnelarchivegenerator.engine.config;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 字段配置注册表 - 管理所有字段的配置规则
 */
@Component
public class FieldConfigRegistry {

    private final Map<String, FieldConfig> configMap;

    public FieldConfigRegistry() {
        this.configMap = new LinkedHashMap<>();
        initializeConfigs();
    }

    /**
     * 初始化所有字段配置
     */
    private void initializeConfigs() {
        // 基础信息字段配置
        register(new FieldConfig("name", "姓名:", 25.0)
                .required(true));

        register(new FieldConfig("gender", "性别:", 25.0));

        register(new FieldConfig("birthDate", "出生日期:", 25.0)
                .type(FieldConfig.FieldType.DATE));

        register(new FieldConfig("nation", "民族:", 25.0));

        register(new FieldConfig("idCard", "身份证号:", 50.0)
                .type(FieldConfig.FieldType.ID_CARD));

        register(new FieldConfig("politicalStatus", "政治面貌:", 25.0));

        register(new FieldConfig("maritalStatus", "婚姻状况:", 25.0));

        register(new FieldConfig("phone", "联系电话:", 33.0)
                .type(FieldConfig.FieldType.PHONE));

        register(new FieldConfig("email", "电子邮箱:", 33.0)
                .type(FieldConfig.FieldType.EMAIL));

        register(new FieldConfig("nativePlace", "籍贯:", 50.0));

        register(new FieldConfig("currentAddress", "现居住地:", 100.0)
                .forceFullWidth(true)
                .maxLength(30));

        register(new FieldConfig("emergencyContact", "紧急联系人:", 33.0));

        register(new FieldConfig("emergencyPhone", "紧急联系电话:", 33.0)
                .type(FieldConfig.FieldType.PHONE));

        // 工作经历字段配置
        register(new FieldConfig("workStartDate", "起始日期:", 25.0)
                .type(FieldConfig.FieldType.DATE));

        register(new FieldConfig("workEndDate", "结束日期:", 25.0)
                .type(FieldConfig.FieldType.DATE));

        register(new FieldConfig("company", "工作单位:", 50.0));

        register(new FieldConfig("position", "职位:", 50.0));

        register(new FieldConfig("duties", "工作职责:", 100.0)
                .type(FieldConfig.FieldType.LONG_TEXT)
                .forceFullWidth(true));

        // 教育经历字段配置
        register(new FieldConfig("eduStartDate", "起始日期:", 25.0)
                .type(FieldConfig.FieldType.DATE));

        register(new FieldConfig("eduEndDate", "结束日期:", 25.0)
                .type(FieldConfig.FieldType.DATE));

        register(new FieldConfig("school", "学校:", 50.0));

        register(new FieldConfig("major", "专业:", 33.0));

        register(new FieldConfig("degree", "学历:", 33.0));

        // 家庭成员字段配置
        register(new FieldConfig("relation", "关系:", 25.0));

        register(new FieldConfig("familyName", "姓名:", 25.0));

        register(new FieldConfig("age", "年龄:", 25.0));

        register(new FieldConfig("workUnit", "工作单位:", 50.0));
    }

    /**
     * 注册字段配置
     */
    public void register(FieldConfig config) {
        configMap.put(config.getFieldKey(), config);
    }

    /**
     * 获取字段配置
     */
    public FieldConfig getConfig(String fieldKey) {
        return configMap.get(fieldKey);
    }

    /**
     * 获取指定分组的字段配置列表
     */
    public List<FieldConfig> getConfigsByGroup(FieldGroup group) {
        List<FieldConfig> configs = new ArrayList<>();
        for (String key : group.getFieldKeys()) {
            FieldConfig config = configMap.get(key);
            if (config != null) {
                configs.add(config);
            }
        }
        return configs;
    }

    /**
     * 字段分组枚举
     */
    public enum FieldGroup {
        BASIC_INFO(Arrays.asList(
                "name", "gender", "birthDate", "nation",
                "idCard", "politicalStatus", "maritalStatus",
                "phone", "email", "nativePlace", "currentAddress",
                "emergencyContact", "emergencyPhone"
        )),

        WORK_EXPERIENCE(Arrays.asList(
                "workStartDate", "workEndDate", "company", "position", "duties"
        )),

        EDUCATION(Arrays.asList(
                "eduStartDate", "eduEndDate", "school", "major", "degree"
        )),

        FAMILY(Arrays.asList(
                "relation", "familyName", "age", "workUnit"
        ));

        private final List<String> fieldKeys;

        FieldGroup(List<String> fieldKeys) {
            this.fieldKeys = fieldKeys;
        }

        public List<String> getFieldKeys() {
            return fieldKeys;
        }
    }
}
