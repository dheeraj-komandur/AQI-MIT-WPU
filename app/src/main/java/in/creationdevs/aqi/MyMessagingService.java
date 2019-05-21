package in.creationdevs.aqi;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyMessagingService extends FirebaseMessagingService {
    public static final String PREFERENCES = "VALUES";
    String aqi;
    String aqisen;
    String suggestionHA;
    String suggestionHR;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }

    public void showNotification(String Title,String message)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES,MODE_PRIVATE);
        if(sharedPreferences.getString("AQI",null)!=null)
        {
            aqi = sharedPreferences.getString("AQI",null);
            aqisen = "AQI : " + aqi;
            suggestionHA = getSuggestionsHA(aqi);
            suggestionHR = getSuggestionsHR(aqi);
        }
        Intent intent = new Intent(getApplicationContext(),TodayActivity.class);
        PendingIntent pi = PendingIntent.getActivities(this,0, new Intent[]{intent},0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"MyNotification")
                .setContentTitle("Air Quality Index")
                .setContentText(aqi)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(aqisen+"\n\n"+suggestionHA+"\n"+suggestionHR));



                /*.setStyle(new NotificationCompat.InboxStyle()
                        .addLine(aqisen)
                        .addLine(suggestionHA)
                        .addLine(suggestionHR));
*/


        NotificationManagerCompat notificationManager= NotificationManagerCompat.from(this);
        notificationManager.notify(999,builder.build());

    }

    public String getSuggestionsHA(String Value)
    {
        if(Value.equals("NA")) //NA is Hardcoded check database for nil,null etc
        {
            Value="-1";
        }
        int value = Integer.valueOf(Value);

        if(value >= 0 && value <= 50)
        {
            return "Health Advisory: Enjoy the day.";
        }
        else if(value >=51  && value <= 100)
        {
            return "Health Advisory: Enjoy the day.";
        }
        else if(value >=101  && value <= 200)
        {
            return "Health Advisory: Very sensitive people should consider reducing longer/heavy exertion and heavy outdoor work.";
        }
        else if(value >=201  && value <= 300)
        {
            return "Health Advisory: People with heart or lung disease, older adults and children should reduce longer or heavy exertion and outdoor activity";
        }
        else if(value >=301  && value <= 400)
        {
            return "Health Advisory: Everyone should reduce heavy exertion.";
        }
        else if(value >=401  && value <= 500)
        {
            return "Health Advisory: Everyone should avoid any outdoor physical activity";
        }

        return "No Data";
    }

    public String getSuggestionsHR(String Value)
    {
        if(Value.equals("NA")) //NA is Hardcoded check database for nil,null etc
        {
            Value="-1";
        }
        int value = Integer.valueOf(Value);

        if(value >= 0 && value <= 50)
        {
            return "Health Risk: No risk to all";
        }
        else if(value >=51  && value <= 100)
        {
            return "Health Risk: No risk";
        }
        else if(value >=101  && value <= 200)
        {
            return "Health Risk: Minor Health issues for sensitive people, no problem to general public.";
        }
        else if(value >=201  && value <= 300)
        {
            return "Health Risk: Elderly at risk. Health may start to experience slight discomfort.";
        }
        else if(value >=301  && value <= 400)
        {
            return "Health Risk: Triggers health alert, Everyone may experience health effects. Significant increase in respiratory problems.";
        }
        else if(value >=401  && value <= 500)
        {
            return "Health Risk: Serious risk of respiratory problems.";
        }

        return "No Data";
    }
}
