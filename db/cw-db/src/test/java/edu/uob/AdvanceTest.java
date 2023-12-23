package edu.uob;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class AdvanceTest {

    private DBServer server;

    // Create a new server _before_ every @Test
    @BeforeEach
    public void setup() {
        server = new DBServer();
        sendCommandToServer("CREATE DATABASE DEDE;");
    }

    // Random name generator - useful for testing "bare earth" queries (i.e. where tables don't previously exist)
    private String generateRandomName()
    {
        String randomName = "";
        for(int i=0; i<10 ;i++) randomName += (char)( 97 + (Math.random() * 25.0));
        return randomName;
    }

    private String sendCommandToServer(String command) {
        // Try to send a command to the server - this call will timeout if it takes too long (in case the server enters an infinite loop)
        return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> { return server.handleCommand(command);},
                "Server took too long to respond (probably stuck in an infinite loop)");
    }

    // A basic test that creates a database, creates a table, inserts some test data, then queries it.
    // It then checks the response to see that a couple of the entries in the table are returned as expected
    @Test
    public void condition1() throws Exception {

//        //use people example to test
        sendCommandToServer("use dede;");
        String PathStr=Paths.get("databases").toAbsolutePath().toString()+File.separator+server.getCurDb().toString()+File.separator+"people"+".tab";
        File newPath=new File(Paths.get("databases").toAbsolutePath().toString()+File.separator+server.getCurDb().toString()+File.separator+"people"+".tab");
        Table tab1=new Table(newPath);

        sendCommandToServer("create table officecontect(Name,Age,Email,Gender,Food);");
        sendCommandToServer("insert into  officeContect values('Bob' , 21 , 'bob@bob.net', 'M','apple');");
        sendCommandToServer("insert into  offIceContect values('Harry' , 32 , 'harry@harry.com','M','banana');");
        sendCommandToServer("insert into  officecontect values('Chris' , 42 , 'chris@chris.ac.uk','M','banana');");
        sendCommandToServer("insert into  officeContect values('Cherry' , 25 , 'Cherry@Cherry.ac.uk','F','tomato');");
        sendCommandToServer("insert into  officeContect values('Ken' , 24 , 'dede@dede.ac.uk','F','orange');");
        String test4=sendCommandToServer("select * FROM offIceContect where (Name == 'Bob');");
        assertTrue(test4.contains("[OK]"));
        assertTrue(test4.contains("Bob"));
        assertTrue(test4.contains("21"));
        assertTrue(test4.contains("M"));
        assertFalse(test4.contains("'M'"));
        String test1=sendCommandToServer("select * from offIceContect where id==2;");
        assertTrue(test1.contains("Harry"));
        assertTrue(test1.contains("32"));
        assertTrue(test1.contains("harry@harry.com"));
        String test2=sendCommandToServer("SELECT * FROM officeContect;");
        assertTrue(test2.contains("Bob"));
        String test3=sendCommandToServer("select ID from officeContect;");
        assertTrue(test3.contains("id"));
        assertTrue(test3.contains("1"));
        assertTrue(test3.contains("2"));
        assertTrue(test3.contains("3"));
        assertFalse(test3.contains("Name"));
        assertFalse(test3.contains("Age"));
        assertFalse(test3.contains("Email"));
        //invalid compare
        String test5=sendCommandToServer("select Name from officeContect where Age >= 'Bob';");
        assertFalse(test5.contains("Bob"));
        assertFalse(test5.contains("Age"));
        assertTrue(test5.contains("[OK]"));
    }

    @Test
    public void compareTest() throws Exception {
        sendCommandToServer("use dede;");
        String PathStr=Paths.get("databases").toAbsolutePath().toString()+File.separator+server.getCurDb().toString()+File.separator+"people"+".tab";
        File newPath=new File(Paths.get("databases").toAbsolutePath().toString()+File.separator+server.getCurDb().toString()+File.separator+"people"+".tab");
        String test1=sendCommandToServer("select * from people where Age >= 32.2; ");
        sendCommandToServer("create table comparetest1(Name,Age,Email,Department);");
        sendCommandToServer("insert INTO comparetest1 VALUES('dede',7,'dede@gmail.com','chief');");
        sendCommandToServer("INSERT into comparetest1 values('baba','23','baba@gmail.com','chief');");
        sendCommandToServer("insert into comparetest1 values('mama','23','mama@gmail.com','doctor');");

        String test2=sendCommandToServer("SELECT * FROM comparetest1 WHERE Age>7;");
        assertFalse(test2.contains("dede"));
        String test3=sendCommandToServer("select * from comparetest1 where Age>=7;");
        assertTrue(test3.contains("dede"));
        String test4=sendCommandToServer("select * from comparetest1 where Department=='chief';");
        assertTrue(test4.contains("baba"));
        assertFalse(test4.contains("mama"));

        //error inquiry
        String test6=sendCommandToServer("select * from comparetest1 where Age>>7;");
        assertTrue(test6.contains("[ERROR]"));
        String test7=sendCommandToServer("select * from comparetest1 where age>7;");
        assertTrue(test7.contains("[ERROR]"));
        String test8=sendCommandToServer("select Age from comparetest1 where Age>7;");
        assertTrue(test8.contains("23"));
        String test9=sendCommandToServer("select * from comparetest1 where Department!='chief';");
        assertFalse(test9.contains("baba"));
        assertFalse(test9.contains("dede"));
        assertTrue(test9.contains("mama"));

    }

    @Test
    public void multiSelect() throws Exception {
        sendCommandToServer("use dede;");
        sendCommandToServer("create table multiTest(Name,Age,Email,Gender,Food);");
        sendCommandToServer("insert into  multiTest values('Bob' , 21 , 'bob@bob.net', 'M','apple');");
        sendCommandToServer("insert into  multiTest values('Harry' , 32 , 'harry@harry.com','M','banana');");
        sendCommandToServer("insert into  multiTest values('Chris' , 42 , 'chris@chris.ac.uk','M','banana');");
        sendCommandToServer("insert into  multiTest values('Cherry' , 25 , 'Cherry@Cherry.ac.uk','F','tomato');");
        sendCommandToServer("insert into  multiTest values('Ken' , 24 , 'dede@dede.ac.uk','F','orange');");

        String response1=sendCommandToServer("SELECT * FROM multitest where Food=='banana' and Age==32;");
        assertTrue(response1.contains("[OK]"));
        assertTrue(response1.contains("Harry"));
        assertFalse(response1.contains("Chris"));

        String response2=sendCommandToServer("SELECT * FROM multitest where (Food=='banana')or(Name=='Ken');");
        assertTrue(response2.contains("[OK]"));
        assertTrue(response2.contains("Ken"));
        assertTrue(response2.contains("Harry"));
        assertTrue(response2.contains("Chris"));
        assertFalse(response2.contains("Cherry"));
        assertFalse(response2.contains("tomato"));

        String response3=sendCommandToServer("SELECT id FROM multitest WHERE (Food=='banana')AND(Age==32);");
        assertTrue(response3.contains("[OK]"));
        assertTrue(response3.contains("id"));
        assertTrue(response3.contains("2"));

        String response4=sendCommandToServer("SELECT * FROM multitest where (Food=='banana(Age==32);");
        assertFalse(response4.contains("Age"));
        assertFalse(response4.contains("banana"));
        assertFalse(response4.contains("Food"));

        String response5=sendCommandToServer("SELECT * FROM multitest where (Food=='banana')AND(gender=='M')AND(Name=='Harry');");
        assertTrue(response5.contains("Harry"));
        assertFalse(response5.contains("Bob"));

    }

    @Test
    public void deleteTest() throws Exception {
        sendCommandToServer("use dede;");
        sendCommandToServer("create table deletetest(Name,Age,Email,Gender,Food);");
        sendCommandToServer("insert into  deletetest values('Bob' , 21 , 'bob@bob.net', 'M','apple');");
        sendCommandToServer("insert into  deletetest values('Harry' , 32 , 'harry@harry.com','M','banana');");
        sendCommandToServer("insert into  deletetest values('Chris' , 42 , 'chris@chris.ac.uk','M','banana');");
        sendCommandToServer("insert into  deletetest values('Cherry' , 25 , 'Cherry@Cherry.ac.uk','F','tomato');");
        sendCommandToServer("insert into  deletetest values('Ken' , 24 , 'dede@dede.ac.uk','F','orange');");
        String response1=sendCommandToServer("DELETE from deletetest where (Age<30);");
        assertTrue(response1.contains("[OK]"));
        String response3=sendCommandToServer("select * from deletetest;");
        assertFalse(response3.contains("Bob"));
        assertFalse(response3.contains("Ken"));
        assertFalse(response3.contains("Cherry"));
        assertTrue(response3.contains("Harry"));
        assertTrue(response3.contains("Chris"));
        String response2=sendCommandToServer("DELETE FROM deletetest where(Age>30);");
        //now the table is nothing
        String response4=sendCommandToServer("select * from deletetest;");
        assertFalse(response4.contains("Harry"));
        assertFalse(response4.contains("Chris"));

    }

    @Test
    public void updateTest() throws Exception {
        sendCommandToServer("use dede;");
        sendCommandToServer("create table updateTest (Name,Age,Email,Gender,Food);");
        sendCommandToServer("insert into  updateTest values('Bob' , 21 , 'bob@bob.net', 'M','apple');");
        sendCommandToServer("insert into  updateTest values('Harry' , 32 , 'harry@harry.com','M','banana');");
        sendCommandToServer("insert into  updateTest values('Chris' , 42 , 'chris@chris.ac.uk','M','banana');");
        sendCommandToServer("insert into  updateTest values('Cherry' , 25 , 'Cherry@Cherry.ac.uk','F','tomato');");
        sendCommandToServer("insert into  updateTest values('Ken' , 24 , 'dede@dede.ac.uk','F','orange');");

        String response1=sendCommandToServer("UPDATE updateTest SET Age=37,Name='dede' WHERE Name=='Bob';");
        assertTrue(response1.contains("[OK]"));
        assertFalse(response1.contains("Bob"));
        assertFalse(response1.contains("21"));

    }
    @Test
    public void JoinTest() throws Exception {
        sendCommandToServer("use dede;");
        sendCommandToServer("create table coursework (task, submission);");
        sendCommandToServer("insert into  coursework values('OXO',3);");
        sendCommandToServer("insert into  coursework values('Db',1);");
        sendCommandToServer("insert into  coursework values('STAG',2);");

        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Steve', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Dave', 55, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Bob', 35, FALSE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Clive', 20, FALSE);");
        String response2=sendCommandToServer("JOIN coursework AND marks ON submission AND id;");
        assertTrue(response2.contains("OK"));
        assertTrue(response2.contains("coursework.task"));
        assertTrue(response2.contains("marks.name"));
        assertFalse(response2.contains("submission"));
        assertTrue(response2.contains("id"));
        assertTrue(response2.contains("1"));
        assertTrue(response2.contains("2"));
        assertTrue(response2.contains("3"));

    }

}
