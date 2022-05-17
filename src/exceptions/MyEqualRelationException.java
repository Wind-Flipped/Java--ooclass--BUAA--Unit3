package exceptions;

import com.oocourse.spec3.exceptions.EqualRelationException;

import java.util.HashMap;

public class MyEqualRelationException extends EqualRelationException {
    private static int numAll = 0;
    private static HashMap<Integer, Integer> numPerson = new HashMap<>();

    private final int id1;
    private final int id2;
    //@ invariant id1 <= id2;

    public MyEqualRelationException(int id1, int id2) {
        if (id1 <= id2) {
            this.id1 = id1;
            this.id2 = id2;
        } else {
            this.id1 = id2;
            this.id2 = id1;
        }
        if (id1 == id2) {
            if (numPerson.containsKey(id1)) {
                numPerson.replace(id1, numPerson.get(id1) + 1);
            } else {
                numPerson.put(id1, 1);
            }
        } else {
            if (numPerson.containsKey(id1)) {
                numPerson.replace(id1, numPerson.get(id1) + 1);
            } else {
                numPerson.put(id1, 1);
            }
            if (numPerson.containsKey(id2)) {
                numPerson.replace(id2, numPerson.get(id2) + 1);
            } else {
                numPerson.put(id2, 1);
            }
        }
        numAll++;
    }

    public void print() {
        System.out.println("er-" + numAll + ", " + id1 + "-" + numPerson.get(id1)
                + ", " + id2 + "-" + numPerson.get(id2));
    }
}
