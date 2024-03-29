package cn.yubajin.multiexport.common.constant;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 每个业务，100个范围值就够了.
 */
@Slf4j
public class ResponseCodeConst {

    // 范围声明
    static {
        // 系统功能，从0开始，step=1000
        ResponseCodeContainer.register(ResponseCodeConst.class, 0, 1000);
//        ResponseCodeContainer.register(InnovationStudioResponseCodeConst.class, 1001, 1999);

    }

    public static final ResponseCodeConst SUCCESS = new ResponseCodeConst(200, "操作成功!", true);

    public static final ResponseCodeConst ERROR_PARAM = new ResponseCodeConst(101, "参数异常！");

    public static final ResponseCodeConst ERROR_PARAM_ANY = new ResponseCodeConst(102, "%s参数异常！");

    public static final ResponseCodeConst SYSTEM_ERROR = new ResponseCodeConst(111, "系统错误");

    public static final ResponseCodeConst DEVELOPMENT = new ResponseCodeConst(112, "此功能正在开发中");

    public static final ResponseCodeConst NOT_EXISTS = new ResponseCodeConst(113, "数据不存在");

    public static ResponseCodeConst REQUEST_METHOD_ERROR = new ResponseCodeConst(114, "请求方式错误");

    public static ResponseCodeConst JSON_FORMAT_ERROR = new ResponseCodeConst(115, "JSON格式错误");

    public static ResponseCodeConst SQL_EXEC_ERROR = new ResponseCodeConst(116, "SQL执行失败");

    public static ResponseCodeConst FILE_UPLOAD_ERROR = new ResponseCodeConst(117, "文件上传失败");

    public static ResponseCodeConst FILE_DEL_ERROR = new ResponseCodeConst(118, "文件删除失败");

    public static ResponseCodeConst FILE_DOWNLOAD_ERROR = new ResponseCodeConst(119, "文件下载失败");

    public static ResponseCodeConst SYSTEM_PARAM_ERROR = new ResponseCodeConst(120, "系统参数解析错误");

    public static ResponseCodeConst TIME_PARAM_ERROR = new ResponseCodeConst(121, "时间格式不正确");

    public static ResponseCodeConst USER_NOT_FOUND = new ResponseCodeConst(122, "用户不存在");

    protected int code;

    protected String msg;

    protected boolean success;

    public ResponseCodeConst() {
    }

    protected ResponseCodeConst(int code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
        ResponseCodeContainer.put(this);
    }

    protected ResponseCodeConst(int code, String msg, boolean success) {
        super();
        this.code = code;
        this.msg = msg;
        this.success = success;
        ResponseCodeContainer.put(this);
    }

    protected ResponseCodeConst(int code) {
        super();
        this.code = code;
        ResponseCodeContainer.put(this);
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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static void init() {
        log.info("ResponseCodeConst init....");
    }

    // =======================分割=======================

    /**
     * 内部类，用于检测code范围
     *
     * @author Anders
     */
    @Slf4j
    private static class ResponseCodeContainer {

        private static final Map<Integer, ResponseCodeConst> RESPONSE_CODE_MAP = new HashMap<>();

        private static final Map<Class<? extends ResponseCodeConst>, int[]> RESPONSE_CODE_RANGE_MAP = new HashMap<>();

        /**
         * id的范围：[start, end]左闭右闭
         *
         * @param clazz
         * @param start
         * @param end
         */
        private static void register(Class<? extends ResponseCodeConst> clazz, int start, int end) {
            if (start > end) {
                throw new IllegalArgumentException("<ResponseDTO> start > end!");
            }

            if (RESPONSE_CODE_RANGE_MAP.containsKey(clazz)) {
                throw new IllegalArgumentException(String.format("<ResponseDTO> Class:%s already exist!", clazz.getSimpleName()));
            }
            RESPONSE_CODE_RANGE_MAP.forEach((k, v) -> {
                if ((start >= v[0] && start <= v[1]) || (end >= v[0] && end <= v[1])) {
                    throw new IllegalArgumentException(String.format("<ResponseDTO> Class:%s 's id range[%d,%d] has " + "intersection with " + "class:%s", clazz.getSimpleName(), start, end,
                            k.getSimpleName()));
                }
            });

            RESPONSE_CODE_RANGE_MAP.put(clazz, new int[]{start, end});

            // 提前初始化static变量，进行范围检测
            Field[] fields = clazz.getFields();
            if (fields.length != 0) {
                try {
                    fields[0].get(clazz);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    log.error("", e);
                }
            }
        }

        public static void put(ResponseCodeConst codeConst) {
            int[] idRange = RESPONSE_CODE_RANGE_MAP.get(codeConst.getClass()); //利用反射获取当前类
            if (idRange == null) {
                throw new IllegalArgumentException(String.format("<ResponseDTO> Class:%s has not been registered!", codeConst.getClass().getSimpleName()));
            }
            int code = codeConst.code;
            if (code < idRange[0] || code > idRange[1]) {
                throw new IllegalArgumentException(String.format("<ResponseDTO> Id(%d) out of range[%d,%d], " + "class:%s", code, idRange[0], idRange[1], codeConst.getClass().getSimpleName()));
            }
            if (RESPONSE_CODE_MAP.keySet().contains(code)) {
                log.error(String.format("<ResponseDTO> Id(%d) out of range[%d,%d], " + "class:%s  code is repeat!", code, idRange[0], idRange[1], codeConst.getClass().getSimpleName()));
                System.exit(0);
            }
            RESPONSE_CODE_MAP.put(code, codeConst);
        }

    }

}
