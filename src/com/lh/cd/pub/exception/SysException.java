package com.lh.cd.pub.exception;

/**
 * 系统级异常
 * @author Richard
 * @version 1.0 Apr 10, 2008
 */
public class SysException
        extends Exception {
    
    /** serialVersionUID */
    private static final long serialVersionUID = -817586272335729001L;

    private String errorCode = "0";
    
    public String getErrorCode(){   
        return errorCode;   
    }   

    public void setErrorCode(String value){   
        errorCode = value;   
    }
    /**
     * 系统级异常构造函数
     * @param description 异常内容描述信息
     * @param throwable
     */
    public SysException(String description, Throwable throwable) {
        super(description, throwable);
    }

    /**
     * 系统级异常构造函数
     * @param description 异常内容描述信息
     */
    public SysException(String description) {
        super(description);

    }

    /**
     * 系统级异常构造函数
     */
    public SysException() {
        super();
    }

    /**
     * 系统级异常信息输出
     */
    public String toString() {
        return getMessage();
    }
}
