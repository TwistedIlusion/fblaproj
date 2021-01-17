package requests;

import etc.Constants;
import gui.etc.Account;
import org.json.JSONException;
import org.json.JSONObject;
import questions.question.QuestionNode;

import java.io.IOException;

/**
 * Makes a request to the database that assigns the correct answers to each question of the quiz node array.
 */
public class AnswerRequest {

    private JSONObject json;
    private final QuestionNode[] questionNodes;

    public AnswerRequest(QuestionNode[] questionNode) {
        this.questionNodes = questionNode;
    }

    public AnswerRequest makeRequest() throws InterruptedException, JSONException, IOException {

        StringBuilder stringBuilder = new StringBuilder().append(Constants.DEFAULT_PATH).append("answer/");

        for (QuestionNode questionNode : this.questionNodes) {
            stringBuilder.append(questionNode.getID()).append(",");
        }

        //get rid of the last "," because it isn't needed
        this.json = Request.getJSONFromURL(stringBuilder.substring(0, stringBuilder.toString().length() - 1), Account.AUTH_TOKEN());

        return this;
    }

    public JSONObject getJson() {
        return json;
    }
}
