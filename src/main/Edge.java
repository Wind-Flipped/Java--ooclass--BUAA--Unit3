package main;

public class Edge implements Comparable<Edge> {
    private final int dot1;
    private final int dot2;
    private final int value;

    public Edge(int dot1, int dot2, int value) {
        this.dot1 = dot1;
        this.dot2 = dot2;
        this.value = value;
    }

    public int getDot1() {
        return dot1;
    }

    public int getDot2() {
        return dot2;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Edge o) {
        return this.value - o.value;
    }
}
