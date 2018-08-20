package b.lf.triviaquiz.utils;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import b.lf.triviaquiz.R;

public class TriviaQuizWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        RemoteViews rvs = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        rvs.setTextViewText(R.id.widget_user_name, "Mr.wtf");
        rvs.setTextViewText(R.id.widget_user_accuracy, "accuracy: 97%");
        appWidgetManager.updateAppWidget(appWidgetIds[0],rvs);
    }
}
