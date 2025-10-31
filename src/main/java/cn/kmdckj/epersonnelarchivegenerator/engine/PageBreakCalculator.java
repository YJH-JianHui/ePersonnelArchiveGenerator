package cn.kmdckj.epersonnelarchivegenerator.engine;

import cn.kmdckj.epersonnelarchivegenerator.engine.layout.LayoutModel;
import cn.kmdckj.epersonnelarchivegenerator.engine.layout.LayoutRow;
import cn.kmdckj.epersonnelarchivegenerator.engine.layout.LayoutZone;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自动分页计算器 - 根据内容高度自动插入分页符
 */
@Component
public class PageBreakCalculator {

    private final ZoneLayoutManager zoneLayoutManager;

    // A4页面常量(单位:mm)
    private static final double A4_HEIGHT = 297.0;
    private static final double TOP_MARGIN = 20.0;
    private static final double BOTTOM_MARGIN = 20.0;
    private static final double PAGE_USABLE_HEIGHT = A4_HEIGHT - TOP_MARGIN - BOTTOM_MARGIN;  // 257mm

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
        int pageNumber = 1;

        for (LayoutZone zone : model.getZones()) {
            // 计算分区总高度
            double zoneHeight = zoneLayoutManager.calculateZoneHeight(zone);

            // 检查是否需要在分区前分页
            if (currentPageHeight + zoneHeight > PAGE_USABLE_HEIGHT) {
                // 需要分页

                // 如果分区可以拆分,则在分区内部插入分页符
                if (canSplitZone(zone)) {
                    double remainingHeight = PAGE_USABLE_HEIGHT - currentPageHeight;
                    insertPageBreakInZone(zone, remainingHeight);

                    // 重新计算当前页高度
                    currentPageHeight = calculateHeightAfterLastBreak(zone);
                } else {
                    // 分区不可拆分,在分区前整体分页
                    // 在前一个分区末尾插入分页符(此处简化处理)
                    currentPageHeight = zoneHeight;
                    pageNumber++;
                }
            } else {
                // 不需要分页,累加高度
                currentPageHeight += zoneHeight;
            }

            // 检查分区内是否需要进一步分页
            if (currentPageHeight > PAGE_USABLE_HEIGHT) {
                double excessHeight = currentPageHeight - PAGE_USABLE_HEIGHT;
                insertAdditionalBreaksInZone(zone, excessHeight);
                currentPageHeight = calculateHeightAfterLastBreak(zone);
            }
        }
    }

    /**
     * 判断分区是否可以拆分
     * 页眉区不可拆分,其他区域可拆分
     */
    private boolean canSplitZone(LayoutZone zone) {
        return zone.getType() != LayoutZone.ZoneType.HEADER_WITH_PHOTO;
    }

    /**
     * 在分区内插入分页符
     *
     * @param zone 分区
     * @param remainingHeight 当前页剩余高度
     */
    private void insertPageBreakInZone(LayoutZone zone, double remainingHeight) {
        List<LayoutRow> rows = zone.getRows();
        if (rows == null || rows.isEmpty()) {
            return;
        }

        double accumulatedHeight = 0.0;
        int breakIndex = -1;

        // 找到最佳分页位置
        for (int i = 0; i < rows.size(); i++) {
            LayoutRow row = rows.get(i);
            double rowHeight = row.getEstimatedHeight();

            if (accumulatedHeight + rowHeight > remainingHeight) {
                // 找到超出位置

                // 检查是否应该在此处分页
                if (rowHeight < MIN_ROW_HEIGHT_FOR_BREAK && i > 0) {
                    // 行高较小且不是第一行,在前一行后分页
                    breakIndex = i;
                } else if (i > 0) {
                    // 行高较大,在前一行后分页避免截断
                    breakIndex = i;
                } else {
                    // 第一行就超出,在此行后分页
                    breakIndex = i + 1;
                }

                break;
            }

            accumulatedHeight += rowHeight;
        }

        // 插入分页标记行
        if (breakIndex > 0 && breakIndex < rows.size()) {
            LayoutRow pageBreakRow = new LayoutRow(true);
            rows.add(breakIndex, pageBreakRow);
        }
    }

    /**
     * 在分区内插入额外的分页符(处理超长分区)
     */
    private void insertAdditionalBreaksInZone(LayoutZone zone, double excessHeight) {
        List<LayoutRow> rows = zone.getRows();
        if (rows == null || rows.isEmpty()) {
            return;
        }

        // 从后往前找到最后一个分页符的位置
        int lastBreakIndex = -1;
        for (int i = rows.size() - 1; i >= 0; i--) {
            if (rows.get(i).isPageBreak()) {
                lastBreakIndex = i;
                break;
            }
        }

        // 计算最后一个分页符之后的内容高度
        double heightAfterBreak = 0.0;
        int startIndex = lastBreakIndex + 1;

        for (int i = startIndex; i < rows.size(); i++) {
            heightAfterBreak += rows.get(i).getEstimatedHeight();

            // 如果累计高度超过页面高度,插入新的分页符
            if (heightAfterBreak > PAGE_USABLE_HEIGHT) {
                LayoutRow pageBreakRow = new LayoutRow(true);
                rows.add(i, pageBreakRow);
                heightAfterBreak = 0.0;
                i++;  // 跳过刚插入的分页符
            }
        }
    }

    /**
     * 计算最后一个分页符之后的内容高度
     */
    private double calculateHeightAfterLastBreak(LayoutZone zone) {
        List<LayoutRow> rows = zone.getRows();
        if (rows == null || rows.isEmpty()) {
            return 0.0;
        }

        double height = 0.0;

        // 从后往前找到最后一个分页符
        for (int i = rows.size() - 1; i >= 0; i--) {
            if (rows.get(i).isPageBreak()) {
                // 找到了,计算其后的高度
                for (int j = i + 1; j < rows.size(); j++) {
                    height += rows.get(j).getEstimatedHeight();
                }
                return height;
            }
        }

        // 没有找到分页符,计算全部高度
        for (LayoutRow row : rows) {
            height += row.getEstimatedHeight();
        }

        return height;
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
