package com.lh.cd.pub.exception;

/**
 * 应用级异常
 * @author Richard
 * @version 1.0 Apr 10, 2008
 */
public class AppException
        extends Exception {

    /** serialVersionUID */
    private static final long serialVersionUID = 3225443745886864197L;

    private String errorCode = "0";
    
    public String getErrorCode(){   
        return errorCode;   
    }   

    public void setErrorCode(String value){   
        errorCode = value;   
    }

	/**
     * 应用级异常构造函数
     * @param description 异常内容描述信息
     * @param throwable
     */
    public AppException(String errorCode, String description, Throwable throwable) {
        super(description, throwable);
    	setErrorCode(errorCode);
    }


    /**
     * 应用级异常构造函数
     * @param description 异常内容描述信息
     * @param throwable
     */
    public AppException(String description, Throwable throwable) {
        super(description, throwable);
    }

    
    /**
     * 应用级异常构造函数
     * @param description 异常内容描述信息
     */
    public AppException(String errorCode, String description) {
        super(description);
    	setErrorCode(errorCode);
    }

    
    /**
     * 应用级异常构造函数
     * @param description 异常内容描述信息
     */
    public AppException(String description) {
        super(description);
    }
    
    /**
     * 应用级异常构造函数
     */
    public AppException() {
        super();
    }

    /**
     * 应用级异常信息输出
     */
    public String toString() {
    	
        return getMessage();
    }
}
