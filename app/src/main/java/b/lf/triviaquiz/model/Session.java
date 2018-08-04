package b.lf.triviaquiz.model;

import java.util.List;

public class Session {
    private User user;
    private List<QuestionCategory> questionsCategories;
    private int questionsQuantity;
    private String difficulty;

    public Session(User user, List<QuestionCategory> questionsCategories, int questionsQuantity, String difficulty) {
        this.user = user;
        this.questionsCategories = questionsCategories;
        this.questionsQuantity = questionsQuantity;
        this.difficulty = difficulty;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<QuestionCategory> getQuestionsCategories() {
        return questionsCategories;
    }

    public void setQuestionsCategories(List<QuestionCategory> questionsCategories) {
        this.questionsCategories = questionsCategories;
    }

    public int getQuestionsQuantity() {
        return questionsQuantity;
    }

    public void setQuestionsQuantity(int questionsQuantity) {
        this.questionsQuantity = questionsQuantity;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}