package app.manager.chat;

import app.manager.client.ChatGPTService;
import app.manager.sheets.repository.FAQRepository;
import app.manager.sheets.repository.FurnitureRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class QuestionResponse {

    public String createCatalogueAnswer(
            String key,
            String model,
            String userQuestion,
            Map<String, String> response,
            ChatGPTService chatGPTService,
            FurnitureRepository furnitureRepository) {

        String catalogue = furnitureRepository.findByFilters(response).toString();
        String systemContent = String.format("Ты менеджер интернет магазина. Твоя задача ответить на вопрос клиента по этим данным о наличии товара в магазине. Отвечай кратко, ясно, без воды.\nСписок товара в наличии: %s", catalogue);

        try {
            String resp = chatGPTService.createChatRequest(
                    key,
                    model,
                    userQuestion,
                    systemContent);
            return extractContent(resp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String createFAQAnswer(String key,
                                  String model,
                                  String userQuestion,
                                  ChatGPTService chatGPTService,
                                  FAQRepository faqRepository) {

        String faq = faqRepository.findAll().toString();
        String systemContent = String.format("Ты менеджер интернет магазина. Твоя задача ответить на вопрос клиента по этим данным часто задаваемых вопросов" +
                "Отвечай кратко, ясно без воды.\nСписок релевантных ответов на часто задаваемые вопросы: %s", faq);

        try {
            String resp = chatGPTService.createChatRequest(
                    key,
                    model,
                    userQuestion,
                    systemContent);
            return extractContent(resp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String extractContent(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);

        JSONArray choices = jsonObject.getJSONArray("choices");
        JSONObject firstChoice = choices.getJSONObject(0);
        JSONObject message = firstChoice.getJSONObject("message");

        return message.getString("content");
    }
}
