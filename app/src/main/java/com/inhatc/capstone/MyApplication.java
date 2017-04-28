package com.inhatc.capstone;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

public class MyApplication extends Application {

    private BeaconManager beaconManager;
    private TextView tvId;


    private UUID uuid;
    public UUID getUUIDD()
    {
        return uuid;
    }
    public void setUUID(UUID uuid)
    {
        this.uuid = uuid;
    }

    private int major;
    public int getMajor()
    {
        return major;
    }
    public void setMajor(int major)
    {
        this.major = major;
    }

    private int minor;
    public int getMinor()
    {
        return minor;
    }
    public void setMinor(int minor)
    {
        this.minor = minor;
    }

    private boolean state;
    public boolean getState()
    {
        return state;
    }
    public void setState(boolean state)
    {
        this.state = state;
    }

    private int premajor;
    public int getPremajor()
    {
        return premajor;
    }
    public void setPremajor(int premajor)
    {
        this.premajor = premajor;
    }

    private boolean state2;
    public boolean getState2()
    {
        return state2;
    }
    public void setState2(boolean state2)
    {
        this.state2 = state2;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                Beacon nearestBeacon = list.get(0);
                showNotification(
                        "Your gate closes in 47 minutes.",
                        "Current security wait time is 15 minutes, "
                                + "and it's a 5 minute walk from security to the gate. "
                                + "Looks like you've got plenty of time!");
                //tvId.append(nearestBeacon.getMajor()+"\n");
                //tvId.append(nearestBeacon.getMajor()+"\n");
                //tvId.append(nearestBeacon.getProximityUUID()+"\n");
                //tvId.append(nearestBeacon.getMacAddress()+"\n");
                //tvId.setText("");
            }
            @Override
            public void onExitedRegion(Region region) {
                // could add an "exit" notification too if you want (-:
            }
        });
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region("monitored region",
                        UUID.fromString("e2c56db5-dffb-48d2-b060-d0f5a71096e0"), null, null));
            }
        });
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, Intro.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
