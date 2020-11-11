package com.github.tanyueran.constant;

public enum OrderStatus {
    /*
    * 订单状态(
    0、已下单，未付款；
    5、未付款，订单取消；
    10、已付款，待发货；
    15、已拒单，订单取消；
    20、已接单，待配货；
    30、已配送，待收货；
    40、已收货，完成订单)下单后30分未付款，则取消订单；付款后30分钟未接单，则订单取消
    * */
    CREATED_NOT_MONEY(0),
    NOT_MONEY_ORDER_CANCEL(5),
    PAID_WAITTING_SEND(10),
    REFUSE_CANCEL(15),
    SEND_WAITTING_GET(30),
    ORDER_SUCCESS(40);

    private Integer status;

    OrderStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

}
