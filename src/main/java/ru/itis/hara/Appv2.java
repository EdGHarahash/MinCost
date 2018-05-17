package ru.itis.hara;

import java.util.*;

import static java.lang.Math.min;

public class Appv2 {
    private static final int C = 15; // 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 15 10
    private static final int P = 10;
    private static Service service = new Service();

    public static void main(String[] args) {

        int count = 0;
        while (true) {
            count++;
            Map<Integer, Map<Integer, Hand>> consumersMap = new HashMap<>();
            Map<Integer, Map<Integer, Hand>> producersMap = new HashMap<>();
            service.consumersMap = consumersMap;
            service.producersMap = producersMap;
            Random randomizer = new Random();
            for (int i = 0; i < P; i++) {
                producersMap.put(i, new HashMap<>());
            }
            int aBound = randomizer.nextInt(20) + 1;

            for (int i = 0; i < C; i++) {
                consumersMap.put(i, new HashMap<>());
                for (int j = 0; j < P; j++) {
                    Integer price = randomizer.nextInt(aBound) + 1;
                    consumersMap.get(i).put(-1, new Hand(true, 0));
                    consumersMap.get(i).put(-2, new Hand(true, 0));
                    consumersMap.get(i).put(-3, new Hand(true, 0));
                    Hand hand = new Hand(price);
                    consumersMap.get(i).put(j, hand);
                    producersMap.get(j).put(-1, new Hand(true, 0));
                    producersMap.get(j).put(-2, new Hand(true, 0));
                    producersMap.get(j).put(-3, new Hand(true, 0));
                    producersMap.get(j).put(i, hand);
                }
            }

            Set<Price> zeros = new HashSet<>();
            System.out.println("Start data:");
            for (int i = 0; i < C; i++) {
                Map<Integer, Hand> temp = consumersMap.get(i);
                for (int j = 0; j < P; j++) {
                    System.out.print(temp.get(j) + " | ");
                }
                System.out.println();
            }
            System.out.println();


            for (int i = 0; i < C; i++) {
                for (Integer index : service.improve(i, true)) {
                    zeros.add(new Price(0, i, index));
                }
            }
            System.out.println();


            for (int i = 0; i < P; i++) {
                for (Integer index : service.improve(i, false)) {
                    zeros.add(new Price(0, index, i));
                }
            }
            System.out.println();

            for (int i = 0; i < C; i++) {
                for (int j = 0; j < P; j++) {
                    System.out.print(producersMap.get(j).get(i) + " | ");
                }
                System.out.println();
            }
            for (int j = 0; j < P; j++) {
                System.out.print("constant: " + producersMap.get(j).get(-1) + "  zeros: " + producersMap.get(j).get(-2));
            }
            System.out.println();
            System.out.println();
            for (int i = 0; i < C; i++) {
                Map<Integer, Hand> temp = consumersMap.get(i);
                for (int j = 0; j < P; j++) {
                    System.out.print(temp.get(j) + " | ");
                }
                System.out.print("constant: " + temp.get(-1) + "  zeros: " + temp.get(-2));
                System.out.println();
            }
            System.out.println();
            int[] consumers = new int[C];
            int conSum = 0;
            int bound = randomizer.nextInt(6) + 1;
            for (int i = 0; i < C - 1; i++) {
                consumers[i] = randomizer.nextInt(bound) + 1;
                conSum += consumers[i];
            }
            int[] producers = new int[P];
            int prodSum = 0;
            for (int i = 0; i < P - 1; i++) {
                producers[i] = randomizer.nextInt(bound) + 1;
                prodSum += producers[i];
            }

            int last = 1;

            if (prodSum > conSum) {
                consumers[C - 1] = prodSum - conSum + last;
                producers[P - 1] = last;
            } else {
                producers[P - 1] = conSum - prodSum + last;
                consumers[C - 1] = last;
            }

            service.firstImprove(C, P);

            Map<Integer, Map<Integer, Hand>> firstConsumersMap = service.copy(consumersMap);
            Map<Integer, Map<Integer, Hand>> firstProducersMap = service.copy(producersMap);
            Set<Price> firstZeros = new HashSet<>(zeros);


            for (int i = 0; i < C; i++) {
                for (int j = 0; j < P; j++) {
                    System.out.print(producersMap.get(j).get(-3) + " | ");
                }
                System.out.println();
            }

            Map<String, Vertex> basePointsPr = new HashMap<>();
            Map<String, Vertex> basePointsCon = new HashMap<>();
            Set<Price> basePoints = new HashSet<>();

            int basePointsCount = 0;
            int emptyStockCount = 0;
            while (emptyStockCount < (C + P)) {
                Price price = service.zeroCompare(zeros, consumersMap, producersMap);

                if ((consumers[price.consumer] != 0) && (producers[price.producer] != 0)) {
                    int basePointValue = min(consumers[price.consumer], producers[price.producer]);
                    producers[price.producer] -= basePointValue;
                    consumers[price.consumer] -= basePointValue;
                    if (producers[price.producer] == 0) {
                        emptyStockCount++;
                        zeros.addAll(service.zeroAndMinCheck(consumersMap, producersMap.get(price.producer),
                                price.producer, false));
                        producersMap.remove(price.producer);
                    }

                    if (consumers[price.consumer] == 0) {
                        emptyStockCount++;
                        zeros.addAll(service.zeroAndMinCheck(producersMap, consumersMap.get(price.consumer),
                                price.consumer, true));
                        consumersMap.remove(price.consumer);
                    }

                    for (int i = 0; i < C; i++) {
                        if (consumersMap.containsKey(i)) {
                            System.out.print("const:  " + consumersMap.get(i).get(-1));
                            System.out.print("zeros:  " + consumersMap.get(i).get(-2));
                            System.out.print("min:    " + consumersMap.get(i).get(-3));
                        }
                        for (int j = 0; j < P; j++) {
                            if (producersMap.containsKey(j) && producersMap.get(j).containsKey(i)
                                    && consumersMap.containsKey(i) && consumersMap.get(i).containsKey(j))
                                System.out.print(" j: " + j + "i: " + i + "  " + producersMap.get(j).get(i) + " | ");
                        }
                        System.out.println();
                    }

                    basePoints.add(new Price(basePointValue, price.consumer, price.producer));

                    if (!basePointsCon.containsKey("c" + price.consumer)) {
                        basePointsCon.put("c" + price.consumer, new Vertex(price.consumer));
                    }

                    if (!basePointsPr.containsKey("p" + price.producer)) {
                        basePointsPr.put("p" + price.producer, new Vertex(price.producer));
                    }

                    basePointsCon.get("c" + price.consumer).addVertex(basePointsPr.get("p" + price.producer));
                    basePointsPr.get("p" + price.producer).addVertex(basePointsCon.get("c" + price.consumer));
                    basePointsCount++;
                }
            }

            for (Vertex v : basePointsPr.values()) {
                if (v.hasGraph()) {
                    continue;
                }
                v.graph = new Graph();
                service.search(v, v);
            }

            service.producersMap = firstProducersMap;
            service.consumersMap = firstConsumersMap;
            for (Price point : basePoints
                    ) {
                Integer c = point.consumer;
                Integer p = point.producer;
                firstZeros.addAll(service.remove(firstConsumersMap.get(c), p, c, true));
                firstZeros.addAll(service.remove(firstProducersMap.get(p), c, p, false));
            }

            System.out.println("grafh pass");
            producersMap = firstProducersMap;
            consumersMap = firstConsumersMap;
            zeros = firstZeros;

            while (basePointsCount < P + C - 1) {
                Price price = service.zeroCompare(zeros, consumersMap, producersMap);
                Graph graph1 = basePointsCon.get("c" + price.consumer).graph.main;
                Graph graph2 = basePointsPr.get("p" + price.producer).graph.main;
                if (graph1 == graph2) {
                    System.out.println("g=g  " + count);
                    zeros.addAll(service.remove(consumersMap.get(price.consumer), price.producer, price.consumer, true));
                    zeros.addAll(service.remove(producersMap.get(price.producer), price.consumer, price.producer, false));
                    continue;
                }

                for (Vertex c1 : graph1.consumers)
                    for (Vertex p2 : graph2.producers) {
                        Integer c = c1.position;
                        Integer p = p2.position;
                        if (consumersMap.containsKey(c) && consumersMap.get(c).containsKey(p)) {
                            zeros.addAll(service.remove(consumersMap.get(c), p, c, true));
                        }
                        if (producersMap.containsKey(p) && producersMap.get(p).containsKey(c)) {
                            zeros.addAll(service.remove(producersMap.get(p), c, p, false));
                        }
                    }
                for (Vertex c2 : graph2.consumers)
                    for (Vertex p1 : graph1.producers) {
                        Integer c = c2.position;
                        Integer p = p1.position;
                        if (consumersMap.containsKey(c) && consumersMap.get(c).containsKey(p)) {
                            zeros.addAll(service.remove(consumersMap.get(c), p, c, true));
                        }
                        if (producersMap.containsKey(p) && producersMap.get(p).containsKey(c)) {
                            zeros.addAll(service.remove(producersMap.get(p), c, p, false));
                        }
                    }
                basePoints.add(new Price(0, price.consumer, price.producer));
                graph1.concatenation(graph1, graph2);
                basePointsCount++;
            }
            for (Price p : basePoints
                    ) {
                System.out.println("c:  " + p.consumer + "  p:   " + p.producer + "   v:  " + p.cost);
            }
            System.out.println("Count:  " + basePointsCount);
        }
    }
}


