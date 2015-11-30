package cn.howardliu.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;
import static java.util.Arrays.asList;

/**
 * <br/>create at 15-11-30
 *
 * @author liuxh
 * @since 1.0.0
 */
public class MongodbTwoPhaseCommitsTest {
    private static final Logger logger = LoggerFactory.getLogger(MongodbTwoPhaseCommitsTest.class);
    private MongoDatabase db = null;

    @Before
    public void before() {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        db = mongoClient.getDatabase("test");

        db.getCollection("accounts").drop();
        db.getCollection("accounts").insertMany(asList(
                new Document().append("_id", "A").append("balance", 1000).append("pendingTransactions", asList()),
                new Document().append("_id", "B").append("balance", 1000).append("pendingTransactions", asList())
        ));

        db.getCollection("transactions").drop();
        db.getCollection("transactions").insertOne(
                new Document().append("_id", 1).append("source", "A").append("destination", "B").append("value", 100)
                        .append("state", "initial").append("lastModified", new Date())
        );
    }

    @Test
    public void test() {
        // 1. Retrieve the transaction to start.
        Document t = db.getCollection("transactions").find(eq("state", "initial")).first();
        logger.debug(t.toJson());
        // 2. Update transaction state to pending.
        db.getCollection("transactions").updateOne(
                and(eq("_id", t.get("_id")), eq("state", "initial")),
                combine(set("state", "pending"), currentDate("lastModified"))
        );
        // 3. Apply the transaction to both accounts.
        db.getCollection("accounts").updateOne(
                and(eq("_id", t.get("source")), ne("pendingTransactions", t.get("_id"))),
                combine(inc("balance", -t.getInteger("value")), push("pendingTransactions", t.get("_id")))
        );
        db.getCollection("accounts").updateOne(
                and(eq("_id", t.get("destination")), ne("pendingTransactions", t.get("_id"))),
                combine(inc("balance", t.getInteger("value")), push("pendingTransactions", t.get("_id")))
        );
        // 4. Update transaction state to applied.
        db.getCollection("transactions").updateOne(
                and(eq("_id", t.get("_id")), eq("state", "pending")),
                combine(set("state", "applied"), currentDate("lastModified"))
        );
        // 5. Update both accounts’ list of pending transactions.
        db.getCollection("accounts").updateOne(
                and(eq("_id", t.get("source")), eq("pendingTransactions", t.get("_id"))),
                pull("pendingTransactions", t.get("_id"))
        );
        db.getCollection("accounts").updateOne(
                and(eq("_id", t.get("destination")), eq("pendingTransactions", t.get("_id"))),
                pull("pendingTransactions", t.get("_id"))
        );
        // 6. Update transaction state to done.
        db.getCollection("transactions").updateOne(
                and(eq("_id", t.get("_id")), eq("state", "applied")),
                combine(set("state", "done"), currentDate("lastModified"))
        );
    }

    @After
    public void after() {
        db.getCollection("transactions").drop();
        db.getCollection("accounts").drop();
    }
}
