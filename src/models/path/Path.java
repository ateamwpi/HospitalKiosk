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

    public String toString() {
        String str = "Path: ";
        for (Node n : this.path) {
            str += n.getID() + ", ";
        }
        return str;
    }

    @Override
    public boolean equals(Object o) {
        Path p = (Path)o;
        if(this.path.size() != p.path.size()) return false;
        for (int i = 0; i < this.path.size(); i++) {
            if(this.path.get(i).getID() != p.path.get(i).getID()) return false;
        }
        return true;
    }
}