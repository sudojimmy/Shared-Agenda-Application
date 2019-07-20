package utils;

import com.mongodb.client.model.Filters;
import constant.ApiConstant;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import store.DataStore;
import types.*;

import java.util.ArrayList;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static controller.BaseController.dataStore;

public class MessageQueueUtils {
    public static MessageQueue createMessageQueueToDatabase() {
        String messageQueueId = new ObjectId().toString();
        MessageQueue messageQueue = new MessageQueue().withMessageQueueId(messageQueueId)
                .withMessageList(new ArrayList<>());
        dataStore.insertToCollection(messageQueue, DataStore.COLLECTION_MESSAGEQUEUES);
        return messageQueue;
    }

    public static MessageQueue getMessageQueue(final String messageQueueId) {
        Document document = new Document();
        document.put(ApiConstant.MESSAGEQUEUE_MESSAGEQUEUE_ID, messageQueueId);
        return dataStore.findOneInCollection(document, DataStore.COLLECTION_MESSAGEQUEUES);
    }

    public static ArrayList<Message> getMessageList(final String messageQueueId){
        MessageQueue messageQueue = getMessageQueue(messageQueueId);
        return new ArrayList<>(messageQueue.getMessageList());
    }

    public static void deleteMessageFromMessageQueue(final String messageId, final String messageQueueId) {
        MessageQueue messageQueue = MessageQueueUtils.getMessageQueue(messageQueueId);
        Message message = getMessageFromMessageQueue(messageId, messageQueue);
        // TODO NullPtr Check!
        messageQueue.getMessageList().remove(message);

        Bson filter = Filters.eq(ApiConstant.MESSAGEQUEUE_MESSAGEQUEUE_ID, messageQueueId);
        Bson query = combine(set(ApiConstant.MESSAGEQUEUE_MESSAGE_LIST, messageQueue.getMessageList()));

        dataStore.updateInCollection(filter, query, DataStore.COLLECTION_MESSAGEQUEUES);
    }

    private static Message getMessageFromMessageQueue(String messageId, MessageQueue messageQueue) {
        for (Message e : messageQueue.getMessageList()) {
            if (e.getMessageId().equals(messageId)) {
                return e;
            }
        }
        return null;
    }

    public static Message getMessageFromMessageQueueId(String messageId, String messageQueueId) {
        MessageQueue messageQueue = MessageQueueUtils.getMessageQueue(messageQueueId);
        final Message message = getMessageFromMessageQueue(messageId, messageQueue);
        if (message == null) {
            ExceptionUtils.invalidProperty(ApiConstant.MESSAGE_MESSAGE_ID);
        }
        return message;
    }

    public static boolean existFriendRequest(final String messageQueueId, final String senderId) {
        for (Message msg : getMessageList(messageQueueId)) {
            if (msg.getType().equals(MessageType.FRIEND) && msg.getSenderId().equals(senderId)) {
                return true;
            }
        }
        return false;
    }

    public static void notifyAccounts(final Account sender, final Account receiver, final MessageType type, final String messageId) {
        String replyId = ReplyMessageUtils.createReplyMessageToDatabase(
                type,
                receiver.getAccountId(),    // The sender/receiver in reply msg is reversed comparing to original msg
                sender.getAccountId(),
                ReplyStatus.valueOf("PENDING"),
                "");            // currently not support to add description

        Message message = new Message()
                .withMessageId(messageId)
                .withType(type)
                .withSenderId(sender.getAccountId())
                .withReplyId(replyId); // only receiver's msg contains replyId

        Message reply = new Message()
                .withMessageId(replyId)
                .withType(MessageType.RESPONSE)
                .withSenderId(receiver.getAccountId());

        String receiverMessageQueueId = receiver.getMessageQueueId();
        String senderMessageQueueId = sender.getMessageQueueId();

        // add the Message(eventMessage) to messageQueue
        MessageUtils.addMessageIdToMessageQueue(message, receiverMessageQueueId);

        // add the Message(replyMessage) to messageQueue
        MessageUtils.addMessageIdToMessageQueue(reply, senderMessageQueueId);

        PushNotificationUtils.getInstance()
                .pushInvitationNotification(sender.getAccountId(), receiver.getAccountId(),type);
    }
}
