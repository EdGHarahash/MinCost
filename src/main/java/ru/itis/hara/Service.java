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

    public HashSet<Integer> improve(Map<Integer, Integer> map) {
        HashSet<Integer> newZeros = new HashSet<>();
        if(map.get(-2)!=0){
            return newZeros;
        }
        int min = Integer.MAX_VALUE;
        for (Integer price : map.values()
                ) {
            if ((price < min) && (price!=0)) {
                min = price;
            }
        }
        int zeroCount = 0;
        for(Map.Entry<Integer, Integer> entry : map.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            if(key < 0){
                continue;
            }
            value =  value - min;
            map.put(key,value);
            if (value == 0){
                zeroCount++;
                newZeros.add(key);
            }
        }

        map.put(-1,map.get(-1)+min);
        map.put(-2, map.get(-1)+zeroCount);
        return newZeros;
    }
}