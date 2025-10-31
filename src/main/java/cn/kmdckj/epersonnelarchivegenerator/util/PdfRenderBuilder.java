package cn.kmdckj.epersonnelarchivegenerator.util;

import cn.kmdckj.epersonnelarchivegenerator.config.PdfConfig;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * PDF渲染构建器 - 配置和创建PDF渲染器
 */
@Component
public class PdfRenderBuilder {

    private static final Logger logger = LoggerFactory.getLogger(PdfRenderBuilder.class);

    private final PdfConfig pdfConfig;
    private final FontManager fontManager;

    public PdfRenderBuilder(PdfConfig pdfConfig, FontManager fontManager) {
        this.pdfConfig = pdfConfig;
        this.fontManager = fontManager;
    }

    /**
     * 创建配置好的PDF渲染器
     *
     * @param html         HTML字符串
     * @param outputStream 输出流
     * @return 配置好的PdfRendererBuilder
     */
    public PdfRendererBuilder createRenderer(String html, ByteArrayOutputStream outputStream) {
        try {
            // 使用JSoup解析HTML并转换为W3C Document
            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
            jsoupDoc.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);

            W3CDom w3cDom = new W3CDom();
            org.w3c.dom.Document doc = w3cDom.fromJsoup(jsoupDoc);

            // 创建PDF渲染器
            PdfRendererBuilder builder = new PdfRendererBuilder();

            // 设置W3C Document
            builder.withW3cDocument(doc, null);

            // 设置输出流
            builder.toStream(outputStream);

            // 配置字体
            if (fontManager.isFontAvailable()) {
                String fontPath = fontManager.getFontFilePath();
                String fontFamily = fontManager.getFontFamily();

                logger.debug("配置PDF字体: family={}, path={}", fontFamily, fontPath);

                builder.useFont(
                        new File(fontPath),
                        fontFamily,
                        400,  // 字重: normal
                        BaseRendererBuilder.FontStyle.NORMAL,
                        true  // 支持子集嵌入
                );
            } else {
                logger.warn("字体不可用,中文可能无法正常显示");
            }

            // 设置DPI
            builder.useDefaultPageSize(
                    210,  // A4宽度(mm)
                    297,  // A4高度(mm)
                    com.openhtmltopdf.pdfboxout.PdfRendererBuilder.PageSizeUnits.MM
            );

            // 设置渲染质量
            if (pdfConfig.getDpi() > 0) {
                // 注意: openhtmltopdf默认使用72 DPI,这里可以通过缩放来模拟更高DPI
                float scale = pdfConfig.getDpi() / 72f;
                // builder.usePageSize() 可以通过调整尺寸来间接控制
            }

            // 启用SVG支持(如果配置了)
            if (pdfConfig.isEnableSvg()) {
                // builder.useSVGDrawer(...) 需要额外配置
            }

            // 调试模式
            if (pdfConfig.isEnableDebug()) {
                builder.testMode(true);
            }

            // 其他优化配置
            builder.useColorProfile(null);  // 使用默认颜色配置
            builder.useFastMode();          // 启用快速模式

            logger.debug("PDF渲染器配置完成");

            return builder;

        } catch (Exception e) {
            logger.error("创建PDF渲染器失败", e);
            throw new RuntimeException("创建PDF渲染器失败", e);
        }
    }
}
