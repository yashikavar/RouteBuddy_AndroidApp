package routebuddy.administrator.example.com.minor_project;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

public class ReceiveNotification extends FirebaseMessagingService {
    public String TAG = "ReceiveNotification";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) try {
            // Log.d(TAG, remoteMessage.getData().toString());
            JSONObject jsonObject = new JSONObject(remoteMessage.getData());

            NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.logo)
                    .setColor(Color.rgb(9, 25, 42))
                    .setContentTitle("HOSTELLER!")
                    .setContentText(jsonObject.getString("message"))
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(jsonObject.getString("message")));
            //Intent intent = new Intent(this, Dashboard.class);
            // PendingIntent pi = PendingIntent.getActivity(this, 0, intent, Intent.FILL_IN_ACTION);
            // mBuilder.setContentIntent(pi);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {

        }


    }

}

