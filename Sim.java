
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.lexical_db.data.Concept;
import edu.cmu.lti.ws4j.Relatedness;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.JiangConrath;
import edu.cmu.lti.ws4j.impl.LeacockChodorow;
import edu.cmu.lti.ws4j.impl.Lesk;
import edu.cmu.lti.ws4j.impl.Lin;
import edu.cmu.lti.ws4j.impl.Path;
import edu.cmu.lti.ws4j.impl.Resnik;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;
import java.util.List;

//package QandA;
public class Sim {

    private static ILexicalDatabase db = new NictWordNet();

    //available options of metrics
    static RelatednessCalculator[] rcs = {
        new LeacockChodorow(db), new Lesk(db), new WuPalmer(db),
        new Resnik(db), new JiangConrath(db), new Lin(db), new Path(db)};

    RelatednessCalculator rc = new WuPalmer(db);

    Sim() {
        System.out.println("Sim Object created.....");

    }

    public double sim(String word1, String word2) {

        double score = rc.calcRelatednessOfWords(word1, word2);
        if (score > 1.0) {
            return 1.0;
        } else {
            return score;
        }
    }

    public double findSimilarity(String word1, String word2) {
        WS4JConfiguration.getInstance().setMFS(true);
        List<edu.cmu.lti.jawjaw.pobj.POS[]> posPairs = rc.getPOSPairs();
        double maxScore = -1.0;

        for (edu.cmu.lti.jawjaw.pobj.POS[] posPair : posPairs) {
            List<Concept> synsets1
                    = (List<Concept>) db.getAllConcepts(word1, posPair[0].toString());

            List<Concept> synsets2
                    = (List<Concept>) db.getAllConcepts(word2, posPair[1].toString());

            for (Concept synset1 : synsets1) {
                for (Concept synset2 : synsets2) {
                    Relatedness relatedness = rc.calcRelatednessOfSynset(synset1, synset2);
                    double score = relatedness.getScore();

                    if (score > maxScore) {
                        maxScore = score;
                    }
                }
            }
        }

        if (maxScore == -1.0) {
            maxScore = 0.0;
        }

        //System.out.println("sim('" + word1 + "', '" + word2 + "') =  " + maxScore);
        return maxScore;
    }

    /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
     *
     * @param s1
     * @param s2
     */
    public double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0;
            /* both strings are zero length */ }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }
// Example implementation of the Levenshtein Edit Distance
    // See http://rosettacode.org/wiki/Levenshtein_distance#Java

    public int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        }
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }
        return costs[s2.length()];
    }

    public double compareStrings(String stringA, String stringB) {
        JaroWinkler algorithm = new JaroWinkler();
        return algorithm.getSimilarity(stringA, stringB);
    }

}
