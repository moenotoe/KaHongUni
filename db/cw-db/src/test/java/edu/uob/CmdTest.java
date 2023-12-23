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
import java.util.List;

public class CmdTest {

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
    public void testCreate1() {
        String randomName = generateRandomName();
        String response2=sendCommandToServer("CREATE DATABASE " + randomName + ";");
        System.out.println(response2);
        assertTrue(response2.contains("[OK]"), "A valid query was made");

        sendCommandToServer("CREATE DATABASE " + randomName + ";");
        String response5=sendCommandToServer("CREATE DATABASE ;");
        assertTrue(response5.contains("[ERROR]"),"missing dbname");
        String response6=sendCommandToServer("USE     " + randomName + ";");
        assertTrue(response6.contains("[OK]"));
        String response1 = sendCommandToServer("USE " + randomName + ";");
        assertTrue(response1.contains("[OK]"), "A valid query was made");
        String response3=sendCommandToServer("use "+"ded"+";");
        assertTrue(response3.contains("[ERROR]"), "Wrong");
        //repeat create the same name for db
        sendCommandToServer("CREATE DATABASE " + "dede" + ";");
        String response4=sendCommandToServer("CREATE DATABASE "+"dede"+";");
        assertTrue(response4.contains("[ERROR]"), "repeat create Db");
        sendCommandToServer("use "+"dede"+";");

    }

