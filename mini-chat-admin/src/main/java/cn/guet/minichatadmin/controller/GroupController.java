package cn.guet.minichatadmin.controller;

import cn.guet.minichatadmin.entity.AjaxResult;
import cn.guet.minichatadmin.entity.GroupInfo;
import cn.guet.minichatadmin.service.GroupInfoService;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/group")
public class GroupController {

    private final static String BUCKET_NAME = "smart-campus-refactoring";
    @Autowired
    private GroupInfoService groupInfoService;
    @Autowired
    private OSS OSSClient;

    @GetMapping("/all")
    public AjaxResult getGroupList(@RequestParam("consumerId") Integer consumerId) {
        return AjaxResult.success(groupInfoService.getGroupListByConsumerId(consumerId));
    }

    @GetMapping("/list")
    public List<Map<String, Object>> getList(@RequestParam("consumerId") Integer consumerId) {
        List<GroupInfo> list = groupInfoService.list();
        List<GroupInfo> joinedGroups = groupInfoService.getGroupListByConsumerId(consumerId);
        List<Map<String, Object>> res = new ArrayList<>();
        for (GroupInfo groupInfo : list) {
            String jsonString = JSONObject.toJSONString(groupInfo);
            Map<String, Object> map = JSONObject.parseObject(jsonString);
            if (joinedGroups.contains(groupInfo)) {
                map.put("isJoined", true);
            } else {
                map.put("isJoined", false);
            }
            res.add(map);
        }
        return res;
    }


    @PostMapping(value = "/upload")
    public AjaxResult uploadGroupImage(MultipartFile file) throws IOException {
        PutObjectResult putObject = OSSClient.putObject(BUCKET_NAME, file.getName(), file.getInputStream());
        return AjaxResult.success(putObject);
    }

    @GetMapping("/consumers")
    public AjaxResult getGroupConsumers(@RequestParam("groupId") Integer groupId) {
        List<Integer> consumerIds = groupInfoService.getConsumerIds(groupId);
        return AjaxResult.success(consumerIds);
    }
}
