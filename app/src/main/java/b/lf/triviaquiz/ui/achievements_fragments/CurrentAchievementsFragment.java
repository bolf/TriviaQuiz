package b.lf.triviaquiz.ui.achievements_fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import b.lf.triviaquiz.R;
import b.lf.triviaquiz.model.AnsweredQuestion;
import b.lf.triviaquiz.model.ResultHolder;
import b.lf.triviaquiz.model.UserAchievements;
import b.lf.triviaquiz.utils.SharedPreferencesUtils;
import b.lf.triviaquiz.viewModels.TriviaQuizBaseViewModel;

public class CurrentAchievementsFragment extends Fragment {
    private TriviaQuizBaseViewModel mViewModel;

    public CurrentAchievementsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(TriviaQuizBaseViewModel.class);
        mViewModel.setUserLiveDataFilter(SharedPreferencesUtils.retrieveCurrentUserId(getActivity()));
        mViewModel.getUserWithAchievements().observe(this, this::processGettingCurrentUserFromDb);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_achievemts, container, false);
    }

    private void processGettingCurrentUserFromDb(UserAchievements userAchievements){
        if(getActivity().findViewById(R.id.achievements_current_result_tv).getVisibility() == View.VISIBLE){return;}

        Map<String, ResultHolder> categoryMap = new HashMap<>();

        for(AnsweredQuestion aQ : userAchievements.getAnsweredQuestions()){
            if(aQ.isCurrent()){
                ResultHolder currR_Holder = categoryMap.get(aQ.getCategory());
                if(currR_Holder == null) {
                    categoryMap.put(aQ.getCategory(), new ResultHolder(1,aQ.isCorrect()? 1:0));
                }else{
                    currR_Holder.totalQuestions++;
                    if(aQ.isCorrect()){currR_Holder.correctAnswers++;}
                }
            }
        }

        LinearLayout lL = getActivity().findViewById(R.id.achievements_current_category_ll);

        String[] categoriesResult = new String[categoryMap.size()];
        int ind = 0;
        int questionsNum = 0;
        int correctAnswers = 0;
        for(Map.Entry<String,ResultHolder> entry : categoryMap.entrySet()){
            categoriesResult[ind] = entry.getKey().concat(": ").concat(String.valueOf(entry.getValue().correctAnswers)).concat(" of ").concat(String.valueOf(entry.getValue().totalQuestions));


            TextView tV = new TextView(getActivity());
            tV.setId(ind);
            tV.setText(categoriesResult[ind]);
            tV.setTextColor(getResources().getColor(R.color.black));
            tV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            lL.addView(tV);

            ind++;
            questionsNum   = questionsNum + entry.getValue().totalQuestions;
            correctAnswers = correctAnswers + entry.getValue().correctAnswers;
        }

        getActivity().findViewById(R.id.achievements_current_result_tv).setVisibility(View.VISIBLE);
        ((TextView)getActivity().findViewById(R.id.achievements_current_result_tv)).setText(getString(R.string.user_results,correctAnswers,questionsNum));
    }
}
