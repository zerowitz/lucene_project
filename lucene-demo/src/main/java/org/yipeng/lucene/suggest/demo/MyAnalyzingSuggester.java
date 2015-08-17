package org.yipeng.lucene.suggest.demo;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.search.suggest.Lookup.LookupResult;
import org.apache.lucene.search.suggest.analyzing.AnalyzingSuggester;
import org.yipeng.lucene.suggest.Input;
import org.yipeng.lucene.suggest.InputArrayIterator;
import org.yipeng.lucene.suggest.base.WhiteSpaceOutStopAnalyzer;

import java.io.IOException;
import java.util.List;

public class MyAnalyzingSuggester {
    public static void main(String[] args) throws IOException {
        Input keys[] = new Input[]{
            new Input("the ghost of christmas past",50),
                new Input("the gh ost of christmas past",40)
        };

        Analyzer analyzer = new WhiteSpaceOutStopAnalyzer();
        /**{@link AnalyzingSuggester#exactFirst}表示精确优先,{@link AnalyzingSuggester#preserveSep}表示去掉分隔符 */
        AnalyzingSuggester suggester = new AnalyzingSuggester(analyzer, analyzer, AnalyzingSuggester.EXACT_FIRST | AnalyzingSuggester.PRESERVE_SEP
                , 256, -1, false);

        suggester.build(new InputArrayIterator(keys));

        List<LookupResult> results = suggester.lookup("the ghost of chris", false, 1);

        for (LookupResult result : results) {
            System.out.println(result.key + " , " + result.value);
        }
        System.out.println("-----");
        //因为the是停用词,单没有the时
        results = suggester.lookup("ghost of chris", false, 1);
        for (LookupResult result : results) {
            System.out.println(result.key + " , " + result.value);
        }

        System.out.println("--------------");
        //没有分隔符
        Input[] inputs = new Input[]{new Input("ab cd",0),
                                        new Input("abcd",1)};

        Analyzer baseAnalyzer = new WhitespaceAnalyzer();
        suggester = new AnalyzingSuggester(baseAnalyzer,baseAnalyzer,0, 256, -1, true);
        suggester.build(new InputArrayIterator(inputs));
        results = suggester.lookup("ab c", false, 2);

        for (LookupResult result : results) {
            System.out.println(result.key + " , " + result.value);
        }


    }
}
