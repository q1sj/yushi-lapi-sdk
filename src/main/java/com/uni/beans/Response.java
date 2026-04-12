package com.uni.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Response {
    @JsonProperty(value = "Response")  
    private LAPIResponse response;
    @Data
    public static class LAPIResponse{
        @JsonProperty(value = "ResponseURL")  
        private String responseURL;
        @JsonProperty(value = "CreatedID")  
        private Integer createdID;
        @JsonProperty(value = "ResponseCode") 
        private int responseCode;
        @JsonProperty(value = "SubResponseCode") 
        private int subResponseCode;
        @JsonProperty(value = "ResponseString") 
        private String responseString;
        @JsonProperty(value = "StatusCode") 
        private int statusCode;
        @JsonProperty(value = "StatusString") 
        private String statusString;
        @JsonProperty(value = "Data") 
        private Object data;

        public <T> T getData(){
            return (T) data;
        }
    }
}
