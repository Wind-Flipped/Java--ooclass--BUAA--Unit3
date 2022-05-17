package exceptions;

import com.oocourse.spec3.exceptions.GroupIdNotFoundException;

import java.util.HashMap;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private static int numAll = 0;
    private static HashMap<Integer, Integer> numPerson = new HashMap<>();

    private final int id;

    public MyGroupIdNotFoundException(int id) {
        this.id = id;
        if (numPerson.containsKey(id)) {
            numPerson.replace(id, numPerson.get(id) + 1);
        } else {
            numPerson.put(id, 1);
        }
        numAll++;
    }

    @Override
    public void print() {
        System.out.println("ginf-" + numAll + ", " + id + "-" + numPerson.get(id));
    }
}
