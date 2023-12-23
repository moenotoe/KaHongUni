package edu.uob;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Paths;
import java.io.IOException;
import java.time.Duration;

class ExampleSTAGTests {

  private GameServer server;

  // Create a new server _before_ every @Test
  @BeforeEach
  void setup() {
      File entitiesFile = Paths.get("config" + File.separator + "extended-entities.dot").toAbsolutePath().toFile();
      File actionsFile = Paths.get("config" + File.separator + "extended-actions.xml").toAbsolutePath().toFile();
      server = new GameServer(entitiesFile, actionsFile);
  }

  String sendCommandToServer(String command) {
      // Try to send a command to the server - this call will timeout if it takes too long (in case the server enters an infinite loop)
      return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> { return server.handleCommand(command);},
      "Server took too long to respond (probably stuck in an infinite loop)");
  }

  // A lot of tests will probably check the game state using 'look' - so we better make sure 'look' works well !
  @Test
  void testLook() {
    String response = sendCommandToServer("simon: look");
    response = response.toLowerCase();
    assertTrue(response.contains("cabin"), "Did not see the name of the current room in response to look");
    assertTrue(response.contains("log cabin"), "Did not see a description of the room in response to look");
    assertTrue(response.contains("magic potion"), "Did not see a description of artifacts in response to look");
    assertTrue(response.contains("wooden trapdoor"), "Did not see description of furniture in response to look");
    assertTrue(response.contains("forest"), "Did not see available paths in response to look");
  }

  // Test that we can pick something up and that it appears in our inventory
  @Test
  void testGet()
  {
      String response;
      sendCommandToServer("simon: get potion");
      response = sendCommandToServer("simon: inv");
      response = response.toLowerCase();
      assertTrue(response.contains("potion"), "Did not see the potion in the inventory after an attempt was made to get it");
      response = sendCommandToServer("simon: look");
      response = response.toLowerCase();
      assertFalse(response.contains("potion"), "Potion is still present in the room after an attempt was made to get it");
  }

  // Test that we can goto a different location (we won't get very far if we can't move around the game !)
  @Test
  void testGoto()
  {
      sendCommandToServer("simon: goto forest");
      String response = sendCommandToServer("simon: look");
      response = response.toLowerCase();
      assertTrue(response.contains("key"), "Failed attempt to use 'goto' command to move to the forest - there is no key in the current location");
  }

    @Test
    void storyTest() {
        String response5 = sendCommandToServer("Simon:look");
        assertFalse(response5.contains("cellar"), "trapdoor locked, so you can not see that yet");
        String response30=sendCommandToServer("Simon:goto cellar");
        assertTrue(response30.contains("Error"));
        assertTrue(response5.contains("A log cabin in the woods"));
        assertTrue(response5.contains("A bottle of magic potion"));
        assertTrue(response5.contains("A razor sharp axe"));
        assertTrue(response5.contains("A silver coin"));
        String response2 = sendCommandToServer("simon:get axe");
        sendCommandToServer("simon:get coin");
        sendCommandToServer("simon:get potion");
        assertTrue(response2.contains("axe"), "success to get the axe");
        String response24=sendCommandToServer("simon:drop coin");
        assertTrue(response24.contains("drop"));
        String response29=sendCommandToServer("simon:drop coin");
        assertTrue(response29.contains("Error"));
        String response25=sendCommandToServer("simon:look");
        assertTrue(response25.contains("coin"));
        sendCommandToServer("simon:get coin");
        String response26=sendCommandToServer("simon:inv");
        assertTrue(response26.contains("coin"));
        String response3 = sendCommandToServer("SIMON:inv");
        assertTrue(response3.contains("axe"), "watch the inventory and test case-insensitive");
        String response4 = sendCommandToServer("simon:look");
        assertFalse(response4.contains("axe"), "the axe not in the location anyway");
        assertFalse(response4.contains("coin"));
        sendCommandToServer("simon: GOTO forest");
        String response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        assertTrue(response.contains("forest"), "the stuff that the forest have");
        assertTrue(response.contains("tree"));
        assertTrue(response.contains("simon"), "You can see your name in that location");
        assertTrue(response.contains("cabin"), "can to this place");
        String response6 = sendCommandToServer("simon:tree cut");
        assertTrue(response6.contains("cut down"), "action success");
        String response7 = sendCommandToServer("simon:inv");
        assertTrue(response7.contains("log"), "cut the tree and get the log");
        sendCommandToServer("simon:get key");
        sendCommandToServer("simon:goto cabin");
        String response8 = sendCommandToServer("simon:open with key");
        assertTrue(response8.contains("You unlock the door and see steps leading down into a cellar"));
        String response9 = sendCommandToServer("simon:look");
        assertTrue(response9.contains("cellar"), "unlocked so you can see the cellar");
        String response10 = sendCommandToServer("simon:goto cellar");
        assertTrue(response10.contains("An angry looking Elf"), "the suff in cellar");
        assertTrue(response10.contains("cabin"), "the path you can go to");
        String response11 = sendCommandToServer("simon:inventory");
        assertTrue(response11.contains("log"));
        assertTrue(response11.contains("coin"));
        sendCommandToServer("simon:fight elf");
        String response27=sendCommandToServer("simon:health");
        assertTrue(response27.contains("2"));
        sendCommandToServer("simon:drink potion");
        String response28=sendCommandToServer("simon:health");
        assertTrue(response28.contains("3"),"the health increase");
        String response12 = sendCommandToServer("simon:pay coin to Elf");
        assertTrue(response12.contains("You pay the elf your silver coin and he produces a shovel"));
        String response13 = sendCommandToServer("simon:inv");
        assertTrue(response13.contains("shovel"), "pay the coin and get the shovel");
        assertFalse(response13.contains("coin"), "The coin in Elf now");
        sendCommandToServer("simon:goto cabin");
        sendCommandToServer("simon:goto forest");
        String response14 = sendCommandToServer("simon:goto riverbank");
        assertTrue(response14.contains("A grassy riverbank"));
        assertTrue(response14.contains("A fast flowing river"));
        assertTrue(response14.contains("An old brass horn"));
        assertFalse(response14.contains("clearing"), "you can not see the clearing yet");
        sendCommandToServer("simon:get horn");
        String response31=sendCommandToServer("simon:goto clearing");
        assertTrue(response31.contains("Error"));
        String response15 = sendCommandToServer("simon:bridge river with log");
        assertTrue(response15.contains("You bridge the river with the log and can now reach the other side"));
        String response16 = sendCommandToServer("simon:look");
        assertTrue(response16.contains("clearing"), "you can see the clearing now");
        String response17 = sendCommandToServer("simon:goto clearing");
        assertTrue(response17.contains("A clearing in the woods"), "the stuff in clearing");
        assertTrue(response17.contains("It looks like the soil has been recently disturbed"));
        sendCommandToServer("simon:dig the ground with shovel");
        String response18 = sendCommandToServer("simon:look");
        assertTrue(response18.contains("A deep hole in the ground"));
        assertTrue(response18.contains("riverbank"), "the path you can go");
        String response19 = sendCommandToServer("simon:blow horn");
        assertTrue(response19.contains("You blow the horn and as if by magic, a lumberjack appears !"));
        String response20 = sendCommandToServer("simon:look");
        assertTrue(response20.contains("A burly wood cutter"), "lumberjack appeared");
        String response21 = sendCommandToServer("simon:inv");
        assertTrue(response21.contains("horn"), "after blow, the horn still on player's inventory");
        sendCommandToServer("simon:goto riverbank");
        String response22=sendCommandToServer("simon:look");
        assertFalse(response22.contains("A burly wood cutter"),"the lumberjack not follow you");
        sendCommandToServer("simon:goto riverbank");
        sendCommandToServer("simon:blow horn");
        String response23=sendCommandToServer("simon:look");
        assertTrue(response23.contains("A burly wood cutter"),"call the lumberjack from cellar");
    }

    @Test
    void testCharacterDead() {
        sendCommandToServer("dede:goto forest");
        sendCommandToServer("dede:get key");
        sendCommandToServer("dede:goto cabin");
        sendCommandToServer("dede:get axe");
        sendCommandToServer("dede:get coin");
        sendCommandToServer("dede:open the trapdoor with key");
        sendCommandToServer("dede:goto cellar");
        sendCommandToServer("dede:fight elf");
        sendCommandToServer("dede:hit elf");

        String response1=sendCommandToServer("dede:health");
        assertTrue(response1.contains("1"),"fight the elf twice, so still have 1 hp");
        String response2=sendCommandToServer("dede:attack elf");
        assertTrue(response2.contains("dead"),"dead message for the player");
        String response3=sendCommandToServer("dede:look");
        assertTrue(response3.contains("cabin"),"reset in cabin");
        String response4=sendCommandToServer("dede:health");
        assertTrue(response4.contains("3"),"after reset, health to 3");
        String response5=sendCommandToServer("dede:inv");
        assertFalse(response5.contains("axe"),"all stuff in inventory throw in the cellar");
        assertFalse(response5.contains("coin"));
        sendCommandToServer("dede:goto cellar");
        String response6=sendCommandToServer("dede:look");
        assertTrue(response6.contains("coin"),"the inventory's stuff in current location");
        assertTrue(response6.contains("axe"));
    }

    @Test
    void testParseError() {
        sendCommandToServer("Ken:goto forest");
        String response16=sendCommandToServer("Ken:goto cabin and riverbank");
        assertTrue(response16.contains("Error"),"No Ambiguous Commands");
        sendCommandToServer("Ken:goto cabin");
        String response0=sendCommandToServer("Ken: get axe and coin");
        assertTrue(response0.contains("Error"),"No Ambiguous Commands");
        String response1=sendCommandToServer("on99");
        assertTrue(response1.contains("Error"));
        String response2=sendCommandToServer("Ken:");
        assertTrue(response2.contains("Error"));
        String response3=sendCommandToServer(":look");
        assertTrue(response3.contains("Error"));
        String response4=sendCommandToServer(":get coin");
        assertTrue(response4.contains("Error"));
        String response5=sendCommandToServer("Ken:look look");
        assertTrue(response5.contains("Error"));
        String response6=sendCommandToServer("Ken:look inv");
        assertTrue(response6.contains("Error"));
        String response7=sendCommandToServer("Ken:get coin and get axe");
        assertTrue(response7.contains("Error"),"test action is error");
        sendCommandToServer("Ken:get axe");
        sendCommandToServer("Ken:goto forest");
        sendCommandToServer("Ken:get key");
        String response8=sendCommandToServer("cut chop tree");
        assertTrue(response8.contains("Error"),"two different trigger with same action is wrong");
        String response9=sendCommandToServer("Ken:drop");
        assertTrue(response9.contains("Error"));
        String response10=sendCommandToServer("Ken:get");
        assertTrue(response10.contains("Error"));
        String response11=sendCommandToServer("Ken:cut log");
        assertTrue(response11.contains("Error"),"log is product, not relate entity");
        String response12=sendCommandToServer("Ken:cut key");
        assertTrue(response12.contains("Error"));
        String response13=sendCommandToServer("Ken: cut tree with key");
        assertTrue(response13.contains("Error"),"contain unrelated entity");
        String response14=sendCommandToServer("Ken:use axe to cut  the tree!!!!!");
        assertTrue(response14.contains("cut"),"valid command,'!' can use as decorate");
        sendCommandToServer("Ken:goto cabin");
        String response15=sendCommandToServer("Ken:open and unlock the trapdoor");
        assertTrue(response15.contains("Error"),"Error action");
        sendCommandToServer("ken:open trapdoor");

    }

    @Test
    void testIncreaseHealth() {
        sendCommandToServer("dede:get potion");
        String response1=sendCommandToServer("dede:health");
        assertTrue(response1.contains("3"));
        sendCommandToServer("dede:drink potion");
        String response2=sendCommandToServer("dede:health");
        assertTrue(response2.contains("3"),"HP is maxed, can not increased any more");
        String response3=sendCommandToServer("dede:inv");
        assertFalse(response3.contains("potion"),"not potion anymore");
    }

    @Test
    void multiPlayersAdvanceStory() {
        sendCommandToServer("son:look");
        String response1=sendCommandToServer("Dad:look!!!!");
        assertTrue(response1.contains("son"),"the '!' as decoration");
        assertTrue(response1.contains("Dad"));
        sendCommandToServer("Dad:get potion");
        String response2=sendCommandToServer("son:get potion");
        assertTrue(response2.contains("Error"),"the potion got by dad");
        String response3=sendCommandToServer("son:inv");
        assertFalse(response3.contains("potion"),"the son inv should not have potion");
        String response4=sendCommandToServer("dad:inv");
        assertTrue(response4.contains("potion"),"the potion is in dad's inv");
        sendCommandToServer("dad:get axe");
        String response5=sendCommandToServer("dad:goto forest");
        assertFalse(response5.contains("son"),"the son still in cabin");
        String response6=sendCommandToServer("son:goto forest");
        assertTrue(response6.contains("Dad"));
        assertFalse(response6.contains("dad"),"Your name command can ignore case, but show it on the screen, must be the name you type at the begin->Dad");
        String response7=sendCommandToServer("son:cut tree with axe");
        assertTrue(response7.contains("Error"));
        String response8=sendCommandToServer("dad:chop tree");
        assertTrue(response8.contains("You cut down the tree with the axe"));
        String response9=sendCommandToServer("dad:inv");
        assertTrue(response9.contains("log"));
        String response10=sendCommandToServer("son:inv");
        assertFalse(response10.contains("log"),"The son should not have log!");
        sendCommandToServer("son:get key");
        sendCommandToServer("son:goto cabin");
        sendCommandToServer("dad:goto cabin");
        String response11=sendCommandToServer("dad:unlock the door with key");
        assertTrue(response11.contains("Error"),"dad have not own the key");
        sendCommandToServer("son:drop key");
        String response12=sendCommandToServer("Dad:unlock the trapdoor !");
        assertTrue(response12.contains("You unlock the door and see steps leading down into a cellar"),"Dad can use the stuff in current location");
        sendCommandToServer("dad :goto cellar");
        sendCommandToServer("son :goto cellar");
        String response13=sendCommandToServer("dad:fight elf");
        assertTrue(response13.contains("You attack the elf, but he fights back and you lose some health"));
        String response14=sendCommandToServer("dad:health");
        assertTrue(response14.contains("2"));
        String response15=sendCommandToServer("son:health");
        assertTrue(response15.contains("3"),"the son's hp still have 3");
        sendCommandToServer("dad:hit ELF");
        String response16=sendCommandToServer("dad:ATTACK ELF");
        assertTrue(response16.contains("dead"));

        //test reset multi player!
        String response17=sendCommandToServer("dad:look");
        assertTrue(response17.contains("cabin"),"dad back to location1 as he was dead");
        String response18=sendCommandToServer("dad:inv");
        assertFalse(response18.contains("log"),"dad lost his stuff as he was dead");
        String response19=sendCommandToServer("son:look");
        assertTrue(response19.contains("log"),"son can see the log in that location");
        String response20=sendCommandToServer("son:get log");
        assertTrue(response20.contains("log"));
        String response21=sendCommandToServer("son:inv");
        assertTrue(response21.contains("log"),"the son pick up the log successfully");

        String response22=sendCommandToServer("Ka Hong:look");
        assertTrue(response22.contains("Ka Hong"));
        String response23=sendCommandToServer("super's Ka-Hong:look");
        assertTrue(response23.contains("super's Ka-Hong"),"Valid player names can consist of uppercase and lowercase letters, spaces, apostrophes and hyphens.");
        assertTrue(response23.contains("Ka Hong"));

        sendCommandToServer("son:goto cabin");
        sendCommandToServer("son:get coin");
        sendCommandToServer("son:goto cellar");
        sendCommandToServer("son:pay coin to elf");
        sendCommandToServer("son:goto cabin");
        sendCommandToServer("son:goto forest");
        sendCommandToServer("son:goto riverbank");
        String response24=sendCommandToServer("son:bridge coin");
        assertTrue(response24.contains("Error"));
        sendCommandToServer("son:bridge river");
        sendCommandToServer("son:get horn");
        sendCommandToServer("son:BLOW HORN");
        String response25=sendCommandToServer("son:fight elf");
        assertTrue(response25.contains("Error"));
        String response26=sendCommandToServer("son:look");
        assertTrue(response26.contains("A burly wood cutter"),"call the NPC to current location");
        sendCommandToServer("son:blow horn");
        String response27=sendCommandToServer("son:look");
        assertTrue(response27.contains("A burly wood cutter"),"blow twice in the same location");
        String response28=sendCommandToServer("son:blow horn lumberjack");
        assertTrue(response28.contains("Error"));
        sendCommandToServer("son:goto clearing");
        String response29=sendCommandToServer("son:blow horn");
        assertTrue(response29.contains("lumberjack"));
        String response30=sendCommandToServer("son:look");
        assertTrue(response30.contains("A burly wood cutter"),"call npc form other place to current place");
        String response31=sendCommandToServer("son:dig ground");
        assertTrue(response31.contains("You dig into the soft ground and unearth a pot of gold !!!"));
        String response32=sendCommandToServer("son:goto cabin");
        assertTrue(response32.contains("Error"));
        String response33=sendCommandToServer("son:goto riverbank");
        assertTrue(response33.contains("riverbank"));

    }
}
