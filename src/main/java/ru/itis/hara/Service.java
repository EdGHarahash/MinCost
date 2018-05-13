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
        if (map.get(-2).index != 0) {
            return newZeros;
        }
        Minimum minimum = minimum(map);
        Integer min = minimum.min;
        Integer min2 = minimum.min2;
        int zeroCount = 0;
        for (Map.Entry<Integer, Hand> entry : map.entrySet()) {
            Integer key = entry.getKey();
            Hand value = entry.getValue();
            if (value.stat) {
                continue;
            }
            value.index = value.index - min;
            if (value.index == 0) {
                zeroCount++;
                newZeros.add(key);
            }
        }

        if (zeroCount < 2) {
            map.get(-3).index = min2 - min;
        } else {
            map.get(-3).index = 0;
        }

        map.get(-1).index += min;
        map.get(-2).index += zeroCount;
        return newZeros;
    }

    public HashSet<Price> zeroAndMinCheck(Map<Integer, Map<Integer, Hand>> bigMap,
                                          Map<Integer, Hand> map, Integer index, boolean reverse) {
        HashSet<Price> newZeros = new HashSet<>();
        for (Map.Entry<Integer, Hand> entry : map.entrySet()) {
            Integer key = entry.getKey();
            Hand value = entry.getValue();
            if (key < 0 || !bigMap.containsKey(key)) {
                continue;
            }
            if (value.index == 0) {
                bigMap.get(key).get(-2).index--;
                if (bigMap.get(key).get(-2).index == 0) {
                    bigMap.get(key).remove(index);
                    for (Integer in : improve(bigMap.get(key))) {
                        newZeros.add(new Price(key, in, reverse));
                    }
                }
            }
            if (value.index.equals(bigMap.get(key).get(-3).index)) {
                bigMap.get(key).remove(index);
                bigMap.get(key).get(-3).index = minimum(bigMap.get(key)).min2;
            }
        }
        return newZeros;
    }


    public void firstImprove(Map<Integer, Map<Integer, Hand>> consumersMap, Integer couCount,
                             Map<Integer, Map<Integer, Hand>> producersMap, Integer prodCount) {
        for (int i = 0; i < couCount; i++) {
            consumersMap.get(i).get(-3).index = minimum(consumersMap.get(i)).min2;
        }
        for (int j = 0; j < prodCount; j++) {
            producersMap.get(j).get(-3).index = minimum(producersMap.get(j)).min2;
        }
    }

    private Minimum minimum(Map<Integer, Hand> map) {
        Minimum min = new Minimum();
        min.min = Integer.MAX_VALUE;
        min.min2 = Integer.MAX_VALUE;
        int zeroCount = 0;
        for (Hand price : map.values()
                ) {
            if (!price.stat) {

                if (price.index == 0) {
                    zeroCount++;
                }
                if (zeroCount == 2) {
                    min.min2 = 0;
                }

                if (price.index < min.min) {
                    min.min = price.index;
                }
                if (price.index != 0 && price.index < min.min2) {
                    min.min2 = price.index;
                }

            }
        }
        if (min.min2 == Integer.MAX_VALUE) {
            min.min2 = 0;
        }
        return min;
    }
}