package cn.kmdckj.epersonnelarchivegenerator.engine.layout;

import java.util.ArrayList;
import java.util.List;

/**
 * 行模型 - 表示布局中的一行,包含多个字段
 */
public class LayoutRow {
    private List<LayoutField> fields;  // 该行包含的字段列表
    private boolean isPageBreak;       // 是否为分页标记行
    private double estimatedHeight;    // 预估高度(单位:mm)
    private boolean isTitle;           // 是否为标题行,用于分页时防止孤行

    public LayoutRow() {
        this.fields = new ArrayList<>();
        this.isPageBreak = false;
        this.estimatedHeight = 8.0;  // 默认单行高度约8mm
        this.isTitle = false;        // 默认为非标题行
    }

    public LayoutRow(boolean isPageBreak) {
        this();
        this.isPageBreak = isPageBreak;
    }

    public void addField(LayoutField field) {
        this.fields.add(field);
    }

    /**
     * 计算当前行已使用的宽度百分比
     */
    public double getUsedWidth() {
        return fields.stream()
                .mapToDouble(LayoutField::getWidthPercent)
                .sum();
    }

    /**
     * 计算当前行剩余可用宽度百分比
     */
    public double getRemainingWidth() {
        return 100.0 - getUsedWidth();
    }

    /**
     * 判断字段是否能放入当前行
     */
    public boolean canFit(LayoutField field) {
        return getRemainingWidth() >= field.getWidthPercent();
    }

    public List<LayoutField> getFields() {
        return fields;
    }

    public void setFields(List<LayoutField> fields) {
        this.fields = fields;
    }

    public boolean isPageBreak() {
        return isPageBreak;
    }

    public void setPageBreak(boolean pageBreak) {
        isPageBreak = pageBreak;
    }

    public double getEstimatedHeight() {
        return estimatedHeight;
    }

    public void setEstimatedHeight(double estimatedHeight) {
        this.estimatedHeight = estimatedHeight;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }
}
