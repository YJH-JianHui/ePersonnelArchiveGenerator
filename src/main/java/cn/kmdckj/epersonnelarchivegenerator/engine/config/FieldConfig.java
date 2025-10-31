package cn.kmdckj.epersonnelarchivegenerator.engine.config;

/**
 * 字段配置 - 定义每个字段的显示规则
 */
public class FieldConfig {
    private String fieldKey;           // 字段键
    private String label;              // 显示标签
    private double widthPercent;       // 默认占用宽度百分比
    private boolean required;          // 是否必填(必填字段即使为空也显示)
    private boolean forceFullWidth;    // 是否强制独占一行
    private FieldConfig.FieldType type; // 字段类型
    private int maxLength;             // 最大长度(超过则视为长文本)

    public enum FieldType {
        TEXT,
        DATE,
        LONG_TEXT,
        EMAIL,
        PHONE,
        ID_CARD
    }

    public FieldConfig(String fieldKey, String label, double widthPercent) {
        this.fieldKey = fieldKey;
        this.label = label;
        this.widthPercent = widthPercent;
        this.required = false;
        this.forceFullWidth = false;
        this.type = FieldType.TEXT;
        this.maxLength = 50;
    }

    public FieldConfig required(boolean required) {
        this.required = required;
        return this;
    }

    public FieldConfig forceFullWidth(boolean forceFullWidth) {
        this.forceFullWidth = forceFullWidth;
        return this;
    }

    public FieldConfig type(FieldType type) {
        this.type = type;
        return this;
    }

    public FieldConfig maxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    /**
     * 判断字段值是否应该显示
     */
    public boolean shouldDisplay(String value) {
        if (required) {
            return true;  // 必填字段始终显示
        }
        return value != null && !value.trim().isEmpty();
    }

    /**
     * 判断是否为长文本
     */
    public boolean isLongText(String value) {
        if (forceFullWidth || type == FieldType.LONG_TEXT) {
            return true;
        }
        return value != null && value.length() > maxLength;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getWidthPercent() {
        return widthPercent;
    }

    public void setWidthPercent(double widthPercent) {
        this.widthPercent = widthPercent;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isForceFullWidth() {
        return forceFullWidth;
    }

    public void setForceFullWidth(boolean forceFullWidth) {
        this.forceFullWidth = forceFullWidth;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}
