package com.uni.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataInfo {
    @JsonProperty(value = "ID")  
    private Integer id;

    @JsonProperty("Reference")
    private String reference;
}
