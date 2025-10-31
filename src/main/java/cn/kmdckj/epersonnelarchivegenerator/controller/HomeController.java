package cn.kmdckj.epersonnelarchivegenerator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 首页控制器
 */
@Controller
public class HomeController {

    /**
     * 首页 - 提供使用说明
     */
    @GetMapping("/")
    @ResponseBody
    public String home() {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>人事档案系统</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            max-width: 800px;
                            margin: 50px auto;
                            padding: 20px;
                            line-height: 1.6;
                        }
                        h1 { color: #333; }
                        .endpoint {
                            background: #f4f4f4;
                            padding: 10px;
                            margin: 10px 0;
                            border-left: 4px solid #007bff;
                        }
                        .endpoint code {
                            background: #fff;
                            padding: 2px 6px;
                            border-radius: 3px;
                        }
                        a {
                            color: #007bff;
                            text-decoration: none;
                        }
                        a:hover {
                            text-decoration: underline;
                        }
                        .example {
                            color: #28a745;
                            font-weight: bold;
                        }
                    </style>
                </head>
                <body>
                    <h1>🗂️ 人事档案系统</h1>
                    <p>欢迎使用人事档案电子化系统!</p>
                
                    <h2>📋 可用接口</h2>
                
                    <div class="endpoint">
                        <strong>查看档案PDF(在线预览)</strong><br>
                        <code>GET /employee/{employeeId}</code><br>
                        <span class="example">示例:</span> 
                        <a href="/employee/001" target="_blank">/employee/001</a> |
                        <a href="/employee/002" target="_blank">/employee/002</a> |
                        <a href="/employee/003" target="_blank">/employee/003</a>
                    </div>
                
                    <div class="endpoint">
                        <strong>下载档案PDF</strong><br>
                        <code>GET /employee/{employeeId}/download</code><br>
                        <span class="example">示例:</span> 
                        <a href="/employee/001/download">/employee/001/download</a>
                    </div>
                
                    <div class="endpoint">
                        <strong>查看HTML(调试用)</strong><br>
                        <code>GET /employee/{employeeId}/html</code><br>
                        <span class="example">示例:</span> 
                        <a href="/employee/001/html" target="_blank">/employee/001/html</a>
                    </div>
                
                    <div class="endpoint">
                        <strong>检查员工是否存在</strong><br>
                        <code>GET /employee/{employeeId}/exists</code><br>
                        <span class="example">示例:</span> 
                        <a href="/employee/001/exists" target="_blank">/employee/001/exists</a>
                    </div>
                
                    <div class="endpoint">
                        <strong>获取布局统计(调试用)</strong><br>
                        <code>GET /employee/{employeeId}/stats</code><br>
                        <span class="example">示例:</span> 
                        <a href="/employee/001/stats" target="_blank">/employee/001/stats</a>
                    </div>
                
                    <h2>📊 测试数据</h2>
                    <ul>
                        <li><strong>001</strong> - 张三(完整信息,包含工作经历、教育背景、家庭成员)</li>
                        <li><strong>002</strong> - 李四(字段较少,测试稀疏数据)</li>
                        <li><strong>003</strong> - 王五(包含超长字段,测试长文本处理)</li>
                    </ul>
                
                    <h2>🚀 快速开始</h2>
                    <p>点击上方链接直接查看档案PDF,或在浏览器地址栏输入:</p>
                    <p><code>http://localhost:8080/employee/001</code></p>
                
                    <hr>
                    <p style="text-align: center; color: #666;">
                        人事档案电子化系统 v1.0 | 基于Spring Boot + Thymeleaf + OpenHTMLToPDF
                    </p>
                </body>
                </html>
                """;
    }
}
