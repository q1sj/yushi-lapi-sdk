package com.uni.utils.http;

import com.uni.beans.Response.LAPIResponse;
import com.uni.utils.SerializedTool;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 获取HTTP结果的类
 */
public class HttpResult {
    private HttpConnection httpConnection;
    private byte[] bytes;

    public HttpResult(HttpConnection httpConnection){
        this.httpConnection = httpConnection;
    }

    /**
     * 获取响应体字符串
     */
    public String readResponseString(Charset charset){
        if(this.bytes != null){
            return new String(this.bytes, charset);
        }

        String response = null;
        byte[] data = readResponseBytes();
        response = new String(data, charset);
        return response;
    }

    /**
     * 获取响应体字符串
     */
    public String readResponseString(){
        return readResponseString(StandardCharsets.UTF_8);
    }

    /**
     * 获取响应体char数组
     */
    public byte[] readResponseBytes(){
        if(this.bytes != null){
            return this.bytes;
        }

        byte[] data = null;
        try {
            data = httpConnection.readResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.bytes = data;
        return data;
    }

    /**
     * 将响应数据封装成类
     * 
     * @param dataType - LAPI的response的Data类型
     */
    public <T> LAPIResponse readLAPIResponse(Class<T> dataType) {
        String responseString = readResponseString();
        if(responseString == null || responseString.isEmpty()){
            return null;
        }
        return SerializedTool.deSerializedResponse(responseString, dataType);
    }

    /**
     * 将响应数据封装成类
     * 
     * @param dataType - LAPI的response的Data类型
     */
    public <T> LAPIResponse readLAPIResponse() {
        return readLAPIResponse(Object.class);
    }
}
