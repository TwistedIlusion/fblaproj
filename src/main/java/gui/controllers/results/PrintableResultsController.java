package gui.controllers.results;

import gui.StageHelper;
import gui.popups.ErrorBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import quiz.QuizManager;
import quiz.questions.nodes.QuizNode;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Base64;

/**
 * Provides methods for ActionEvents on Printable Results Page.
 */

public class PrintableResultsController implements Initializable {

    @FXML
    Label testName;

    @FXML
    Label resultsArea;

    @FXML
    Button seeQuestionsButton;

    private long bMap;

    /**
     * Initial startup method.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        seeQuestionsButton.setDisable(!Boolean.parseBoolean(QuizManager.getPreferences().get("Show Correct Answers")));

        QuizManager.checkAnswers();

        testName.setText(testName.getText() + QuizManager.getPreferences().get("Quiz Name"));

        float correctAnswers = 0;
        ArrayList<Integer> ids = new ArrayList<>();
        for (QuizNode quizNode : QuizManager.getQuizNodes()) {

            ids.add(quizNode.getQuestion().getID());

            if (quizNode.isCorrect()) {
                correctAnswers++;
            }
        }

        //Create a bitmap data structure from ids
        bMap = 0;
        for (Integer id : ids) {
            bMap |= (1L << (id - 1));
        }

        //add how many correct out of possible, percentage, put bitmap to Base64
        System.out.println(Base64.getEncoder().withoutPadding().encodeToString(String.valueOf(bMap).getBytes()));
        resultsArea.setText(
                (int) correctAnswers + " out of " + QuizManager.getQuizNodes().size() + "\n"
                        + (correctAnswers / QuizManager.getQuizNodes().size()) * 100 + "%" + "\n"
        );

    }


    public void onPrintButton() {
    }

    public void onRetakeCodeButton() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(Base64.getEncoder().withoutPadding().encodeToString(String.valueOf(bMap).getBytes()));
        clipboard.setContent(content);
    }

    public void onSeeQuestions() {

        //Try not to reload page if already created
        if (StageHelper.getScenes().containsKey("questionresults")) {

            StageHelper.getStages().get("Quiz").setScene(StageHelper.getScenes().get("questionresults"));

        } else {
            try {

                Parent results = FXMLLoader.load(getClass().getResource("/questionresults.fxml"));
                Scene scene = new Scene(results);
                StageHelper.addScene("questionresults", scene);
                StageHelper.getStages().get("Quiz").setScene(scene);

            } catch (IOException | NullPointerException e) {
                ErrorBox.display("A page failed to load.", false);
                e.printStackTrace();
            }
        }
    }
}