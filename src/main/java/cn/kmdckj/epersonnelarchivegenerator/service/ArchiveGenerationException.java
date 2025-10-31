package cn.kmdckj.epersonnelarchivegenerator.service;

/**
 * 档案生成异常
 */
public class ArchiveGenerationException extends RuntimeException {

    public ArchiveGenerationException(String message) {
        super(message);
    }

    public ArchiveGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}