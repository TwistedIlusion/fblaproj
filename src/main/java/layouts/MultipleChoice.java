package layouts;

import javafx.scene.control.Button;
import questions.Question;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import quiz.QuizController;

import java.util.Arrays;

class MultipleChoice{
    public static VBox getNode(Question question) {
        VBox vbox = new VBox();

        RadioButton radio1 = new RadioButton(question.getOptions().get(0));
        RadioButton radio2 = new RadioButton(question.getOptions().get(1));
        RadioButton radio3 = new RadioButton(question.getOptions().get(2));
        RadioButton radio4 = new RadioButton(question.getOptions().get(3));


        ToggleGroup mChoice = new ToggleGroup(); //Make buttons only have 1 toggle at a time.
        for (RadioButton radioButton : Arrays.asList(radio1, radio2, radio3, radio4)) {
            radioButton.setToggleGroup(mChoice);

            radioButton.setOnMouseClicked(e -> {
                if (QuizController.responses.containsKey(question.getID())) {
                    QuizController.responses.remove(question.getID());
                }
                QuizController.addResponse(question.getID(), radioButton.getText());
            });
        }

        vbox.getChildren().addAll(radio1, radio2, radio3, radio4);




//            RadioButton selectedRadioButton = (RadioButton) mChoice.getSelectedToggle();
//            QuizController.addResponse(question.getID(), selectedRadioButton.getText());
//        }
//        else



        return vbox;
    }
}
