package app.manager.util;

import org.json.JSONArray;
import org.json.JSONObject;

public class ChatGptExtractContent {

    public static String extractContent(String jsonString) {

        System.out.println("Ответ из чата:\n" + jsonString);

        JSONObject jsonObject = new JSONObject(jsonString);

        JSONArray choices = jsonObject.getJSONArray("choices");
        JSONObject firstChoice = choices.getJSONObject(0);
        JSONObject message = firstChoice.getJSONObject("message");

        return message.getString("content");
    }

}
