package MDP;

import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.ie.util.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.PropertiesUtils;

import java.util.List;
import java.util.Properties;
import MDP.Node;

public class ModifierDependentsParser {
    static String[] results;
    static int k;
    static Node last;

    public static void generate_mdpjson()  {
        Node now = new Node(results[k].substring(results[k].indexOf("->")+3, results[k].indexOf("/")), results[k].substring(results[k].indexOf("/")+1, results[k].indexOf("(")-1),
                results[k].substring(results[k].indexOf("(")+1, results[k].indexOf(")")));
        last.addChildren(now);
//        System.out.println(k + ", " + step);
        if (k==results.length-1) { return; }
        while (k<results.length-1 && results[k].indexOf("->")<=results[k+1].indexOf("->")) {
            if (results[k].indexOf("->")==results[k+1].indexOf("->")) {
                k++;
//                System.out.println(k + ", " + step);
                Node tmp = new Node(results[k].substring(results[k].indexOf("->")+3, results[k].indexOf("/")), results[k].substring(results[k].indexOf("/")+1, results[k].indexOf("(")-1),
                        results[k].substring(results[k].indexOf("(")+1, results[k].indexOf(")")));
                now = tmp;
                last.addChildren(tmp);
            }
            else {
                Node tmp = last;
                last = now;
                k++;
                generate_mdpjson();
                last = tmp;
            }
        }
        k++;
        return;
    }
    public static void show_tree(Node nowroot, int depth) {
        for (int i=0;i<depth;i++) System.out.print(' ');
        nowroot.printnode();
        for (int i=0;i<nowroot.children.size();i++) {
            show_tree(nowroot.children.get(i), depth+1);
        }
    }
    public static void main(String[] args) throws JSONException {
        Properties props = PropertiesUtils.asProperties("annotators", "tokenize,ssplit,pos,parse,lemma",
                "ssplit.isOneSentence", "true",
                "segment.model","edu/stanford/nlp/models/segmenter/chinese/ctb.gz",
                "tokenize.language","zh",
                "segment.sighanCorporaDict","edu/stanford/nlp/models/segmenter/chinese",
                "segment.serDictionary","edu/stanford/nlp/models/segmenter/chinese/dict-chris6.ser.gz",
                "segment.sighanPostProcessing","true");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        String text = "Daslab谁是最帅的那个人啊？";
        Annotation document = new Annotation(text);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
            System.out.println(dependencies + "\n");
            String result = dependencies.toString();
//            String sp = ""+(char)10;
//            results = result.split(sp);
//            Node root = new Node(results[0].substring(results[0].indexOf("->")+3, results[0].indexOf("/")), results[0].substring(results[0].indexOf("/")+1, results[0].indexOf("(")-1),
//                    results[0].substring(results[0].indexOf("(")+1, results[0].indexOf(")")));
//            k=1;
//            last = root;
//            generate_mdpjson();
//            show_tree(root,0);
        }
    }
}

