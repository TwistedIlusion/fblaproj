package gui.quiz;

import etc.Constants;
import gui.PrimaryStageHolder;
import gui.etc.FXHelper;
import gui.popup.error.ErrorNotifier;
import gui.startmenu.PremadeQuizzes;
import javafx.stage.StageStyle;
import questions.QuizQuestions;
import requests.QuestionJSONRequest;

import java.io.IOException;
import java.util.HashMap;

/**
 * Starts and ends quizzes
 */
public class QuizHelper {

    private QuizHelper() {
    }

    //private static String quizAuthToken;

    /**
     * @param loadDefault if the test should be default questions. False if otherwise.
     */
    public static void startQuiz(boolean loadDefault) {
        try {

            PrimaryStageHolder.getPrimaryStage().close();

            if (PremadeQuizzes.stage != null) {
                PremadeQuizzes.stage.close();
            }

            if (loadDefault) {
                QuizQuestions.initializeQuestions(Constants.DEFAULT_QUESTION_AMOUNT, new QuestionJSONRequest());
            }

            //Make a PopupStage because the test window shares their qualities.
            PrimaryStageHolder.setPrimaryStage(FXHelper.getPopupStage(FXHelper.Window.QUIZ, false));

            PrimaryStageHolder.getPrimaryStage().initStyle(StageStyle.UNDECORATED);
            PrimaryStageHolder.getPrimaryStage().show();

        } catch (Exception e) {
            new ErrorNotifier("A page failed to load", true).display();
            e.printStackTrace();
        }

    }


    //Ends the entire test and begins the results page
    static void endQuiz() {
        try {
            PrimaryStageHolder.getPrimaryStage().setScene(FXHelper.getScene(FXHelper.Window.PRINTRESULTS));
        } catch (IOException e) {
            new ErrorNotifier("Results could not display.", true).display();
        }

    }

    /**
     * Preferences for the quiz, determines what tools are displayed
     */
    public enum Preference {
        NOTEPAD,
        CALCULATOR,
        DRAWINGPAD,
        QUIZNAME,
        SHOWANSWERS,
        TIME;

        //Hashmap of user preferences, initialize with default preferences
        public static HashMap<Preference, String> preferences = new HashMap<>();

        static {
            preferences.put(Preference.NOTEPAD, "true");
            preferences.put(Preference.CALCULATOR, "true");
            preferences.put(Preference.DRAWINGPAD, "true");
            preferences.put(Preference.QUIZNAME, "FBLA QUIZ - 5 Questions, Random");
            preferences.put(Preference.SHOWANSWERS, "true");
            preferences.put(Preference.TIME, "1800");
        }
    }
}