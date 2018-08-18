package b.lf.triviaquiz.utils;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import b.lf.triviaquiz.database.TQ_DataBase;
import b.lf.triviaquiz.model.AnsweredQuestion;

public class CurrentQuizPersistService extends IntentService {

    public CurrentQuizPersistService() {
        super("CurrentQuizPersistService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        ArrayList<AnsweredQuestion> currentQuizQuestions = intent.getParcelableArrayListExtra("currentQuizQuestions");

        TQ_DataBase db = TQ_DataBase.getInstance(getApplicationContext());

        //get previously answered questions
        List<AnsweredQuestion> answeredQuestions = db.answeredQuestionDao().getAnsweredQuestionsCurrent(currentQuizQuestions.get(0).getUserId());
        //mark them all not current
        if(answeredQuestions != null && answeredQuestions.size() > 0) {
            AnsweredQuestion[] tmpArr = new AnsweredQuestion[answeredQuestions.size()];
            for (int ind = 0; ind < answeredQuestions.size(); ind++) {
                tmpArr[ind] = answeredQuestions.get(ind);
                tmpArr[ind].setCurrent(false);
            }
            //save marked
            db.answeredQuestionDao().bulkInsert(tmpArr);
        }
        //insert current questions
        AnsweredQuestion[] tmpArr = new AnsweredQuestion[currentQuizQuestions.size()];
        for (int ind = 0; ind < currentQuizQuestions.size(); ind++) {
            tmpArr[ind] = currentQuizQuestions.get(ind);
        }
        db.answeredQuestionDao().bulkInsert(tmpArr);

        //inform that saving curr quiz is done and it's time to start achievements activity
        Intent intentAfterSavingQuiz = new Intent();
        intentAfterSavingQuiz.setAction("AfterSavingQuizResultsReceiver");
        sendBroadcast(intentAfterSavingQuiz);
    }
}
