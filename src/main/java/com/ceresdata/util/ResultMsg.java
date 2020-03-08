package com.ceresdata.util;

import java.util.List;
import java.util.Map;

public class ResultMsg {
    private int code;
    private String msg;
    private Object data;

    public ResultMsg() {
    }

    public ResultMsg(Object P2pData) {
        this.code = ResultStatusCode.OK.getErrcode();
        this.msg = ResultStatusCode.OK.getErrmsg();
        this.data = P2pData;
    }

    public ResultMsg(int code, String msg, Object P2pData) {
        this.code = code;
        this.msg = msg;
        this.data = P2pData;
    }

    public static ResultMsg UNLogin() {
        return new ResultMsg(ResultStatusCode.UN_LOGIN.getErrcode(),
                ResultStatusCode.UN_LOGIN.getErrmsg(), "无权访问，请登陆");
    }

    public static ResultMsg UNAuthorized() {
        return new ResultMsg(ResultStatusCode.UN_AUTHORIZED.getErrcode(),
                ResultStatusCode.UN_AUTHORIZED.getErrmsg(), "权限认证失败");
    }

    public static ResultMsg UNAuthorized(String err) {
        return new ResultMsg(ResultStatusCode.UN_AUTHORIZED.getErrcode(),
                ResultStatusCode.UN_AUTHORIZED.getErrmsg(), err);
    }

    public static ResultMsg SessionFailure() {
        return new ResultMsg(ResultStatusCode.SESSION_FAILED.getErrcode(),
                ResultStatusCode.SESSION_FAILED.getErrmsg(), "登录时间过长，请重新登陆");
    }

    public static ResultMsg RepeatLogin() {
        return new ResultMsg(ResultStatusCode.SESSION_REPEAT.getErrcode(),
                ResultStatusCode.SESSION_REPEAT.getErrmsg(), "已经在其他地方登陆，被迫下线");
    }

    public static ResultMsg Failed(String errmsg) {
        return new ResultMsg(ResultStatusCode.FAILED.getErrcode(),
                ResultStatusCode.FAILED.getErrmsg(), errmsg);
    }

    /**
     * User Parameter Error
     *
     * @return
     */
    public static ResultMsg parameterError(String errmsg) {
        return new ResultMsg(ResultStatusCode.INVALID_PARAMETER_ERROR.getErrcode(),
                ResultStatusCode.INVALID_PARAMETER_ERROR.getErrmsg(), errmsg);
    }

    /**
     *  返回错误编码和消息
     *
     * @param errmsg
     * @return
     */
    public static ResultMsg error(String errmsg) {
        ResultMsg resultMsg =  new ResultMsg();
        resultMsg.setMsg(errmsg);
        resultMsg.setCode(ResultStatusCode.RULE_REPEAT.getErrcode());
        return  resultMsg;
    }

    /**
     * 返回成功消息实体
     * @param msg
     * @return
     */
    public static ResultMsg success(String msg) {
        return  success(msg,null);
    }

    /**
     * 返回成功消息实体
     * @param msg 消息
     * @param data 数据
     * @return
     */
    public static ResultMsg success(String msg, Object data) {
        ResultMsg resultMsg =  new ResultMsg();
        resultMsg.setMsg(msg);
        resultMsg.setData(data);
        return  resultMsg;
    }

    /**
     * 返回列表数据的消息
     * @param data
     * @param totalCount
     * @return
     */
    public static ResultMsg returnList(List data, int totalCount) {
       return new ResultListMsg(data,totalCount);
    }
    /**
     * 返回的列表的消息格式
     *
     * @param map
     * @return
     */
    public static ResultMsg returnList(Map map) {
        List list = (List)map.get("data");
        list = list  == null ? (List) map.get("list"): list;
        Integer all = (Integer)map.get("all");
        return returnList(list,all);
    }

    /**
     * 返回列表消息格式
     *
     * @param list
     * @return
     */
    public static ResultMsg returnList(List list) {
        Integer all = list.size();
        return returnList(list,all);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 多个验证规则消息将错误信息拼接在一起
     * @param messages
     * @return
     */
    public static ResultMsg combineMesaages(ResultMsg[] messages){
        return combineMesaages(messages,false);
    }

    public static ResultMsg combineMesaages(ResultMsg[] messages, boolean allCombine){
        ResultMsg result = new ResultMsg();
        result.setMsg("成功:");
        StringBuffer errmsg = new StringBuffer();
        StringBuffer succmsg = new StringBuffer();
        // 收集错误消息
        if(messages != null){
            for(ResultMsg rm : messages){
                if(rm.getCode() != 0){
                    errmsg.append(rm.getMsg());
                }else{
                    // 成功消息也也需要合并
                    if(allCombine){
                        succmsg.append(rm.getMsg());
                    }
                }
            }
        }
        if(errmsg.length() > 0){
            result.setMsg(errmsg.toString());
            result.setCode(ResultStatusCode.FAILED.getErrcode());
        }else{
            String msg = succmsg.length() > 0 ? succmsg.toString() : "success";
            result.setMsg(msg);
        }
        return result;
    }

    /**
     * 合并消息
     * @param messages
     * @return
     */
    public static ResultMsg combineMesaages(List<ResultMsg> messages){
        return combineMesaages(messages,false);
    }

    /**
     * 合并消息：
     * @param messages 消息数据
     * @param isCombineAll 如果属性为true 将错误和正确的消息全部合并
     * @return
     */
    public static ResultMsg combineMesaages(List<ResultMsg> messages, boolean isCombineAll){
        ResultMsg[] array = messages.toArray(new ResultMsg[]{});
        return combineMesaages(array,isCombineAll);
    }

    /**
     * 是否成功消息
     * @param rm
     * @return
     */
    public static boolean isSucessMsg(ResultMsg rm){
        return ResultStatusCode.OK.getErrcode() == rm.getCode();
    }
}
