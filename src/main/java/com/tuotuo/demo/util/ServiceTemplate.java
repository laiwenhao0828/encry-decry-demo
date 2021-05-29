package com.tuotuo.demo.util;


import com.tuotuo.demo.resp.BaseResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;


/**
 * @Author xuconghui
 * @Date 2020/10/15 15:25
 * @Version 1.0
 **/

public class ServiceTemplate {
    private static Logger log = LoggerFactory.getLogger(ServiceTemplate.class);
    public static <T> BaseResp<T> service(Supplier<BaseResp<T>> action){
        try {
            return action.get();
        } catch (RuntimeException e) {
            log.error("ServiceTemplate.service - runtimException ", e);
            return BaseResp.error("runtimException");
        } catch (Exception e) {
            log.error("ServiceTemplate.service - exception ", e);
            return BaseResp.error("exception");
        } catch (Error e) {
            log.error("ServiceTemplate.service - error ", e);
            return BaseResp.error("error");
        }
    }
}
