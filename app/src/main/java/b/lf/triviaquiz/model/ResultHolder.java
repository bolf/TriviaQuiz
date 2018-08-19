package b.lf.triviaquiz.model;

public class ResultHolder{
    public int totalQuestions;
    public int correctAnswers;

    public ResultHolder(int totalQuestions, int correctAnswers) {
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
    }
}