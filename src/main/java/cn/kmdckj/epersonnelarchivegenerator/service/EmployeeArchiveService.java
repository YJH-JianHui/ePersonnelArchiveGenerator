package cn.kmdckj.epersonnelarchivegenerator.service;

import cn.kmdckj.epersonnelarchivegenerator.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 员工档案服务 - 整合完整的档案生成流程
 * 数据获取 → HTML渲染 → PDF生成
 */
@Service
public class EmployeeArchiveService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeArchiveService.class);

    private final MockDataService mockDataService;
    private final ArchiveRenderService archiveRenderService;
    private final PdfGeneratorService pdfGeneratorService;

    public EmployeeArchiveService(MockDataService mockDataService,
                                  ArchiveRenderService archiveRenderService,
                                  PdfGeneratorService pdfGeneratorService) {
        this.mockDataService = mockDataService;
        this.archiveRenderService = archiveRenderService;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    /**
     * 生成员工档案PDF
     *
     * @param employeeId 员工ID
     * @return PDF字节数组
     */
    public byte[] generateEmployeeArchivePdf(String employeeId) {
        try {
            logger.info("开始生成员工档案PDF, 员工ID: {}", employeeId);

            // 1. 获取员工数据
            Employee employee = mockDataService.getEmployeeById(employeeId);
            if (employee == null) {
                throw new EmployeeNotFoundException("员工不存在: " + employeeId);
            }

            // 2. 渲染HTML
            String html = archiveRenderService.renderEmployeeArchiveHtml(employee);

            // 3. 生成PDF
            byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtml(html);

            // 4. 验证PDF
            if (!pdfGeneratorService.validatePdf(pdfBytes)) {
                throw new PdfGenerationException("生成的PDF无效");
            }

            logger.info("员工档案PDF生成成功, 员工ID: {}, PDF大小: {:.2f} KB",
                    employeeId,
                    pdfGeneratorService.getPdfSizeInKB(pdfBytes));

            return pdfBytes;

        } catch (EmployeeNotFoundException e) {
            logger.warn("员工不存在: {}", employeeId);
            throw e;
        } catch (Exception e) {
            logger.error("生成员工档案PDF失败, 员工ID: {}", employeeId, e);
            throw new ArchiveGenerationException("生成档案失败: " + e.getMessage(), e);
        }
    }

    /**
     * 生成员工档案HTML(用于调试)
     *
     * @param employeeId 员工ID
     * @return HTML字符串
     */
    public String generateEmployeeArchiveHtml(String employeeId) {
        Employee employee = mockDataService.getEmployeeById(employeeId);
        if (employee == null) {
            throw new EmployeeNotFoundException("员工不存在: " + employeeId);
        }

        return archiveRenderService.renderEmployeeArchiveHtml(employee);
    }

    /**
     * 检查员工是否存在
     */
    public boolean employeeExists(String employeeId) {
        return mockDataService.getEmployeeById(employeeId) != null;
    }

    /**
     * 获取布局统计信息(调试用)
     */
    public String getLayoutStatistics(String employeeId) {
        Employee employee = mockDataService.getEmployeeById(employeeId);
        if (employee == null) {
            throw new EmployeeNotFoundException("员工不存在: " + employeeId);
        }

        return archiveRenderService.getLayoutStatistics(employee);
    }
}
