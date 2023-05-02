package cn.guet.minichatadmin.controller;

import cn.guet.minichatadmin.entity.AjaxResult;
import cn.guet.minichatadmin.entity.ConveyContainer;
import cn.guet.minichatadmin.entity.GroupConsumer;
import cn.guet.minichatadmin.entity.GroupInfo;
import cn.guet.minichatadmin.listener.ExampleStreamListener;
import cn.guet.minichatadmin.service.GroupConsumerService;
import cn.guet.minichatadmin.service.GroupInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Record;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.StreamReceiver;
import org.springframework.data.redis.stream.Subscription;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Map;

@RestController()
@RequestMapping("/chat")
@Slf4j
public class ChatController {

    private static final String CONVEY_KEY = "convey:key:";
    private static final String CONVEY_GROUP = "convey:group:";
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private ExampleStreamListener streamListener;
    @Autowired
    private GroupInfoService groupInfoService;
    @Autowired
    private GroupConsumerService groupConsumerService;

    @PostMapping("/send")
    public AjaxResult sendMessage(@RequestBody ConveyContainer conveyContainer) {
        if (conveyContainer.getUserId() == null || conveyContainer.getConveyGroupId() == null) {
            return AjaxResult.error("参数有误!");
        }
        System.out.println(conveyContainer);
        String key = CONVEY_KEY + conveyContainer.getConveyGroupId();
        String group = CONVEY_GROUP + conveyContainer.getConveyGroupId();
        ObjectRecord<String, ConveyContainer> record = StreamRecords.newRecord().in(key).ofObject(conveyContainer);
        RecordId id = redisTemplate.opsForStream(new Jackson2HashMapper(true)).add(record.withStreamKey(key));
        redisTemplate.execute((RedisCallback<Object>) v -> v.streamCommands().xReadGroup(Consumer.from(group, String.valueOf(conveyContainer.getUserId())), StreamOffset.create((key).getBytes(), ReadOffset.lastConsumed())));
        return AjaxResult.success(id);
    }

    @GetMapping("/get")
    public Flux<Map<String, String>> getMessage(@RequestParam("groupId") Integer groupId) {
        String key = CONVEY_KEY + groupId;
        StreamReceiver.StreamReceiverOptions<String, MapRecord<String, String, String>> options = StreamReceiver.StreamReceiverOptions.builder().pollTimeout(Duration.ofMillis(1000)).build();
        StreamReceiver<String, MapRecord<String, String, String>> receiver = StreamReceiver.create((ReactiveRedisConnectionFactory) redisConnectionFactory, options);
        Flux<MapRecord<String, String, String>> flux = receiver.receive(StreamOffset.fromStart(key));
        return flux.map(Record::getValue);
    }

    @PostMapping("/readAll")
    public boolean readAll(@RequestBody ConveyContainer conveyContainer) {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> containerOptions = StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder().pollTimeout(Duration.ofMillis(100)).build();
        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container = StreamMessageListenerContainer.create(redisConnectionFactory, containerOptions);
        Subscription subscription = container.receiveAutoAck(Consumer.from(CONVEY_GROUP + conveyContainer.getConveyGroupId(), String.valueOf(conveyContainer.getUserId())), StreamOffset.fromStart(CONVEY_KEY + conveyContainer.getConveyGroupId()), streamListener);
        container.start();
        return subscription.isActive();
    }

    @PostMapping("/init_group")
    public boolean createGroupAndKey(@RequestBody ConveyContainer conveyContainer) {
        String key = CONVEY_KEY + conveyContainer.getConveyGroupId();
        String group = CONVEY_GROUP + conveyContainer.getConveyGroupId();
        Boolean hasKey = redisTemplate.hasKey(key);
        ConveyContainer entity = new ConveyContainer();
        if (Boolean.FALSE.equals(hasKey)) {
            entity.setMessage("开始聊天吧~");
            ObjectRecord<String, ConveyContainer> record = StreamRecords.newRecord().in(key).ofObject(entity);
            redisTemplate.opsForStream(new Jackson2HashMapper(true)).add(record.withStreamKey(key));
        } else {
            entity.setUserId(conveyContainer.getUserId());
            entity.setMessage(conveyContainer.getUserId() + "加入了聊天。");
            ObjectRecord<String, ConveyContainer> record = StreamRecords.newRecord().in(key).ofObject(entity);
            redisTemplate.opsForStream(new Jackson2HashMapper(true)).add(record.withStreamKey(key));
        }
        try {
            groupConsumerService.save(new GroupConsumer(conveyContainer.getConveyGroupId(), conveyContainer.getUserId()));
            StreamInfo.XInfoGroups groups = redisTemplate.opsForStream().groups(key);
            if (groups.isEmpty()) {
                redisTemplate.opsForStream().createGroup(key, group);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return true;
    }

    @PostMapping("/get_group_id")
    public GroupInfo getGroupId(@RequestBody GroupInfo groupInfo) {
        boolean save = groupInfoService.saveOrUpdate(groupInfo);
        return groupInfo;
    }

    @GetMapping("/consumer/info")
    public AjaxResult getConsumerInfo(ConveyContainer conveyContainer) {
        String group = CONVEY_GROUP + conveyContainer.getConveyGroupId();
        String key = CONVEY_KEY + conveyContainer.getConveyGroupId();
        StreamInfo.XInfoConsumers consumers = redisTemplate.opsForStream().consumers(key, group);
        return AjaxResult.success(consumers);
    }

    @PostMapping("/test")
    public AjaxResult test(@RequestBody ConveyContainer conveyContainer) {
        return AjaxResult.success(conveyContainer);
    }
}
