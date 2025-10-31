package cn.kmdckj.epersonnelarchivegenerator.util;

import cn.kmdckj.epersonnelarchivegenerator.config.PdfConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * 字体管理器 - 管理PDF渲染所需的字体文件
 */
@Component
public class FontManager {

    private static final Logger logger = LoggerFactory.getLogger(FontManager.class);

    private final PdfConfig pdfConfig;
    private String fontFilePath;

    public FontManager(PdfConfig pdfConfig) {
        this.pdfConfig = pdfConfig;
        initializeFont();
    }

    /**
     * 初始化字体
     */
    private void initializeFont() {
        try {
            // 尝试从classpath加载字体
            ClassPathResource fontResource = new ClassPathResource(pdfConfig.getFontPath());

            if (fontResource.exists()) {
                // 字体文件存在,复制到临时目录
                Path tempFontFile = Files.createTempFile("pdf-font-", ".ttc");
                try (InputStream is = fontResource.getInputStream()) {
                    Files.copy(is, tempFontFile, StandardCopyOption.REPLACE_EXISTING);
                }
                this.fontFilePath = tempFontFile.toAbsolutePath().toString();
                logger.info("字体文件已加载: {}", fontFilePath);
            } else {
                // 字体文件不存在,使用系统默认字体
                logger.warn("字体文件不存在: {}, 将使用系统默认字体", pdfConfig.getFontPath());
                this.fontFilePath = findSystemFont();
            }

        } catch (IOException e) {
            logger.error("加载字体文件失败", e);
            this.fontFilePath = null;
        }
    }

    /**
     * 查找系统字体
     */
    private String findSystemFont() {
        // Windows系统字体路径
        String[] windowsFonts = {
                "C:/Windows/Fonts/simsun.ttc",
                "C:/Windows/Fonts/simhei.ttf",
                "C:/Windows/Fonts/msyh.ttc"
        };

        // Linux系统字体路径
        String[] linuxFonts = {
                "/usr/share/fonts/truetype/wqy/wqy-zenhei.ttc",
                "/usr/share/fonts/truetype/arphic/uming.ttc",
                "/usr/share/fonts/opentype/noto/NotoSansCJK-Regular.ttc"
        };

        // macOS系统字体路径
        String[] macFonts = {
                "/System/Library/Fonts/PingFang.ttc",
                "/Library/Fonts/Songti.ttc"
        };

        // 检查各个系统的字体
        for (String fontPath : windowsFonts) {
            if (new File(fontPath).exists()) {
                logger.info("找到系统字体: {}", fontPath);
                return fontPath;
            }
        }

        for (String fontPath : linuxFonts) {
            if (new File(fontPath).exists()) {
                logger.info("找到系统字体: {}", fontPath);
                return fontPath;
            }
        }

        for (String fontPath : macFonts) {
            if (new File(fontPath).exists()) {
                logger.info("找到系统字体: {}", fontPath);
                return fontPath;
            }
        }

        logger.warn("未找到任何中文字体,PDF中的中文可能无法正常显示");
        return null;
    }

    /**
     * 获取字体文件路径
     */
    public String getFontFilePath() {
        return fontFilePath;
    }

    /**
     * 获取字体族名称
     */
    public String getFontFamily() {
        return pdfConfig.getFontFamily();
    }

    /**
     * 检查字体是否可用
     */
    public boolean isFontAvailable() {
        return fontFilePath != null && new File(fontFilePath).exists();
    }
}
