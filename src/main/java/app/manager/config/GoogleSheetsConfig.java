package app.manager.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
public class GoogleSheetsConfig {

    private final AppConfig appConfig;
    private final JsonFactory jsonFactory;

    public GoogleSheetsConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
        jsonFactory = JacksonFactory.getDefaultInstance();
    }


    @Bean
    public Sheets sheetsService() throws IOException, GeneralSecurityException {
        FileInputStream serviceAccountStream = new FileInputStream(appConfig.getCredentialsFilePath());
        GoogleCredential credential = GoogleCredential.fromStream(serviceAccountStream)
                .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));

        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
                .setApplicationName(appConfig.getApplicationName())
                .build();
    }
}