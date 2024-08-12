package app.manager.chat;

import app.manager.client.ChatGPTService;
import app.manager.util.ChatGptExtractContent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class DefinitionQuestionType {

    public static Map<String, String> startConversation(
            ChatGPTService chatGPTService,
            String userContent,
            String key,
            String model) {

        String resp = "";
        try {
            resp = chatGPTService.createChatRequest(key, model, userContent, SystemRoleData.DEFINE_THE_TYPE_OF_QUESTION.getValue());
        } catch (Exception e) {
            return Map.of("questionType", "service exception", "service exception", e.toString());
        }

        if (resp.contains("Вопрос на другие темы")) {
            return Map.of("questionType", "fq");
        } else {
            try {
                String clearResponse = ChatGptExtractContent.extractContent(resp);
                Map<String, String> map = convertJsonToMap(clearResponse);
                map.put("questionType", "catalogue");
                return map;
            } catch (Exception e) {
                return Map.of("questionType", "service exception", "service exception", e + "\n"+ resp);
            }
        }
    }

    private static Map<String, String> convertJsonToMap(String jsonString) throws IOException {
        jsonString = prepareStringToConvert(jsonString);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, Map.class);
    }

    private static String prepareStringToConvert(String jsonString) {
        return "{" + jsonString.split("\\{")[1].split("}")[0] + "}";
    }


}
