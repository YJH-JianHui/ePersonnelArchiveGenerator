package cn.kmdckj.epersonnelarchivegenerator.engine.layout;

/**
 * 分页标记 - 表示需要在此处插入分页符
 */
public class PageBreak {
    private String breakType;  // 分页类型: "auto"(自动) 或 "manual"(手动)

    public PageBreak() {
        this.breakType = "auto";
    }

    public PageBreak(String breakType) {
        this.breakType = breakType;
    }

    /**
     * 转换为特殊的LayoutRow用于模板识别
     */
    public LayoutRow toLayoutRow() {
        LayoutRow row = new LayoutRow(true);
        return row;
    }

    public String getBreakType() {
        return breakType;
    }

    public void setBreakType(String breakType) {
        this.breakType = breakType;
    }
}
