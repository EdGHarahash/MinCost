package ru.itis.hara;

public class Hand {
    public Hand(Integer index) {
        this.index = index;
    }

    public Hand(boolean stat, Integer index) {
        this.stat = stat;
        this.index = index;
    }

    boolean stat = false;
    Integer index;

    @Override
    public String toString(){
        return index.toString();
    }
}
