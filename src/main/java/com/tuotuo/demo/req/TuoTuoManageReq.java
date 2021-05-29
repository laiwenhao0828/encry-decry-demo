package com.tuotuo.demo.req;


/**
 * @author weili
 * @date 2020/11/29
 */
public class TuoTuoManageReq {

    private String url;

    private String merchantNo;

    private Object paramObj;

    private String version;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public Object getParamObj() {
        return paramObj;
    }

    public void setParamObj(Object paramObj) {
        this.paramObj = paramObj;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public TuoTuoManageReq(String url, String merchantNo, Object paramObj, String version, boolean specialReq) {
        this.url = url;
        this.merchantNo = merchantNo;
        this.paramObj = paramObj;
        this.version = version;
    }
    public TuoTuoManageReq() {
    }

    @Override
    public String toString() {
        return "TuoTuoManageReq{" +
                "url='" + url + '\'' +
                ", merchantNo='" + merchantNo + '\'' +
                ", paramObj=" + paramObj +
                ", version='" + version + '\'' +
                '}';
    }
}
