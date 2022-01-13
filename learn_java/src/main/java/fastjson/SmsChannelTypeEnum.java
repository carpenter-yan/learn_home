package fastjson;

public enum SmsChannelTypeEnum {
    JD_RETAIL(1, "京东零售"),

    JD_CLOUD(2, "京东云"),

    CHUANG_LAN(3, "创蓝平台");

    private int code;

    private String desc;

    SmsChannelTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
