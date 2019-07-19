package utils;

import com.pusher.pushnotifications.PushNotifications;
import controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import types.MessageType;
import types.ReplyMessage;
import types.ReplyStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PushNotificationUtils {
    private static final String INSTANCE_ID = "3dda1e61-a4af-4f29-bca8-6ad42366e5f9";
    Logger logger = LoggerFactory.getLogger(BaseController.class);
    private String secretKey;
    private static PushNotificationUtils instance = new PushNotificationUtils();

    PushNotificationUtils() {
        secretKey = System.getenv("PUSHER_SECRET_KEY");
    }

    public static PushNotificationUtils getInstance() {
        return instance;
    }

    public void pushInvitationNotification(final String senderId, final String receiverId, MessageType type) {
        String title;
        String body;
        switch (type) {
            case FRIEND:
                title = "New Friend Request";
                body = senderId + " want to add you as a friend!";
                break;
            case EVENT:
                title = "New Event Invitation";
                body = senderId + " invite you to an event!";
                break;
            default:
                return; // NOT SUPPORTED TYPE
        }

        push(title, body, receiverId);
    }

    public void pushFriendNotification(final ReplyMessage replyMessage) {
        String title = "You Got a Response";
        String body = String.format("%s %s your friend request!", replyMessage.getSenderId(),
                replyMessage.getStatus().equals(ReplyStatus.ACCEPT) ? "accepted" : "declined");
        push(title, body, replyMessage.getReceiverId());
    }

    public void pushEventNotification(final ReplyMessage replyMessage, final String eventname) {
        String title = "Event Invitation Response";
        String body = String.format("%s %s your invitation to %s!", replyMessage.getSenderId(),
                replyMessage.getStatus().equals(ReplyStatus.ACCEPT) ? "accepted" : "declined",
                eventname);
        push(title, body, replyMessage.getReceiverId());
    }

    private void push(final String title, final String body, final String receiverId) {
        if (secretKey == null) {
            logger.info("Secret Key for Push Notification NOT FOUND. Skip push notification!");
            return;
        }

        PushNotifications beamsClient = new PushNotifications(INSTANCE_ID, secretKey);

        List<String> interests = Collections.singletonList(receiverId); // TODO remove debug

        Map<String, Map> publishRequest = new HashMap();
        Map<String, String> fcmNotification = new HashMap();
        fcmNotification.put("title", title);
        fcmNotification.put("body", body);
        Map<String, Map> fcm = new HashMap();
        fcm.put("notification", fcmNotification);
        publishRequest.put("fcm", fcm);

        try {
            beamsClient.publishToInterests(interests, publishRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
