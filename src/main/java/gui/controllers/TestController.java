package gui.controllers;

import gui.GuiHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import quiz.questions.nodes.ConfirmBox;
import quiz.QuizController;
import quiz.questions.NodeHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TestController implements Initializable, Exitable {

    @FXML
    Button backButton, nextButton, notePadButton, calculatorButton, drawingPadButton;

    @FXML
    Label questionPrompt, questionDirections;

    @FXML
    VBox questionArea;

    @FXML
    AnchorPane questionPane;

    @FXML
    Canvas paintCanvas;

    private static GraphicsContext gc;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (!QuizController.preferences.isEmpty()) {//Get preferences and apply them, if any.

            notePadButton.setVisible(QuizController.preferences.get("Notepad"));

            calculatorButton.setVisible(QuizController.preferences.get("Calculator"));

            drawingPadButton.setVisible(QuizController.preferences.get("Drawing Pad"));

        }

        displayNewQuestion(); //Display the new question.


        backButton.setDisable(true); //Disable back button by default.

        paintCanvas.setDisable(true); //Disable canvas addon

        gc = paintCanvas.getGraphicsContext2D(); //Establish canvas properties

    }

    //When next button is clicked
    public void onNextButton() {

        if (QuizController.getCurrNum() < QuizController.getQuestionAmount() - 1) {

            QuizController.nextQuestion(); //Change to the next question.

            displayNewQuestion(); //Display the new question.
        }

        if (QuizController.getCurrNum() != 0) {

            backButton.setDisable(false); //Enable the back button if not on first question.

        }

        if (QuizController.getCurrNum() == QuizController.getQuestionAmount() - 1) {

            nextButton.setDisable(true); //Disable next button if on last

        }

    }

    //When back button is clicked
    public void onBackButton() {

        if (QuizController.getCurrNum() > 0) {

            QuizController.prevQuestion(); //Goto previous question.

            displayNewQuestion(); //Load previous question.

        }

        if (QuizController.getCurrNum() == 0) {

            backButton.setDisable(true); //Disable back button if on last question.

        }

        if (QuizController.getCurrNum() != QuizController.getQuestionAmount() - 1 && nextButton.isDisable()) {

            nextButton.setDisable(false); //Enable next button if not on last question.

        }
    }

    //When submit button is clicked
    public void onSubmitButton(MouseEvent mouseEvent) {
        try {

            if (QuizController.responses.size() == QuizController.getQuestionAmount()) { //If all questions are answered.

                if (ConfirmBox.display("Are you sure you want to submit?")) {

                    exit(mouseEvent); //Exit this page.

                    displayResults(); //Create results page.

                }

            } else if (ConfirmBox.display("Some answers are unfinished. Are sure you want to submit?")) {//If all questions aren't answered

                exit(mouseEvent); //Exit this page.

                displayResults(); //Create results page.

            }
        } catch (IOException ioException) {

            ioException.printStackTrace();

        }

    }

    //When pressing mouse
    public void canvasOnPressed(MouseEvent e) {
        //begin drawing
        gc.beginPath();

        gc.lineTo(e.getX(), e.getY());

        gc.stroke();
    }

    //When dragging mouse
    public void canvasOnDragged(MouseEvent event) {

        gc.lineTo(event.getX(), event.getY());

        gc.stroke();
    }

    //When the calculator button is clicked
    public void onCalculatorButton() throws IOException {

        if (!GuiHelper.getOpenedWindows().containsKey("calculator")) { //Make sure one calculator only is open.

            Parent root = FXMLLoader.load(getClass().getResource("/calculator.fxml")); //Grab calculator

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.setAlwaysOnTop(true); //Keep notepad on test.
            stage.initStyle(StageStyle.UTILITY);//Get rid of minimize
            stage.resizableProperty().setValue(false);

            GuiHelper.addWindow("calculator", stage); //Add this to current stages

            stage.setOnCloseRequest(e -> { //Make sure if X is pressed it removes from stages
                GuiHelper.closeWindow("calculator");
            });

            stage.show();

        } else {

            //If already open, close all
            GuiHelper.closeWindow("calculator");

        }
    }

    //When the notepad button is clicked
    public void onNotepadButton() throws IOException {

        if (!GuiHelper.getOpenedWindows().containsKey("notepad")) {//Make sure only one notepad is open.

            Parent root = FXMLLoader.load(getClass().getResource("/notepad.fxml")); //Grab calculator

            //Establish scene and stage
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.setAlwaysOnTop(true); //Keep notepad on test.
            stage.initStyle(StageStyle.UTILITY);//Get rid of minimize
            stage.resizableProperty().setValue(false); //Make non resizeable

            GuiHelper.addWindow("notepad", stage); //Add this to current stages

            stage.setOnCloseRequest(e -> { //Make sure if X is pressed it removes from stages
                GuiHelper.closeWindow("notepad");
            });

            stage.show();

        } else {

            //If already open, close all.
            GuiHelper.closeWindow("notepad");

        }
    }

    //On drawing button clicked
    public void onDrawingPadButton() throws IOException {

        if (!GuiHelper.getOpenedWindows().containsKey("drawingpad")) { //Make sure only one drawingpad is open

            Parent root = FXMLLoader.load(getClass().getResource("/drawingpad.fxml")); //Grab calculator

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.setAlwaysOnTop(true); //Keep pad on test.
            stage.initStyle(StageStyle.UTILITY);
            stage.resizableProperty().setValue(false);

            GuiHelper.addWindow("drawingpad", stage); //Add this to current stages

            stage.setOnCloseRequest(e -> { //Make sure if X is pressed it removes from stages, clears drawings

                paintCanvas.setDisable(true); //Disable canvas so other parts can be used

                gc.clearRect(0, 0, 1920, 1080); //Clear canvas

                GuiHelper.closeWindow("drawingpad");

            });

            paintCanvas.setDisable(false); //Enable the canvas

            stage.show();

        } else {

            //If already open, close all.
            paintCanvas.setDisable(true);

            gc.clearRect(0, 0, 1920, 1080);

            GuiHelper.closeWindow("drawingpad");

        }

    }


    private void displayNewQuestion() {

        questionArea.getChildren().clear(); //Clear previous question from questionArea

        questionArea.getChildren().add(NodeHelper.getNodeFromQuestion(QuizController.getCurrQuestion())); //Add new question

        questionPrompt.setText(QuizController.getCurrQuestion().getPrompt()); // Set prompt

        questionDirections.setText(QuizController.getCurrQuestion().getDirections()); //Set directions

    }

    private void displayResults() throws IOException {

        Parent results = FXMLLoader.load(getClass().getResource("/results.fxml")); //Grab results fxml

        Scene scene = new Scene(results);

        Stage stage = new Stage();

        stage.setScene(scene);

        stage.show();

    }

    public static void changeColor(Color color) {
        gc.setStroke(color);
    }

    public static void changeWidth(Double width) {
        gc.setLineWidth(width);
    }

}
