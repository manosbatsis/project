package com.topideal.shiro.session;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


public class RedisManager {
	
    private String host;
	
    private int port;

    private int expire = 1800;

    //timeout for jedis try to connect to redis server, not expire time! In milliseconds
    private int timeout = 10000;

    private long globalSessionTimeout;
   
    private String password;

    private static JedisPool jedisPool = null;

    public RedisManager(){

    }

    /**
     * 初始化方法
     */
    public void init(){
        if(jedisPool == null){
            if(password != null && !"".equals(password)){
                jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout, password);
            }else if(timeout != 0){
                jedisPool = new JedisPool(new JedisPoolConfig(), host, port,timeout);
            }else{
                jedisPool = new JedisPool(new JedisPoolConfig(), host, port);
            }

        }
    }

    /**
     * get value from redis
     * @param key
     * @return
     */
    public byte[] get(byte[] key){
        byte[] value = null;
        Jedis jedis = jedisPool.getResource();
        try{
            value = jedis.get(key);
        }finally{
            jedis.close();

        }
        return value;
    }



    /**
     * set
     * @param key
     * @param value
     * @return
     */
    public byte[] set(byte[] key,byte[] value){
        Jedis jedis = jedisPool.getResource();
        try{
            jedis.set(key,value);
            if(this.expire != 0){
                jedis.expire(key, this.expire);
            }
        }finally{
            jedis.close();

        }
        return value;
    }

    /**
     * set
     * @param key
     * @param value
     * @param expire 单位秒
     * @return
     */
    public byte[] set(byte[] key,byte[] value,int expire){
        Jedis jedis = jedisPool.getResource();
        try{
            jedis.set(key,value);
            if(expire != 0){
                jedis.expire(key, expire);
            }
        }finally{
            jedis.close();

        }
        return value;
    }

    /**
     * set
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public String set(String key,String value){
        Jedis jedis = jedisPool.getResource();
        try{
            jedis.set(key,value);
            if(expire != 0){
                jedis.expire(key, expire);
            }
        }finally{
            jedis.close();

        }
        return value;
    }



    /**
     * del
     * @param key
     */
    public void del(byte[] key){
        Jedis jedis = jedisPool.getResource();
        try{
            jedis.del(key);
        }finally{
            jedis.close();

        }
    }

    /**
     * del
     * @param key
     */
    public void del(String key){
        Jedis jedis = jedisPool.getResource();
        try{
            jedis.del(key);
        }finally{
            jedis.close();

        }
    }



    /**
     * flush
     */
    public void flushDB(){
        Jedis jedis = jedisPool.getResource();
        try{
            jedis.flushDB();
        }finally{
            jedis.close();

        }
    }

    /**
     * size
     */
    public Long dbSize(){
        Long dbSize = 0L;
        Jedis jedis = jedisPool.getResource();
        try{
            dbSize = jedis.dbSize();
        }finally{
            jedis.close();

        }
        return dbSize;
    }

    /**
     * keys
     * @param pattern
     * @return
     */
    public Set<byte[]> keys(String pattern){
        Set<byte[]> keys = null;
        Jedis jedis = jedisPool.getResource();
        try{
            keys = jedis.keys(pattern.getBytes());
        }finally{
            jedis.close();

        }
        return keys;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public long getGlobalSessionTimeout() {
        return globalSessionTimeout;
    }
    public void setGlobalSessionTimeout(long globalSessionTimeout) {
        this.globalSessionTimeout = globalSessionTimeout;
    }
}