    @Test
    public void testCreate2() throws Exception {
        //test create table with attribute
        sendCommandToServer("Use dede;");
        String respo1=sendCommandToServer("create table test1(name,mark,address);");
        assertTrue(respo1.contains("[OK]"));
        //test read stream
        ExeCom testRead=new ExeCom();
        //read the attributes
        String PathStr=Paths.get("databases").toAbsolutePath().toString()+File.separator+server.getCurDb().toString()+File.separator+"test1"+".tab";
        File newPath=new File(Paths.get("databases").toAbsolutePath().toString()+File.separator+server.getCurDb().toString()+File.separator+"test1"+".tab");
        Table attri1=new Table(newPath);
        String attr1t=attri1.inputFromFile(newPath);
        attri1.oriStringToAttri(attr1t);
        List<String> str1=attri1.getAttributes();
        assertTrue(str1.contains("name")&&str1.contains("mark")&&str1.contains("address"));

        //just command line
        String response1=sendCommandToServer("CREATE TABLE test2 (name, mark, pass);");
        assertTrue(response1.contains("[OK]"));
        String response2=sendCommandToServer("CREATE TABLE test3 (mark2.name, age, mark2.pass);");
        assertTrue(response2.contains("[ERROR]"));
        String response3=sendCommandToServer("CREATE TABLE mark2 (mark2.name, , age, mark2.pass);");
        assertTrue(response3.contains("[ERROR]"));
        String response4=sendCommandToServer("CREATE TABLE test3 (mark2.name age , mark2.pass);");
        assertTrue(response4.contains("[ERROR]"));
        String response7=sendCommandToServer("create table test4 (name,.**$,age);");
        assertTrue(response7.contains("[ERROR]"));
        String response8=sendCommandToServer("create table test5 (,name,age,**);");
        assertTrue(response8.contains("[ERROR]"));
        String response9=sendCommandToServer("create table test5 (name,name,age);");
        assertTrue(response9.contains("[ERROR]"));
        String response10=sendCommandToServer("create abc test6 (name,name,age);");
        assertTrue(response10.contains("[ERROR]"));

        //attri name can not repeat, (tab.name) can not use another table's name
        //EX:create table taName(taName.att1, taName,att2);
        sendCommandToServer("create table test1 (Name,Age,occupation);");
        String response5=sendCommandToServer("insert into test1 values('a','b','c');");
        assertTrue(response5.contains("[OK]"));
        String response6=sendCommandToServer("insert into test1 values(d,e,f);");
        assertTrue(response6.contains("[ERROR]"));
        String response11=sendCommandToServer("create table test5 (test5.name,test5.age,address);");
        System.out.println(response11);
        assertTrue(response11.contains("[OK]"));
        String response12=sendCommandToServer("create table test6 (name,name,name)");
        assertTrue(response12.contains("[ERROR]"));
    }
    @Test
    public void testInsert() throws Exception {
        //test oriString to row
        sendCommandToServer("use dede;");
        sendCommandToServer("create table insertTest (Name,Age,occupation);");
        String response1=sendCommandToServer("insert into insertTest VALUES('baba,22,doctor');");
        assertTrue(response1.contains("[OK]"));
        String response2=sendCommandToServer("insert INTO insertTest values(baba,22,student);");
        assertTrue(response2.contains("[ERROR]"));
        String response3=sendCommandToServer("insert INTO insertTest values(NULL, 22, 'student');");
        System.out.println(response3);
        assertTrue(response3.contains("[OK]"));
        String response4=sendCommandToServer("insert INTO insertTest values('name',52.2,FALSE);");
        assertTrue(response4.contains("[OK]"));
        String response5=sendCommandToServer("insert INTO iAmNotATable values('name',22,'student');");
        assertTrue(response5.contains("[ERROR]"));
        String response6=sendCommandToServer("insert INTO insertTest values('name',-52.1, 3.2);");
        System.out.println(response6);
        assertTrue(response6.contains("[OK]"));
        String response7=sendCommandToServer("insert INTO insertTest values('name' , 263 , 'student');");
        assertTrue(response7.contains("[OK]"));
        System.out.println(response7);
        String response8=sendCommandToServer("insert INTO insertTest values(^&%^%& , +23 , 'student');");
        assertTrue(response8.contains("[ERROR]"));
        //test insert
    }
    @Test
    public void testAlter() throws Exception{
        String trunDB=sendCommandToServer("Use dede;");
        System.out.println(trunDB);
        assertTrue(trunDB.contains("[OK]"));
        String createTable=sendCommandToServer("create table testAlter;");
        System.out.println(createTable);
        assertTrue(createTable.contains("[OK]"));
        //Table names:case insensitive
        String response1=sendCommandToServer("alter table testAlter add Name;");
        assertTrue(response1.contains("[OK]"));
        String response2=sendCommandToServer("alter table testAlter add *&^*&;");
        assertTrue(response2.contains("[ERROR]"));
        String response3=sendCommandToServer("alter table testAlter add age;");
        assertTrue(response3.contains("[OK]"));
        String response10=sendCommandToServer("alter table testAlter add age;");
        assertTrue(response10.contains("[ERROR]"),"repeat add attribute");
        String response4=sendCommandToServer("alter table testAlter drop Name;");
        assertTrue(response4.contains("[OK]"));
        String response5=sendCommandToServer("alter table testAlter drop Name;");
        assertTrue(response5.contains("[ERROR]"),"drop without the attribute");
        String response6=sendCommandToServer("alter table testAlter add a^ge;");
        assertTrue(response6.contains("[ERROR]"));
        String response7=sendCommandToServer("alter table testAlter nope age;");
        assertTrue(response7.contains("[ERROR]"));
        String response8=sendCommandToServer("alter table noThisTable add age;");
        assertTrue(response8.contains("[ERROR]"));
        String response9=sendCommandToServer("aalter table testAlter add age;");
        assertTrue(response9.contains("[ERROR]"));

        String response12=sendCommandToServer("create table people(Name,Age,Email);");
        assertTrue(response12.contains("[OK]"));
        sendCommandToServer("insert into  PEOPLE values('Bob' , 21 , 'bob@bob.net');");
        sendCommandToServer("insert into  PEOPLE values('Harry' , 32 , 'harry@harry.com');");
        sendCommandToServer("insert into  PEOPLE values('Chris' , 42 , 'chris@chris.ac.uk');");
        String response13=sendCommandToServer("SELECT * FROM PEOPLE;");
        assertTrue(response13.contains("bob@bob.net"));
        assertTrue(response13.contains("harry@harry.com"));
        assertTrue(response13.contains("chris@chris.ac.uk"));
        //drop the Email column
        String response11=sendCommandToServer("alter table people drop Email;");
        assertFalse(response11.contains("Email"));
        assertFalse(response11.contains("bob@bob.net"));
        assertFalse(response11.contains("harry@harry.com"));
        assertFalse(response11.contains("chris@chris.ac.uk"));
        String response14=sendCommandToServer("alter table people drop id;");
        System.out.println(response14);
    }
    @Test
    public void testBasicSelect() throws Exception{
        sendCommandToServer("Use dede;");
        String response12=sendCommandToServer("create table peoplea(Name,Age,Email);");
        assertTrue(response12.contains("[OK]"));
        sendCommandToServer("insert into  peoplea values('Bob' , 21 , 'bob@bob.net');");
        sendCommandToServer("insert into  peoplea values('Harry' , 32 , 'harry@harry.com');");
        sendCommandToServer("insert into  peoplea values('Chris' , 42 , 'chris@chris.ac.uk');");
        //test invalid wildAttri
        String parser=sendCommandToServer("select name,age from peoplea where Age>=30;");
        System.out.println(parser);
        String test1=sendCommandToServer("select Name, Age , people.Email ,p from peoplea;");
        assertTrue(test1.contains("[OK]"));
        String test2=sendCommandToServer("SELECT name,age from peoplea Age>5;");
        assertTrue(test2.contains("[ERROR]"));

        //column names as case insensitive for querying,preserve the case when storing them
        String test3=sendCommandToServer("select nAme,aGe from PEOPLEA;");
        System.out.println(test3);
        assertTrue(test3.contains("Name"));
        assertTrue(test3.contains("Bob"));
        assertTrue(test3.contains("21"));

        String test4=sendCommandToServer("SELECT Age FROM peopleA where age>5;");
        assertTrue(test4.contains("[ERROR]"));
        String test5=sendCommandToServer("select * for peopleA;");
        assertTrue(test5.contains("[ERROR]"));
        String test6=sendCommandToServer("select Age from iamnotatable;");
        assertTrue(test6.contains("[ERROR]"));
        String test7=sendCommandToServer("selectname,agefromPEOPLEa;");
        assertTrue(test7.contains("[ERROR]"));
        String test8=sendCommandToServer("select name,age frompeopleA;");
        assertTrue(test8.contains("[ERROR]"));
        String test9=sendCommandToServer("select * from peoplea where (age>5;");
        assertTrue(test9.contains("[ERROR]"));
        String test10=sendCommandToServer("select * from peopleA where age>5);");
        assertTrue(test10.contains("[ERROR]"));
    }
}
