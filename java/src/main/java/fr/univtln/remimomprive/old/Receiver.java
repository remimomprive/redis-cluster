package fr.univtln.remimomprive.old;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisMonitor;
import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class Receiver
{
    public static void main( String[] args )
    {
        Jedis jSubscriber = new Jedis();

        /*jSubscriber.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                System.out.println(message);
            }
        }, "channel");*/

        /*jSubscriber.monitor(new JedisMonitor() {
            public void onCommand(String command) {
                System.out.println(command);
            }
        });*/

        System.out.println(jSubscriber.get("events/1"));
    }
}
