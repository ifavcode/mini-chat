package cn.guet.minichatadmin.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ExampleStreamListener implements StreamListener<String, MapRecord<String, String, String>> {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        System.out.println("MessageId: " + message.getId());
        System.out.println("Stream: " + message.getStream());
        System.out.println("Body: " + message.getValue());
        redisTemplate.opsForStream().delete(Objects.requireNonNull(message.getStream()), message.getId());
    }
}
