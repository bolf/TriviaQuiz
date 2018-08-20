package b.lf.triviaquiz.utils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.ui.StarterActivity;

public class TriviaQuizWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            RemoteViews rvs = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            rvs.setTextViewText(R.id.widget_user_name, SharedPreferencesUtils.retrieveCurrentUserNick(context));
            rvs.setTextViewText(R.id.widget_user_accuracy, "accuracy: 97%");

            int appWidgetId = appWidgetIds[i];
            Intent intent = new Intent(context, StarterActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            rvs.setOnClickPendingIntent(R.id.imageButton, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, rvs);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

    }
}
