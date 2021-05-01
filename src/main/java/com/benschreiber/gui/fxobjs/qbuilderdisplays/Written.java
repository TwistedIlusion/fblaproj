package com.benschreiber.gui.fxobjs.qbuilderdisplays;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.Collections;
import java.util.List;

class Written extends OptionNode {

    //TextField for user to write answer to classes.question in
    private final TextField answer = new TextField();

    /**
     * Constructor
     */
    Written() {

        answer.setPrefWidth(150);

        //Place text field in hbox with label
        HBox hBox = new HBox();
        hBox.getChildren().addAll(new Label("Answer: "), answer);

        this.node = hBox;

    }


    @Override
    public List<String> getOptions() {
        return null;
    }

    @Override
    public List<String> getAnswer() {
        return Collections.singletonList(answer.getText());
    }

    @Override
    public boolean isAnswered() {
        return !answer.getText().isEmpty();
    }

}