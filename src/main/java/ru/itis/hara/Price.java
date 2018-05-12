package ru.itis.hara;

import java.util.Comparator;

public class Price {
    public int cost;
    public int consumer;
    public int producer;

    public Price(int cost, int consumer, int producer) {
        this.cost = cost;
        this.consumer = consumer;
        this.producer = producer;
    }
}
class SortByCost implements Comparator<Price>
{
    public int compare(Price a, Price b)
    {
        return a.cost - b.cost;
    }
}
