package cn.kmdckj.epersonnelarchivegenerator.service;

/**
 * PDF生成异常
 */
public class PdfGenerationException extends RuntimeException {

    public PdfGenerationException(String message) {
        super(message);
    }

    public PdfGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
