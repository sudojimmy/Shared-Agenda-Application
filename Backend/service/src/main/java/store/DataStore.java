package store;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import types.Account;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;



public class DataStore {
    public static final String AGENDA_APP_DATABASE = "AGENDA_APP_DATABASE";
    private MongoDatabase database;

    public DataStore() {
        init();
    }

    void init() {
        System.out.println("Connecting ... "); // TODO replace all system.xxx with log
        try {
            MongoClient client = new MongoClient(); // TODO change default ip:port
            System.out.println("Connected");

            CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
            database = client.getDatabase(AGENDA_APP_DATABASE).withCodecRegistry(pojoCodecRegistry);
        } catch (Exception e) {
            System.out.println("Failed to connect to MongoDB");
            System.exit(1);
        }
    }

    void insert() {
        MongoCollection<Account> collection = database.getCollection("addr", Account.class);

        Account p = new Account().withName("Jim").withAccountId(255);
        collection.insertOne(p);
        System.out.println(collection.countDocuments());
    }

    void delete() {
        MongoCollection<Document> collection = database.getCollection("account");

        Document document = new Document();
        document.put("key", "val");
        collection.deleteMany(document);
        System.out.println(collection.countDocuments());
    }

    void find() {
        MongoCollection<Account> collection = database.getCollection("addr", Account.class);

        Document document = new Document();
        document.put("accountId", 255);
        System.out.println(collection.find(document).first());
        System.out.println(collection.countDocuments());
    }
}
