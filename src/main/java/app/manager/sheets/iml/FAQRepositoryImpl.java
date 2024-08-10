package app.manager.sheets.iml;

import app.manager.sheets.model.FAQItem;
import app.manager.sheets.repository.FAQRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FAQRepositoryImpl implements FAQRepository {

    private static final String PREFIX = "faq:item:";

    @Autowired
    private JedisPool jedisPool;

    @Override
    public void save(FAQItem item) {
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> map = new HashMap<>();
            map.put("answer", item.getAnswer());

            jedis.hmset(PREFIX + item.getQuestion(), map);
        }
    }

    @Override
    public FAQItem findByQuestion(String question) {
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> map = jedis.hgetAll(PREFIX + question);

            if (map.isEmpty()) {
                return null;
            }

            return new FAQItem(
                    question,
                    map.get("answer")
            );
        }
    }

    @Override
    public List<FAQItem> findAll() {
        try (Jedis jedis = jedisPool.getResource()) {
            List<FAQItem> items = new ArrayList<>();
            for (String key : jedis.keys(PREFIX + "*")) {
                Map<String, String> map = jedis.hgetAll(key);
                String question = key.substring(PREFIX.length());
                FAQItem item = new FAQItem(
                        question,
                        map.get("answer")
                );
                items.add(item);
            }
            return items;
        }
    }

    @Override
    public void deleteByQuestion(String question) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(PREFIX + question);
        }
    }
}
