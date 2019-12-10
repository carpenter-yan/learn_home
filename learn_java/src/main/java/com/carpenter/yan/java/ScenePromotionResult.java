package com.carpenter.yan.java;

public enum ScenePromotionResult {

    NO_SEND_HAVE_PROMOTION(0, "其他商品已催拍"),
    NO_SEND_UNPAID_ORDER_SW_CLOSED(1, "下单未付款场景开关关闭"),
    NO_SEND_UNPAID_ORDER(2, "下单未付款场景条件不满足"),
    NO_SEND_ADD_CART_SW_CLOSED(3, "加购未下单场景开关关闭"),
    NO_SEND_ADD_CART(4, "加购未下单场景条件不满足"),
    NO_SEND_WATCH_ITEM_SW_CLOSED(5, "用户24小时关注场景开关关闭"),
    NO_SEND_WATCH_ITEM(6, "用户24小时关注场景条件不满足"),
    NO_SEND_HIGH_INTENT_SW_CLOSED(7, "高意图场景开关关闭"),
    NO_SEND_HIGH_INTENT(8, "高意图场景条件不满足"),
    NO_SEND_DELIVER_FAIL(9, "推送GW失败"),
    NO_SEND_ANSWER_PROBLEM(10, "获取配置答案问题"),
    NO_SEND_TOTAL_SW_CLOSED(11, "咨询催拍总开关关闭");

    ScenePromotionResult(Integer value, String name){
        this.value = value;
        this.name = name;
    }

    private final Integer value;
    private final String name;

    public String getName() {
        return name;
    }

    public Integer getValue(){
        return value;
    }
}
