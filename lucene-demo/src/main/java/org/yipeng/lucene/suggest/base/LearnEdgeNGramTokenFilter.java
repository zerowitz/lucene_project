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

    /**
     * 其功能相当与将每个词的前缀,从minGram到maxGram,都切分出来,具体可以看此类都输出
     */
    static class EdgeNGramAnalyzerWrapper extends AnalyzerWrapper {
        private int minGram = 1;
        private int maxGram = 4;
        private Analyzer analyzer = new WhitespaceAnalyzer();

        /**
         * Creates a new AnalyzerWrapper with the given reuse strategy.
         * <p>If you want to wrap a single delegate Analyzer you can probably
         * reuse its strategy when instantiating this subclass:
         * {@code super(delegate.getReuseStrategy());}.
         * <p>If you choose different analyzers per field, use
         * {@link #PER_FIELD_REUSE_STRATEGY}.
         *
         * @param reuseStrategy
         * @see #getReuseStrategy()
         */
        protected EdgeNGramAnalyzerWrapper(ReuseStrategy reuseStrategy) {
            super(reuseStrategy);
        }


        @Override
        protected Analyzer getWrappedAnalyzer(String fieldName) {
            return analyzer;
        }

        @Override
        protected TokenStreamComponents wrapComponents(String fieldName, TokenStreamComponents components) {
            if(maxGram >= minGram && minGram > 0) {//最大的Gram不能最小的Gram
                TokenFilter filter = new EdgeNGramTokenFilter(components.getTokenStream(), minGram, maxGram);
                return new TokenStreamComponents(components.getTokenizer(), filter);
            }else{
                return components;
            }
        }

        public void setMinGram(int minGram) {
            this.minGram = minGram;
        }

        public void setMaxGram(int maxGram) {
            this.maxGram = maxGram;
        }
    }
}
