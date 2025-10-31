package cn.kmdckj.epersonnelarchivegenerator.engine.layout;

import java.util.ArrayList;
import java.util.List;

/**
 * 整体布局模型 - 表示整个档案文档的布局结构
 */
public class LayoutModel {
    private List<LayoutZone> zones;  // 布局分区列表

    public LayoutModel() {
        this.zones = new ArrayList<>();
    }

    public void addZone(LayoutZone zone) {
        this.zones.add(zone);
    }

    public List<LayoutZone> getZones() {
        return zones;
    }

    public void setZones(List<LayoutZone> zones) {
        this.zones = zones;
    }
}
