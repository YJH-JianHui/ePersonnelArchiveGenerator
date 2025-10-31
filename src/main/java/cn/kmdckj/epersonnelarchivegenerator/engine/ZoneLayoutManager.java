package cn.kmdckj.epersonnelarchivegenerator.engine;

import cn.kmdckj.epersonnelarchivegenerator.engine.layout.LayoutRow;
import cn.kmdckj.epersonnelarchivegenerator.engine.layout.LayoutZone;
import org.springframework.stereotype.Component;

/**
 * 分区布局管理器 - 管理不同类型分区的特殊布局需求
 */
@Component
public class ZoneLayoutManager {

    // 分区高度常量(单位:mm)
    private static final double HEADER_ZONE_BASE_HEIGHT = 60.0;  // 页眉区基础高度(含照片)
    private static final double TITLE_ROW_HEIGHT = 10.0;         // 标题行高度
    private static final double NORMAL_ROW_HEIGHT = 8.0;         // 普通行高度
    private static final double SEPARATOR_HEIGHT = 5.0;          // 分隔行高度

    /**
     * 计算分区总高度
     *
     * @param zone 布局分区
     * @return 高度(mm)
     */
    public double calculateZoneHeight(LayoutZone zone) {
        if (zone == null || zone.getRows() == null) {
            return 0.0;
        }

        double totalHeight = 0.0;

        // 根据分区类型添加基础高度
        if (zone.getType() == LayoutZone.ZoneType.HEADER_WITH_PHOTO) {
            // 页眉区:需要考虑照片高度
            totalHeight += HEADER_ZONE_BASE_HEIGHT;
        }

        // 累加所有行的高度
        for (LayoutRow row : zone.getRows()) {
            totalHeight += row.getEstimatedHeight();
        }

        // 添加分区间距
        totalHeight += 10.0;  // 分区底部间距

        return totalHeight;
    }

    /**
     * 优化页眉区布局
     * 页眉区特点:左侧文本 + 右侧照片,照片垂直跨越多行
     *
     * @param zone 页眉分区
     */
    public void optimizeHeaderZone(LayoutZone zone) {
        if (zone.getType() != LayoutZone.ZoneType.HEADER_WITH_PHOTO) {
            return;
        }

        // 页眉区的行宽需要为左侧文本预留空间
        // 右侧照片约占35mm,页面宽度约170mm(A4去边距),照片占约20%
        double textAreaWidth = 75.0;  // 左侧文本区占75%

        for (LayoutRow row : zone.getRows()) {
            // 调整行内字段的宽度百分比,基于文本区而非全页宽
            double rowUsedWidth = row.getUsedWidth();
            if (rowUsedWidth > 0) {
                // 将字段宽度映射到75%的文本区
                for (var field : row.getFields()) {
                    double originalWidth = field.getWidthPercent();
                    double adjustedWidth = (originalWidth / 100.0) * textAreaWidth;
                    field.setWidthPercent(adjustedWidth);
                }
            }
        }
    }

    /**
     * 优化单栏区布局
     *
     * @param zone 单栏分区
     */
    public void optimizeSingleColumnZone(LayoutZone zone) {
        if (zone.getType() != LayoutZone.ZoneType.SINGLE_COLUMN) {
            return;
        }

        // 单栏区使用全宽,无需特殊处理
        // 可以在这里添加其他优化逻辑,如行间距调整等
    }
}
