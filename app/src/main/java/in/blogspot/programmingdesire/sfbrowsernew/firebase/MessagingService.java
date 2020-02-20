package in.blogspot.programmingdesire.sfbrowsernew.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import in.blogspot.programmingdesire.sfbrowsernew.MainActivity;
import in.blogspot.programmingdesire.sfbrowsernew.R;

/**
 * Created by Shashi kumar on 26-Apr-18.
 */

public class MessagingService extends FirebaseMessagingService {
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//        showNotification(remoteMessage.getNotification().getBody());
//        Log.d(TAG, "onMessageReceived: " + remoteMessage.getData().get("message"));
//    }
//
//    private void showNotification(String message) {
//        PendingIntent pi=PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class),0);
//        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
//        builder.setSmallIcon(R.drawable.ic_stat_name);
//        builder.setContentTitle("SF Browser");;
//        builder.setContentText(message);
//        builder.setContentIntent(pi);
//        builder.setAutoCancel(true);
//
//        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(0,builder.build());
//
//
//    }
private static final String TAG = "Android News App";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        //It is optional
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        //Calling method to generate notification
        sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }

    //This method is only generating push notification
    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

       // Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
              //  .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

}
