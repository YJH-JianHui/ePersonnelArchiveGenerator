package cn.kmdckj.epersonnelarchivegenerator.service;

import org.springframework.stereotype.Component;

/**
 * HTML预处理器
 * 对渲染后的HTML进行优化处理
 */
@Component
public class HtmlPreprocessor {

    /**
     * 预处理HTML
     * - 清理多余空白
     * - 优化CSS
     * - 处理特殊字符
     *
     * @param html 原始HTML
     * @return 处理后的HTML
     */
    public String preprocess(String html) {
        if (html == null || html.isEmpty()) {
            return html;
        }

        String processed = html;

        // 1. 清理多余的空白行(保留必要的格式)
        processed = cleanExcessiveWhitespace(processed);

        // 2. 确保中文字符正常
        processed = ensureChineseCharacters(processed);

        // 3. 优化内联样式(如果需要)
        processed = optimizeInlineStyles(processed);

        return processed;
    }

    /**
     * 清理多余空白
     */
    private String cleanExcessiveWhitespace(String html) {
        // 移除连续的空白行(超过2个换行符的情况)
        return html.replaceAll("(\r?\n){3,}", "\n\n");
    }

    /**
     * 确保中文字符正常显示
     */
    private String ensureChineseCharacters(String html) {
        // 检查是否包含charset声明
        if (!html.contains("charset=") && !html.contains("charset =")) {
            // 在head标签后添加charset声明
            html = html.replaceFirst("<head>",
                    "<head>\n    <meta charset=\"UTF-8\"/>");
        }
        return html;
    }

    /**
     * 优化内联样式
     */
    private String optimizeInlineStyles(String html) {
        // 这里可以添加样式优化逻辑
        // 例如:合并重复的样式,移除无效样式等
        return html;
    }

    /**
     * 添加调试信息(开发环境使用)
     */
    public String addDebugInfo(String html, String info) {
        String debugComment = String.format("\n<!-- Debug Info: %s -->\n", info);
        return html.replaceFirst("<body", debugComment + "<body");
    }
}
