

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import java.util.ArrayList;
import java.util.HashSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jeffreyyoung
 */
class WordNet {

    WordNet() {
        System.out.println("Wordnet object created!!!");
  
    }

    public ArrayList<String> getSyn(String word, SynsetType pos) {
        System.setProperty("wordnet.database.dir", "//Users/jeffreyyoung/Desktop/Java_Packages/SD/WordNet-3.0/dict");
        WordNetDatabase database = WordNetDatabase.getFileInstance();

        Synset[] synsets = database.getSynsets(word, pos/*like NOUN, or VERB*/);

        HashSet hs = new HashSet();
        if (synsets.length > 0) {
            ArrayList<String> set = new ArrayList<>();
            for (int i = 0; i < synsets.length; i++) {
                String[] wordForms = synsets[i].getWordForms();
                for (int j = 0; j < wordForms.length; j++) {
                    hs.add(wordForms[j]);
                }
            }
            //showing all synsets
            for (int j = 0; j < set.size(); j++) {
                System.out.println("al :: " + set.get(j));
            }

        } else {
            System.err.println("No synsets exist that contain the word form '" + word + "'");
        }
        return new ArrayList<>(hs);
    }

}
