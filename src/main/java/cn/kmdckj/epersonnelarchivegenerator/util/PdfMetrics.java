package cn.kmdckj.epersonnelarchivegenerator.util;

/**
 * PDF生成指标 - 记录PDF生成的性能数据
 */
public class PdfMetrics {

    private long htmlLength;
    private long pdfSize;
    private long renderTime;
    private String employeeId;

    public PdfMetrics(String employeeId, long htmlLength, long pdfSize, long renderTime) {
        this.employeeId = employeeId;
        this.htmlLength = htmlLength;
        this.pdfSize = pdfSize;
        this.renderTime = renderTime;
    }

    public double getCompressionRatio() {
        if (htmlLength == 0) return 0.0;
        return (double) pdfSize / htmlLength;
    }

    public double getPdfSizeKB() {
        return pdfSize / 1024.0;
    }

    @Override
    public String toString() {
        return String.format(
                "PdfMetrics{员工ID='%s', HTML大小=%d字符, PDF大小=%.2fKB, 渲染耗时=%dms, 压缩比=%.2f}",
                employeeId, htmlLength, getPdfSizeKB(), renderTime, getCompressionRatio()
        );
    }

    // Getters
    public long getHtmlLength() {
        return htmlLength;
    }

    public long getPdfSize() {
        return pdfSize;
    }

    public long getRenderTime() {
        return renderTime;
    }

    public String getEmployeeId() {
        return employeeId;
    }
}
