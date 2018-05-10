package ru.itis.hara;

import java.util.LinkedList;
import java.util.List;

public class Vertex {
    Graph graph;
    public int position;
    List<Vertex> vertices = new LinkedList<>();

    Vertex(int position) {
        this.position = position;
    }

    public void addVertex(Vertex vertex){
        vertices.add(vertex);
    }
    public boolean hasGraph(){
        return graph != null;
    }
}
