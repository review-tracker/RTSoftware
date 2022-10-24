
import edu.smu.tspell.wordnet.SynsetType;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/*
 */
/**
 *
 * @author jeffreyyoung
 */
public class Classifier {

    static Properties props = new Properties();
    // build pipeline
    static StanfordCoreNLP pipeline;
    // creates sim object
    static Sim sim = new Sim();
    // creates WordNet Thesaurus
    static WordNet W = new WordNet();

    public static void main(String[] args) throws IOException {

        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit,pos");
        pipeline = new StanfordCoreNLP(props);

        InOut iO = new InOut();
        ArrayList<ArrayList<String>> names = new ArrayList<>();
        iO.input(names, new ArrayList<>(), "Negative_Cleaned_Data_2020/names.xlsx");

        int sum = 0;
        for (String n : names.get(0)) {
            System.out.println("Review Component------------------------------------");
            ArrayList<ArrayList<String>> reviews = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<>();
            iO.input(reviews, labels, "Negative_Cleaned_Data_2020/" + n);

            ArrayList<ArrayList<String>> out = new ArrayList<>();
            for (ArrayList<String> s : reviews) {
                out.add(new ArrayList<>());
            }
            out.add(new ArrayList<>());

            int i = 0;
            for (String r : reviews.get(7)) {
                String cl = "";
                String rv = r.replaceAll("\\p{Punct}", "").toLowerCase();

                //Rule 1: does not work
                ArrayList<String> syn = new ArrayList<>();
                expand("work", syn, 1);
                for (String s : syn) {
                    if (contains(rv, s)) {
                        if (rv.contains("does not " + s)) {
                            cl += "Xx ";
                        } else if (rv.contains("wont " + s)) {
                            cl += "Xx ";
                        } else if (rv.contains("not " + s + "ing")) {
                            cl += "Xx ";
                        } else if (rv.contains("doesnt " + s)) {
                            cl += "Xx ";
                        }
                    }
                }

                //Rule 2 - 12: 11 classes dealing with content
                syn = new ArrayList<>();
                expand("content", syn, 2);
                for (String s : syn) {
                    if (contains(rv, s)) {

                        if (rv.contains("appropriate " + s)) {
                            cl += "A ";
                        }
                        if (rv.contains("misleading " + s)) {
                            cl += "F ";
                        }
                        if (rv.contains("suitable " + s)) {
                            cl += "B ";
                        }
                        if (contains(rv, "sex")) {
                            cl += "C ";
                        }
                        if (rv.contains("unsettling " + s)) {
                            cl += "D ";
                        }
                        if (contains(rv, "false")) {
                            cl += "E ";
                        }
                        if (rv.contains("violent " + s)) {
                            cl += "G ";
                        }
                        if (rv.contains("hateful " + s)) {
                            cl += "H ";
                        }
                        if (contains(rv, "promotional")) {
                            cl += "I ";
                        }
                        if (contains(rv, "legal")) {
                            cl += "I2 ";
                        }
                        if (rv.contains("advertis")) {
                            cl += "R ";
                        }
                    }
                }

                //Rule 13
                if (contains(rv, "nazi")) {
                    cl += "J ";
                }
                //Rule 14
                if (contains(rv, "pirated")) {
                    cl += "K ";
                }
                //Rule 15
                if (rv.contains("black market")) {
                    cl += "L ";
                }
                //Rule 16
                if (rv.contains("forced marriage")) {
                    cl += "M ";
                }
                //Rule 17
                if (contains(rv, "language")) {
                    if (contains(rv, "support")) {
                        cl += "N ";
                    }
                }

                //Rule 18: malicious content
                syn = new ArrayList<>();
                expand("malicious", syn, 3);
                for (String s : syn) {
                    if (contains(rv, s)) {
                        cl += "O ";
                    }
                }

                //Rule 19
                syn = new ArrayList<>();
                expand("invocation", syn, 2);
                for (String s : syn) {
                    if (rv.contains(s + " name")) {
                        cl += "P ";
                    }
                }

                //Rule 20 - 22
                syn = new ArrayList<>();
                expand("contains", syn, 1);
                for (String s : syn) {
                    if (contains(rv, s)) {
                        ArrayList<String> synT = new ArrayList<>();
                        expand("profanity", synT, 2);
                        for (String sy : synT) {
                            if (contains(rv, sy)) {
                                cl += "Q ";
                            }
                        }
                        synT = new ArrayList<>();
                        expand("advertising", synT, 2);
                        for (String sy : synT) {
                            if (contains(rv, sy)) {
                                cl += "R ";
                            }
                        }
                        synT = new ArrayList<>();
                        expand("Violence", synT, 2);
                        for (String sy : synT) {
                            if (contains(rv, sy)) {
                                cl += "S ";
                            }
                        }
                    }
                }

                //Rule 23 - 29
                syn = new ArrayList<>();
                expand("promotes", syn, 1);
                for (String s : syn) {
                    if (contains(rv, s)) {
                        if (contains(rv, "terror")) {
                            cl += "T ";
                        }
                        if (contains(rv, "gambling")) {
                            cl += "U ";
                        }
                        if (contains(rv, "product")) {
                            cl += "V ";
                        }
                        if (contains(rv, "service")) {
                            cl += "W ";
                        }
                        if (contains(rv, "tobacco")) {
                            cl += "KK ";
                        }
                        if (contains(rv, "cigarette")) {
                            cl += "LL ";
                        }
                        if (contains(rv, "alcohol")) {
                            cl += "NN ";
                        }
                    }
                }

                //Rule 30
                syn = new ArrayList<>();
                expand("sell", syn, 1);
                for (String s : syn) {
                    if (contains(rv, s)) {
                        if (rv.contains(s + " me")) {
                            cl += "X ";
                        } else if (rv.contains("me something")) {
                            cl += "X ";
                        }
                    }
                }

                //Rule 31
                syn = new ArrayList<>();
                expand("cure", syn, 2);
                for (String s : syn) {
                    if (contains(rv, s)) {
                        if (rv.contains("offers " + s)) {
                            cl += "Y ";
                        } else if (contains(rv, "disease")) {
                            cl += "Y ";
                        }
                    }
                }

                //Rule 32
                syn = new ArrayList<>();
                expand("recommend", syn, 1);
                for (String s : syn) {
                    if (contains(rv, s)) {
                        if (rv.contains("other skill")) {
                            cl += "Z ";
                        }
                    }
                }

                //rule 33
                syn = new ArrayList<>();
                expand("compensation", syn, 2);
                for (String s : syn) {
                    if (rv.contains("offer") && (rv.contains(s) || rv.contains("compensate"))) {
                        cl += "BB ";
                    }
                }

                //Rule 34
                syn = new ArrayList<>();
                expand("donation", syn, 2);
                for (String s : syn) {
                    if (contains(rv, s)) {
                        if (contains(rv, "solicit")) {
                            cl += "CC ";
                        } else if (contains(rv, "ask")) {
                            cl += "CC ";
                        } else if (contains(rv, "request")) {
                            cl += "CC ";
                        }
                    }
                }

                //Rule 35
                syn = new ArrayList<>();
                expand("gender", syn, 2);
                for (String s : syn) {
                    if (contains(rv, s)) {
                        cl += "DD ";
                    }
                }

                //Rule 36
                if (contains(rv, "emergency")) {
                    syn = new ArrayList<>();
                    expand("contact", syn, 1);
                    for (String s : syn) {
                        if (contains(rv, s)) {
                            cl += "EE ";
                        }
                    }
                }

                //Rule 37
                if (contains(rv, "create")) {
                    syn = new ArrayList<>();
                    expand("dangerous", syn, 3);
                    for (String s : syn) {
                        if (contains(rv, s)) {
                            cl += "FF ";
                        }
                    }
                }

                //Rule 38
                if (contains(rv, "bomb")) {
                    syn = new ArrayList<>();
                    expand("build", syn, 1);
                    for (String s : syn) {
                        if (contains(rv, s)) {
                            cl += "GG ";
                        }
                    }
                }

                //Rule 39
                if (rv.contains("a review")) {
                    syn = new ArrayList<>();
                    expand("asks", syn, 1);
                    for (String s : syn) {
                        if (contains(rv, s)) {
                            cl += "OO ";
                        }
                    }
                }

                //Rule 40
                syn = new ArrayList<>();
                expand("directs", syn, 1);
                for (String s : syn) {
                    if (contains(rv, s)) {
                        if (rv.contains("another skill")) {
                            cl += "PP ";
                        }
                    }
                }

                //Rule 41
                if (rv.contains("call 911")) {
                    cl += "QQ ";
                }

                //Rule 42
                if (rv.contains("life saving")) {
                    cl += "RR ";
                }

                //Rule 43 - 45
                syn = new ArrayList<>();
                expand("collects", syn, 1);
                for (String s : syn) {
                    if (contains(rv, s)) {
                        if (rv.contains("kid") || rv.contains("child") || rv.contains("personal")) {
                            if (rv.contains("info") || rv.contains("data")) {
                                cl += "SS ";
                            }
                        }
                        if (rv.contains("sensitive")) {
                            if (rv.contains("info") || rv.contains("data")) {
                                cl += "TT ";
                            }
                        }

                        if (rv.contains("health")) {
                            if (rv.contains("info") || rv.contains("data")) {
                                cl += "UU ";
                            }
                        }
                    }
                }

                //Rule 46
                if (contains(rv, "unresponsive")) {
                    cl += "VV1 ";
                }

                if (rv.contains("does not") || rv.contains("will not") || rv.contains("wont")) {
                    if (rv.contains("turn off")) {
                        cl += "VV ";
                    } else if (rv.contains("not stop")) {
                        cl += "VV ";
                    } else if (rv.contains("shut")) {
                        cl += "VV ";
                    } else if (rv.contains("wont stop")) {
                        cl += "VV ";
                    }
                }

                if (rv.contains("not understand")) {
                    cl += "WW ";
                }

                if (rv.contains("advertising")) {
                    if (rv.contains("false")) {
                        cl += "YY ";
                    }
                }
                if (rv.contains("stop") && rv.contains("working")) {
                    cl += "ZZ ";
                }

                if (!cl.isEmpty()) {
                    System.out.println(rv + " " + cl);
                    System.out.println();
                    sum++;
                }

            } //end for
            System.out.println(sum);

        }

    }

    public static boolean contains(String s, String term) {
        ArrayList<String> words = new ArrayList<>();
        String temp = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ' && i != s.length() - 1) {
                temp += s.charAt(i);
            } else {
                if (i == s.length() - 1) {
                    temp += s.charAt(i);
                }
                words.add(temp);
                temp = "";
            }
        }

        for (String word : words) {
            if (sim.similarity(word, term) >= 0.85) {
                return true;
            }
        }
        return false;

    }

    public static void expand(String word, ArrayList<String> sn, int part) throws IOException {

        switch (part) {
            case 1 -> {
                for (String s : W.getSyn(word, SynsetType.VERB)) {
                    sn.add(s);
                }
            }
            case 2 -> {
                for (String s : W.getSyn(word, SynsetType.NOUN)) {
                    sn.add(s);
                }
            }
            case 3 -> {
                for (String s : W.getSyn(word, SynsetType.ADJECTIVE)) {
                    sn.add(s);
                }
            }
            default -> {
                System.out.println("No Syn");
            }
        }

        for (int i = 0; i < sn.size(); i++) {
            if (sim.sim(sn.get(i), word) < 0.5 || sn.get(i).equals(word)) {
                sn.remove(i--);
            }
        }
        sn.add(word);

    }
}
