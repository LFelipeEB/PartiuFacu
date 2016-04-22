package com.partiufacu.kronaproject.partiufacu.GoogleServices;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.partiufacu.kronaproject.partiufacu.R;
import com.partiufacu.kronaproject.partiufacu.activity.DetalhesCaronaActivity;
import com.partiufacu.kronaproject.partiufacu.activity.MainActivity;

/**
 * Classe responsavel por receber e tratos as mensagens recebidas pelo Serviço GCM;
 *
 */
public class PfGCMListenerService extends GcmListenerService{

    private String id;

    public PfGCMListenerService() {
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
//        super.onMessageReceived(from, data);
        Log.i("GCM PartiuFacu", " "+data.toString());

        if((id = data.getString("carona") )!= null) {
            Bundle b = new Bundle();
            b.putString("id", id);
            b.putString("nome_dono", data.getString("nome_dono"));
            Intent it = new Intent(this, DetalhesCaronaActivity.class);
            it.putExtras(b);
            buildNotification(data, it, 1);
        }else{
            Bundle b = new Bundle();
            b.putString("id", id);
            b.putString("nome_dono", data.getString("nome_dono"));
            Intent it = new Intent(this, MainActivity.class);
            buildNotification(data, it, 1);
        }
    }

    /**
     * Metodo tem a feito para criar notificações simples no aparelho.
     * @param data - Bundle recebido do Servidor GCM
     * @param intent - Intent a ser chamada quando clicado na notificação
     * @param id - id da notificação.
     */
    private void buildNotification(Bundle data, Intent intent, int id){
        NotificationCompat.Builder ncBuilder= new NotificationCompat.Builder(getApplicationContext());
        ncBuilder.setTicker("PartiuFacu")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(data.getString("title"))
                .setContentText( data.getString("message"))
                .setAutoCancel(true);

        PendingIntent pi = PendingIntent.getActivities(this, 0,new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT);
        ncBuilder.setContentIntent(pi);

        ncBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        ncBuilder.setVibrate(new long[]{500, 500, 500, 500, 500, 500});
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        ncBuilder.setSound(sound);
        ncBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(id, ncBuilder.build());
    }
}
