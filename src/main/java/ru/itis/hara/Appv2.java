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
                    Integer price = randomizer.nextInt(14) + 1;
                    consumersMap.get(i).put(-1, new Hand(true,0));
                    consumersMap.get(i).put(-2, new Hand(true,0));
                    Hand hand = new Hand(price);
                    consumersMap.get(i).put(j, hand);
                    producersMap.get(j).put(-1, new Hand(true,0));
                    producersMap.get(j).put(-2, new Hand(true,0));
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
            for (int j = 0; j<P; j++) {
                System.out.print(temp.get(j)+" | ");
            }
            System.out.println();
        }
        System.out.println();

        for (int i = 0; i < C; i++) {
            for (int j = 0; j<P; j++) {
                System.out.print(producersMap.get(j).get(i)+" | ");
            }
            System.out.println();
        }
        System.out.println("consum improve");

        for (int i = 0; i < C; i++) {
            for (Integer index : service.improve(consumersMap.get(i))) {
                zeros.add(new Price(0, i, index));
            }
        }
        System.out.println();

        for (int i = 0; i < C; i++) {
            Map<Integer, Hand> temp = consumersMap.get(i);
            for (int j = 0; j<P; j++) {
                System.out.print(temp.get(j)+" | ");
            }
            System.out.print("constant: " + temp.get(-1) + "  zeros: "+ temp.get(-2));
            System.out.println();
        }
        System.out.println();

        for (int i = 0; i < C; i++) {
            for (int j = 0; j<P; j++) {
                System.out.print(producersMap.get(j).get(i)+" | ");
            }
            System.out.println();
        }
        for (int j = 0; j<P; j++) {
            System.out.print("constant: " + producersMap.get(j).get(-1)+"  zeros: "+  producersMap.get(j).get(-2));
        }
        System.out.println();

        for (int i = 0; i < P; i++) {
            for (Integer index : service.improve(producersMap.get(i))) {
                zeros.add(new Price(0, i, index));
            }
        }
        System.out.println();

        for (int i = 0; i < C; i++) {
            for (int j = 0; j<P; j++) {
                System.out.print(producersMap.get(j).get(i)+" | ");
            }
            System.out.println();
        }
        for (int j = 0; j<P; j++) {
            System.out.print("constant: " + producersMap.get(j).get(-1)+"  zeros: "+  producersMap.get(j).get(-2));
        }
        System.out.println();
//        Arrays.sort(goodPrices, new SortByCost());
//        System.out.println("Enter a consumers values");
//        int[] consumers = new int[C];
//        for (int i = 0; i < C; i++) {
//            consumers[i] = reader.nextInt();
//        }
//        System.out.println("Enter a producers values");
//        int[] producers = new int[P];
//        for (int i = 0; i < P; i++) {
//            producers[i] = reader.nextInt();
//        }
//        reader.close();
//
//        for (int i = 0; i < C; i++) {
//            for (int j = 0; j < P; j++) {
//                System.out.print(consumersMap.get(i).get(j) + "   | ");
//            }
//            System.out.println();
//        }
//
//
//        Map<String, Vertex> basePointsPr = new HashMap<>();
//        Map<String, Vertex> basePointsCon = new HashMap<>();

//        int basePointsCount = 0;
//        int emptyStockCount = 0;
//        int index = 0;
//        while (emptyStockCount < (C + P)) {
//            Price price = goodPrices[index];
//            if ((consumers[price.consumer] != 0) && (producers[price.producer] != 0)) {
//                int basePointValue = min(consumers[price.consumer], producers[price.producer]);
//                producers[price.producer] -= basePointValue;
//                consumers[price.consumer] -= basePointValue;
//                if (producers[price.producer] == 0) {
//                    emptyStockCount++;
//                }
//
//                if (consumers[price.consumer] == 0) {
//                    emptyStockCount++;
//                }
//
//
//                prices[price.consumer][price.producer] = -basePointValue;
//
//                if (!basePointsCon.containsKey("c" + price.consumer)) {
//                    basePointsCon.put("c" + price.consumer, new Vertex(price.consumer));
//                }
//
//                if (!basePointsPr.containsKey("p" + price.producer)) {
//                    basePointsPr.put("p" + price.producer, new Vertex(price.producer));
//                }
//
//                basePointsCon.get("c" + price.consumer).addVertex(basePointsPr.get("p" + price.producer));
//                basePointsPr.get("p" + price.producer).addVertex(basePointsCon.get("c" + price.consumer));
//                basePointsCount++;
//            }
//            index++;
//        }
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


