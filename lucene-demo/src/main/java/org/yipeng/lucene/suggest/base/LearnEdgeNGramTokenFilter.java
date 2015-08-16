package org.yipeng.lucene.suggest.base;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.AnalyzerWrapper;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttributeImpl;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttributeImpl;
import org.yipeng.lucene.suggest.EdgeNGramAnalyzerWrapper;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by yipeng on 15/8/16.
 * 在使用 {@link org.apache.lucene.search.suggest.analyzing.AnalyzingInfixSuggester}时
 * ,在建索引时会,用到{@link org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter}
 */
public class LearnEdgeNGramTokenFilter {
    public static void main(String[] args) throws IOException {
        EdgeNGramAnalyzerWrapper analyzerWrapper = new EdgeNGramAnalyzerWrapper(Analyzer.PER_FIELD_REUSE_STRATEGY);

        StringReader reader = new StringReader("hello world");
        TokenStream ts = analyzerWrapper.tokenStream("gramtext", reader);

        CharTermAttribute charAtt = ts.addAttribute(CharTermAttribute.class);

        OffsetAttribute offsetAtt = ts.addAttribute(OffsetAttribute.class);
        ts.reset();
        while (ts.incrementToken()) {
            System.out.println(charAtt.toString() +" , " + "start : " + offsetAtt.startOffset() + " , " +"end : " + offsetAtt.endOffset());
        }


    }

}
