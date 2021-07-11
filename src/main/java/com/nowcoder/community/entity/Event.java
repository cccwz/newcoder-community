package com.nowcoder.community.entity;


import java.util.HashMap;
import java.util.Map;

public class Event {
    private String topic;//主题
    private int userId;//谁发的，谁点赞的
    private int entityType;//对x点赞或者评论--- x的种类
    private int entityId;//x的id
    private int entityUserId;//x的归属者---即系统要把消息发给谁
    private Map<String,Object> data=new HashMap<>();

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object value) {
        this.data.put(key,value);
        return this;
    }
}
