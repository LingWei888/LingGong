package cn.exitcode.day001.apicontect.common;

import lombok.Data;

@Data
public class Result {
    private int code;//200成功400失败
    private String msg;
    private Long total;
    private Object data;
    public static Result result(int code ,String msg, Long total, Object data){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setTotal(total);
        result.setData(data);
        return result;
    }
    public static Result failstr(String msg) {
        Result result = new Result();
        result.setMsg(msg);
        result.setCode(400);
        return result;
    }
    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(200);
        result.setMsg("成功");
        result.setData(data);
        return result;
    }

}
