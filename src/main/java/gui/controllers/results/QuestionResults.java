package gui.controllers.results;

import etc.Constants;
import gui.StageHelper;
import gui.popups.ErrorBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import quiz.QuizManager;
import quiz.questions.nodes.QuizNode;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Provides methods for ActionEvents on Results page.
 * Shows the missed questions, if enabled.
 */

public class QuestionResults implements Initializable {

    @FXML
    VBox correctAnswersVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Create question answers
        int quizNumber = 1;
        for (QuizNode quizNode : QuizManager.getQuizNodes()) {

            //Make a container for the answered question, add question to it
            VBox answeredQuestion = new VBox(15);

            //Display question number before question, make bold
            Label label = new Label("Question " + quizNumber + ":");
            label.setStyle("-fx-font-weight: bold;");
            answeredQuestion.getChildren().add(label);

            answeredQuestion.getChildren().add(new Label(quizNode.getQuestion().getPrompt()));
            answeredQuestion.getChildren().add(quizNode.getNode());

            //Make un-intractable.
            quizNode.getNode().setFocusTraversable(false);
            quizNode.getNode().setMouseTransparent(true);

            if (quizNode.isCorrect()) {

                //Set background to green with low opacity
                answeredQuestion.setStyle("-fx-background-color: rgba(86, 234, 99, .5);");

            } else {

                //Set background to red with low opacity
                answeredQuestion.setStyle("-fx-background-color: rgba(255, 158, 173, .7);");

                //Show correct answer if desired in preferences
                if (Boolean.parseBoolean(QuizManager.getPreferences().get("Show Correct Answers"))) {

                    answeredQuestion.getChildren().add(new Label("Correct answer: "
                            + quizNode.getQuestion().getAnswer()
                            .toString()
                            .replace("[", "")
                            .replace("]", "")
                    ));
                }

            }
            //Add container to correct answers container.
            correctAnswersVBox.getChildren().add(answeredQuestion);
            quizNumber++;
        }
    }

    public void onBackButton() {

        if (StageHelper.getScenes().containsKey(Constants.Window.PRINTRESULTS)) {

            StageHelper.getStages().get(Constants.Window.QUIZ).setScene(StageHelper.getScenes().get(Constants.Window.PRINTRESULTS));

        } else {

            try {

                Parent results = FXMLLoader.load(getClass().getResource("/printableresults.fxml"));
                Scene scene = new Scene(results);
                StageHelper.addScene(Constants.Window.PRINTRESULTS, scene);
                StageHelper.getStages().get(Constants.Window.QUIZ).setScene(scene);

            } catch (IOException | NullPointerException e) {
                ErrorBox.display("A page failed to load.", false);
                e.printStackTrace();
            }

        }
    }
}

