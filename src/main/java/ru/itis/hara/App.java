package ru.itis.hara;

import java.util.*;

import static java.lang.Math.min;

public class App {
    private static final int C = 3;
    private static final int P = 4;
    private static Service service = new Service();

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter a prices line");
        Integer[][] prices = new Integer[C][P];
        Price[] goodPrices = new Price[C * P];
        for (int i = 0; i < C; i++) {
            for (int j = 0; j < P; j++) {
                prices[i][j] = reader.nextInt();
                Price price = new Price();
                price.cost = prices[i][j];
                price.consumer = i;
                price.producer = j;
                goodPrices[i * P + j] = price;
            }
        }
        Arrays.sort(goodPrices, new SortByCost());
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

        int basePointsCount = 0;
        int emptyStockCount = 0;
        int index = 0;
        while (emptyStockCount < (C + P)) {
            Price price = goodPrices[index];
            if ((consumers[price.consumer] != 0) && (producers[price.producer] != 0)) {
                int basePointValue = min(consumers[price.consumer], producers[price.producer]);
                producers[price.producer] -= basePointValue;
                consumers[price.consumer] -= basePointValue;
                if (producers[price.producer] == 0) {
                    emptyStockCount++;
                }

                if (consumers[price.consumer] == 0) {
                    emptyStockCount++;
                }


                prices[price.consumer][price.producer] = -basePointValue;

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

        ArrayList<Graph> graphs = new ArrayList<>();
        for (Vertex v : basePointsPr.values()) {
            if (v.hasGraph()) {
                continue;
            }
            v.graph = new Graph();
            graphs.add(v.graph);
            service.search(v, v);
        }

        for (int i = 0; i < C; i++) {
            for (int j = 0; j < P; j++) {
                System.out.print(prices[i][j] + "   | ");
            }
            System.out.println();
        }
        for (Graph g : graphs) {
            for (Vertex p : g.producers) {
                System.out.print("p   " + p.position);
            }
            System.out.println();
            for (Vertex c : g.consumers) {
                System.out.print("c   " + c.position);
            }
            System.out.println("   -------    ");
        }

        index = 0;
        while (basePointsCount < P + C - 1) {
            if ((prices[goodPrices[index].consumer][goodPrices[index].producer] == null) ||
                    (prices[goodPrices[index].consumer][goodPrices[index].producer] < 0)) {
                index++;
                continue;
            }
            Graph graph1 = basePointsCon.get("c" + goodPrices[index].consumer).graph.main;
            Graph graph2 = basePointsPr.get("p" + goodPrices[index].producer).graph.main;
            if (graph1 == graph2) {
                index++;
                continue;
            }

            for (Vertex c1 : graph1.consumers)
                for (Vertex p2 : graph2.producers) {
                    if (prices[c1.position][p2.position] > 0)
                        prices[c1.position][p2.position] = null;
                }
            for (Vertex c2 : graph2.consumers)
                for (Vertex p1 : graph1.producers) {
                    if (prices[c2.position][p1.position] > 0)
                        prices[c2.position][p1.position] = null;
                }
            prices[goodPrices[index].consumer][goodPrices[index].producer] = 0;
            graph1.concatenation(graph1, graph2);
            index++;
            basePointsCount++;
        }

        for (int i = 0; i < C; i++) {
            for (int j = 0; j < P; j++) {
                System.out.print(prices[i][j] + "   | ");
            }
            System.out.println();
        }
        for (Graph g : graphs) {
            for (Vertex p : g.producers) {
                System.out.print("p   " + p.position);
            }
            System.out.println();
            for (Vertex c : g.consumers) {
                System.out.print("c   " + c.position);
            }
            System.out.println("   -------    ");
        }
    }

}
