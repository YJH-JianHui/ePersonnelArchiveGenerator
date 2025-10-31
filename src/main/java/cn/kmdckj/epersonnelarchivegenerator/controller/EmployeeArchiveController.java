package cn.kmdckj.epersonnelarchivegenerator.controller;

import cn.kmdckj.epersonnelarchivegenerator.service.EmployeeArchiveService;
import cn.kmdckj.epersonnelarchivegenerator.service.EmployeeNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 员工档案控制器
 */
@RestController
@RequestMapping("/employee")
public class EmployeeArchiveController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeArchiveController.class);

    private final EmployeeArchiveService employeeArchiveService;

    public EmployeeArchiveController(EmployeeArchiveService employeeArchiveService) {
        this.employeeArchiveService = employeeArchiveService;
    }

    /**
     * 获取员工档案PDF
     * 访问: http://localhost:8080/employee/{employeeId}
     * 例如: http://localhost:8080/employee/001
     *
     * @param employeeId 员工ID
     * @return PDF文件
     */
    @GetMapping("/{employeeId}")
    public ResponseEntity<byte[]> getEmployeeArchivePdf(@PathVariable String employeeId) {
        try {
            logger.info("收到档案PDF请求, 员工ID: {}", employeeId);

            // 生成PDF
            byte[] pdfBytes = employeeArchiveService.generateEmployeeArchivePdf(employeeId);

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentLength(pdfBytes.length);
            // inline: 在浏览器中直接显示; attachment: 下载
            headers.setContentDispositionFormData("inline", "employee-" + employeeId + ".pdf");
            // 禁用缓存,确保每次都是最新数据
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);

            logger.info("返回档案PDF, 员工ID: {}, 大小: {} KB",
                    employeeId, pdfBytes.length / 1024);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (EmployeeNotFoundException e) {
            logger.warn("员工不存在: {}", employeeId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("生成档案PDF失败, 员工ID: {}", employeeId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 下载员工档案PDF
     * 访问: http://localhost:8080/employee/{employeeId}/download
     *
     * @param employeeId 员工ID
     * @return PDF文件(下载模式)
     */
    @GetMapping("/{employeeId}/download")
    public ResponseEntity<byte[]> downloadEmployeeArchivePdf(@PathVariable String employeeId) {
        try {
            logger.info("收到档案PDF下载请求, 员工ID: {}", employeeId);

            byte[] pdfBytes = employeeArchiveService.generateEmployeeArchivePdf(employeeId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentLength(pdfBytes.length);
            // attachment: 强制下载
            headers.setContentDispositionFormData("attachment", "employee-archive-" + employeeId + ".pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("下载档案PDF失败, 员工ID: {}", employeeId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取员工档案HTML(调试用)
     * 访问: http://localhost:8080/employee/{employeeId}/html
     *
     * @param employeeId 员工ID
     * @return HTML字符串
     */
    @GetMapping("/{employeeId}/html")
    public ResponseEntity<String> getEmployeeArchiveHtml(@PathVariable String employeeId) {
        try {
            logger.info("收到档案HTML请求, 员工ID: {}", employeeId);

            String html = employeeArchiveService.generateEmployeeArchiveHtml(employeeId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_HTML);
            headers.setContentLength(html.length());

            return new ResponseEntity<>(html, headers, HttpStatus.OK);

        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("生成档案HTML失败, 员工ID: {}", employeeId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 检查员工是否存在
     * 访问: http://localhost:8080/employee/{employeeId}/exists
     *
     * @param employeeId 员工ID
     * @return 是否存在
     */
    @GetMapping("/{employeeId}/exists")
    public ResponseEntity<Boolean> checkEmployeeExists(@PathVariable String employeeId) {
        boolean exists = employeeArchiveService.employeeExists(employeeId);
        return ResponseEntity.ok(exists);
    }

    /**
     * 获取布局统计信息(调试用)
     * 访问: http://localhost:8080/employee/{employeeId}/stats
     *
     * @param employeeId 员工ID
     * @return 统计信息
     */
    @GetMapping("/{employeeId}/stats")
    public ResponseEntity<String> getLayoutStatistics(@PathVariable String employeeId) {
        try {
            String stats = employeeArchiveService.getLayoutStatistics(employeeId);
            return ResponseEntity.ok(stats);
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("获取统计信息失败, 员工ID: {}", employeeId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
