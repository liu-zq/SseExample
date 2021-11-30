package com.example.ssedemo.controller;

import com.example.ssedemo.utils.ReqContextUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class SubscribeController {
    @RequestMapping(value = "/subscribe", method = RequestMethod.GET, produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter subscribe(HttpServletRequest req, HttpServletResponse res, @RequestParam("topic") String topic) {
        return ReqContextUtils.addSubscrib(topic, req, res);
    }

    @RequestMapping("/publish")
    public void publish(@RequestParam("topic") String topic, @RequestParam("content") String content) {
        ReqContextUtils.publishMessage(topic, content);
    }
}
