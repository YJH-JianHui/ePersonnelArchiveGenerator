package cn.kmdckj.epersonnelarchivegenerator.service;

import cn.kmdckj.epersonnelarchivegenerator.engine.layout.LayoutModel;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Thymeleaf模板渲染服务
 * 将布局模型渲染为HTML字符串
 */
@Service
public class ThymeleafRenderService {

    private final TemplateEngine templateEngine;

    public ThymeleafRenderService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * 渲染档案HTML
     *
     * @param layoutModel 布局模型
     * @return HTML字符串
     */
    public String renderArchiveHtml(LayoutModel layoutModel) {
        if (layoutModel == null) {
            throw new IllegalArgumentException("布局模型不能为空");
        }

        // 创建Thymeleaf上下文
        Context context = new Context();
        context.setVariable("layoutModel", layoutModel);

        // 渲染模板
        return templateEngine.process("archive", context);
    }

    /**
     * 渲染档案HTML(带额外变量)
     *
     * @param layoutModel         布局模型
     * @param additionalVariables 额外变量
     * @return HTML字符串
     */
    public String renderArchiveHtml(LayoutModel layoutModel,
                                    java.util.Map<String, Object> additionalVariables) {
        if (layoutModel == null) {
            throw new IllegalArgumentException("布局模型不能为空");
        }

        Context context = new Context();
        context.setVariable("layoutModel", layoutModel);

        // 添加额外变量
        if (additionalVariables != null && !additionalVariables.isEmpty()) {
            additionalVariables.forEach(context::setVariable);
        }

        return templateEngine.process("archive", context);
    }

    /**
     * 验证HTML是否生成成功
     *
     * @param html HTML字符串
     * @return 是否有效
     */
    public boolean validateHtml(String html) {
        if (html == null || html.trim().isEmpty()) {
            return false;
        }

        // 简单验证:检查是否包含基本的HTML结构
        return html.contains("<html") &&
                html.contains("</html>") &&
                html.contains("<body") &&
                html.contains("</body>");
    }
}
