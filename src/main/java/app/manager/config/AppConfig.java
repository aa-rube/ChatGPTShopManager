package app.manager.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppConfig {

    // Общие настройки
    @Value("${google.sheets.application.name}")
    private String applicationName;

    @Value("${google.sheets.credentials.file.path}")
    private String credentialsFilePath;

    @Value("${chat.gpt.api.url}")
    private String chatGptApiUrl;

    // Настройки для каталога
    @Value("${google.sheets.spreadsheet.id.catalogue}")
    private String spreadsheetIdCatalogue;

    @Value("${google.sheets.spreadsheet.catalogue.list.name}")
    private String catalogueListName;

    @Value("${google.sheets.spreadsheet.catalogue.range}")
    private String catalogueRange;

    // Настройки для часто задаваемых вопросов (FAQ)
    @Value("${google.sheets.spreadsheet.id.fq}")
    private String spreadsheetIdFq;

    @Value("${google.sheets.spreadsheet.fq.list.name}")
    private String fqListName;

    @Value("${google.sheets.spreadsheet.fq.range}")
    private String fqRange;
}
