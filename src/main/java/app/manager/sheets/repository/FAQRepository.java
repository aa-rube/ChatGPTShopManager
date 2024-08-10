package app.manager.sheets.repository;
import app.manager.sheets.model.FAQItem;

import java.util.List;

public interface FAQRepository {
    void save(FAQItem item);
    FAQItem findByQuestion(String question);
    List<FAQItem> findAll();
    void deleteByQuestion(String question);
}
