package com.cus.center;

import com.cus.center.service.RedisService;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.TrainingParameters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = CustomerCenterApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("local")
class CustomerCenterApplicationTests {

  @Autowired
  RedisService redisService;

  @Test
  void 레디스데이터입력() throws Exception {
    redisService.setQuestionList("Q001", new String[]{"안녕하세요?", "안녕", "하이", "반가워", "어이", "안뇽", "잘 지내?", "뭐해?"});
    redisService.setQuestionList("Q002", new String[]{"주문을 취소하고 싶어요.", "주문취소", "주문취소방법", "취소 원해요", "취소할래요", "오더 취소"});
    redisService.setQuestionList("Q003", new String[]{"배송 상태를 확인하고 싶어요.", "배송상태", "배송", "배송 어디야?", "배송추적", "배송 됐나요?"});
    redisService.setQuestionList("Q004", new String[]{"환불이 가능한가요?", "환불", "환불가능", "환불여부", "돈 돌려줘", "환불할 수 있나요?", "환불해줘"});
    redisService.setQuestionList("Q005", new String[]{"회원 가입은 어떻게 하나요?", "회원가입", "가입방법", "회원가입하기", "회원갚", "회원가입", "가입법", "회원하러"});
    redisService.setQuestionList("Q006", new String[]{"비밀번호를 잊어버렸어요.", "비밀번호분실", "비밀번호변경"});
    redisService.setQuestionList("Q007", new String[]{"주문 확인 이메일을 못 받았어요.", "확인메일", "주문확인메일"});
    redisService.setQuestionList("Q008", new String[]{"제품의 보증 기간은 얼마나 되나요?", "보증", "보증기간"});
    redisService.setQuestionList("Q009", new String[]{"교환 정책은 어떻게 되나요?", "교환", "교환정책"});
    redisService.setQuestionList("Q010", new String[]{"문제가 해결되지 않았어요.", "해결되지않음", "여전히", "문제해결"});
    redisService.setQuestionList("Q011", new String[]{"고마워", "감사", "고맙"});
    redisService.setQuestionList("Q012", new String[]{"결제가 안돼요.", "결제문제", "결제오류", "결제 불가", "결제 안됨", "결제가 안됩니다"});
    redisService.setQuestionList("Q013", new String[]{"계정이 잠겼어요.", "계정잠김", "로그인 불가", "계정 잠김", "계정 로그인 불가"});
    redisService.setQuestionList("Q014", new String[]{"앱이 작동하지 않아요.", "앱오류", "앱 문제", "앱 안됨", "어플리케이션 오류", "어플 문제"});
    redisService.setQuestionList("Q015", new String[]{"연락처를 변경하고 싶어요.", "연락처변경", "전화번호변경", "핸드폰 번호 변경", "휴대폰번호 변경"});
    redisService.setQuestionList("Q016", new String[]{"할인 코드가 적용되지 않아요.", "할인코드오류", "할인코드문제", "쿠폰오류", "쿠폰안됨", "할인코드 안됨"});


    redisService.setAnswerList("Q001", "안녕하세요! 무엇을 도와드릴까요?");
    redisService.setAnswerList("Q002", "주문 취소를 원하시면, 주문 번호와 함께 고객 서비스 센터에 문의해 주세요.");
    redisService.setAnswerList("Q003", "배송 상태를 확인하려면 주문 번호를 입력해 주세요. 고객 서비스 센터에서 상태를 확인해 드리겠습니다.");
    redisService.setAnswerList("Q004", "제품에 따라 다릅니다. 일반적으로 제품 수령 후 30일 이내에 환불이 가능합니다. 자세한 사항은 고객 서비스 센터에 문의해 주세요.");
    redisService.setAnswerList("Q005", "회원 가입은 웹사이트의 '회원 가입' 버튼을 클릭하고 필요한 정보를 입력하면 됩니다.");
    redisService.setAnswerList("Q006", "비밀번호를 잊으셨다면, 로그인 페이지에서 '비밀번호 찾기'를 클릭하여 이메일을 통해 비밀번호를 재설정할 수 있습니다.");
    redisService.setAnswerList("Q007", "주문 확인 이메일이 발송되지 않았다면, 스팸 폴더를 확인해 보시고, 이메일 주소가 올바른지 확인해 주세요. 여전히 문제를 해결하지 못했다면 고객 서비스 센터에 문의해 주세요.");
    redisService.setAnswerList("Q008", "제품의 보증 기간은 제품에 따라 다릅니다. 보증 기간에 대한 정보는 제품 설명서나 웹사이트에서 확인할 수 있습니다.");
    redisService.setAnswerList("Q009", "제품 교환은 제품 수령 후 30일 이내에 가능합니다. 교환을 원하시면 고객 서비스 센터에 문의해 주세요.");
    redisService.setAnswerList("Q010", "문제가 해결되지 않았으면, 구체적인 문제를 자세히 설명해 주시면 추가로 도와드리겠습니다. 고객 서비스 센터에 문의해 주세요.");
    redisService.setAnswerList("Q011", "별말씀을요");
    redisService.setAnswerList("Q012", "결제가 진행되지 않을 경우, 사용 중인 결제 수단의 상태를 확인하시고, 문제가 지속되면 고객 서비스 센터에 문의해 주세요.");
    redisService.setAnswerList("Q013", "계정이 잠긴 경우, 보안상의 이유로 잠긴 것일 수 있습니다. 계정 복구를 위해 고객 서비스 센터에 문의해 주세요.");
    redisService.setAnswerList("Q014", "앱이 정상적으로 작동하지 않으면, 최신 버전으로 업데이트 후 다시 시도해 주세요. 문제가 지속되면 고객 서비스 센터에 문의해 주세요.");
    redisService.setAnswerList("Q015", "연락처를 변경하시려면, 계정 설정에서 연락처를 업데이트 하시거나, 고객 서비스 센터에 문의해 주세요.");
    redisService.setAnswerList("Q016", "할인 코드가 적용되지 않을 경우, 코드가 유효한지 확인하고 다시 시도해 주세요. 문제가 해결되지 않으면 고객 서비스 센터에 문의해 주세요.");

  }

