package b.lf.triviaquiz.utils;

import java.util.Map;

import b.lf.triviaquiz.model.QuestionCategoryWrapper;
import b.lf.triviaquiz.model.QuestionWrapper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface NetworkService {
    @GET("api_category.php")
    Call<QuestionCategoryWrapper> getCategories();

    @GET("api.php")
    Call<QuestionWrapper> getQuestions(@QueryMap Map<String, String> params);
}
