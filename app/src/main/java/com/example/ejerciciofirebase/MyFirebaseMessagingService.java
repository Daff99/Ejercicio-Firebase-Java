package com.example.ejerciciofirebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage mensaje) {
        super.onMessageReceived(mensaje);
        String title = mensaje.getNotification().getTitle();
        String body = mensaje.getNotification().getBody();
        final String CHANNEL_ID = "HEADS_UP_NOTIFICATION";
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID, "Heads Up Notification", NotificationManager.IMPORTANCE_DEFAULT
        );
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notificacion = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(title).setContentText(body).setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true);
        NotificationManagerCompat.from(this).notify(1, notificacion.build());
    }
}
