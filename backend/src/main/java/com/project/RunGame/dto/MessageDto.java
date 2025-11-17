package com.project.RunGame.dto;


public class MessageDto {
    private String type;
    private Object data;

    public MessageDto(String type, Object object) {

        this.data = object;
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public Object getData() {
        return this.data;
    }
}
