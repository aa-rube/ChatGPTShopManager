package app.manager.sheets.repository;

import app.manager.sheets.model.FurnitureItem;

import java.util.List;
import java.util.Map;

public interface FurnitureRepository {
    void save(FurnitureItem item);
    FurnitureItem findByName(String name);
    List<FurnitureItem> findAll();
    List<FurnitureItem> findByFilters(Map<String, String> filters);
    void deleteByName(String name);
}