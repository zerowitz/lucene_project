package org.yipeng.lucene.suggest.demo;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
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
				new Input("ipad", 3299, new BytesRef("apple")),
				new Input("iwatch", 2588, new BytesRef("apple"))
		};
		
		Analyzer analyzer = new EdgeNGramAnalyzerWrapper(Analyzer.PER_FIELD_REUSE_STRATEGY);
		
		Directory directory = new RAMDirectory();
		
		AnalyzingInfixSuggester suggester = new AnalyzingInfixSuggester(directory, analyzer, analyzer, 3, false);
		suggester.build(new InputArrayIterator(inputs));
		
		List<LookupResult> results = suggester.lookup("ip", 10, true, true);
		
		for(LookupResult result : results){
			System.out.println(result.key+"  ,   " + result.value +"  ,  " + result.payload.toString());
		}
		
		suggester.close();
	}
}
