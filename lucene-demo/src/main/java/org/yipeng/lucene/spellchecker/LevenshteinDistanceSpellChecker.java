package org.yipeng.lucene.spellchecker;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.spell.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

/**
 * Created by yipeng on 15/8/13.
 */
public class LevenshteinDistanceSpellChecker {
    public static void main(String[] args) throws IOException {
        DirectSpellChecker spellchecker = new DirectSpellChecker();
        Directory directory = new RAMDirectory();
        Analyzer analyzer = new WhitespaceAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(directory, config);

        String[] termsToAdd = { "metanoia", "metanoian", "metanoiai", "metanoias", "metanoi&" };
        for (int i = 0; i < termsToAdd.length; i++) {
            Document doc = new Document();
            doc.add(newTextField("repentance", termsToAdd[i], Field.Store.NO));
            writer.addDocument(doc);
        }
        writer.commit();
        writer.close();
        IndexReader ir = DirectoryReader.open(directory);
        String misspelled = "metanoix";
        SuggestWord[] similar = spellchecker.suggestSimilar(new Term("repentance", misspelled), 4, ir, SuggestMode.SUGGEST_WHEN_NOT_IN_INDEX);

        StringDistance sd = spellchecker.getDistance();
        for(SuggestWord word : similar) {
            System.out.println(word.string+","+word.score);
        }

    }


    private static Field newTextField(String fieldName, String value, Field.Store stored) {
        return new Field(fieldName, value, stored == Field.Store.YES ? TextField.TYPE_STORED : TextField.TYPE_NOT_STORED);
    }
}
