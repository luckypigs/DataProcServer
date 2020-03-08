package com.ceresdata.util;

public enum ResultStatusCode {

    OK(00000, "Success"),

    /**
     * Authority certification error
     */
    INVALID_TOKEN(10001, "Invalid token"),
    UN_AUTHORIZED(10002,"Authority certification error"),
    UN_LOGIN(10003,"Please login again"),
    SESSION_FAILED(10004,"Session failure"),
    SESSION_REPEAT(10005,"Session repeat"),
    /**
     * rules exception
     */
    RULE_REPEAT(20001,"rule of repeat"),

    /**
     *
     */
    FAILED(99999, "Failed"),
    //ALREADY(99998,"Already running or stoped"),


    INVALID_ParamError(10006, "Param error"),
    INVALID_LoginNameOrPassWordError(100100, "User name or password error！"),
    INVALID_UserNameUsed(10102, "The user name already exists"),
    INVALID_PassWordError(10105, "Password verification error"),
    INVALID_UserNameLengthLimit(10106, "User name length exceeds limit"),
    INVALID_LoginNameNotExists(10107, "This user name is not registered"),
    INVALID_LoginNameOrPasswordisEmpty(10108, "The user name or password is null"),
    SYSTEM_ERR(30001, "System error"),
    INVALID_CLIENTID(30003, "Invalid clientid"),
    INVALID_PASSWORD(30004, "User name or password is incorrect"),
    INVALID_CAPTCHA(30005, "Invalid captcha or captcha overdue"),
    INVALID_ACTIVITY(30007, "Invalid activity"),
    INVALID_PARAMETER_ERROR(40001, "User request parameter error"),
    INVALID_File_Exists(20001, "The md5 test file already exists");



    public String get;
    private int errcode;
    private String errmsg;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    private ResultStatusCode(int Errode, String ErrMsg) {
        this.errcode = Errode;
        this.errmsg = ErrMsg;
    }
}