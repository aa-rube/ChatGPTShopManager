import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Map;

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
        jsonBody.put("key", "sk-fjk6DBxrN2WMTWM4JPKhBEsztwCr2-WlJW2-84RSi5T3BlbkFJzyjX9bBSb7cXeFL3kHzOVvARdXf3DHLIifuI0xtWIA");
        jsonBody.put("model", "gpt-4o");
        jsonBody.put("userRole", "Какие стулья у вас есть?");

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonBody.toString()
        );

        Request request = new Request.Builder()
                .url("http://191.84.65.59:65001/api/chat/request")
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