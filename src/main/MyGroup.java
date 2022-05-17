package main;

import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

import java.util.ArrayList;
import java.util.Objects;

public class MyGroup implements Group {
    private final int id;
    private final ArrayList<Person> people;
    private int sum;
    private int squareSum;
    private int size;
    private int valueSum;

    public MyGroup(int id) {
        this.id = id;
        people = new ArrayList<>();
        sum = 0;
        squareSum = 0;
        size = 0;
        valueSum = 0;
    }

    public int getId() {
        return id;
    }

    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Group) {
            return (((Group) obj).getId() == id);
        } else {
            return false;
        }
    }

    public boolean hasPerson(Person person) {
        return people.contains(person);
    }

    public void addPerson(Person person) {
        if (!hasPerson(person)) {
            people.add(person);
            sum += person.getAge();
            squareSum += person.getAge() * person.getAge();
            size += 1;
            for (Person person1:people) {
                if (person.isLinked(person1)) {
                    valueSum += 2 * person.queryValue(person1);
                }
            }
        }
    }

    public void addValueSum(int addSum) {
        valueSum += 2 * addSum;
    }

    @Override
    public int getSize() {
        return people.size();
    }

    @Override
    public int getValueSum() {
        /*
        int sum = 0;
        for (Person person1 : people) {
            for (Person person2 : people) {
                if (person1.isLinked(person2)) {
                    sum += person1.queryValue(person2);
                }
            }
        }
         */
        return valueSum;
    }

    @Override
    public int getAgeMean() {
        if (people.isEmpty()) {
            return 0;
        }
        /*
        int size = people.size();
        int sum = 0;
        for (Person person : people) {
            sum += person.getAge();
        }
         */
        return sum / size;
    }

    @Override
    public int getAgeVar() {
        if (people.isEmpty()) {
            return 0;
        }
        /*
        int ageMean = getAgeMean();
        int sum = 0;
        int size = people.size();
        for (Person person:people) {
            sum += (person.getAge() - ageMean) * (person.getAge() - ageMean);
        }
         */
        return (squareSum - 2 * sum * getAgeMean() + size * getAgeMean() * getAgeMean()) / size;
    }

    @Override
    public void delPerson(Person person) {
        if (hasPerson(person)) {
            people.remove(person);
            sum -= person.getAge();
            squareSum -= person.getAge() * person.getAge();
            size -= 1;
            for (Person person1:people) {
                if (person.isLinked(person1)) {
                    valueSum -= 2 * person.queryValue(person1);
                }
            }
        }
    }
}
