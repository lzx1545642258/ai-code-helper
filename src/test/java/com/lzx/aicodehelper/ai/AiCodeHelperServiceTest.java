package com.lzx.aicodehelper.ai;

import dev.langchain4j.service.Result;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiCodeHelperServiceTest {

    @Resource
    private AiCodeHelperService aiCodeHelperService;

    @Test
    void chat() {
        String chat = aiCodeHelperService.chat("你好，我是程序员李泽翔");
        System.out.println(chat);
    }

    @Test
    void chatWithMemory() {
        String result = aiCodeHelperService.chat("你好，我是程序员李泽翔");
        System.out.println(result);
        result = aiCodeHelperService.chat("你好，我是谁来着？");
        System.out.println(result);
    }

    @Test
    void chatForReport() {
        String userMessage = "你好，我是程序员李泽翔，学Java两个月，请帮我制定学习报告";
        AiCodeHelperService.Report report = aiCodeHelperService.chatForReport(userMessage);
        System.out.println(report);
    }

    @Test
    void chatWithRag() {
        Result<String> result = aiCodeHelperService.chatWithRag("怎么学习Java？有哪些常见的面试题？");
        System.out.println(result.content());
        System.out.println(result.sources());
    }

    @Test
    void chatWithMcp() {
        String  result = aiCodeHelperService.chatWithMcp("程序员鱼皮的编程导航是什么？");
        System.out.println(result);
    }

    @Test
    void chatWithGuardRail(){
        String result = aiCodeHelperService.chat("kill the game");
        System.out.println(result);
    }
}