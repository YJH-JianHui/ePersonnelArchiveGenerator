package cn.kmdckj.epersonnelarchivegenerator.engine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 布局配置类 - 从配置文件读取布局参数
 */
@Configuration
@ConfigurationProperties(prefix = "archive.layout")
public class LayoutConfig {

    private PageConfig page = new PageConfig();
    private RowConfig row = new RowConfig();
    private FieldConfig field = new FieldConfig();
    private ZoneConfig zone = new ZoneConfig();

    public static class PageConfig {
        private double width = 210.0;
        private double height = 297.0;
        private double marginTop = 20.0;
        private double marginBottom = 20.0;
        private double marginLeft = 20.0;
        private double marginRight = 20.0;
        private double usableWidth = 170.0;
        private double usableHeight = 257.0;

        // getters and setters
        public double getWidth() { return width; }
        public void setWidth(double width) { this.width = width; }
        public double getHeight() { return height; }
        public void setHeight(double height) { this.height = height; }
        public double getMarginTop() { return marginTop; }
        public void setMarginTop(double marginTop) { this.marginTop = marginTop; }
        public double getMarginBottom() { return marginBottom; }
        public void setMarginBottom(double marginBottom) { this.marginBottom = marginBottom; }
        public double getMarginLeft() { return marginLeft; }
        public void setMarginLeft(double marginLeft) { this.marginLeft = marginLeft; }
        public double getMarginRight() { return marginRight; }
        public void setMarginRight(double marginRight) { this.marginRight = marginRight; }
        public double getUsableWidth() { return usableWidth; }
        public void setUsableWidth(double usableWidth) { this.usableWidth = usableWidth; }
        public double getUsableHeight() { return usableHeight; }
        public void setUsableHeight(double usableHeight) { this.usableHeight = usableHeight; }
    }

    public static class RowConfig {
        private double defaultHeight = 8.0;
        private double titleHeight = 10.0;
        private double separatorHeight = 5.0;
        private double minBreakHeight = 15.0;

        // getters and setters
        public double getDefaultHeight() { return defaultHeight; }
        public void setDefaultHeight(double defaultHeight) { this.defaultHeight = defaultHeight; }
        public double getTitleHeight() { return titleHeight; }
        public void setTitleHeight(double titleHeight) { this.titleHeight = titleHeight; }
        public double getSeparatorHeight() { return separatorHeight; }
        public void setSeparatorHeight(double separatorHeight) { this.separatorHeight = separatorHeight; }
        public double getMinBreakHeight() { return minBreakHeight; }
        public void setMinBreakHeight(double minBreakHeight) { this.minBreakHeight = minBreakHeight; }
    }

    public static class FieldConfig {
        private double minWidth = 20.0;
        private int maxLengthThreshold = 50;

        // getters and setters
        public double getMinWidth() { return minWidth; }
        public void setMinWidth(double minWidth) { this.minWidth = minWidth; }
        public int getMaxLengthThreshold() { return maxLengthThreshold; }
        public void setMaxLengthThreshold(int maxLengthThreshold) { this.maxLengthThreshold = maxLengthThreshold; }
    }

    public static class ZoneConfig {
        private double headerBaseHeight = 60.0;
        private double headerTextWidthPercent = 75.0;
        private double photoWidth = 35.0;
        private double photoHeight = 45.0;

        // getters and setters
        public double getHeaderBaseHeight() { return headerBaseHeight; }
        public void setHeaderBaseHeight(double headerBaseHeight) { this.headerBaseHeight = headerBaseHeight; }
        public double getHeaderTextWidthPercent() { return headerTextWidthPercent; }
        public void setHeaderTextWidthPercent(double headerTextWidthPercent) { this.headerTextWidthPercent = headerTextWidthPercent; }
        public double getPhotoWidth() { return photoWidth; }
        public void setPhotoWidth(double photoWidth) { this.photoWidth = photoWidth; }
        public double getPhotoHeight() { return photoHeight; }
        public void setPhotoHeight(double photoHeight) { this.photoHeight = photoHeight; }
    }

    // Main getters and setters
    public PageConfig getPage() { return page; }
    public void setPage(PageConfig page) { this.page = page; }
    public RowConfig getRow() { return row; }
    public void setRow(RowConfig row) { this.row = row; }
    public FieldConfig getField() { return field; }
    public void setField(FieldConfig field) { this.field = field; }
    public ZoneConfig getZone() { return zone; }
    public void setZone(ZoneConfig zone) { this.zone = zone; }
}
