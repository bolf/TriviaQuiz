package b.lf.triviaquiz.model;

import java.util.List;

public class QuestionWrapper {
    private List<Question> results;

    public QuestionWrapper(List<Question> results) {
        this.results = results;
    }

    public List<Question> getResults() {
        return results;
    }

    public void setResults(List<Question> results) {
        this.results = results;
    }
}
