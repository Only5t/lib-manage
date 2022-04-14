package qiwen.com.library.common.exception;


import qiwen.com.library.common.common.IResultCode;

/**
 * 自定义API异常
 */
public class ApiException extends RuntimeException {
    private IResultCode resultCode;

    public ApiException(IResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public ApiException(String message) {
        super(message);
    }

    public IResultCode getResultCode() {
        return resultCode;
    }

}
