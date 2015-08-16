package org.yipeng.lucene.suggest;

/**
 * Created by yipeng on 15/8/16.
 */

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.AnalyzerWrapper;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;

/**
 * 其功能相当与将每个词的前缀,从minGram到maxGram,都切分出来,具体可以看此类都输出
 */
public class EdgeNGramAnalyzerWrapper extends AnalyzerWrapper {
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
    public EdgeNGramAnalyzerWrapper(ReuseStrategy reuseStrategy) {
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
