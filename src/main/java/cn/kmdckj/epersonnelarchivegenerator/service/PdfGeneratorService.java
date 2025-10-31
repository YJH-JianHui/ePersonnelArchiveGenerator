package cn.kmdckj.epersonnelarchivegenerator.service;

import cn.kmdckj.epersonnelarchivegenerator.util.PdfRenderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

/**
 * PDF生成服务 - 将HTML转换为PDF
 */
@Service
public class PdfGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(PdfGeneratorService.class);

    private final PdfRenderBuilder pdfRenderBuilder;

    public PdfGeneratorService(PdfRenderBuilder pdfRenderBuilder) {
        this.pdfRenderBuilder = pdfRenderBuilder;
    }

    /**
     * 将HTML字符串转换为PDF字节数组
     *
     * @param html HTML字符串
     * @return PDF字节数组
     */
    public byte[] generatePdfFromHtml(String html) {
        if (html == null || html.trim().isEmpty()) {
            throw new IllegalArgumentException("HTML内容不能为空");
        }

        try {
            logger.debug("开始生成PDF, HTML长度: {} 字符", html.length());
            long startTime = System.currentTimeMillis();

            // 创建输出流
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // 创建PDF渲染器并渲染
            pdfRenderBuilder.createRenderer(html, outputStream).run();

            // 获取PDF字节数组
            byte[] pdfBytes = outputStream.toByteArray();

            long duration = System.currentTimeMillis() - startTime;
            logger.info("PDF生成成功, 大小: {} KB, 耗时: {} ms",
                    pdfBytes.length / 1024, duration);

            return pdfBytes;

        } catch (Exception e) {
            logger.error("生成PDF失败", e);
            throw new PdfGenerationException("生成PDF失败: " + e.getMessage(), e);
        }
    }

    /**
     * 验证PDF是否生成成功
     *
     * @param pdfBytes PDF字节数组
     * @return 是否有效
     */
    public boolean validatePdf(byte[] pdfBytes) {
        if (pdfBytes == null || pdfBytes.length == 0) {
            return false;
        }

        // 检查PDF文件头(PDF文件以 "%PDF-" 开始)
        if (pdfBytes.length < 5) {
            return false;
        }

        String header = new String(pdfBytes, 0, 5);
        return header.equals("%PDF-");
    }

    /**
     * 获取PDF文件大小(KB)
     */
    public double getPdfSizeInKB(byte[] pdfBytes) {
        if (pdfBytes == null) {
            return 0.0;
        }
        return pdfBytes.length / 1024.0;
    }
}
