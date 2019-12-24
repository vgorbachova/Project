package com.example.project1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    Timer mTimer;
    MyTimerTask mMyTimerTask;

    DBBlockHelper dbBlockHelper;
    SQLiteDatabase database;

    ArrayList<String> learnIt;

    int step = 1;

    ArrayList<Integer> pims;
    ArrayList<Integer> ebi;


    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();
        mTimer.schedule(mMyTimerTask, 0, 15000);

        learnIt = new ArrayList<>();
        pims = new ArrayList<>();
        ebi = new ArrayList<>();

        //Промежутки времени, через которые придет уведомление в зависимости от выбранной методики изучения материала
        pims.add(120000);
        pims.add(10 * 60000);
        pims.add(60 * 60000);
        pims.add(300 * 60000);
        pims.add(1440 * 60000);
        pims.add(300 * 60000);

        ebi.add(180000);
        ebi.add(60 * 60000);
        ebi.add(1440 * 60000);
        ebi.add(10080 * 60000);

    }

    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class MyTimerTask extends TimerTask {
        private int timer = 0;

        @Override
        public void run() {
            Context context = getApplicationContext();
            int indexlesson=0;
            String namelesson="";
            boolean flag=false;
            dbBlockHelper = new DBBlockHelper(context);
            database = dbBlockHelper.getWritableDatabase();
            Cursor cursor = database.query(DBBlockHelper.TABLE_BLOCKS, null, "learning==" + 1, null, null, null, null);
            int nameIndex = cursor.getColumnIndex(DBBlockHelper.KEY_NAME);
            if (cursor.moveToFirst()) {
                do {
                    int keyid = cursor.getColumnIndex(DBBlockHelper.KEY_ID);
                    int nameid = cursor.getColumnIndex(DBBlockHelper.KEY_NAME);
                    int previous = cursor.getColumnIndex(DBBlockHelper.KEY_PREVIOUS_LESSON);
                    int next = cursor.getColumnIndex(DBBlockHelper.KEY_NEXT_LESSON);
                    int learnStep = cursor.getColumnIndex(DBBlockHelper.KEY_LEARNING);
                    int method = cursor.getColumnIndex(DBBlockHelper.KEY_METHOD);
                    indexlesson = cursor.getInt(keyid);
                    namelesson =  cursor.getString(nameid);
                    long pre = cursor.getLong(previous);
                    int ne = cursor.getInt(next);
                    int lStep = cursor.getInt(learnStep);
                    String met = cursor.getString(method);
                    if (met.equals("PIMS")) {
                        long x=pre + pims.get(ne-1);
                        long xx=System.currentTimeMillis();
                        if (pre + pims.get(ne-1) < System.currentTimeMillis()) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(DBBlockHelper.KEY_PREVIOUS_LESSON, System.currentTimeMillis());
                            contentValues.put(DBBlockHelper.KEY_NEXT_LESSON, ne +1);
                            int updCount = database.update(DBBlockHelper.TABLE_BLOCKS, contentValues, "_id==" + indexlesson, null);
                            flag=true;
                        }
                    }
                    else if (met.equals("EBBI")) {
                        if (pre + ebi.get(ne-1) < System.currentTimeMillis()) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(DBBlockHelper.KEY_PREVIOUS_LESSON, System.currentTimeMillis());
                            contentValues.put(DBBlockHelper.KEY_NEXT_LESSON, ne + 1);
                            int updCount = database.update(DBBlockHelper.TABLE_BLOCKS, contentValues, "_id==" + indexlesson, null);
                            flag = true;
                        }
                    }
                }
                while ( (cursor.moveToNext()) && (!flag));
            }
            if (flag) {
                Intent resultIntent = new Intent(getApplicationContext(), CardActivity_Recycler.class);
                resultIntent.putExtra("block_id", String.valueOf(indexlesson));
                PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                                .setContentTitle("Lessons")
                                .setContentText(namelesson + "#" + String.valueOf(indexlesson))
                                .setContentIntent(resultPendingIntent)
                                .setAutoCancel(true);

                Notification notification = builder.build();

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                assert notificationManager != null;
                notificationManager.notify(1, notification);
            }
        }
    }
}