  @Test
  void 학습데이터생성() throws Exception {
    Map<Object, Object> qnaMap = redisService.getQuestionList();

    List<DocumentSample> sampleList = new ArrayList<>();
    for (Map.Entry<Object, Object> map : qnaMap.entrySet()) {
      String key = String.valueOf(map.getKey());
      DocumentSample sample = new DocumentSample(key, redisService.getQuestionList(key));
      sampleList.add(sample);
    }

    ObjectStream<DocumentSample> sampleStream = new ListObjectStream<>(sampleList);

    TrainingParameters params = new TrainingParameters();
    params.put(TrainingParameters.ITERATIONS_PARAM, 150); // 반복 학습 횟수
    params.put(TrainingParameters.CUTOFF_PARAM, 1);       // 최소 카운트 기준

    // 모델 학습
    DoccatModel model = DocumentCategorizerME.train("ko", sampleStream, params, new DoccatFactory());

    // 모델 저장 경로
    File modelFile = new File("src/main/resources/models/doccat.bin");
    modelFile.getParentFile().mkdirs();  // 디렉토리 생성

    // 모델 파일 저장
    try (FileOutputStream modelOut = new FileOutputStream(modelFile)) {
      model.serialize(modelOut);
    }

    System.out.println("모델 생성 완료 : " + modelFile.getAbsolutePath());

  }

  public static class ListObjectStream<T> implements ObjectStream<T> {
    private final List<T> list;
    private int index = 0;

    public ListObjectStream(List<T> list) {
      this.list = list;
    }

    @Override
    public T read() throws IOException {
      if (index < list.size()) {
        return list.get(index++);
      }
      return null;
    }

    @Override
    public void reset() throws IOException, UnsupportedOperationException {
      index = 0;
    }

    @Override
    public void close() throws IOException {
      // No resources to close
    }
  }

}
