package org.yipeng.lucene.suggest.demo;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.search.suggest.Lookup.LookupResult;
import org.apache.lucene.search.suggest.analyzing.AnalyzingInfixSuggester;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.yipeng.lucene.suggest.EdgeNGramAnalyzerWrapper;
import org.yipeng.lucene.suggest.Input;
import org.yipeng.lucene.suggest.InputArrayIterator;

/**
 * Created by yipeng on 15/8/16.
 */
public class MySuggester {
	public static void main(String[] args) throws IOException {
		Input[] inputs = new Input[]{new Input("iphone6", 5299, new BytesRef("apple")),
				new Input("iphone5s", 3299, new BytesRef("apple")),
				new Input("iphone5", 2899, new BytesRef("apple")),
				new Input("iphone4s", 1899, new BytesRef("apple")),
				new Input("ipad", 3399, new BytesRef("apple")),
				new Input("iwatch", 2588, new BytesRef("apple"))
		};
		
		Analyzer analyzer = new WhitespaceAnalyzer();//以为间隔的空格analyzer
		
		Directory directory = new RAMDirectory();

		//minPrefixChars=3,表示最小的前缀字符个数是3的时候才提示
		AnalyzingInfixSuggester suggester = new AnalyzingInfixSuggester(directory, analyzer, analyzer, 3, false);
		suggester.build(new InputArrayIterator(inputs));
		
		List<LookupResult> results = suggester.lookup("ip", 10, true, true);

		/**对于highlight可以修改{@link AnalyzingInfixSuggester#addPrefixMatch(StringBuilder, String, String, String)}
		 * 方法,修改强调的字符串
		 */
		for(LookupResult result : results){
			System.out.println(result.key+"  ,   " + result.value +"  ,  " + result.payload.toString() +" " + result.highlightKey);
		}
		
		suggester.close();
	}
}
