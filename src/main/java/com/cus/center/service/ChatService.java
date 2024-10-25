package com.cus.center.service;

import com.cus.center.dto.ChatReq;
import com.cus.center.dto.ChatRes;
import com.cus.center.dto.TrainReq;
import com.cus.center.dto.TrainRes;
import com.cus.center.model.ListObjectStream;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.TrainingParameters;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

  /**
   * NLP 기반 응답 생성
   */
  public ChatRes generateResponse(final ChatReq param) {
    ChatRes res = new ChatRes();

    // 한국어 문장 분석 및 토큰화
    String[] tokens = tokenizeKorean(param.getUserInput());

    // 카테고리 분류
    double[] outcomes = categorizer.categorize(tokens);
    String category = categorizer.getBestCategory(outcomes);

    // 카테고리에 따른 응답 생성
    String result = redisService.getAnswerData(category);

    if (result == null || result.isEmpty()) {
      res.setValue("알아듣지 못했어요.");
    } else {
      res.setValue(result);
    }

    return res;
  }

  /**
   * 학습시키기
   */
  public TrainRes trainModel(final TrainReq param) throws Exception {
    TrainRes res = new TrainRes();

    // 학습 데이터 생성
    Map<Object, Object> qnaMap = redisService.getQuestionList();
    List<DocumentSample> sampleList = new ArrayList<>();
    for (Map.Entry<Object, Object> map : qnaMap.entrySet()) {
      String key = String.valueOf(map.getKey());
      DocumentSample sample = new DocumentSample(key, redisService.getQuestionList(key));
      sampleList.add(sample);
    }

    ObjectStream<DocumentSample> sampleStream = new ListObjectStream<>(sampleList);

    TrainingParameters trainingParameters = new TrainingParameters();
    trainingParameters.put(TrainingParameters.ITERATIONS_PARAM, 150); // 반복 학습 횟수
    trainingParameters.put(TrainingParameters.CUTOFF_PARAM, 1); // 최소 카운트 기준

    // 모델 학습
    DoccatModel model = DocumentCategorizerME.train("ko", sampleStream, trainingParameters, new DoccatFactory());

    // 모델 저장 경로
    File modelFile = new File("src/main/resources/models/doccat.bin");
    modelFile.getParentFile().mkdirs();  // 디렉토리 생성

    // 모델 파일 저장
    try (FileOutputStream modelOut = new FileOutputStream(modelFile)) {
      model.serialize(modelOut);
      res.setSuccess(true);
    } catch (Exception e) {
      res.setSuccess(false);
      e.printStackTrace();
    }

    return res;
  }

  /*****************************************************************************************************************/
  /*****************************************************************************************************************/
  /*****************************************************************************************************************/

  /**
   * 토큰 생성
   */
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

  /**
   * QID max 값 가져오기
   */
  private String getMaxQuestionId(String type) {
    // QID 최댓값 가져오기
    String maxId = "";

    if ("question".equals(type)) {
      //maxId = questionDAO.selectMaxQuestionId();
    } else if ("menu".equals(type)) {
      //maxId = questionDAO.selectMaxMenuId();
    }

    // ID에서 숫자 부분 추출
    String prefix = maxId.replaceAll("[0-9]", "");
    int number = Integer.parseInt(maxId.replaceAll("[^0-9]", "")); // 숫자만 추출

    // 새로운 ID 생성
    String newId = prefix + String.format("%03d", number + 1); // 3자리 숫자로 패딩
    return newId;
  }


}
