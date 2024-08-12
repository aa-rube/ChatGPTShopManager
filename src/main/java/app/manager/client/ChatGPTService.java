package app.manager.client;

import okhttp3.*;
import app.manager.config.AppConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ChatGPTService {
    private final String API_URL;

    @Autowired
    public ChatGPTService(AppConfig appConfig) {
        API_URL = appConfig.getChatGptApiUrl();
    }

    public String createChatRequest(String apiKey, String model, String userContent, String systemContent) throws IOException {
        OkHttpClient client = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("model", model);
        jsonObject.put("messages", new JSONArray()
                .put(new JSONObject().put("role", "system").put("content", systemContent))
                .put(new JSONObject().put("role", "user").put("content", userContent))
        );

        RequestBody body = RequestBody.create(
                jsonObject.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "Unexpected code " + response;
            }
            return response.body().string();
        }
    }
}
