package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    //支持分页的  查询当前用户的会话列表 每个会话只返回一条最新的私信
    List<Message> selectConversations(int userId, int offset, int limit);

    //查询当前用户的会话数量
    int selectConversationCount(int userId);

    //查询某某个会话包含的私信列表
    List<Message> selectLetters(String conversationId, int offset, int limit);

    //查询某个会话包含的私信数量
    int selectLettersCount(String conversationId);

    //查询未读的私信数量  String conversationId-----不是一定有
    int selectLetterUnreadCount(int userId, String conversationId);

    //增加一条消息
    int insertMessage(Message message);

    //修改消息的状态
    int updateStatus(List<Integer> ids, int status);

    //查询某个主题下最新的通知
    Message selectLatestNotice(int userId, String topic);

    //查询某个主题包含的通知数量
    int selectNoticeCount(int userId, String topic);

    //查询未读的通知的数量
    int selectNoticeUnread(int userId, String topic);

    //查询某个主题包含的通知
    List<Message> selectNotices(int userId, String topic, int offset, int limit);

}
