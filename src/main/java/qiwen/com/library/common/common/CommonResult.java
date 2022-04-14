package qiwen.com.library.common.common;

public class CommonResult<T> {
    private long code;
    private String message;
    private T data;

    public CommonResult() {
    }

    public CommonResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public CommonResult(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonResult(T data) {
        this.data = data;
        this.code = 200;
        this.message = "";
    }

    public static CommonResult failed(IResultCode resultCode) {
        return new CommonResult(resultCode.getCode(),resultCode.getMessage());
    }

    public static CommonResult failed(String message) {
        return new CommonResult(ResultCode.FAILED.getCode(),message);
    }

    public static CommonResult validateFailed(String message) {
        return new CommonResult(ResultCode.PARAM_VALIDATE_FAILED.getCode(),message);
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
