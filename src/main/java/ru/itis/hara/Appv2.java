package ru.itis.hara;


import javax.print.attribute.HashDocAttributeSet;
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
        Price[] goodPrices = new Price[C * P];
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

        Map<String, Vertex> basePointsPr = new HashMap<>();
        Map<String, Vertex> basePointsCon = new HashMap<>();
        Set<Price> basePoints = new HashSet<>();

        int basePointsCount = 0;
        int emptyStockCount = 0;
        int index = 0;
        while (emptyStockCount < (C + P)) {
            int max = 0;
            Price price = new Price(0,0,0);
            for (Price p:zeros
                 ) {
                int innerMax = 0;
                if (consumersMap.get(p.consumer).get(-2).index < 2){
                    innerMax += consumersMap.get(p.consumer).get(-3).index;
                }
                if (producersMap.get(p.consumer).get(-2).index < 2){
                    innerMax += producersMap.get(p.consumer).get(-3).index;
                }
                if (innerMax > max){
                    price = p;
                }
            }
            if ((consumers[price.consumer] != 0) && (producers[price.producer] != 0)) {
                int basePointValue = min(consumers[price.consumer], producers[price.producer]);
                producers[price.producer] -= basePointValue;
                consumers[price.consumer] -= basePointValue;
                if (producers[price.producer] == 0) {
                    emptyStockCount++;
                    service.zeroAndMinCheck(consumersMap,producersMap.get(price.producer));
                    producersMap.remove(price.producer);
                }

                if (consumers[price.consumer] == 0) {
                    emptyStockCount++;
                    service.zeroAndMinCheck(producersMap,consumersMap.get(price.consumer));
                    consumersMap.remove(price.consumer);
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
            index++;
        }
//
//        for (Vertex v : basePointsPr.values()) {
//            if (v.hasGraph()) {
//                continue;
//            }
//            v.graph = new Graph();
//            service.search(v, v);
//        }
//
//        index = 0;
//        while (basePointsCount < P + C - 1) {
//            if ((prices[goodPrices[index].consumer][goodPrices[index].producer] == null) ||
//                    (prices[goodPrices[index].consumer][goodPrices[index].producer] < 0)) {
//                index++;
//                continue;
//            }
//            Graph graph1 = basePointsCon.get("c" + goodPrices[index].consumer).graph.main;
//            Graph graph2 = basePointsPr.get("p" + goodPrices[index].producer).graph.main;
//            if (graph1 == graph2) {
//                index++;
//                continue;
//            }
//
//            for (Vertex c1 : graph1.consumers)
//                for (Vertex p2 : graph2.producers) {
//                    if (prices[c1.position][p2.position] > 0)
//                        prices[c1.position][p2.position] = null;
//                }
//            for (Vertex c2 : graph2.consumers)
//                for (Vertex p1 : graph1.producers) {
//                    if (prices[c2.position][p1.position] > 0)
//                        prices[c2.position][p1.position] = null;
//                }
//
//            prices[goodPrices[index].consumer][goodPrices[index].producer] = 0;
//            graph1.concatenation(graph1, graph2);
//            index++;
//            basePointsCount++;
//        }
//
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


