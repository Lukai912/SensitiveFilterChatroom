package com.ubuntu.chatroomserver.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubuntu.filter.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class AnalysisEntity {
    @Autowired
    public Topic topic;
    @Autowired
    public SensitiveWords sensitiveWords;

    private void topicAys(String contents){
        if(LifeWordFilter.LifeWordFilter_instance.isContains(contents)){
            topic.life++;
        }
        if(WorkWordFilter.WorkWordFilter_instance.isContains(contents)){
            topic.work++;
        }
        if(LifeWordFilter.LifeWordFilter_instance.isContains(contents)){
            topic.life++;
        }
        if(EntertainmentFilter.EntertainmentFilter_instance.isContains(contents)){
            topic.entertainment++;
        }
    }
    private void sensitiveAys(String contents){
        sensitiveWords.cult += CultSensitiveFilter.CultSensitiveFilter_instance.containsNum(contents);
        sensitiveWords.illegalwords += IllegalwordsFilter.IllegalwordsFilter_instance.containsNum(contents);
        sensitiveWords.names += NamesFilter.NamesFilter_instance.containsNum(contents);
        sensitiveWords.news += NewsFilter.NewsFilter_instance.containsNum(contents);
        sensitiveWords.political += PoliticalFilter.PoliticalFilter_instance.containsNum(contents);
        sensitiveWords.otherSenesitive += OhterSensitiveFilter.OhterSensitiveFilter_instance.containsNum(contents);
    }
    public synchronized void analysis(String log) throws IOException {
        AnalysisEntity entity = new AnalysisEntity();
        Path rootLocation = Paths.get("historyLog");
        if(Files.notExists(rootLocation)){
            Files.createDirectories(rootLocation);
        }

        Path path = rootLocation.resolve(log);
        File file = new File(String.valueOf(path.toAbsolutePath()));
        if (!file.exists()) {
            return;
        }
        System.out.println(file.getAbsolutePath());
        List<String> loglist = Files.readAllLines(path);
        topic.totalNum = loglist.size();
        for (int i =0;i<loglist.size();i++){
            String line = loglist.get(i);
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(line, Message.class);
            topicAys(message.getContent());
            sensitiveAys(message.getContent());
        }
    }
    public AnalysisEntity(){
        topic = new Topic();
        sensitiveWords = new SensitiveWords();
    }
}
