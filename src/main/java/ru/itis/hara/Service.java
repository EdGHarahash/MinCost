package ru.itis.hara;

import java.util.*;

public class Service {

    public void search(Vertex v, Vertex previousVertex) {
        v.graph.producers.add(v);
        for (Vertex consumerVertex : v.vertices) {
            if (consumerVertex == previousVertex) {
                continue;
            }
            consumerVertex.graph = v.graph;
            v.graph.consumers.add(consumerVertex);
            for (Vertex producerVertex : consumerVertex.vertices) {
                if (producerVertex == v) {
                    continue;
                }
                producerVertex.graph = v.graph;
                search(producerVertex, consumerVertex);
            }
        }
    }

    public HashSet<Integer> improve(Map<Integer, Hand> map) {
        HashSet<Integer> newZeros = new HashSet<>();
        if(map.get(-2).index!=0){
            return newZeros;
        }
        int min = Integer.MAX_VALUE;
        for (Hand price : map.values()
                ) {
            if ((price.index < min)&&(!price.stat)) {
                min = price.index;
            }
        }
        int zeroCount = 0;
        for(Map.Entry<Integer, Hand> entry : map.entrySet()) {
            Integer key = entry.getKey();
            Hand value = entry.getValue();
            if(value.stat){
                continue;
            }
            value.index =  value.index - min;
            if (value.index == 0){
                zeroCount++;
                newZeros.add(key);
            }
        }

        map.get(-1).index += min;
        map.get(-2).index += zeroCount;
        return newZeros;
    }
}