package store;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import types.Account;

import java.util.HashMap;
import java.util.Map;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;



public class DataStore {
    public static final String AGENDA_APP_DATABASE = "AGENDA_APP_DATABASE";
    public static final String COLLECTION_ACCOUNTS = "ACCOUNTS";

    private MongoDatabase database;
    private Map<String, MongoCollection<?>> map = new HashMap<>();

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

        map.put(COLLECTION_ACCOUNTS, database.getCollection(COLLECTION_ACCOUNTS, Account.class));
    }

    public <T>void insertToCollection(T document, String collectionName) {
        MongoCollection<T> collection = (MongoCollection<T>) map.get(collectionName);

        collection.insertOne(document);
    }

    public <T>boolean existInCollection(Document document, String collectionName) {
        MongoCollection<T> collection = (MongoCollection<T>) map.get(collectionName);

        return collection.countDocuments(document) != 0;
    }

/*

    void delete() {
        MongoCollection<Document> collection = database.getCollection("account");

        Document document = new Document();
        document.put("key", "val");
        collection.deleteOne(document);
        System.out.println(collection.countDocuments());
    }
*/

}
