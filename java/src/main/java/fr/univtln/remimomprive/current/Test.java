package fr.univtln.remimomprive.current;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


/***
 * DATA TYPES
 * Strings
 * Lists (lpush : ajout top, rpush : ajout bottom)
 * Sets
 * Sorted sets
 * Hashes
 *
 * DATA REPLICATION
 * Sentinel
 * At least 3 instances of Redis running in "sentinel" mode
 * Master - Slave
 * Auto promote a new master if the current master fails
 * By default, read only slave
 * Write on master, replicate asynchronously
 */
public class Test {
    private static void updatePerson(Person person) {
        String key = "person#" + person.getId();
        String timestamp = String.valueOf(new Date().getTime());

        Jedis jedis = new Jedis();
        // Use a transaction
        Transaction t = jedis.multi();

        // Save the data
        t.hset(key, "first_name", person.getFirstName());
        t.hset(key, "last_ name", person.getLastName());
        // Save the timestamp
        t.hset("last_update_date", key, timestamp);
        // Save into a log
        t.lpush("history", "update " + key + " " + timestamp);

        t.exec();
    }

    private static void updatePerson(List<Person> persons) {
        for (Person person : persons) {
            updatePerson(person);
        }
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        updatePerson(Arrays.asList(new Person(1, "John", "Doe"), new Person(2, "Jane", "Doe")));

        long time = Long.valueOf(jedis.hgetAll("last_update_date").get("person#1"));
        System.out.println(new Date(time));
    }
}
