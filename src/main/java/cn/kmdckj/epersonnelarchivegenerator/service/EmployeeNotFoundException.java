package cn.kmdckj.epersonnelarchivegenerator.service;

/**
 * 员工不存在异常
 */
public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
