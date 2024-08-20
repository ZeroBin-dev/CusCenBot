package com.cus.center.controller;

import com.cus.center.dto.ChatReq;
import com.cus.center.dto.ChatRes;
import com.cus.center.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatApiController {

  private final ChatService chatService;

  @PostMapping("/chat")
  public ChatRes chat(@RequestBody ChatReq req) {
    // NLP 기반 응답 생성
    return new ChatRes(chatService.generateResponse(req.getUserInput()));
  }

}
