package app.manager;

import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        try {
            System.out.println(createChatRequest());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String createChatRequest() throws IOException {
        OkHttpClient client = new OkHttpClient();

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("key", "");
        jsonBody.put("model", "gpt-4o");
        jsonBody.put("userRole", "Какие стулья у вас есть?");

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonBody.toString()
        );

        Request request = new Request.Builder()
                .url("http://191.84.68.41:65001/api/chat/request")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }
}