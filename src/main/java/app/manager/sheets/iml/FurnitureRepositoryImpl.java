package app.manager.sheets.iml;

import app.manager.sheets.model.FurnitureItem;
import app.manager.sheets.repository.FurnitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class FurnitureRepositoryImpl implements FurnitureRepository {

    private static final String PREFIX = "furniture:item:";

    @Autowired
    private JedisPool jedisPool;

    @Override
    public void save(FurnitureItem item) {
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> map = new HashMap<>();
            map.put("color", item.getColor());
            map.put("price", String.valueOf(item.getPrice()));
            map.put("material", item.getMaterial());
            map.put("collection", item.getCollection());
            map.put("interiorType", item.getInteriorType());
            map.put("type", item.getType());

            jedis.hmset(PREFIX + item.getName(), map);
        }
    }

    @Override
    public FurnitureItem findByName(String name) {
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> map = jedis.hgetAll(PREFIX + name);

            if (map.isEmpty()) {
                return null;
            }

            return new FurnitureItem(
                    name,
                    map.get("color"),
                    map.get("price"),
                    map.get("material"),
                    map.get("collection"),
                    map.get("interiorType"),
                    map.get("type")
            );
        }
    }

    @Override
    public List<FurnitureItem> findAll() {
        try (Jedis jedis = jedisPool.getResource()) {
            List<FurnitureItem> items = new ArrayList<>();
            for (String key : jedis.keys(PREFIX + "*")) {
                Map<String, String> map = jedis.hgetAll(key);
                String name = key.substring(PREFIX.length());
                FurnitureItem item = new FurnitureItem(
                        name,
                        map.get("color"),
                        map.get("price"),
                        map.get("material"),
                        map.get("collection"),
                        map.get("interiorType"),
                        map.get("type")
                );
                items.add(item);
            }
            return items;
        }
    }

    @Override
    public List<FurnitureItem> findByFilters(Map<String, String> filters) {
        List<FurnitureItem> allItems = findAll();

        return allItems.stream()
                .filter(item -> matchesFilters(item, filters))
                .collect(Collectors.toList());
    }

    private boolean matchesFilters(FurnitureItem item, Map<String, String> filters) {
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            String key = entry.getKey().toLowerCase();
            String value = entry.getValue();

            if ("-".equals(value)) {
                continue;
            }

            switch (key) {
                case "тип товара":
                    if (!value.equalsIgnoreCase(item.getType())) {
                        return false;
                    }
                    break;
                case "цвет":
                    if (!value.equalsIgnoreCase(item.getColor())) {
                        return false;
                    }
                    break;
                case "цена":
                    if (!matchesPrice(item.getPrice(), value)) {
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    private boolean matchesPrice(String itemPrice, String filterPrice) {
        if (filterPrice.contains("-")) {
            String[] range = filterPrice.split("-");
            int min = Integer.parseInt(range[0]);
            int max = Integer.parseInt(range[1]);
            int price = Integer.parseInt(itemPrice);
            return price >= min && price <= max;
        } else {
            return itemPrice.equals(filterPrice);
        }
    }

    @Override
    public void deleteByName(String name) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(PREFIX + name);
        }
    }
}
