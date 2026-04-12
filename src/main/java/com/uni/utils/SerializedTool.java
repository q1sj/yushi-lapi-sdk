package com.uni.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.uni.beans.Response.LAPIResponse;

import java.io.IOException;

public class SerializedTool {
    private String jsonString;
    private JsonNode rootJsonNode;

    public SerializedTool(String jsonString) {
        this.jsonString = jsonString;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            rootJsonNode = objectMapper.readTree(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JsonNode getRootJsonNode(){
        return rootJsonNode;
    }

    public String getJsonString(){
        try{
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(rootJsonNode);
        }
        catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    public JsonNode path(String path) {
        try {
            JsonNode tempJsonNode = rootJsonNode;
            String[] split = path.split("\\.");
            for(Integer i = 0; i < split.length; i++){
                String paramName = split[i];
                Integer index = paramName.indexOf("[");
                if(index > -1){
                    Integer paramIndex = 
                        Integer.parseInt(
                            paramName.substring(index + 1, paramName.indexOf("]"))
                        );

                    paramName = paramName.substring(0, index);
                    ArrayNode arrayNode = (ArrayNode) tempJsonNode.get(paramName);
                    tempJsonNode = arrayNode.get(paramIndex);
                }
                else{
                    tempJsonNode = tempJsonNode.get(paramName);
                }
            }
            return tempJsonNode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String format(String jsonString) {
        if(jsonString == null || jsonString.isEmpty()){
            return "";
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            return objectMapper.writeValueAsString(jsonNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public static <T> LAPIResponse deSerializedResponse(String jsonString, Class<T> valueType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            JsonNode responseNode = jsonNode.get("Response");
            JsonNode dataNode = responseNode.get("Data");
            LAPIResponse response = objectMapper.treeToValue(responseNode, LAPIResponse.class);
            T data = objectMapper.treeToValue(dataNode, valueType);
            response.setData(data);
            
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
