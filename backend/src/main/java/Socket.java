import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Socket {
    private Map<String, List<Consumer<Object>>> consumers = new HashMap<>();

    public void subscribe(String key, Consumer<Object> consumer){
        List<Consumer<Object>> keyConsumers = consumers.getOrDefault(key, new ArrayList<>());
        keyConsumers.add(consumer);
        consumers.put(key, keyConsumers);
    }


    public void onEvent(String key, Object object){
        consumers.computeIfPresent(key, (k, v) -> {
            for (Consumer<Object> objectConsumer : v) {
                objectConsumer.accept(object);
            }
            return v;
        });
    }
}
