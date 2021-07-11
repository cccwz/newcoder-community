package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private RedisTemplate redisTemplate;

    //点赞

    /**
     *
     * @param userId 当前用户的id
     * @param entityType
     * @param entityId
     * @param entityUserId  实体所属的user的id
     */
    public void like(int userId,int entityType,int entityId,int entityUserId){
        /*String entityLikeKey= RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        Boolean ismember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
        if(ismember){
            redisTemplate.opsForSet().remove(entityLikeKey,userId);
        }else {
            redisTemplate.opsForSet().add(entityLikeKey,userId);
        }*/
        //放入redis的执行队列
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey= RedisKeyUtil.getEntityLikeKey(entityType,entityId);
                //entityUserId---该实体拥有者的userid
                String userLikeKey= RedisKeyUtil.getUserLikeKey(entityUserId);
                boolean isMember=redisTemplate.opsForSet().isMember(entityLikeKey,userId);
                operations.multi();
                if(isMember){
                    operations.opsForSet().remove(entityLikeKey,userId);
                    operations.opsForValue().decrement(userLikeKey);
                }else {
                    operations.opsForSet().add(entityLikeKey,userId);
                    operations.opsForValue().increment(userLikeKey);
                }
                return operations.exec();
            }
        });
    }

    //查询某个实体点赞的数量
    public long findEntityLikeCount(int entityType,int entityId){
        String entityLikeKey= RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    //某人是否对某实体点赞了
    public int findEntityLikeStatus(int userId,int entityType,int entityId){
        String entityLikeKey= RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        //1---点赞了    0---没点
        return redisTemplate.opsForSet().isMember(entityLikeKey,userId)?1:0;
    }

    //查询某个用户获得的赞数
    public int findUserLikeCount(int userId){
        String userLikeKey= RedisKeyUtil.getUserLikeKey(userId);
        Integer count= (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count==null?0:count.intValue();
    }

}
