package com.ubuntu.chatroomserver.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubuntu.chatroomserver.config.WebSocketConfig;
import com.ubuntu.chatroomserver.entity.AnalysisEntity;
import com.ubuntu.filter.*;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class AnalysisController {
    @RequestMapping(value = {"/analysis"},method = RequestMethod.GET)
    public String analysis(@RequestParam("log") String log){
        AnalysisEntity entity = new AnalysisEntity();
        try {
            entity.analysis(log);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
    @RequestMapping(value = {"/historyList"},method = RequestMethod.GET)
    public String getHistory(){
        Path rootLocation = Paths.get("historyLog");
        File file = new File(String.valueOf(rootLocation.toAbsolutePath()));
        String list[] = file.list();
        ObjectMapper mapper = new ObjectMapper();
        String historyList = "";
        try {
            historyList = mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return historyList;
    }
    @RequestMapping(value = {"/filterTopicByRoom"},method = RequestMethod.GET)
    public String getClassificationByRoom(@RequestParam ("log")String log, @RequestParam ("topic") String topic) throws JsonProcessingException {
        BaseWordFilter filter = null;
        switch (topic){
            case "entertainment":
                filter = EntertainmentFilter.EntertainmentFilter_instance;
                break;
            case "life":
                filter = LifeWordFilter.LifeWordFilter_instance;
                break;
            case "learn":
                filter = LearnFilter.LearnFilter_instance;
                break;
            case "work":
                filter = WorkWordFilter.WorkWordFilter_instance;
                break;
            default:
                break;
        }
        if(filter == null) {
            return "[]";
        }
        Path rootLocation = Paths.get("historyLog");
        Path path = rootLocation.resolve(log);
        List<String> historyContent = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        List<Map> contentList = new ArrayList<>();
        try {
            historyContent = Files.readAllLines(path);
            for (int i =0;i <historyContent.size();i++){
                Map contnetnMap = mapper.readValue(historyContent.get(i), Map.class);
                if(filter.isContains((String) contnetnMap.get("content"))) {
                    contentList.add(contnetnMap);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ujson = mapper.writeValueAsString(contentList);
        System.out.println(ujson);
        return ujson;
    }
    @RequestMapping(value = {"/historyContent"},method = RequestMethod.GET)
    public String getHistoryContent(@RequestParam ("log") String log){
        Path rootLocation = Paths.get("historyLog");
        Path path = rootLocation.resolve(log);
        List<String> historyContent = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        StringBuilder content = new StringBuilder();
        content.append("[");
        try {
            historyContent = Files.readAllLines(path);
            for (int i =0;i <historyContent.size()-1;i++){
                content.append(historyContent.get(i));
                content.append(",");
            }
            content.append(historyContent.get(historyContent.size()-1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        content.append("]");
        System.out.println(content.toString());
        return content.toString();
    }
}
