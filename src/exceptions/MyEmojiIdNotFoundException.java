package exceptions;

import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;

import java.util.HashMap;

public class MyEmojiIdNotFoundException extends EmojiIdNotFoundException {
    private static int numAll = 0;
    private static HashMap<Integer, Integer> numPerson = new HashMap<>();

    private final int id;

    public MyEmojiIdNotFoundException(int id) {
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
        System.out.println("einf-" + numAll + ", " + id + "-" + numPerson.get(id));
    }
}
