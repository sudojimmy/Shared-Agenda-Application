package store;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import types.Account;
import types.Event;
import types.Group;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;



public class DataStore {
    public static final String AGENDA_APP_DATABASE = "AGENDA_APP_DATABASE";
    public static final String COLLECTION_ACCOUNTS = "ACCOUNTS";
    public static final String COLLECTION_GROUPS = "GROUPS";
    public static final String COLLECTION_EVENTS = "EVENTS";

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
        map.put(COLLECTION_GROUPS, database.getCollection(COLLECTION_GROUPS, Group.class));
        map.put(COLLECTION_EVENTS, database.getCollection(COLLECTION_EVENTS, Event.class));

    }

    public <T>void insertToCollection(T document, String collectionName) {
        MongoCollection<T> collection = (MongoCollection<T>) map.get(collectionName);

        collection.insertOne(document);
    }

    public <T>boolean existInCollection(Document document, String collectionName) {
        MongoCollection<T> collection = (MongoCollection<T>) map.get(collectionName);

        return collection.countDocuments(document) != 0;
    }

    public <T> T findOneInCollection(Document document, String collectionName) {
        MongoCollection<T> collection = (MongoCollection<T>) map.get(collectionName);

        return collection.find(document).limit(1).first();
    }

    public <T> Collection<T> findMoreInCollection(Document document, String collectionName) {
        MongoCollection<T> collection = (MongoCollection<T>) map.get(collectionName);

        Collection<T> listT = new ArrayList<T>();

        Iterable<T> iterableListT = collection.find(document);

        for (T t : iterableListT)
            listT.add(t);

        return listT;
    }

    public <T>boolean updateInCollection(Bson filter, Bson query, String collectionName) {
        MongoCollection<T> collection = (MongoCollection<T>) map.get(collectionName);


        return collection.updateOne(filter, query).getMatchedCount() == 1;
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
