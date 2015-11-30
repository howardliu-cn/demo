package cn.howardliu.mongodb;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Indexes.ascending;
import static com.mongodb.client.model.Indexes.descending;
import static java.util.Arrays.asList;

/**
 * <br/>create at 15-11-30
 *
 * @author liuxh
 * @since 1.0.0
 */
public class MongodbTest {
    private static final Logger logger = LoggerFactory.getLogger(MongodbTest.class);
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
    private MongoDatabase db = null;

    @Before
    public void before() {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        db = mongoClient.getDatabase("test");
    }

    @Test
    public void testInsertOne() throws ParseException {
        db.getCollection("restaurants").insertOne(
                new Document()
                        .append("address", new Document()
                                .append("street", "2 Avenue")
                                .append("zipcode", "10075")
                                .append("building", "1480")
                                .append("coord", asList(-73.9557413, 40.7720266)))
                        .append("borough", "Manhattan")
                        .append("cuisine", "Italian")
                        .append("grades", asList(
                                new Document()
                                        .append("date", format.parse("2014-10-01T00:00:00Z"))
                                        .append("grade", "A")
                                        .append("score", 11),
                                new Document()
                                        .append("date", format.parse("2014-01-16T00:00:00Z"))
                                        .append("grade", "B")
                                        .append("score", 17)))
                        .append("name", "Vella")
                        .append("restaurant_id", "41704620"));
    }

    @Test
    @Ignore
    public void testFindAll() {
        FindIterable<Document> iterable = db.getCollection("restaurants").find();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document);
            }
        });
    }

    @Test
    @Ignore
    public void testFindByCondition() {
        // Query by a Top Level Field
        db.getCollection("restaurants").find(new Document("borough", "Manhattan"));
        db.getCollection("restaurants").find(eq("borough", "Manhattan"));
        // Query by a Field in an Embedded Document
        db.getCollection("restaurants").find(new Document("address.zipcode", "10075"));
        db.getCollection("restaurants").find(eq("address.zipcode", "10075"));
        // Query by a Field in an Array
        db.getCollection("restaurants").find(new Document("grades.grade", "B"));
        db.getCollection("restaurants").find(eq("grades.grade", "B"));

        // Specify Conditions with Operators
        // Greater Than Operator ($gt)
        db.getCollection("restaurants").find(new Document("grades.score", new Document("$gt", 30)));
        db.getCollection("restaurants").find(gt("grades.score", 30));
        // Less Than Operator ($lt)
        db.getCollection("restaurants").find(new Document("grades.score", new Document("$lt", 10)));
        db.getCollection("restaurants").find(lt("grades.score", 10));

        // Combine Conditions
        // Logical AND
        db.getCollection("restaurants")
                .find(new Document().append("cuisine", "Italian").append("address.zipcode", "10075"));
        db.getCollection("restaurants").find(and(eq("cuisine", "Italian"), eq("address.zipcode", "10075")));
        // Logical OR
        db.getCollection("restaurants").find(new Document()
                .append("$or", asList(new Document("cuisine", "Italian"), new Document("address.zipcode", "10075"))));
        db.getCollection("restaurants").find(or(eq("cuisine", "Italian"), eq("address.zipcode", "10075")));

        // Sort Query Results
        db.getCollection("restaurants").find().sort(new Document("borough", 1).append("address.zipcode", 1));
        db.getCollection("restaurants").find().sort(ascending("borough", "address.zipcode"));
        db.getCollection("restaurants").find().sort(ascending("borough")).sort(descending("address.zipcode"));
    }

    @Test
    @Ignore
    public void testUpdate() {
        // Update Specific Fields
        // Update Top-Level Fields
        db.getCollection("restaurants")
                .updateOne(new Document("name", "Juni"),
                        new Document("$set", new Document("cuisine", "American (New)"))
                                .append("$currentDate", new Document("lastModified", true)));
        // Update an Embedded Field
        db.getCollection("restaurants")
                .updateOne(new Document("restaurant_id", "41156888"),
                        new Document("$set", new Document("address.street", "East 31st Street")));
        // Update Multiple Documents
        db.getCollection("restaurants")
                .updateMany(new Document("address.zipcode", "10016").append("cuisine", "Other"),
                        new Document("$set", new Document("cuisine", "Category To Be Determined"))
                                .append("$currentDate", new Document("lastModified", true)));

        // Replace a Document
        db.getCollection("restaurants")
                .replaceOne(new Document("restaurant_id", "41704620"),
                        new Document()
                                .append("address",
                                        new Document()
                                                .append("street", "2 Avenue")
                                                .append("zipcode", "10075")
                                                .append("building", "1480")
                                                .append("coord", asList(-73.9557413, 40.7720266)))
                                .append("name", "Vella 2"));
    }

    @Test
    @Ignore
    public void testRemove() {
        // Remove All Documents That Match a Condition
        db.getCollection("restaurants").deleteMany(new Document("borough", "Manhattan"));
        // Remove All Documents
        db.getCollection("restaurants").deleteMany(new Document());
        // Drop a Collection
        db.getCollection("restaurants").drop();
    }

    @Test
    @Ignore
    public void testDataAggregation() {
        // Group Documents by a Field and Calculate Count
        db.getCollection("restaurants")
                .aggregate(asList(new Document("$group",
                        new Document()
                                .append("_id", "$borough")
                                .append("count", new Document("$sum", 1)))));
        // Filter and Group Documents
        db.getCollection("restaurants")
                .aggregate(
                        asList(new Document("$match", new Document()
                                        .append("borough", "Queens")
                                        .append("cuisine", "Brazilian")),
                                new Document("$group", new Document()
                                        .append("_id", "$address.zipcode")
                                        .append("count", new Document("$sum", 1)))));
    }

    @Test
    @Ignore
    public void testCreateIndex() {
        // Create a Single-Field Index
        db.getCollection("restaurants").createIndex(new Document("cuisine", 1));
        // Create a compound index
        db.getCollection("restaurants")
                .createIndex(
                        new Document()
                                .append("cuisine", 1)
                                .append("address.zipcode", -1));
    }
}
