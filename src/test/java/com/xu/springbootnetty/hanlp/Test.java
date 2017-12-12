package com.xu.springbootnetty.hanlp;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import java.util.List;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {

  @org.junit.Test
  public void test1() {
    List<Term> segment = HanLP.segment("你好，欢迎使用HanLP汉语处理包！");
    System.out.println(segment);
    List<Term> segment1 = StandardTokenizer.segment("商品和服务");
    System.out.println(segment1);

    List<Term> termList = NLPTokenizer.segment("中国科学院计算技术研究所的宗成庆教授正在教授自然语言处理课程");
    System.out.println(termList);


  }

  //索引分词
  @org.junit.Test
  public void test2() {
    List<Term> termList = IndexTokenizer.segment("主副食品");
    for (Term term : termList) {
      System.out
          .println(term + " [" + term.offset + ":" + (term.offset + term.word.length()) + "]");
    }
  }

  //N-最短路径分词
  @org.junit.Test
  public void test03() {
    Segment nShortSegment = new NShortSegment().enableCustomDictionary(false)
        .enablePlaceRecognize(true).enableOrganizationRecognize(true);
    Segment shortestSegment = new DijkstraSegment().enableCustomDictionary(false)
        .enablePlaceRecognize(true).enableOrganizationRecognize(true);
    String[] testCase = new String[]{
        "今天，刘志军案的关键人物,山西女商人丁书苗在市二中院出庭受审。",
        "刘喜杰石国祥会见吴亚琴先进事迹报告团成员",
    };
    for (String sentence : testCase) {
      System.out.println(
          "N-最短分词：" + nShortSegment.seg(sentence) + "\n最短路分词：" + shortestSegment.seg(sentence));
    }
  }

  //中国人名识别 + 关键词提取
  @org.junit.Test
  public void test04() {
    String[] testCase = new String[]{
        "签约仪式前，秦光荣、李纪恒、仇和等一同会见了参加签约的企业家。",
        "王国强、高峰、汪洋、张朝阳光着头、韩寒、小四",
        "张浩和胡健康复员回家了",
        "王总和小丽结婚了",
        "编剧邵钧林和稽道青说",
        "这里有关天培的有关事迹",
        "龚学平等领导,邓颖超生前",
    };
    String content = "程序员(英文Programmer)是从事程序开发、维护的专业人员。一般将程序员分为程序设计人员和程序编码人员，但两者的界限并不非常清楚，特别是在中国。软件从业人员分为初级程序员、高级程序员、系统分析员和项目经理四大类。";
    content = "我想听 周杰伦 的那首 晴天";
    Segment segment = HanLP.newSegment().enableNameRecognize(true);
    for (String sentence : testCase) {
      System.out.println(segment.seg(sentence));
    }
    System.out.println(segment.seg(content));
    List<String> keywordList = HanLP.extractKeyword(content, 5);
    System.out.println(keywordList);
  }
}
