package com.cus.center.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

  private DocumentCategorizerME categorizer;
  private final RedisService redisService;

  @PostConstruct
  public void init() throws Exception {
    // 모델 초기화
    try (InputStream modelIn = getClass().getResourceAsStream("/models/doccat.bin")) {
      DoccatModel model = new DoccatModel(modelIn);
      categorizer = new DocumentCategorizerME(model);
    }
  }

  public String generateResponse(String userInput) {
    // 한국어 문장 분석 및 토큰화
    String[] tokens = tokenizeKorean(userInput);

    // 카테고리 분류
    double[] outcomes = categorizer.categorize(tokens);
    String category = categorizer.getBestCategory(outcomes);

    // 카테고리에 따른 응답 생성
    String result = redisService.getAnswerData(category);

    if (result == null || result.isEmpty()) {
      return "알아듣지 못했어요.";
    } else {
      return result;
    }
  }

  private String[] tokenizeKorean(String text) {
    List<String> tokens = new ArrayList<>();

    // KoreanAnalyzer는 매번 새로 생성하여 사용
    try (Analyzer analyzer = new KoreanAnalyzer()) {
      try (var tokenStream = analyzer.tokenStream(null, new StringReader(text))) {
        CharTermAttribute termAttr = tokenStream.addAttribute(CharTermAttribute.class);
        OffsetAttribute offsetAttr = tokenStream.addAttribute(OffsetAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
          tokens.add(termAttr.toString());
        }
        tokenStream.end();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return tokens.toArray(new String[0]);
  }
}
