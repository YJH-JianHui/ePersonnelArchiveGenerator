package cn.kmdckj.epersonnelarchivegenerator.engine.layout;

/**
 * 字段模型 - 表示单个显示字段(标签+值)
 */
public class LayoutField {
    private String fieldKey;         // 字段键(用于识别字段)
    private String label;            // 显示标签(如"姓名:")
    private String value;            // 字段值
    private double widthPercent;     // 该字段占行宽的百分比
    private FieldType type;          // 字段类型
    private boolean forceFullWidth;  // 是否强制独占一行

    public enum FieldType {
        TEXT,           // 普通文本
        DATE,           // 日期
        LONG_TEXT,      // 长文本
        IMAGE           // 图片
    }

    public LayoutField() {
        this.type = FieldType.TEXT;
        this.forceFullWidth = false;
    }

    public LayoutField(String fieldKey, String label, String value, double widthPercent) {
        this();
        this.fieldKey = fieldKey;
        this.label = label;
        this.value = value;
        this.widthPercent = widthPercent;
    }

    /**
     * 判断是否为超长内容(需要独占一行)
     */
    public boolean isLongContent() {
        return forceFullWidth ||
                (value != null && value.length() > 50) ||
                type == FieldType.LONG_TEXT;
    }

    /**
     * 获取显示文本(标签+值)
     */
    public String getDisplayText() {
        if (label == null || label.isEmpty()) {
            return value != null ? value : "";
        }
        return label + " " + (value != null ? value : "");
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public double getWidthPercent() {
        return widthPercent;
    }

    public void setWidthPercent(double widthPercent) {
        this.widthPercent = widthPercent;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public boolean isForceFullWidth() {
        return forceFullWidth;
    }

    public void setForceFullWidth(boolean forceFullWidth) {
        this.forceFullWidth = forceFullWidth;
    }
}
