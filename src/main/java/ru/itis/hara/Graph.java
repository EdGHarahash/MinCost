package ru.itis.hara;

import java.util.HashSet;

public class Graph {
    Graph main = this;
    HashSet<Vertex> producers = new HashSet<>();
    HashSet<Vertex> consumers = new HashSet<>();

    public void concatenation(Graph g1, Graph g2) {
        g1.producers.addAll(g2.producers);
        g1.consumers.addAll(g2.consumers);
        g2.main = g1;
    }
}
