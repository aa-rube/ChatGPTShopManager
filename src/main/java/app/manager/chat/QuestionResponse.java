package app.manager.chat;

import app.manager.client.ChatGPTService;
import app.manager.sheets.repository.FAQRepository;
import app.manager.sheets.repository.FurnitureRepository;
import app.manager.util.ChatGptExtractContent;
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
        String systemContent = String.format("%s%s", SystemRoleData.CATALOGUE.getValue(), catalogue);

        try {
            String resp = chatGPTService.createChatRequest(
                    key,
                    model,
                    userQuestion,
                    systemContent);
            return ChatGptExtractContent.extractContent(resp);
        } catch (IOException e) {
            return e.toString();
        }
    }

    public String createFAQAnswer(String key,
                                  String model,
                                  String userQuestion,
                                  ChatGPTService chatGPTService,
                                  FAQRepository faqRepository) {

        String faq = faqRepository.findAll().toString();
        String systemContent = String.format("%s%s", SystemRoleData.FAQ.getValue(), faq);

        try {
            String resp = chatGPTService.createChatRequest(
                    key,
                    model,
                    userQuestion,
                    systemContent);
            return ChatGptExtractContent.extractContent(resp);
        } catch (IOException e) {
            return e.toString();
        }
    }

}
