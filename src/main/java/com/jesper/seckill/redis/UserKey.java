package com.jesper.seckill.redis;

/**
 * Created by Tim on 2018/5/21.
 */
public class UserKey extends BasePrefix {

    public static final int TOKEN_SESSION_EXPIRE = 60*30;//60秒*30
    public static final int TOKEN_EXPIRE = 3600 * 24 * 2;//默认两天172800


    /**
     * 防止被外面实例化
     */
    private UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    /**
     * 需要缓存的字段
     */
    public static UserKey tokenSession = new UserKey(TOKEN_SESSION_EXPIRE, "token");
    public static UserKey getById = new UserKey(0, "id");

}
