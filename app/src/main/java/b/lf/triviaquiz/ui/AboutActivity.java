package b.lf.triviaquiz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.Tracker;

import java.lang.ref.WeakReference;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.utils.GlobalQuestionCountAsyncTask;

public class AboutActivity extends AppCompatActivity {
    private AdView mAdView;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mAdView = findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);


        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType(getString(R.string.share_type));
            String shareBody = getString(R.string.share_body);
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_extra));
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
        });

        new GlobalQuestionCountAsyncTask(new WeakReference<>(this)).execute();

    }

    public void updateGlobalQuestionCount(String count) {
        if (count != null) {
            ((TextView) findViewById(R.id.total_num_of_verified_questions)).setText(getString(R.string.total_num_of_verified_questions).concat(" " + count));
        } else {
            findViewById(R.id.total_num_of_verified_questions).setVisibility(View.GONE);
        }
    }

}
