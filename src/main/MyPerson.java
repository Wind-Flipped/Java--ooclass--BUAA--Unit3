package main;

import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private final HashMap<Person, Integer> acquaintances;
    private int money;
    private int socialValue;
    private ArrayList<Message> messages;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        acquaintances = new HashMap<>();
        money = 0;
        socialValue = 0;
        messages = new ArrayList<>();
    }

    public MyPerson(Person person) {
        this.id = person.getId();
        this.name = person.getName();
        this.age = person.getAge();
        acquaintances = new HashMap<>();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyPerson)) {
            return false;
        }
        MyPerson myPerson = (MyPerson) o;
        return id == myPerson.id;
    }

    public HashMap<Person, Integer> getAcquaintances() {
        return acquaintances;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean isLinked(Person person) {
        return person.getId() == id || acquaintances.containsKey(person);
    }

    public int queryValue(Person person) {
        return acquaintances.getOrDefault(person, 0);
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    public void addAcquaintance(Person person, int value) {
        acquaintances.put(person, value);
    }

    public ArrayList<Person> getPersonList() {
        return new ArrayList<>(acquaintances.keySet());
    }

    public void addSocialValue(int num) {
        socialValue += num;
    }

    public int getSocialValue() {
        return socialValue;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<Message> getReceivedMessages() {
        //取最近4条消息
        ArrayList<Message> receivedMessages = new ArrayList<>();
        for (int i = 0; i < messages.size() && i <= 3; i += 1) {
            receivedMessages.add(messages.get(i));
        }
        return receivedMessages;
    }

    public void addMoney(int num) {
        money += num;
    }

    public int getMoney() {
        return money;
    }

    public void addMessageHead(Message message) {
        ArrayList<Message> addMessage = new ArrayList<>();
        addMessage.add(message);
        messages.addAll(0, addMessage);
    }
}
