package cn.kmdckj.epersonnelarchivegenerator.service;

/**
 * 渲染异常
 */
public class RenderException extends RuntimeException {

    public RenderException(String message) {
        super(message);
    }

    public RenderException(String message, Throwable cause) {
        super(message, cause);
    }
}
