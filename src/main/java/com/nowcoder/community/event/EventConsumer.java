package com.nowcoder.community.event;


import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class EventConsumer implements CommunityConstant {
    private static final Logger LOGGER= LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private DiscussPostService discussPostService;

    /*@Autowired
    private ElasticSearchService elasticSearchService;*/

    @KafkaListener(topics = {TOPIC_LIKE,TOPIC_FOLLOW,TOPIC_COMMENT})
    public void handleCommentMessage(ConsumerRecord record){
        if(record==null||record.value()==null){
            LOGGER.error("消息为空");
        }
        Event event= JSONObject.parseObject(record.value().toString(), Event.class);
        if(event==null){
            LOGGER.error("消息格式错误");
        }
        //发送站内通知
        Message message = new Message();
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());

        Map<String,Object> content=new HashMap<>();
        content.put("userId",event.getUserId());
        content.put("entityType",event.getEntityType());
        content.put("entityId",event.getEntityId());

        if(!event.getData().isEmpty()){
            for(Map.Entry<String,Object> entry:event.getData().entrySet()){
                content.put(entry.getKey(),entry.getValue());
            }
        }

        //content字段是json字符串，取的时候需要转回来
        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);
    }

    /*//消费发帖事件
    @KafkaListener(topics = {TOPIC_PUBLISH})
    public void handlePublishMessage(ConsumerRecord record){
        if(record==null||record.value()==null){
            LOGGER.error("消息为空");
            return;
        }
        Event event= JSONObject.parseObject(record.value().toString(), Event.class);
        if(event==null){
            LOGGER.error("消息格式错误");
            return;
        }

        //得到帖子的id 从数据库中找  上传到elasticsearch
        DiscussPost post = discussPostService.findDiscussPostById(event.getEntityId());
        elasticSearchService.saveDiscussPost(post);
    }

    //消费发帖事件
    @KafkaListener(topics = {TOPIC_DELETE})
    public void handleDeleteMessage(ConsumerRecord record){
        if(record==null||record.value()==null){
            LOGGER.error("消息为空");
            return;
        }
        Event event= JSONObject.parseObject(record.value().toString(), Event.class);
        if(event==null){
            LOGGER.error("消息格式错误");
            return;
        }


        elasticSearchService.deleteDiscussPostById(event.getEntityId());
    }*/
}
