package com.uni.utils.http;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * LAPI HTTP请求类 包含处理digest鉴权的流程
 */
@Slf4j
public class LAPIHttpConnection extends HttpConnection{

    private String username;
    private String password;

    private int nonceCount; //nonce计数参数。客户端请求的十六进制计数，以00000001开始，每次请求加1，目的是防止重放攻击。

    Map<String, String> authParamsMap; //WWW-Authenticate未鉴权参数map，复用上一次请求获取的鉴权参数

    public LAPIHttpConnection(String username, String password) {
        super();
        this.username = username;
        this.password = password;
        nonceCount = 1;
    }

    public HttpResult get(String url){
        return sendLAPIRequest("GET", url, null);
    }

    public HttpResult post(String url, String body){
        return sendLAPIRequest("POST", url, body);
    }

    public HttpResult put(String url, String body){
        return sendLAPIRequest("PUT", url, body);
    }

    public HttpResult delete(String url){
        return sendLAPIRequest("DELETE", url, null);
    }

    public HttpResult sendLAPIRequest(String method, String url, String body) {
        HttpResult httpResult = new HttpResult(this);
        try {
            //复用上一次请求的WWW-Authenticate未鉴权参数，创建鉴权头
            if(this.authParamsMap != null){
                addRequestHeader("Authorization", createDigestAuthHeader(authParamsMap, method, url));
            }
            //加上Content-Length请求头
            if(body != null){
                addRequestHeader("Content-Length", body.getBytes().length + "");
            }

            //第一次发送请求，不带鉴权头，或复用上一次的WWW-Authenticate未鉴权参数
            sendRequest(method, url, body);

            if(getStatusCode() == 200){
                return httpResult;
            }
            //不带鉴权头，或复用未鉴权参数鉴权失败，服务端返回响应401
            if(getStatusCode() == 401){
                //从响应头中获取WWW-Authenticate未鉴权参数
                String wwwAuthenticate = getResponseHeader("WWW-Authenticate");
                //将WWW-Authenticate未鉴权参数提取成map
                this.authParamsMap = parseAuthHeader(wwwAuthenticate);

                //根据WWW-Authenticate参数创建鉴权头
                String authHeader = createDigestAuthHeader(authParamsMap, method, url);
                addRequestHeader("Authorization", authHeader);

                //发送第二次请求
                sendRequest(method, url, body);

                //第二次请求失败依然返回401，则可能是用户名或密码错误
                if(getStatusCode() == 401){
                    log.error("用户名或密码错误");
                    this.authParamsMap = null;
                }
            }
            else{
                log.error("请求出错，错误码:{}", getStatusCode());
            }

            //每次请求递增nonceCount
            nonceCount++;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        } 
        return httpResult;
    }

    private String createDigestAuthHeader(Map<String, String> authParamsMap, String httpMethod, String uri) throws NoSuchAlgorithmException, URISyntaxException{
        uri = new URI(uri).getPath();
        String nonce = authParamsMap.get("nonce");
        String realm = authParamsMap.get("realm");
        String qop = authParamsMap.get("qop");
        String algorithm = authParamsMap.get("algorithm");
        String cnonce = "0wQGXJQP";
        String HA1 = md5(this.username + ":" + realm + ":" + this.password);
        String HA2 = md5(httpMethod + ":" + uri);
        String response = md5(
                HA1 + ":" + nonce + ":" + String.format("%08x", nonceCount) + ":" + cnonce + ":" + qop + ":" + HA2);
        return "Digest username=\"" + this.username + "\", realm=\"" + realm + "\", nonce=\"" + nonce + "\", uri=\"" + uri
                + "\", algorithm=\"" + algorithm + "\", qop=" + qop + ", response=\"" + response + "\", nc="
                + String.format("%08x", nonceCount) + ", cnonce=\"0wQGXJQP\"";
    }

    // 解析认证头部信息
    private Map<String, String> parseAuthHeader(String authHeader) {
        Map<String, String> headerParams = new HashMap<>();
        Pattern pattern = Pattern.compile("(\\w+)=\"(.*?)\"");
        Matcher matcher = pattern.matcher(authHeader);
        while (matcher.find()) {
            headerParams.put(matcher.group(1), matcher.group(2));
        }
        return headerParams;
    }

    // 计算 MD5 哈希值
    private String md5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(messageDigest);
    }

    // 将字节数组转换为十六进制字符串
    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }
}
