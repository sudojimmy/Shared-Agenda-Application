package utils;

import com.mongodb.client.model.Filters;
import constant.ApiConstant;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import store.DataStore;
import types.MessageType;
import types.ReplyMessage;
import types.ReplyStatus;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static controller.BaseController.dataStore;

public class ReplyMessageUtils {
    public static String createReplyMessageToDatabase(final MessageType type,
                                                      final String senderId,
                                                      final String receiverId,
                                                      final ReplyStatus status,
                                                      final String description) {
        ReplyMessage reply = new ReplyMessage()
                .withReplyId(new ObjectId().toString())
                .withSenderId(senderId)
                .withReceiverId(receiverId)
                .withStatus(status)
                .withType(type)
                .withDescription(description);

        dataStore.insertToCollection(reply, DataStore.COLLECTION_REPLYS);
        return reply.getReplyId();
    }

    public static ReplyMessage getReplyMessage(final String replyId) {
        Document document = new Document();
        document.put(ApiConstant.REPLY_REPLY_ID, replyId);
        return dataStore.findOneInCollection(document, DataStore.COLLECTION_REPLYS);
    }

    public static boolean updateReplyMessageInDatabase(final String replyId,
                                                       final String senderId,
                                                       final String receiverId,
                                                       final ReplyStatus status,
                                                       final String description) {

        Bson query = combine(
                set(ApiConstant.REPLY_REPLY_ID, replyId),
                set(ApiConstant.REPLY_SENDER_ID, senderId),
                set(ApiConstant.REPLY_RECEIVER_ID, receiverId),
                set(ApiConstant.REPLY_STATUS, status),
                set(ApiConstant.REPLY_DESCRIPTION, description));


        Bson filter = Filters.eq(ApiConstant.REPLY_REPLY_ID, replyId);
        return dataStore.updateInCollection(filter, query, DataStore.COLLECTION_REPLYS);
    }

}
