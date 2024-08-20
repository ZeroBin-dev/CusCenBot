package com.cus.center.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisService {

  private final RedisTemplate<String, String> redisTemplate;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public String get(final String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public void set(final String key, final String value) {
    redisTemplate.opsForValue().set(key, value);
  }

  public void setHash(final String group, final String key, final String value) {
    redisTemplate.opsForHash().put(group, key, value);
  }

  public void setAnswerList(String key, String value) {
    setHash("qna:answer", key, value);
  }

  public String getHash(final String group, final String key) {
    return String.valueOf(redisTemplate.opsForHash().get(group, key));
  }

  public Map<Object, Object> getQuestionList() {
    return redisTemplate.opsForHash().entries("qna:question");
  }

  public String[] getQuestionList(String category) throws Exception {
    String jsonValue = getHash("qna:question", category);
    return objectMapper.readValue(jsonValue, new TypeReference<String[]>() {
    });
  }

  public void setQuestionList(String category, String[] valueList) throws Exception {
    String jsonValue = objectMapper.writeValueAsString(valueList);
    setHash("qna:question", category, jsonValue);
  }

  public String getAnswerData(String category) {
    return getHash("qna:answer", category);
  }

}
