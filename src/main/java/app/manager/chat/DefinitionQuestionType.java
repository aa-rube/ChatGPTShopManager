package app.manager.chat;

import app.manager.client.ChatGPTService;
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

        String systemContent = createSystemContent();

        String resp = "";
        try {
            resp = chatGPTService.createChatRequest(key, model, userContent, systemContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (resp.contains("Вопрос на другие темы")) {
            return Map.of("questionType", "fq");
        } else {
            try {
                Map<String, String> map = convertJsonToMap(extractContent(resp));
                map.put("questionType", "catalogue");
                return map;
            } catch (IOException e) {
                return Map.of("questionType", "service exception", "service exception", e.toString());
            }
        }
    }

    private static String createSystemContent() {
        return "Ты менеджер интернет магазина. Нужно определить к какой категории относится вопрос." +
                "1. если вопрос по наличию товара, то нужно составить его в виде json строки:\n" +
                "{\n" +
                "\"Тип товара\": \"тип (сущ., в единственном числе)\",\n" +
                "\"Цвет\": \"если не указан то -\",\n" +
                "\"Цена\": \"если нет, то -, если есть то в виде цифры например 1000, если диапазон цен то 1000-5000\"\n" +
                "}" +

                "\n\n2. Если вопрос не про наличие товара, то ответить:\n" +
                "Вопрос на другие темы";
    }

    private static Map<String, String> convertJsonToMap(String jsonString) throws IOException {
        jsonString = prepareStringToConvert(jsonString);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, Map.class);
    }

    public static String extractContent(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);

        JSONArray choices = jsonObject.getJSONArray("choices");
        JSONObject firstChoice = choices.getJSONObject(0);
        JSONObject message = firstChoice.getJSONObject("message");

        return message.getString("content");
    }

    private static String prepareStringToConvert(String jsonString) {
        return "{" + jsonString.split("\\{")[1].split("}")[0] + "}";
    }
}
