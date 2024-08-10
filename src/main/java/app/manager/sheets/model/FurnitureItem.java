package app.manager.sheets.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FurnitureItem {
    private String name;
    private String color;
    private String price;
    private String material;
    private String collection;
    private String interiorType;
    private String type;
}
