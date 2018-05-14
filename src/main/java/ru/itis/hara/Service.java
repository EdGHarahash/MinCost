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
            if (key < 0) {
                continue;
            }
            bigMap.get(key).remove(index);
            if (value.index == 0) {
                bigMap.get(key).get(-2).index--;
                if (bigMap.get(key).get(-2).index == 0) {
                    for (Integer in : improve(bigMap.get(key))) {
                        newZeros.add(new Price(key, in, reverse));
                    }
                }
            }
            if (value.index.equals(bigMap.get(key).get(-3).index)) {
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

                if (price.index < min.min) {
                    min.min = price.index;
                }
                if (price.index != 0 && price.index < min.min2) {
                    min.min2 = price.index;
                }

            }
        }
        if (min.min2 == Integer.MAX_VALUE  || zeroCount == 2) {
            min.min2 = 0;
        }
        return min;
    }

    public HashSet<Price> remove(Map<Integer, Hand> cMap, Integer cIndex, boolean reverse, Price price) {
        HashSet<Price> newZeros = new HashSet<>();
        System.out.println("PRICE:  " + price.consumer + "  " + price.producer + "  " + price.cost);
        Integer cValue = cMap.get(cIndex).index;
        cMap.remove(cIndex);
        if (cValue == 0) {
            cMap.get(-2).index--;
            if (cMap.get(-2).index == 0) {
                for (Integer in : improve(cMap)) {
                    newZeros.add(new Price(cIndex, in, reverse));
                }
            }
        }
        if (cValue.equals(cMap.get(-3).index)) {
            cMap.get(-3).index = minimum(cMap).min2;
        }
        return newZeros;
    }

    public Price zeroCompare(Set<Price> zeros, Map<Integer, Map<Integer, Hand>> consumersMap,
                             Map<Integer, Map<Integer, Hand>> producersMap) {
        Set<Price> badZeros = new HashSet<>();
        int max = -1;
        Price price = new Price(-1, 0, 0);
        for (Price p : zeros
                ) {
            int innerMax = 0;
            if (!consumersMap.containsKey(p.consumer) || !producersMap.containsKey(p.producer) ||
                    !consumersMap.get(p.consumer).containsKey(p.producer) ||
                    !producersMap.get(p.producer).containsKey(p.consumer)) {
                badZeros.add(p);
                continue;
            }
            if (consumersMap.get(p.consumer).get(-2).index < 2) {
                innerMax += consumersMap.get(p.consumer).get(-3).index;
            }
            if (producersMap.get(p.producer).get(-2).index < 2) {
                innerMax += producersMap.get(p.producer).get(-3).index;
            }
            if (innerMax > max) {
                max = innerMax;
                price = p;
            }
        }
        zeros.removeAll(badZeros);
        zeros.remove(price);
        return price;
    }

    public Map<Integer, Map<Integer, Hand>> copy(Map<Integer, Map<Integer, Hand>> map) {
        Map<Integer, Map<Integer, Hand>> newMap = new HashMap<>();
        for (Map.Entry<Integer, Map<Integer, Hand>> entry : map.entrySet()) {
            Map<Integer, Hand> innerMap = new HashMap<>();
            newMap.put(entry.getKey(), innerMap);
            for (Map.Entry<Integer, Hand> e : entry.getValue().entrySet()) {
                innerMap.put(e.getKey(), new Hand(e.getValue().stat, e.getValue().index));
            }
        }
        return newMap;
    }
}