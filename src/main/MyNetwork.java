package main;

import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.NoticeMessage;
import exceptions.MyEqualPersonIdException;
import exceptions.MyEqualRelationException;
import exceptions.MyPersonIdNotFoundException;
import exceptions.MyRelationNotFoundException;
import exceptions.MyEqualGroupIdException;
import exceptions.MyGroupIdNotFoundException;
import exceptions.MyEqualMessageIdException;
import exceptions.MyMessageIdNotFoundException;
import exceptions.MyEmojiIdNotFoundException;
import exceptions.MyEqualEmojiIdException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;

public class MyNetwork implements Network {
    private final ArrayList<Person> people;
    private final ArrayList<Group> groups;
    private final ArrayList<Message> messages;
    private final HashMap<Integer, Integer> emojiIdList;
    private final ArrayList<ArrayList<Integer>> peopleBlock;
    private int blocks;
    private final ArrayList<ArrayList<Integer>> blockPeople;

    public MyNetwork() {
        people = new ArrayList<>();
        groups = new ArrayList<>();
        messages = new ArrayList<>();
        emojiIdList = new HashMap<>();
        peopleBlock = new ArrayList<>(new ArrayList<>());
        blocks = 0;
        blockPeople = new ArrayList<>();
    }

    public boolean contains(int id) {
        for (Person person : people) {
            if (person.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public Person getPerson(int id) {
        for (Person person : people) {
            if (person.getId() == id) {
                return person;
            }
        }
        return null;
    }

    public void addPerson(Person person) throws EqualPersonIdException {
        if (people.contains(person)) {
            throw new MyEqualPersonIdException(person.getId());
        } else {
            people.add(person);
            blocks++;
            ArrayList<Integer> newBlock = new ArrayList<>();
            newBlock.add(person.getId());
            peopleBlock.add(newBlock);
        }
    }

    public int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        } else {
            return getPerson(id1).queryValue(getPerson(id2));
        }
    }

    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualRelationException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1, id2);
        } else {
            ((MyPerson) getPerson(id1)).addAcquaintance(getPerson(id2), value);
            ((MyPerson) getPerson(id2)).addAcquaintance(getPerson(id1), value);
            int[] index = getIndex(id1, id2);
            if (index[0] != index[1]) {
                peopleBlock.get(index[0]).addAll(peopleBlock.get(index[1]));
                blocks--;
                peopleBlock.remove(index[1]);
            }
            for (Group group : groups) {
                if (group.hasPerson(getPerson(id1)) && group.hasPerson(getPerson(id2))) {
                    ((MyGroup) group).addValueSum(value);
                }
            }
        }
    }

    public int queryPeopleSum() {
        return people.size();
    }

    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else {
            int[] index = getIndex(id1, id2);
            return index[0] == index[1];
        }
    }

    public int queryBlockSum() {
        return blocks;
    }

    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.contains(group)) {
            throw new MyEqualGroupIdException(group.getId());
        }
        groups.add(group);
    }

    public Group getGroup(int id) {
        for (Group group : groups) {
            if (group.getId() == id) {
                return group;
            }
        }
        return null;
    }

    public void addToGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        if (getGroup(id2) == null) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        } else {
            if (getGroup(id2).getSize() < 1111) {
                getGroup(id2).addPerson(getPerson(id1));
            }
        }
    }

    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (getGroup(id2) == null) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        } else {
            getGroup(id2).delPerson(getPerson(id1));
        }
    }

    public boolean containsMessage(int id) {
        for (Message message : messages) {
            if (message.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public void addMessage(Message message) throws
            EqualMessageIdException, EqualPersonIdException, EmojiIdNotFoundException {
        if (messages.contains(message)) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (message instanceof EmojiMessage &&
                !containsEmojiId(((EmojiMessage) message).getEmojiId())) {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
        } else if (message.getType() == 0 && message.getPerson1() == message.getPerson2()) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        } else {
            messages.add(message);
        }
    }

    public Message getMessage(int id) {
        if (containsMessage(id)) {
            for (Message message : messages) {
                if (message.getId() == id) {
                    return message;
                }
            }
        }
        return null;
    }

    public void sendMessage(int id) throws
            RelationNotFoundException, MessageIdNotFoundException, PersonIdNotFoundException {
        if (!containsMessage(id)) {
            throw new MyMessageIdNotFoundException(id);
        } else if (getMessage(id).getType() == 0 &&
                !(getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2()))) {
            throw new MyRelationNotFoundException(getMessage(id).getPerson1().getId(),
                    getMessage(id).getPerson2().getId());
        } else if (getMessage(id).getType() == 1 &&
                !(getMessage(id).getGroup().hasPerson(getMessage(id).getPerson1()))) {
            throw new MyPersonIdNotFoundException(getMessage(id).getPerson1().getId());
        } else if (getMessage(id).getType() == 0) {
            getMessage(id).getPerson1().addSocialValue(getMessage(id).getSocialValue());
            getMessage(id).getPerson2().addSocialValue(getMessage(id).getSocialValue());
            if (getMessage(id) instanceof RedEnvelopeMessage) {
                getMessage(id).getPerson1().addMoney(
                        -((RedEnvelopeMessage) getMessage(id)).getMoney());
                getMessage(id).getPerson2().addMoney(
                        ((RedEnvelopeMessage) getMessage(id)).getMoney());
            } else if (getMessage(id) instanceof EmojiMessage) {
                emojiIdList.put(((EmojiMessage) getMessage(id)).getEmojiId(),
                        emojiIdList.get(((EmojiMessage) getMessage(id)).getEmojiId()) + 1);
            }
            ((MyPerson) getMessage(id).getPerson2()).addMessageHead(getMessage(id));
            messages.remove(getMessage(id));
        } else if (getMessage(id).getType() == 1) {
            for (Person person : people) {
                if (getMessage(id).getGroup().hasPerson(person)) {
                    person.addSocialValue(getMessage(id).getSocialValue());
                }
            }
            if (getMessage(id) instanceof RedEnvelopeMessage) {
                int i = ((RedEnvelopeMessage) getMessage(id)).getMoney()
                        / getMessage(id).getGroup().getSize();
                getMessage(id).getPerson1().addMoney(
                        -i * getMessage(id).getGroup().getSize());
                for (Person person : people) {
                    if (getMessage(id).getGroup().hasPerson(person)) {
                        person.addMoney(i);
                    }
                }
            } else if (getMessage(id) instanceof EmojiMessage) {
                emojiIdList.put(((EmojiMessage) getMessage(id)).getEmojiId(),
                        emojiIdList.get(((EmojiMessage) getMessage(id)).getEmojiId()) + 1);
            }
            messages.remove(getMessage(id));
        }
    }

    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getSocialValue();
    }

    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getReceivedMessages();
    }

    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        for (Group group : groups) {
            if (group.getId() == id) {
                return group.getSize();
            }
        }
        throw new MyGroupIdNotFoundException(id);
    }

    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        for (Group group : groups) {
            if (group.getId() == id) {
                return group.getValueSum();
            }
        }
        throw new MyGroupIdNotFoundException(id);
    }

    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        for (Group group : groups) {
            if (group.getId() == id) {
                return group.getAgeVar();
            }
        }
        throw new MyGroupIdNotFoundException(id);
    }

    public int queryLeastConnection(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        int sum = 0;
        ArrayList<Edge> edges = new ArrayList<>();
        blockPeople.clear();
        for (int i = 0; i < blocks; i++) {
            if (peopleBlock.get(i).contains(id)) {
                //System.out.println(peopleBlock.get(i));
                for (Integer integer : peopleBlock.get(i)) {
                    ArrayList<Integer> temp = new ArrayList<>();
                    temp.add(integer);
                    blockPeople.add(temp);
                }

                for (int j : peopleBlock.get(i)) {
                    addEdges(edges, j);
                }
                Collections.sort(edges);
                while (!edges.isEmpty()) {
                    Edge edge = edges.get(0);
                    edges.remove(0);
                    int[] index = getIndex(edge.getDot1(), edge.getDot2());
                    if (index[0] == index[1]) {
                        continue;
                    } else {
                        blockPeople.get(index[0]).addAll(blockPeople.get(index[1]));
                        blockPeople.remove(index[1]);
                    }
                    sum += edge.getValue();
                    if (blockPeople.size() == 1) {
                        break;
                    }
                }
                return sum;
            }
        }
        return sum;
    }

    //if it can go on, return true
    public int[] getIndex(int id1, int id2) {
        int i1 = 0;
        int i2 = 0;
        boolean flag1 = false;
        boolean flag2 = false;
        int[] index = new int[2];
        int size = blockPeople.size();
        for (int i = 0; i < size && (!flag1 || !flag2); i++) {
            if (!flag1 && blockPeople.get(i).contains(id1)) {
                i1 = i;
                flag1 = true;
            }
            if (!flag2 && blockPeople.get(i).contains(id2)) {
                i2 = i;
                flag2 = true;
            }
        }
        if (i1 <= i2) {
            index[0] = i1;
            index[1] = i2;
        } else {
            index[0] = i2;
            index[1] = i1;
        }
        return index;
    }

    public boolean containsEmojiId(int id) {
        return emojiIdList.containsKey(id);
    }

    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (containsEmojiId(id)) {
            throw new MyEqualEmojiIdException(id);
        }
        emojiIdList.put(id, 0);
    }

    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getMoney();
    }

    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!containsEmojiId(id)) {
            throw new MyEmojiIdNotFoundException(id);
        }
        return emojiIdList.get(id);
    }

    public int deleteColdEmoji(int limit) {
        Iterator<Map.Entry<Integer, Integer>> entries = emojiIdList.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, Integer> entry = entries.next();
            if (entry.getValue() < limit) {
                messages.remove(getMessage(entry.getKey()));
                entries.remove();
            }
        }
        return emojiIdList.size();
    }

    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (!contains(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }

        getPerson(personId).getMessages().removeIf(iter -> iter instanceof NoticeMessage);
    }

    public int sendIndirectMessage(int id) throws
            MessageIdNotFoundException {
        if (!containsMessage(id) ||
                containsMessage(id) && getMessage(id).getType() == 1) {
            throw new MyMessageIdNotFoundException(id);
        }

        try {
            if (!isCircle(getMessage(id).getPerson1().getId(),
                    getMessage(id).getPerson2().getId())) {
                return -1;
            }
        } catch (PersonIdNotFoundException e) {
            e.printStackTrace();
        }
        Message message = getMessage(id);
        try {
            sendMessage(id);
        } catch (RelationNotFoundException | PersonIdNotFoundException e) {
            e.printStackTrace();
        }
        final int id1 = message.getPerson1().getId();
        final int id2 = message.getPerson2().getId();
        ArrayList<Integer> node = new ArrayList<>();
        HashMap<Integer, Integer> path = new HashMap<>();
        ArrayList<Edge> edges = new ArrayList<>();
        node.add(id1);
        path.put(id1, 0);
        addEdges(edges, id1);
        while (!node.contains(id2)) {
            Collections.sort(edges);
            Iterator<Edge> iters = edges.iterator();
            while (iters.hasNext()) {
                Edge iter = iters.next();
                int i1 = iter.getDot1();
                int i2 = iter.getDot2();
                if (!node.contains(i1) && node.contains(i2)) {
                    node.add(i1);
                    path.put(i1, path.get(i2) + iter.getValue());
                    addEdges(edges, i1);
                    break;
                } else if ((node.contains(i1) && !node.contains(i2))) {
                    node.add(i2);
                    path.put(i2, path.get(i1) + iter.getValue());
                    addEdges(edges, i2);
                    break;
                } else {
                    iters.remove();
                }
            }
        }
        return path.get(id2);
    }

    public void addEdges(ArrayList<Edge> edges, int personId) {
        for (Map.Entry<Person, Integer> entry :
                ((MyPerson) getPerson(personId)).getAcquaintances().entrySet()) {
            Edge edge = new Edge(personId, entry.getKey().getId(), entry.getValue());
            edges.add(edge);
        }
    }
}
