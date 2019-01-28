package fr.univtln.remimomprive.old;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Hello world!
 *
 */
public class Intro
{
    public static void main( String[] args )
    {
        Jedis jedis = new Jedis();

        // Strings
        jedis.set("events/city/rome", "32,15,223,828");
        String cachedResponse = jedis.get("events/city/rome");

        // Lists
        jedis.lpush("queue#tasks", "firstTask");
        jedis.lpush("queue#tasks", "secondTask");
        String task = jedis.rpop("queue#tasks");

        // Sets
        jedis.sadd("nicknames", "nickname#1");
        jedis.sadd("nicknames", "nickname#2");
        jedis.sadd("nicknames", "nickname#1");
        Set<String> nicknames = jedis.smembers("nicknames");
        boolean exists = jedis.sismember("nicknames", "nickname#1");

        // Hashes
        jedis.hset("user#1", "name", "Peter");
        jedis.hset("user#1", "job", "politician");
        String name = jedis.hget("user#1", "name");
        Map<String, String> fields = jedis.hgetAll("user#1");
        String job = fields.get("job");

        // Sorted sets
        Map<String, Double> scores = new HashMap<>();
        scores.put("PlayerOne", 3000.0);
        scores.put("PlayerTwo", 1500.0);
        scores.put("PlayerThree", 8200.0);
        scores.keySet().forEach(player -> {
            jedis.zadd("ranking", scores.get(player), player);
        });
        String player = jedis.zrevrange("ranking", 0, 1).iterator().next();
        long rank = jedis.zrevrank("ranking", "PlayerOne");

        // Transactions
        String friendsPrefix = "friends#";
        String userOneId = "4352523";
        String userTwoId = "5552321";
        Transaction t = jedis.multi();
        t.sadd(friendsPrefix + userOneId, userTwoId);
        t.sadd(friendsPrefix + userTwoId, userOneId);
        t.exec();

        // Pipelining
        Pipeline p = jedis.pipelined();
        p.sadd("searched#" + userOneId, "paris");
        p.zadd("ranking", 126, userOneId);
        p.zadd("ranking", 325, userTwoId);
        Response<Boolean> pipeExists = p.sismember("searched#" + userOneId, "paris");
        Response<Set<String>> pipeRanking = p.zrange("ranking", 0, -1);
        p.sync();

        exists = pipeExists.get();
        Set<String> ranking = pipeRanking.get();
    }
}
