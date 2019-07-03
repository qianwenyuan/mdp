package MDP;

import java.util.ArrayList;
import java.util.List;

public class Node {
    String word;
    String type;
    String dependency;
    List<Node> children = new ArrayList<Node>();

    public Node() {}
    public Node(String w, String t, String d) {
        word = w;
        type = t;
        dependency = d;
    }
    public Node(Node node) {
        word = node.word;
        type = node.type;
        dependency = node.dependency;
        children = node.children;
    }

    public void set(String w, String t, String d) {
        word = w;
        type = t;
        dependency = d;
    }

    public void addChildren(Node node) {
        children.add(node);
    }

    public void printnode() {
        System.out.println("->"+word+"/"+type+"("+dependency+")");
    }
}
