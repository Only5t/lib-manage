package qiwen.com.library.common.common;

public enum ResultCode implements IResultCode {
    SUSSCESS(200,"操作成功"),
    FAILED(500,"操作失败"),

    UNAUTHORIZED(40001,"token失效"),
    AUTHORIZED_FAILED(40002,"用户认证失败"),
    FORBIDDEN(40003,"没有权限"),

    PARAM_VALIDATE_FAILED(30001,"参数验证失败");


    private long code;
    private String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
