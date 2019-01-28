package fr.univtln.remimomprive.old;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class Sender
{
    public static void main( String[] args )
    {
        Jedis jPublisher = new Jedis();
        Scanner s = new Scanner(System.in);
        String message;

        do {
            message = s.nextLine();
            jPublisher.publish("channel", message);
        } while (!message.equals("/stop"));
    }
}
