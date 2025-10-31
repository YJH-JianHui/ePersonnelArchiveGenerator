package cn.kmdckj.epersonnelarchivegenerator.engine.layout;

import java.util.ArrayList;
import java.util.List;

/**
 * 布局分区 - 表示页面中的一个逻辑区域
 */
public class LayoutZone {
    private String zoneId;           // 分区唯一标识
    private ZoneType type;           // 分区类型
    private List<LayoutRow> rows;    // 该分区包含的行列表
    private Object metadata;         // 额外元数据(如照片数据等)

    public enum ZoneType {
        HEADER_WITH_PHOTO,   // 页眉区:左侧多栏文本 + 右侧照片
        SINGLE_COLUMN,       // 单栏内容区
        TABLE,               // 表格区(用于工作经历、教育背景等)
        CUSTOM               // 自定义区
    }

    public LayoutZone(String zoneId, ZoneType type) {
        this.zoneId = zoneId;
        this.type = type;
        this.rows = new ArrayList<>();
    }

    public void addRow(LayoutRow row) {
        this.rows.add(row);
    }

    public void addPageBreak() {
        PageBreak pageBreak = new PageBreak();
        this.rows.add(pageBreak.toLayoutRow());
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public ZoneType getType() {
        return type;
    }

    public void setType(ZoneType type) {
        this.type = type;
    }

    public List<LayoutRow> getRows() {
        return rows;
    }

    public void setRows(List<LayoutRow> rows) {
        this.rows = rows;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }
}
