package com.ubuntu.chatroomserver.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubuntu.chatroomserver.config.WebSocketConfig;
import com.ubuntu.chatroomserver.entity.AnalysisEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
    @RequestMapping(value = {"/historyContent"},method = RequestMethod.GET)
    public String getHistoryContent(@RequestParam ("log") String log){
        Path rootLocation = Paths.get("historyLog");
        Path path = rootLocation.resolve(log);
        List<String> historyContent = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        String content = "";
        try {
            historyContent = Files.readAllLines(path);
            content = mapper.writeValueAsString(historyContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
