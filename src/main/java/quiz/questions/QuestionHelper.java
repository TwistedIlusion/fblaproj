package quiz.questions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class QuestionHelper {

    public static Question questionFromJSON(JSONObject json) throws JSONException {

        int id;

        String type = json.get("type_id").toString();

        String subject = json.get("subjects").toString();

        String question = json.get("question").toString();

        String directions;

        ArrayList<String> options;

        id = Integer.parseInt(

                json.get("question_num").toString()

        );

        if (json.has("options")) {

            options = new ArrayList<>(Arrays.asList(json.get("options").toString()
                    .split(", ")));

        } else {

            options = null;

        }


        if (!json.has("directions")) { //poggers

            directions = switch (type) {

                case "1" -> "Select the correct answer.";

                case "2" -> "Determine if the problem is true or false.";

                case "3" -> "Check all the boxes that apply.";

                default -> "Correctly type the solution.";

            };
        } else {

            directions = json.get("directions").toString();

        }

        return new Question(type, subject, options, question, directions, id);

    }

    public static List<String> answerFromJSON(JSONObject json) throws JSONException {

        return new ArrayList<>(
                Arrays.asList(json.get("answer").toString()
                        .split(", ")));

    }
}
