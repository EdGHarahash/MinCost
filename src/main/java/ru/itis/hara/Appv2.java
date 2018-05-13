package ru.itis.hara;

import java.util.*;

import static java.lang.Math.min;

public class Appv2 {
    private static final int C = 10; // 5 5 5 5 5 5 5 5 5 5 5 5 5 5 5 15 10
    private static final int P = 7;
    private static Service service = new Service();

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);

        System.out.println("random? y/n");
        Map<Integer, Map<Integer, Hand>> consumersMap = new HashMap<>();
        Map<Integer, Map<Integer, Hand>> producersMap = new HashMap<>();
        if (reader.next().equals("y")) {
            Random randomizer = new Random();
            for (int i = 0; i < P; i++) {
                producersMap.put(i, new HashMap<Integer, Hand>());
            }
            for (int i = 0; i < C; i++) {
                consumersMap.put(i, new HashMap<Integer, Hand>());
                for (int j = 0; j < P; j++) {
                    Integer price = randomizer.nextInt(19) + 1;
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
        } else {
            return;
////            System.out.println("Enter a prices line");
////            for (int i = 0; i < C; i++) {
////                for (int j = 0; j < P; j++) {
////                    prices[i][j] = reader.nextInt();
////                    Price price = new Price(prices[i][j], i, j);
////                    goodPrices[i * P + j] = price;
//                }
//            }
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
            for (int j = 0; j < P; j++) {
                System.out.print(producersMap.get(j).get(i) + " | ");
            }
            System.out.println();
        }
        System.out.println("consum improve");

        for (int i = 0; i < C; i++) {
            for (Integer index : service.improve(consumersMap.get(i))) {
                zeros.add(new Price(0, i, index));
                producersMap.get(index).get(-2).index += 1;
            }
        }
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

        for (int i = 0; i < P; i++) {
            for (Integer index : service.improve(producersMap.get(i))) {
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
        System.out.println("Enter a consumers values");
        int[] consumers = new int[C];
        for (int i = 0; i < C; i++) {
            consumers[i] = reader.nextInt();
        }
        System.out.println("Enter a producers values");
        int[] producers = new int[P];
        for (int i = 0; i < P; i++) {
            producers[i] = reader.nextInt();
        }
        reader.close();

        service.firstImprove(consumersMap, C, producersMap, P);

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
        int index = 0;
        while (emptyStockCount < (C + P)) {
            Set<Price> badZeros = new HashSet<>();
            int max = -1;
            Price price = new Price(0,0,0);
            for (Price p:zeros
                 ) {
                int innerMax = 0;
                if (!consumersMap.containsKey(p.consumer) || !producersMap.containsKey(p.producer) ||
                        !consumersMap.get(p.consumer).containsKey(p.producer) ||
                        !producersMap.get(p.producer).containsKey(p.consumer)){
                    badZeros.add(p);
                    System.out.println("add to bad zeros" + zeros.size());
                    continue;
                }
                if (consumersMap.get(p.consumer).get(-2).index < 2){
                    innerMax += consumersMap.get(p.consumer).get(-3).index;
                }
                if (producersMap.get(p.producer).get(-2).index < 2){
                    innerMax += producersMap.get(p.producer).get(-3).index;
                }
                if (innerMax > max){
                    max = innerMax;
                    price = p;
                }
            }
            if (max == -1){return;}
            System.out.println("c:  "+price.consumer+"  p: "+price.producer+"   m: "+max+" b: "+emptyStockCount);
            zeros.removeAll(badZeros);
            zeros.remove(price);
            if ((consumers[price.consumer] != 0) && (producers[price.producer] != 0)) {
                int basePointValue = min(consumers[price.consumer], producers[price.producer]);
                producers[price.producer] -= basePointValue;
                consumers[price.consumer] -= basePointValue;
                if (producers[price.producer] == 0) {
                    emptyStockCount++;
                    zeros.addAll(service.zeroAndMinCheck(consumersMap,producersMap.get(price.producer),
                            price.producer,false));
                    producersMap.remove(price.producer);
                    System.out.println("remove producer");
                }

                if (consumers[price.consumer] == 0) {
                    emptyStockCount++;
                    zeros.addAll(service.zeroAndMinCheck(producersMap,consumersMap.get(price.consumer),
                            price.consumer, true));
                    consumersMap.remove(price.consumer);
                    System.out.println("remove consumer");
                }

                for (int i = 0; i < C; i++) {
                    if (consumersMap.containsKey(i)) {
                        System.out.print("const:  " + consumersMap.get(i).get(-1));
                        System.out.print("zeros:  "+consumersMap.get(i).get(-2));
                        System.out.print("min:    "+consumersMap.get(i).get(-3));
                    }
                    for (int j = 0; j < P; j++) {
                        if(producersMap.containsKey(j) && producersMap.get(j).containsKey(i)
                                &&consumersMap.containsKey(i) && consumersMap.get(i).containsKey(j))
                            System.out.print(" j: "+j+"i: "+i +"  "+producersMap.get(j).get(i) + " | ");
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

        System.out.println("grafh pass");
//        index = 0;
//        while (basePointsCount < P + C - 1) {
//            int max = 0;
//            Price price = new Price(0,0,0);
//            for (Price p:zeros
//                    ) {
//                int innerMax = 0;
//                if (!consumersMap.containsKey(p.consumer) || !producersMap.containsKey(p.producer) ||
//                        !consumersMap.get(p.consumer).containsKey(p.producer) ||
//                        !producersMap.get(p.producer).containsKey(p.consumer)){
//                    zeros.remove(p);
//                    continue;
//                }
//                if (consumersMap.get(p.consumer).get(-2).index < 2){
//                    innerMax += consumersMap.get(p.consumer).get(-3).index;
//                }
//                if (producersMap.get(p.producer).get(-2).index < 2){
//                    innerMax += producersMap.get(p.producer).get(-3).index;
//                }
//                if (innerMax > max){
//                    price = p;
//                }
//            }
//            zeros.remove(price);
//
//            Graph graph1 = basePointsCon.get("c" + price.consumer).graph.main;
//            Graph graph2 = basePointsPr.get("p" + price.producer).graph.main;
//            if (graph1 == graph2) {
//                index++;
//                continue;
//            }
//
//            for (Vertex c1 : graph1.consumers)
//                for (Vertex p2 : graph2.producers) {
//                    if(consumersMap.containsKey(c1)){consumersMap.get(c1).remove(p2);}
//                    if(producersMap.containsKey(p2)){producersMap.get(p2).remove(c1);}
//                }
//            for (Vertex c2 : graph2.consumers)
//                for (Vertex p1 : graph1.producers) {
//                    consumersMap.get(c2).remove(p1);
//                    producersMap.get(p1).remove(c2);
//                }
//            basePoints.add(new Price(0, price.consumer, price.producer));
//            graph1.concatenation(graph1, graph2);
//            index++;
//            basePointsCount++;
//        }

//        for (int i = 0; i < C; i++) {
//            for (int j = 0; j < P; j++) {
//                String temp = "-";
//                if (prices[i][j] != null) {
//                    if (prices[i][j] < 0) {
//                        temp = ((Integer) (0 - prices[i][j])).toString();
//                    }
//                    if (prices[i][j] == 0) {
//                        temp = ((Integer) (prices[i][j])).toString();
//                    }
//                }
//
//
//                System.out.print(temp + "   | ");
//            }
//            System.out.println();
//        }
    }
}


