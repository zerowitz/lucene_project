package org.yipeng.lucene.suggest.base;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

import java.io.IOException;

/**
 * Created by yipeng on 15/8/17.
 */
public class WhiteSpaceOutStopAnalyzer extends Analyzer{
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final Tokenizer source = new WhitespaceTokenizer();
        return new TokenStreamComponents(source,new StopFilter(source, StopAnalyzer.ENGLISH_STOP_WORDS_SET));
    }


    public static void main(String[] args) throws IOException {
        Analyzer analyzer = new WhiteSpaceOutStopAnalyzer();
        TokenStream ts = analyzer.tokenStream("test","this is my best friend");
        CharTermAttribute charTermAttribute = ts.addAttribute(CharTermAttribute.class);
        OffsetAttribute offsetAttribute = ts.addAttribute(OffsetAttribute.class);

        ts.reset();

        while (ts.incrementToken()) {
            System.out.println(charTermAttribute.toString() +" , " + offsetAttribute.startOffset() + " -- " + offsetAttribute.endOffset());
        }
    }
}
