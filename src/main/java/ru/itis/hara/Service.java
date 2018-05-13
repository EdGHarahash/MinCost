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
        Minimum minimum = minimum(map);
        Integer min = minimum.min;
        Integer min2 = minimum.min2;
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

        if(zeroCount == 1){
            map.get(-3).index=min2-min;
        }

        map.get(-1).index += min;
        map.get(-2).index += zeroCount;
        return newZeros;
    }

    public void zeroAndMinCheck(Map<Integer, Map<Integer, Hand>> bigMap, Map<Integer, Hand> map){
        for(Map.Entry<Integer, Hand> entry : map.entrySet()) {
            Integer key = entry.getKey();
            Hand value = entry.getValue();
            if (value.index == 0){
                bigMap.get(key).get(-2).index--;
            }
                if (value==bigMap.get(key).get(-3)){
                bigMap.get(key).get(-3).index=minimum(bigMap.get(key)).min2;
            }
        }
    }

    private Minimum minimum(Map<Integer, Hand> map){
        Minimum min = new Minimum();
        min.min = Integer.MAX_VALUE;
        min.min2 = 0;
        for (Hand price : map.values()
                ) {
            if ((price.index < min.min)&&(!price.stat)) {
                min.min2 = min.min;
                min.min = price.index;
            }
        }
        return min;
    }
}