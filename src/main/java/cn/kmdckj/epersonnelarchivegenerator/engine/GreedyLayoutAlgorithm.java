package cn.kmdckj.epersonnelarchivegenerator.engine;

import cn.kmdckj.epersonnelarchivegenerator.engine.layout.LayoutField;
import cn.kmdckj.epersonnelarchivegenerator.engine.layout.LayoutRow;

import java.util.ArrayList;
import java.util.List;

/**
 * 贪心布局算法 - 独立工具类
 * 可用于单元测试和算法验证
 */
public class GreedyLayoutAlgorithm {

    private static final double ROW_WIDTH = 100.0;

    /**
     * 贪心填充算法
     * <p>
     * 算法步骤:
     * 1. 遍历所有字段
     * 2. 对于每个字段:
     * a. 如果是超长字段,独占一行
     * b. 否则尝试放入当前行
     * c. 如果当前行放不下,换行
     * 3. 返回行列表
     *
     * @param fields 字段列表
     * @return 行列表
     */
    public static List<LayoutRow> fillGreedy(List<LayoutField> fields) {
        List<LayoutRow> rows = new ArrayList<>();

        if (fields == null || fields.isEmpty()) {
            return rows;
        }

        LayoutRow currentRow = new LayoutRow();

        for (LayoutField field : fields) {
            // 处理超长字段
            if (field.isLongContent()) {
                if (!currentRow.getFields().isEmpty()) {
                    rows.add(currentRow);
                    currentRow = new LayoutRow();
                }

                field.setWidthPercent(100.0);
                currentRow.addField(field);
                rows.add(currentRow);
                currentRow = new LayoutRow();
                continue;
            }

            // 尝试放入当前行
            if (currentRow.canFit(field)) {
                currentRow.addField(field);
            } else {
                // 放不下,换行
                if (currentRow.getFields().isEmpty()) {
                    // 字段太宽,强制占满一行
                    field.setWidthPercent(100.0);
                    currentRow.addField(field);
                    rows.add(currentRow);
                    currentRow = new LayoutRow();
                } else {
                    // 结束当前行,开启新行
                    rows.add(currentRow);
                    currentRow = new LayoutRow();
                    currentRow.addField(field);
                }
            }
        }

        // 添加最后一行
        if (!currentRow.getFields().isEmpty()) {
            rows.add(currentRow);
        }

        return rows;
    }

    /**
     * 计算布局统计信息
     */
    public static LayoutStats calculateStats(List<LayoutRow> rows) {
        LayoutStats stats = new LayoutStats();
        stats.totalRows = rows.size();

        double totalUsedWidth = 0.0;
        int fieldCount = 0;

        for (LayoutRow row : rows) {
            double usedWidth = row.getUsedWidth();
            totalUsedWidth += usedWidth;
            fieldCount += row.getFields().size();

            if (usedWidth < stats.minRowUsage) {
                stats.minRowUsage = usedWidth;
            }
            if (usedWidth > stats.maxRowUsage) {
                stats.maxRowUsage = usedWidth;
            }
        }

        stats.averageRowUsage = stats.totalRows > 0 ? totalUsedWidth / stats.totalRows : 0.0;
        stats.totalFields = fieldCount;

        return stats;
    }

    /**
     * 布局统计信息
     */
    public static class LayoutStats {
        public int totalRows = 0;
        public int totalFields = 0;
        public double averageRowUsage = 0.0;
        public double minRowUsage = 100.0;
        public double maxRowUsage = 0.0;

        @Override
        public String toString() {
            return String.format(
                    "LayoutStats{总行数=%d, 总字段数=%d, 平均行利用率=%.2f%%, 最小利用率=%.2f%%, 最大利用率=%.2f%%}",
                    totalRows, totalFields, averageRowUsage, minRowUsage, maxRowUsage
            );
        }
    }
}
