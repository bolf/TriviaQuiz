package b.lf.triviaquiz.model;

import java.util.List;

public class QuestionCategoryWrapper {
    private List<QuestionCategory> trivia_categories;

    public QuestionCategoryWrapper(List<QuestionCategory> trivia_categories) {
        this.trivia_categories = trivia_categories;
    }

    public List<QuestionCategory> getTrivia_categories() {
        return trivia_categories;
    }

    public void setTrivia_categories(List<QuestionCategory> trivia_categories) {
        this.trivia_categories = trivia_categories;
    }
}
