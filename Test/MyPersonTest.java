import com.oocourse.spec3.main.Message;
import main.MyMessage;
import main.MyPerson;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyPersonTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        MyPerson myPerson = new MyPerson(2,"hsuirf",318);
        MyMessage message1 = new MyMessage(1,3,myPerson,myPerson);
        MyMessage message2 = new MyMessage(2,3,myPerson,myPerson);
        MyMessage message3 = new MyMessage(3,3,myPerson,myPerson);
        MyMessage message4 = new MyMessage(4,3,myPerson,myPerson);
        MyMessage message5 = new MyMessage(5,3,myPerson,myPerson);
        MyMessage message6 = new MyMessage(6,3,myPerson,myPerson);
        myPerson.addMessageHead(message1);
        myPerson.addMessageHead(message2);
        List<Message> messageList = myPerson.getReceivedMessages();
        assertSame(messageList.get(0),message2);
        assertSame(messageList.get(1),message1);
        myPerson.addMessageHead(message3);
        myPerson.addMessageHead(message4);
        messageList = myPerson.getReceivedMessages();
        assertSame(messageList.get(0),message4);
        assertSame(messageList.get(1),message3);
        myPerson.addMessageHead(message5);
        myPerson.addMessageHead(message6);
        messageList = myPerson.getReceivedMessages();
        assertEquals(messageList.size(),4);
        assertSame(messageList.get(0),message6);
        assertSame(messageList.get(1),message5);
        assertSame(messageList.get(2),message4);
        assertSame(messageList.get(3),message3);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void getReceivedMessages() {
    }

    @org.junit.jupiter.api.Test
    void addMessageHead() {
    }
}