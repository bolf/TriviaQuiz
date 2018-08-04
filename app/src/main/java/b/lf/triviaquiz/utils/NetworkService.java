package b.lf.triviaquiz.utils;

import b.lf.triviaquiz.model.QuestionCategoryWrapper;
import retrofit2.Call;
import retrofit2.http.GET;

public interface NetworkService {
    @GET("api_category.php")
    Call<QuestionCategoryWrapper> getCategories();
}
