package com.qa.util;

import java.util.Date;
import java.util.UUID;

/**
 * Creaed by fj on 2018/6/6
 */
public class OrderUtil {

    public static String createOrderId(){
        UUID uuid = UUID.randomUUID();
        String orderId = uuid.toString()+new Date().getTime();
        return orderId;
    }
}
