package cn.kmdckj.epersonnelarchivegenerator.service;

import cn.kmdckj.epersonnelarchivegenerator.engine.LayoutEngine;
import cn.kmdckj.epersonnelarchivegenerator.engine.LayoutOptimizer;
import cn.kmdckj.epersonnelarchivegenerator.engine.layout.LayoutModel;
import cn.kmdckj.epersonnelarchivegenerator.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 档案渲染服务 - 整合布局计算和HTML渲染
 */
@Service
public class ArchiveRenderService {

    private static final Logger logger = LoggerFactory.getLogger(ArchiveRenderService.class);

    private final LayoutEngine layoutEngine;
    private final LayoutOptimizer layoutOptimizer;
    private final ThymeleafRenderService thymeleafRenderService;
    private final HtmlPreprocessor htmlPreprocessor;

    public ArchiveRenderService(LayoutEngine layoutEngine,
                                LayoutOptimizer layoutOptimizer,
                                ThymeleafRenderService thymeleafRenderService,
                                HtmlPreprocessor htmlPreprocessor) {
        this.layoutEngine = layoutEngine;
        this.layoutOptimizer = layoutOptimizer;
        this.thymeleafRenderService = thymeleafRenderService;
        this.htmlPreprocessor = htmlPreprocessor;
    }

    /**
     * 渲染员工档案HTML
     *
     * @param employee 员工数据
     * @return HTML字符串
     */
    public String renderEmployeeArchiveHtml(Employee employee) {
        try {
            logger.info("开始渲染员工档案HTML, 员工ID: {}", employee.getId());

            // 1. 计算布局
            logger.debug("步骤1: 计算布局模型");
            LayoutModel layoutModel = layoutEngine.calculateLayout(employee);

            // 2. 优化布局(包括自动分页)
            logger.debug("步骤2: 优化布局并插入分页符");
            layoutModel = layoutOptimizer.optimize(layoutModel);

            // 3. 渲染HTML
            logger.debug("步骤3: 渲染Thymeleaf模板");
            String html = thymeleafRenderService.renderArchiveHtml(layoutModel);

            // 4. 预处理HTML
            logger.debug("步骤4: 预处理HTML");
            html = htmlPreprocessor.preprocess(html);

            // 5. 验证HTML
            if (!thymeleafRenderService.validateHtml(html)) {
                throw new RenderException("生成的HTML无效");
            }

            logger.info("档案HTML渲染完成, 员工ID: {}, HTML长度: {} 字符",
                    employee.getId(), html.length());

            return html;

        } catch (Exception e) {
            logger.error("渲染档案HTML失败, 员工ID: {}", employee.getId(), e);
            throw new RenderException("渲染档案HTML失败: " + e.getMessage(), e);
        }
    }

    /**
     * 渲染员工档案HTML(调试模式)
     * 包含额外的调试信息
     *
     * @param employee 员工数据
     * @return HTML字符串
     */
    public String renderEmployeeArchiveHtmlDebug(Employee employee) {
        String html = renderEmployeeArchiveHtml(employee);

        // 添加调试信息
        String debugInfo = String.format(
                "员工ID: %s, 渲染时间: %s",
                employee.getId(),
                java.time.LocalDateTime.now()
        );

        return htmlPreprocessor.addDebugInfo(html, debugInfo);
    }

    /**
     * 获取布局统计信息(用于调试)
     */
    public String getLayoutStatistics(Employee employee) {
        LayoutModel layoutModel = layoutEngine.calculateLayout(employee);
        layoutModel = layoutOptimizer.optimize(layoutModel);

        LayoutOptimizer.OptimizationReport report =
                layoutOptimizer.getOptimizationReport(layoutModel);

        return report.toString();
    }
}
