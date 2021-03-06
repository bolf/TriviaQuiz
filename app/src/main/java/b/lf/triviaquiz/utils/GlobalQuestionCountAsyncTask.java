package b.lf.triviaquiz.utils;

import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.ui.AboutActivity;

public class GlobalQuestionCountAsyncTask extends AsyncTask<Void,Void,String> {
    WeakReference<Activity> weakActivity;

    public GlobalQuestionCountAsyncTask(WeakReference<Activity> weakActivity) {
        this.weakActivity = weakActivity;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String num = null;
        try {
            URL url = new URL(weakActivity.get().getResources().getString(R.string.count_global_url));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();

            String inputString;
            while ((inputString = bufferedReader.readLine()) != null) {
                builder.append(inputString);
            }

            JSONObject topLevel = new JSONObject(builder.toString());
            JSONObject main = topLevel.getJSONObject(weakActivity.get().getResources().getString(R.string.overall));
            num = String.valueOf(main.getInt(weakActivity.get().getResources().getString(R.string.total_num_of_verified_questions_)));

            urlConnection.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return num;
    }

    @Override
    protected void onPostExecute(String count) {
        super.onPostExecute(count);
        ((AboutActivity)weakActivity.get()).updateGlobalQuestionCount(count);
    }
}
