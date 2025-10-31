package cn.kmdckj.epersonnelarchivegenerator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * PDF生成配置
 */
@Configuration
@ConfigurationProperties(prefix = "archive.pdf")
public class PdfConfig {

    private String fontPath = "fonts/simsun.ttc";  // 中文字体路径
    private String fontFamily = "SimSun";           // 字体名称
    private int dpi = 96;                           // DPI设置
    private boolean enableSvg = false;              // 是否启用SVG支持
    private boolean enableDebug = false;            // 是否启用调试模式

    // Getters and Setters
    public String getFontPath() {
        return fontPath;
    }

    public void setFontPath(String fontPath) {
        this.fontPath = fontPath;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public int getDpi() {
        return dpi;
    }

    public void setDpi(int dpi) {
        this.dpi = dpi;
    }

    public boolean isEnableSvg() {
        return enableSvg;
    }

    public void setEnableSvg(boolean enableSvg) {
        this.enableSvg = enableSvg;
    }

    public boolean isEnableDebug() {
        return enableDebug;
    }

    public void setEnableDebug(boolean enableDebug) {
        this.enableDebug = enableDebug;
    }
}
