package cn.kmdckj.epersonnelarchivegenerator.engine;

import cn.kmdckj.epersonnelarchivegenerator.engine.layout.LayoutModel;
import cn.kmdckj.epersonnelarchivegenerator.engine.layout.LayoutZone;
import org.springframework.stereotype.Component;

/**
 * 布局优化器 - 整合各种布局优化逻辑
 */
@Component
public class LayoutOptimizer {

    private final ZoneLayoutManager zoneLayoutManager;
    private final PageBreakCalculator pageBreakCalculator;

    public LayoutOptimizer(ZoneLayoutManager zoneLayoutManager,
                           PageBreakCalculator pageBreakCalculator) {
        this.zoneLayoutManager = zoneLayoutManager;
        this.pageBreakCalculator = pageBreakCalculator;
    }

    /**
     * 优化整个布局模型
     *
     * @param model 布局模型
     * @return 优化后的模型
     */
    public LayoutModel optimize(LayoutModel model) {
        if (model == null) {
            return model;
        }

        // 1. 优化各个分区的布局
        optimizeZones(model);

        // 2. 计算并插入分页符
        pageBreakCalculator.insertPageBreaks(model);

        // 3. 后处理:调整分页后的间距等
        postProcessAfterPagination(model);

        return model;
    }

    /**
     * 优化各个分区
     */
    private void optimizeZones(LayoutModel model) {
        if (model.getZones() == null) {
            return;
        }

        for (LayoutZone zone : model.getZones()) {
            switch (zone.getType()) {
                case BASIC_INFO_WITH_PHOTO:
                    zoneLayoutManager.optimizeBasicInfoZone(zone);
                    break;

                case BODY_CONTENT:
                    zoneLayoutManager.optimizeBodyContentZone(zone);
                    break;

                default:
                    // 其他类型暂无特殊处理
                    break;
            }
        }
    }

    /**
     * 分页后的后处理
     */
    private void postProcessAfterPagination(LayoutModel model) {
        if (model.getZones() == null) {
            return;
        }

        // 可以在这里添加分页后的调整逻辑
        // 例如:调整分页符前后的间距,优化孤行/寡行等
    }

    /**
     * 获取优化统计信息
     */
    public OptimizationReport getOptimizationReport(LayoutModel model) {
        OptimizationReport report = new OptimizationReport();

        if (model == null || model.getZones() == null) {
            return report;
        }

        report.totalZones = model.getZones().size();

        for (LayoutZone zone : model.getZones()) {
            report.totalRows += zone.getRows() != null ? zone.getRows().size() : 0;
            report.totalHeight += zoneLayoutManager.calculateZoneHeight(zone);
        }

        PageBreakCalculator.LayoutStatistics stats =
                pageBreakCalculator.getStatistics(model);
        report.pageBreaks = stats.totalPageBreaks;
        report.estimatedPages = stats.estimatedPages;

        return report;
    }

    /**
     * 优化报告
     */
    public static class OptimizationReport {
        public int totalZones = 0;
        public int totalRows = 0;
        public double totalHeight = 0.0;
        public int pageBreaks = 0;
        public int estimatedPages = 0;

        @Override
        public String toString() {
            return String.format(
                    "OptimizationReport{分区数=%d, 总行数=%d, 总高度=%.2fmm, 分页符=%d, 预估页数=%d}",
                    totalZones, totalRows, totalHeight, pageBreaks, estimatedPages
            );
        }
    }
}
