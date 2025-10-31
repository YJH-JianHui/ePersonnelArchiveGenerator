package cn.kmdckj.epersonnelarchivegenerator.engine.layout;

/**
 * 分区元数据 - 存储特殊分区的额外信息
 */
public class ZoneMetadata {

    /**
     * 基础信息区照片元数据
     */
    public static class PhotoMetadata {
        private String photoBase64;  // Base64编码的照片
        private double widthMm;      // 照片宽度(mm)
        private double heightMm;     // 照片高度(mm)

        public PhotoMetadata(String photoBase64) {
            this.photoBase64 = photoBase64;
            this.widthMm = 35.0;   // 默认35mm(约1寸照片宽度)
            this.heightMm = 45.0;  // 默认45mm
        }

        public String getPhotoBase64() {
            return photoBase64;
        }

        public void setPhotoBase64(String photoBase64) {
            this.photoBase64 = photoBase64;
        }

        public double getWidthMm() {
            return widthMm;
        }

        public void setWidthMm(double widthMm) {
            this.widthMm = widthMm;
        }

        public double getHeightMm() {
            return heightMm;
        }

        public void setHeightMm(double heightMm) {
            this.heightMm = heightMm;
        }
    }

    /**
     * 表格区元数据
     */
    public static class TableMetadata {
        private String[] columnHeaders;  // 列标题
        private double[] columnWidths;   // 列宽百分比

        public TableMetadata(String[] columnHeaders, double[] columnWidths) {
            this.columnHeaders = columnHeaders;
            this.columnWidths = columnWidths;
        }

        public String[] getColumnHeaders() {
            return columnHeaders;
        }

        public void setColumnHeaders(String[] columnHeaders) {
            this.columnHeaders = columnHeaders;
        }

        public double[] getColumnWidths() {
            return columnWidths;
        }

        public void setColumnWidths(double[] columnWidths) {
            this.columnWidths = columnWidths;
        }
    }
}
