package cn.kmdckj.epersonnelarchivegenerator.engine;

import cn.kmdckj.epersonnelarchivegenerator.config.LayoutConfig;
import cn.kmdckj.epersonnelarchivegenerator.engine.layout.LayoutModel;
import cn.kmdckj.epersonnelarchivegenerator.engine.layout.LayoutRow;
import cn.kmdckj.epersonnelarchivegenerator.engine.layout.LayoutZone;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自动分页计算器 - 根据内容高度自动插入分页符 (V2 - 统一分页逻辑)
 */
@Component
public class PageBreakCalculator {

    private final ZoneLayoutManager zoneLayoutManager;
    private final LayoutConfig layoutConfig;

    public PageBreakCalculator(ZoneLayoutManager zoneLayoutManager, LayoutConfig layoutConfig) {
        this.zoneLayoutManager = zoneLayoutManager;
        this.layoutConfig = layoutConfig;
    }

    /**
     * 为布局模型插入分页符 (重写后的统一逻辑)
     *
     * @param model 布局模型
     */
    public void insertPageBreaks(LayoutModel model) {
        if (model == null || model.getZones() == null || model.getZones().isEmpty()) {
            return;
        }

        final double usableHeight = layoutConfig.getPage().getUsableHeight();
        double currentPageHeight = 0.0;

        // 1. 将所有分区的所有行合并到一个总列表中
        List<LayoutRow> allRows = model.getZones().stream()
                .flatMap(zone -> zone.getRows().stream())
                .collect(Collectors.toList());

        // 2. 创建一个新的行列表用于存放包含分页符的结果
        List<LayoutRow> newRows = new ArrayList<>();

        // 3. 遍历所有行，应用分页逻辑
        for (int i = 0; i < allRows.size(); i++) {
            LayoutRow currentRow = allRows.get(i);
            double currentRowHeight = currentRow.getEstimatedHeight();

            // 如果当前行本身就是分页符，则跳过
            if (currentRow.isPageBreak()) {
                continue;
            }

            // --- 核心分页判断逻辑 ---
            double requiredHeight = currentRowHeight;

            // "防止孤行"逻辑：如果当前是标题行，则需要额外计算下一行的高度
            if (currentRow.isTitle() && (i + 1) < allRows.size()) {
                LayoutRow nextRow = allRows.get(i + 1);
                requiredHeight += nextRow.getEstimatedHeight();
            }

            // 如果当前页放不下所需高度，则插入分页符
            if (currentPageHeight + requiredHeight > usableHeight) {
                newRows.add(new LayoutRow(true)); // 插入分页符
                currentPageHeight = 0; // 重置新页面的高度
            }

            // 将当前行加入新列表，并累加高度
            newRows.add(currentRow);
            currentPageHeight += currentRowHeight;
        }

        // 4. 清空旧模型，用包含正确分页符的新行列表重建
        model.getZones().clear();
        LayoutZone unifiedZone = new LayoutZone("unified", LayoutZone.ZoneType.BODY_CONTENT);
        unifiedZone.setRows(newRows);
        model.addZone(unifiedZone);
    }


    /**
     * 获取布局统计信息(用于调试)
     */
    public LayoutStatistics getStatistics(LayoutModel model) {
        LayoutStatistics stats = new LayoutStatistics();
        final double usableHeight = layoutConfig.getPage().getUsableHeight();

        if (model == null || model.getZones() == null) {
            return stats;
        }

        double totalHeight = 0.0;
        int totalRows = 0;
        int totalPageBreaks = 0;

        for (LayoutZone zone : model.getZones()) {
            if (zone.getRows() != null) {
                for (LayoutRow row : zone.getRows()) {
                    if (row.isPageBreak()) {
                        totalPageBreaks++;
                    } else {
                        totalRows++;
                        totalHeight += row.getEstimatedHeight();
                    }
                }
            }
        }

        stats.totalHeight = totalHeight;
        stats.totalRows = totalRows;
        stats.totalPageBreaks = totalPageBreaks;
        if (usableHeight > 0) {
            stats.estimatedPages = (int) Math.ceil(totalHeight / usableHeight);
        }

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
