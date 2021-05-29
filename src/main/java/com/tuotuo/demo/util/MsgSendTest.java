package com.tuotuo.demo.util;

import com.tuotuo.demo.req.TuoTuoManageReq;
import com.tuotuo.demo.resp.BaseResp;
import com.tuotuo.demo.resp.RespCodeEnum;

/**
 * @Author xch
 * @Date 2021/5/24 16:51
 * @Version 1.0
 **/
public class MsgSendTest {
    public static void main(String[] args) {
        TuoTuoManageReq request = new TuoTuoManageReq();
        request.setParamObj("nihao");
        request.setUrl("http://localhost:9001/camelloanweb/kth/preliminariesPush");
        request.setMerchantNo("1122");
        request.setVersion("1.0");
        TuoTuoManageFacade tuoTuoManageFacade = new TuoTuoManageFacade();
        BaseResp baseResp = tuoTuoManageFacade.execute(request);
        if(RespCodeEnum.SUCC.getCode().equals(baseResp.getCode())){
            System.out.println("baseResp = " + baseResp.getData());
        }
    }
}
