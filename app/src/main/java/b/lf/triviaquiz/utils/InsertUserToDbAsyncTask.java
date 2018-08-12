package b.lf.triviaquiz.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import b.lf.triviaquiz.database.TQ_DataBase;
import b.lf.triviaquiz.model.User;
import b.lf.triviaquiz.ui.ChoosingQuestionsCategoriesActivity;

public class InsertUserToDbAsyncTask extends AsyncTask<User,Void,Void>{
    private final WeakReference<Activity> weakContext;

    public InsertUserToDbAsyncTask(Activity context){
        this.weakContext = new WeakReference<>(context);
    }

    @Override
    protected Void doInBackground(User... users) {
        TQ_DataBase.getInstance(weakContext.get()).userDao().insertUser(users[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        Activity weakContext = this.weakContext.get();
        if (weakContext == null || weakContext.isFinishing() || weakContext.isDestroyed()) {
            return;
        }

        Intent intent = new Intent(weakContext, ChoosingQuestionsCategoriesActivity.class);
        weakContext.startActivity(intent);
        weakContext.finish();
    }
}

