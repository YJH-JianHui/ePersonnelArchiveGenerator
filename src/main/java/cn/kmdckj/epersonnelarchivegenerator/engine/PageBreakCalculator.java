package cn.kmdckj.epersonnelarchivegenerator.engine;

import cn.kmdckj.epersonnelarchivegenerator.engine.layout.LayoutModel;
import cn.kmdckj.epersonnelarchivegenerator.engine.layout.LayoutRow;
import cn.kmdckj.epersonnelarchivegenerator.engine.layout.LayoutZone;
import org.springframework.stereotype.Component;

/**
 * 自动分页计算器 - 根据内容高度自动插入分页符
 */
@Component
public class PageBreakCalculator {

    private final ZoneLayoutManager zoneLayoutManager;

    // A4页面常量(单位:mm)
    private static final double A4_HEIGHT = 297.0;
    private static final double TOP_MARGIN = 0.0;
    private static final double BOTTOM_MARGIN = 0.0;
    private static final double PAGE_USABLE_HEIGHT = A4_HEIGHT - TOP_MARGIN - BOTTOM_MARGIN;  // 277mm

    // 分页策略常量
    private static final double MIN_ROW_HEIGHT_FOR_BREAK = 15.0;  // 最小分页行高阈值

    public PageBreakCalculator(ZoneLayoutManager zoneLayoutManager) {
        this.zoneLayoutManager = zoneLayoutManager;
    }

    /**
     * 为布局模型插入分页符
     *
     * @param model 布局模型
     */
    public void insertPageBreaks(LayoutModel model) {
        if (model == null || model.getZones() == null) {
            return;
        }

        double currentPageHeight = 0.0;

        for (LayoutZone zone : model.getZones()) {
            if (zone.getRows() == null || zone.getRows().isEmpty()) {
                continue;
            }

            // 基础信息区特殊处理，不允许分页
            if (zone.getType() == LayoutZone.ZoneType.BASIC_INFO_WITH_PHOTO) {
                currentPageHeight += zoneLayoutManager.calculateZoneHeight(zone);
                continue;
            }

            java.util.List<LayoutRow> newRows = new java.util.ArrayList<>();
            for (LayoutRow row : zone.getRows()) {
                double rowHeight = row.getEstimatedHeight();

                if (currentPageHeight + rowHeight > PAGE_USABLE_HEIGHT) {
                    // 插入分页符
                    newRows.add(new LayoutRow(true));

                    // 重置当前页高度
                    currentPageHeight = 0;
                }
                currentPageHeight += rowHeight;
                newRows.add(row);
            }
            zone.setRows(newRows);
        }
    }

    /**
     * 获取布局统计信息(用于调试)
     */
    public LayoutStatistics getStatistics(LayoutModel model) {
        LayoutStatistics stats = new LayoutStatistics();

        if (model == null || model.getZones() == null) {
            return stats;
        }

        double totalHeight = 0.0;
        int totalRows = 0;
        int totalPageBreaks = 0;

        for (LayoutZone zone : model.getZones()) {
            double zoneHeight = zoneLayoutManager.calculateZoneHeight(zone);
            totalHeight += zoneHeight;

            if (zone.getRows() != null) {
                totalRows += zone.getRows().size();

                for (LayoutRow row : zone.getRows()) {
                    if (row.isPageBreak()) {
                        totalPageBreaks++;
                    }
                }
            }
        }

        stats.totalHeight = totalHeight;
        stats.totalRows = totalRows;
        stats.totalPageBreaks = totalPageBreaks;
        stats.estimatedPages = (int) Math.ceil(totalHeight / PAGE_USABLE_HEIGHT);

        return stats;
    }

    /**
     * 布局统计信息
     */
    public static class LayoutStatistics {
        public double totalHeight = 0.0;      // 总高度(mm)
        public int totalRows = 0;             // 总行数
        public int totalPageBreaks = 0;       // 分页符数量
        public int estimatedPages = 0;        // 预估页数

        @Override
        public String toString() {
            return String.format(
                    "LayoutStatistics{总高度=%.2fmm, 总行数=%d, 分页符数=%d, 预估页数=%d}",
                    totalHeight, totalRows, totalPageBreaks, estimatedPages
            );
        }
    }
}
