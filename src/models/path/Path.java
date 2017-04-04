package models.path;


import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by mattm on 3/29/2017.
 */
public class Path {
    private Node start;
    private Node end;
    private LinkedList<Node> path;

    public Path(){
//        this.start = start;
//        this.end = end;
        this.path = new LinkedList<>();
    }

    public void addStep(Node n){
        this.path.addFirst(n);
    }
}