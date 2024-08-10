package app.manager.sheets.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import app.manager.config.AppConfig;
import app.manager.sheets.model.FAQItem;
import app.manager.sheets.model.FurnitureItem;
import app.manager.sheets.repository.FAQRepository;
import app.manager.sheets.repository.FurnitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class GoogleSheetService {
    private final AppConfig appConfig;
    private final Sheets sheetsService;
    private final FurnitureRepository furnitureRepository;
    private final FAQRepository faqRepository;

    @Autowired
    public GoogleSheetService(AppConfig appConfig, Sheets sheetsService, FurnitureRepository furnitureRepository,
                              FAQRepository faqRepository) {
        this.appConfig = appConfig;
        this.sheetsService = sheetsService;
        this.furnitureRepository = furnitureRepository;
        this.faqRepository = faqRepository;
    }

    @Scheduled(fixedRate = 60000 * 10)
    public void getDataFromSheetCatalogue() throws IOException, GeneralSecurityException {
        String range = String.format("%s%s%s", appConfig.getCatalogueListName(), "!", appConfig.getCatalogueRange());
        ValueRange response = sheetsService.spreadsheets().values()
                .get(appConfig.getSpreadsheetIdCatalogue(), range)
                .execute();

        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            return;
        }

        for (int i = 0; i < values.size(); i++) {
            if (i == 0) continue;
            try {
                List<Object> row = values.get(i);

                String name = row.get(0).toString();
                String color = row.get(1).toString();
                String price = row.get(2).toString();
                String material = row.get(3).toString();
                String collection = row.get(4).toString();
                String interiorType = row.get(5).toString();
                String type = row.get(6).toString();

                FurnitureItem item = new FurnitureItem(name, color, price, material, collection, interiorType, type);
                furnitureRepository.save(item);
            } catch (Exception e) {
                continue;
            }
        }

        range = String.format("%s%s%s", appConfig.getCatalogueListName(), "!", appConfig.getCatalogueRange());
        response = sheetsService.spreadsheets().values()
                .get(appConfig.getSpreadsheetIdFq(), range)
                .execute();

        values = response.getValues();
        if (values == null || values.isEmpty()) {
            return;
        }

        for (int i = 0; i < values.size(); i++) {
            if (i == 0) continue;
            try {
                List<Object> row = values.get(i);

                String name = row.get(0).toString();
                String color = row.get(1).toString();
                FAQItem item = new FAQItem(name, color);
                faqRepository.save(item);
            } catch (Exception e) {
                continue;
            }
        }
    }
}