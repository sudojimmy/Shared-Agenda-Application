package store;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import types.*;
import utils.VoteQueueUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.bson.codecs.configuration.CodecRegistries.*;


public class DataStore {
    public static final String AGENDA_APP_DATABASE = "AGENDA_APP_DATABASE";
    public static final String COLLECTION_ACCOUNTS = "ACCOUNTS";
    public static final String COLLECTION_GROUPS = "GROUPS";
    public static final String COLLECTION_EVENTS = "EVENTS";
    public static final String COLLECTION_CALENDARS = "CALENDARS";
    public static final String COLLECTION_MESSAGEQUEUES = "MESSAGE_QUEUES";
    public static final String COLLECTION_MESSAGES = "MESSAGES";
    public static final String COLLECTION_EVENTMESSAGES = "EVENT_MESSAGES";
    public static final String COLLECTION_REPLYS = "REPLYS";
    public static final String COLLECTION_FRIENDQUEUES = "FRIEND_QUEUES";
    public static final String COLLECTION_GROUPQUEUES = "GROUP_QUEUES";
    public static final String COLLECTION_VOTES = "VOTES";
    public static final String COLLECTION_VOTEQUEUES = "VOTE_QUEUES";
    public static final String DEFAULT_MONGODB_CONNECTION_STRING = "mongodb://localhost:27017";
    public static final String MONGODB_CONNECTION_STRING_SYS_ENV = "AGENDA_APP_DATABASE_CONN_STR";

    private MongoDatabase database;
    private Map<String, MongoCollection<?>> map = new HashMap<>();

    public DataStore() {
        init();
    }

    void init() {
        System.out.println("Connecting ... "); // TODO replace all system.xxx with log
        try {
            String sys_env_conn_str = System.getenv(MONGODB_CONNECTION_STRING_SYS_ENV); // for Production Environment
            MongoClientURI uri = new MongoClientURI(sys_env_conn_str != null ?
                sys_env_conn_str : DEFAULT_MONGODB_CONNECTION_STRING);

            MongoClient client = new MongoClient(uri); // TODO change default ip:port
            System.out.println("Connected");

            CodecRegistry pojoCodecRegistry = fromRegistries(
                    MongoClient.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build()),
                    fromCodecs(new EventRepeatTypeCodec()),
                    fromCodecs(new ReplyStatusTypeCodec()),
                    fromCodecs(new MessageTypeTypeCodec()),
                    fromCodecs(new PermissionTypeTypeCodec()),
                    fromCodecs(new EventStateTypeCodec()),
                    fromCodecs(new EventTypeTypeCodec()));

            database = client.getDatabase(AGENDA_APP_DATABASE).withCodecRegistry(pojoCodecRegistry);
        } catch (Exception e) {
            System.out.println("Failed to connect to MongoDB");
            System.exit(1);
        }

        map.put(COLLECTION_ACCOUNTS, database.getCollection(COLLECTION_ACCOUNTS, Account.class));
        map.put(COLLECTION_GROUPS, database.getCollection(COLLECTION_GROUPS, Group.class));
        map.put(COLLECTION_EVENTS, database.getCollection(COLLECTION_EVENTS, Event.class));
        map.put(COLLECTION_CALENDARS, database.getCollection(COLLECTION_CALENDARS, Calendar.class));
        map.put(COLLECTION_EVENTMESSAGES, database.getCollection(COLLECTION_EVENTMESSAGES, EventMessage.class));
        map.put(COLLECTION_REPLYS, database.getCollection(COLLECTION_REPLYS, ReplyMessage.class));
        map.put(COLLECTION_MESSAGES, database.getCollection(COLLECTION_MESSAGES, Message.class));
        map.put(COLLECTION_MESSAGEQUEUES, database.getCollection(COLLECTION_MESSAGEQUEUES, MessageQueue.class));
        map.put(COLLECTION_FRIENDQUEUES, database.getCollection(COLLECTION_FRIENDQUEUES, FriendQueue.class));
        map.put(COLLECTION_GROUPQUEUES, database.getCollection(COLLECTION_GROUPQUEUES, GroupQueue.class));
        map.put(COLLECTION_VOTES, database.getCollection(COLLECTION_VOTES, Vote.class));
        map.put(COLLECTION_VOTEQUEUES, database.getCollection(COLLECTION_VOTEQUEUES, VoteQueue.class));
    }

    public <T>void insertToCollection(T document, String collectionName) {
        MongoCollection<T> collection = (MongoCollection<T>) map.get(collectionName);

        collection.insertOne(document);
    }

    public <T>boolean existInCollection(Document document, String collectionName) {
        MongoCollection<T> collection = (MongoCollection<T>) map.get(collectionName);

        return collection.count(document) != 0;
    }

    public <T> T findOneInCollection(Document document, String collectionName) {
        MongoCollection<T> collection = (MongoCollection<T>) map.get(collectionName);

        return collection.find(document).limit(1).first();
    }

    public <T> Collection<T> findManyInCollection(Document document, String collectionName) {
        MongoCollection<T> collection = (MongoCollection<T>) map.get(collectionName);

        Collection<T> listT = new ArrayList<T>();

        Iterable <T>iterableListT = collection.find(document);
        iterableListT.forEach(listT::add);

        return listT;
    }

    public <T> Collection<T> findAllCollection(String collectionName) {
        MongoCollection<T> collection = (MongoCollection<T>) map.get(collectionName);

        Collection<T> listT = new ArrayList<T>();

        Iterable <T>iterableListT = collection.find();
        iterableListT.forEach(listT::add);

        return listT;
    }


    public <T> Collection<T> findManyInCollection(Bson filter, String collectionName) {
        MongoCollection<T> collection = (MongoCollection<T>) map.get(collectionName);

        Collection<T> listT = new ArrayList<T>();

        Iterable <T>iterableListT = collection.find(filter);
        iterableListT.forEach(listT::add);

        return listT;
    }

    public <T>boolean updateInCollection(Bson filter, Bson query, String collectionName) {
        MongoCollection<T> collection = (MongoCollection<T>) map.get(collectionName);

        return collection.updateOne(filter, query).getMatchedCount() == 1;
    }

    public boolean delete(Bson filter, String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);

        DeleteResult dr = collection.deleteOne(filter);
        return dr.getDeletedCount() == 1;
    }

    public boolean deleteAll(Bson filter, String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);

        DeleteResult dr = collection.deleteMany(filter);
        return dr.getDeletedCount() > 0;
    }
}
