package b.lf.triviaquiz.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import b.lf.triviaquiz.ui.AchievementsActivity;

public class AfterSavingQuizResultsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentOpenAchievements = new Intent(context, AchievementsActivity.class);
        intentOpenAchievements.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentOpenAchievements);
    }
}
