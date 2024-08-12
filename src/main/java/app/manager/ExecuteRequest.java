package app.manager;

import app.manager.chat.DefinitionQuestionType;
import app.manager.chat.QuestionResponse;
import app.manager.client.ChatGPTService;
import app.manager.sheets.repository.FAQRepository;
import app.manager.sheets.repository.FurnitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ExecuteRequest {

    private final FAQRepository faqRepository;
    private final FurnitureRepository furnitureRepository;
    private final ChatGPTService chatGPTService;
    private final QuestionResponse questionResponse;

    @Autowired
    public ExecuteRequest(FAQRepository faqRepository,
                          FurnitureRepository furnitureRepository,
                          ChatGPTService chatGPTService,
                          QuestionResponse questionResponse) {

        this.faqRepository = faqRepository;
        this.furnitureRepository = furnitureRepository;
        this.chatGPTService = chatGPTService;
        this.questionResponse = questionResponse;
    }


    public Map<String, String> execute(Map<String, String> params) {
        String key = params.getOrDefault("key", "");
        String userQuestion = params.getOrDefault("userRole", "");
        String model = params.getOrDefault("model", "gpt-4o");

        if (checkParams(key, userQuestion) != null) return checkParams(key, userQuestion);

        Map<String, String> map = DefinitionQuestionType.startConversation(chatGPTService, userQuestion, key, model);

        String answer = "";
        if (map.get("questionType").equals("service exception")) {
            return Map.of("answer", map.getOrDefault("service exception", "service exception"));
        } else if (map.get("questionType").equals("fq")) {
            answer = questionResponse.createFAQAnswer(key, model, userQuestion, chatGPTService, faqRepository);
        } else {
            answer = questionResponse.createCatalogueAnswer(key, model, userQuestion, map, chatGPTService, furnitureRepository);
        }

        System.out.println(answer);

        return Map.of("answer", answer);
    }

    private Map<String, String> checkParams(String key, String userQuestion) {
        Map<String, String> result = new HashMap<>();

        if (key.isBlank() || key.isEmpty()) result.put("401, message error=", "Chat GPT API key is empty");
        if (userQuestion.isEmpty() || userQuestion.isBlank()) result.put("404, message error=", "User role is empty");

        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }
}