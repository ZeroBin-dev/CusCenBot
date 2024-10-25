package com.cus.center.controller;

import com.cus.center.dto.ChatReq;
import com.cus.center.dto.ChatRes;
import com.cus.center.dto.TrainReq;
import com.cus.center.dto.TrainRes;
import com.cus.center.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatApiController {

  private final ChatService chatService;

  /**
   * NLP 기반 응답 생성
   */
  @PostMapping("/chat")
  public ChatRes chat(@RequestBody ChatReq req) throws Exception {
    return chatService.generateResponse(req);
  }

  /**
   * 데이터 학습
   */
  @PostMapping("/train")
  public TrainRes train(@RequestBody TrainReq req) throws Exception {
    return chatService.trainModel(req);
  }

}
