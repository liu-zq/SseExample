package com.example.ssedemo.utils;

import com.google.gson.JsonObject;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ReqContextUtils {
    //超时时间
    private static int DEFAULT_TIME_OUT = 60*60*1000;
    //订阅列表，存储所有主题的订阅请求，每个topic对应一个ArrayList，ArrayList里该topic的所有订阅请求
    private static HashMap<String, ArrayList<SseEmitter>> subscribeArray = new LinkedHashMap<>();

    //添加订阅消息
    public static SseEmitter addSubscrib(String topic, HttpServletRequest request, HttpServletResponse response) {
        if (null == topic || "".equals(topic)) {
            return null;
        }

        SseEmitter emitter = new SseEmitter();
        ArrayList<SseEmitter> emitterList = subscribeArray.get(topic);
        if (null == emitterList) {
            emitterList = new ArrayList<SseEmitter>();
            subscribeArray.put(topic, emitterList);
        }
        emitterList.add(emitter);

        return emitter;
    }

    //获取订阅列表
    public static ArrayList<SseEmitter> getSubscribList(String topic) {
        return subscribeArray.get(topic);
    }

    //推送消息
    public static void publishMessage(String topic, String content) {
        ArrayList<SseEmitter> emitterList = subscribeArray.get(topic);
        if (null != emitterList) {
            for(SseEmitter emitter :emitterList) {
                try {
                    //emitter.send(content);
                    emitter.send(SseEmitter.event().name("message").data(content));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
