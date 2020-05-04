package com.ubuntu.chatroomserver.entity;

import org.springframework.beans.factory.annotation.Autowired;

public class SensitiveWords {
    @Autowired
    public int cult;
    @Autowired
    public int illegalwords;
    @Autowired
    public int names;
    @Autowired
    public int news;
    @Autowired
    public int otherSenesitive;
    @Autowired
    public int political;
}
