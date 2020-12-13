package test;


import java.util.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import org.junit.Test;
import static org.junit.Assert.*;

// Currently only testing one Room, one Player, one Passage and one Item
import src.*;

public class UnitTest {

    //build the parser first
    private Dungeon testDungeon;
    private ArrayList <Room> testRooms;
    private ArrayList <Creature> testCreatures;
    private ArrayList <Item> testItems;
    private ArrayList <Passage> testPassages;


    @Before
    public void initializeParser()
    {

        testDungeon = Dungeon.getDungeon("scroll", 80, 34);
        testDungeon.setTopHeight(2);
        testDungeon.setBottomHeight(4);
 
        Room testRoom = new Room(1);
        testRoom.setVisible();
        testRoom.setPosX(0);
        testRoom.setPosY(0);
        testRoom.setWidth(6);
        testRoom.setHeight(5);

        testDungeon.addRoom(testRoom);

        Player testPlayer = new Player("Player", 1, 2);
        testPlayer.setVisible();
        testPlayer.setPosX(1);
        testPlayer.setPosY(2);
        testPlayer.setHp(20);
        testPlayer.setHpMoves(5);
        testPlayer.setMaxHit(6);
        
        
        //equip player with sword
        Item sword = new Item("Sword", 1, 2);
        testPlayer.addItemToList(sword);
        //equip player with armor
        Item armor = new Item("Armor", 1,2);
        testPlayer.addItemToList(armor);
        
    
        //set death action
        YouWin youWin = new YouWin("YouWin", "death");
        testPlayer.setDeathAction(youWin);
        
        //set hit action
        DropPack dropPack = new DropPack("DropPack","hit");
        testPlayer.setHitAction(dropPack);


        testDungeon.addCreature(testPlayer);


        // will test monster later 

        // Monster testMonster = new Monster("Monster", 1, 2);
        // testPlayer.setVisible();
        // testPlayer.setPosX(1);
        // testPlayer.setPosY(2);
        // testPlayer.setHp(20);
        // testPlayer.setHpMoves(5);
        // testPlayer.setMaxHit(6);
        // make sure monster can have creature action too


        Passage testPassage = new Passage(1,3);
        testPassage.setVisible();
        testPassage.setPosX(3);
        testPassage.setPosY(4);
        testPassage.setPosX(3);
        testPassage.setPosY(16);
        testPassage.setPosX(17);
        testPassage.setPosY(16);
        testPassage.setPosX(15);
        testPassage.setPosY(5);
        testPassage.setPosX(15);
        testPassage.setPosY(3);

        testDungeon.addPassage(testPassage);


        Scroll testScroll = new Scroll("Scroll", 1, 2);
        testScroll.setVisible();
        testScroll.setPosX(3);
        testScroll.setPosY(3);

        testDungeon.addItem(testScroll);
       
    }

    @After
    public void removeElement()
    {
        ArrayList <Room> rooms = testDungeon.getRoom();
        rooms.clear();

        ArrayList <Creature> creatures = testDungeon.getCreature();
        creatures.clear();

        ArrayList <Passage> passages = testDungeon.getPassage();
        passages.clear();

        ArrayList <Item> items = testDungeon.getItem();
        items.clear();
      
    }

    @Test
    public void testDungeon() 
    {
        // test the name
        assertTrue( "Expected the name to be scroll ", testDungeon.getName().equals("scroll"));

        // test the width
        assertTrue( "Expected the width to be 80", testDungeon.getWidth() ==  80);

        // test the topHeight
        assertTrue( "Expected the top height to be 2", testDungeon.getTopHeight() == 2);

        // test the gameHeight
        assertTrue( "Expected the game height to be 34", testDungeon.getGameHeight() == 34);

        // test the bottomHeight
        assertTrue( "Expected the game height to be 34", testDungeon.getBottomHeight() == 4);

    }

    @Test
    public void testRooms()
    {

        int i = 0;

        testRooms = testDungeon.getRoom();

        // set up attributes to test
        int[] posX =   {0};
        int[] posY =   {0};
        int[] width =  {6};
        int[] height = {5};

        ArrayList <Integer> listPosX;
        ArrayList <Integer> listPosY;


        // check the room length
        assertTrue("Room should be 1", testRooms.size() == 1);

       // run through all the room
       for (Room each_room : testRooms)
       {
           assertFalse( "Everything should be visible", each_room.getVisible() != true );
           
           listPosX =  each_room.getPosX();
           assertTrue("Attributes aren't right", listPosX.get(0) == posX[i]);
           listPosY =  each_room.getPosY();
           assertTrue("Attributes aren't right", listPosY.get(0) == posY[i]);
           
           assertTrue("Attributes aren't right", each_room.getWidth() == width[i]);
           assertTrue("Attributes aren't right", each_room.getHeight() == height[i]);
           i++;
       }
    }

    @Test
    public void testCreature()
    {        
        
        int i = 0;
        int playerAmount = 0;

        testCreatures = testDungeon.getCreature();


        // set up attributes to test
        int[] posX =    {1};
        int[] posY =    {2};
        int[] hp =      {20};
        int[] hpMoves = {5};
        int[] maxhit =  {6};

        ArrayList <Integer> listPosX;
        ArrayList <Integer> listPosY;
        ArrayList <CreatureAction> listDeathAction;
        ArrayList <CreatureAction> listHitAction;

       // run through all the room
       for (Creature each_creature : testCreatures)
       {
           if(each_creature.getName().equals("Player"))
           {

                // cast each creature to Player
                Player player = (Player) each_creature;

                assertFalse( "Everything should be visible", player.getVisible() != true );
                        
                listPosX =  player.getPosX();
                assertTrue("Attributes aren't right", listPosX.get(0) == posX[i]);
                listPosY =  player.getPosY();
                assertTrue("Attributes aren't right", listPosY.get(0) == posY[i]);
                
                assertTrue("Attributes aren't right", player.getHp() == hp[i]);
                assertTrue("Attributes aren't right", player.getHpMoves() == hpMoves[i]);
                assertTrue("Attributes aren't right", player.getMaxHit() == maxhit[i]);

                //check if it has weapon
                ArrayList <Item> itemList = player.getItemList();

                Item armor = itemList.get(1);
                assertTrue("Armor is not set",armor.getName().equals("Armor"));

                Item sword = itemList.get(0);
                assertTrue("Weapon is not set",sword.getName().equals("Sword"));

                // check if it has the actions
                listHitAction   = player.getHitAction();
                CreatureAction hitAction = listHitAction.get(0);
                assertTrue("Hit Action is not set", hitAction.getName().equals("DropPack"));

                listDeathAction   = player.getDeathAction();
                CreatureAction deathAction = listDeathAction.get(0);
                assertTrue("deathAction is not set", deathAction.getName().equals("YouWin"));
                
                playerAmount++;
           }

           i++;
       }

       assertFalse("Should not have more than one player", playerAmount > 1 );

    }

    @Test
    public void testPassage()
    {
        int i = 0;
        int j = 0;
        testPassages = testDungeon.getPassage();

        int[][] testPosX =  { 
                            { 3,3,17,15,15 }
                        };
        int[][] testPosY =  { 
                            { 4,16,16,5,3 }
                        };



        for (Passage each_passage : testPassages)
        {
            i = 0;
            assertFalse( "Everything should be visible", each_passage.getVisible() != true );
            
            ArrayList <Integer> posX = each_passage.getPosX();
            ArrayList <Integer> posY = each_passage.getPosY();

            assertFalse("posX and posY should have the same amount" , posX.size() != posY.size());

            for(j = 0; j < posX.size(); j++ )
            {
                assertTrue("Attributes aren't right", posX.get(j) == testPosX[i][j]);
                assertTrue("Attributes aren't right", posY.get(j) == testPosY[i][j]);
            }

        }

    }

    @Test 
    public void testItem() throws Exception
    {
        int i = 0;
        int j = 0;

        testItems = testDungeon.getItem();

        // set up attributes to test
        int[] posX =    {3};
        int[] posY =    {3};
        int[] itemIntValue = {};

        ArrayList <Integer> listPosX;
        ArrayList <Integer> listPosY;

       // run through all the room
       for (Item each_item : testItems)
       {
           if(each_item.getName().contains("Armor") || each_item.getName().contains("Sword"))
           {
                assertFalse( "Everything should be visible", each_item.getVisible() != true );
          
                listPosX =  each_item.getPosX();
                assertTrue("Attributes aren't right", listPosX.get(0) == posX[i]);
                listPosY =  each_item.getPosY();
                assertTrue("Attributes aren't right", listPosY.get(0) == posY[i]);
                
                assertTrue("Attributes aren't right", each_item.getIntValue() == itemIntValue[j]);
                i++;
                j++;
           }
           else if (each_item.getName().contains("Scroll"))
           {
                assertFalse( "Everything should be visible", each_item.getVisible() != true );
                          
                listPosX =  each_item.getPosX();
                assertTrue("Attributes aren't right", listPosX.get(0) == posX[i]);
                listPosY =  each_item.getPosY();
                assertTrue("Attributes aren't right", listPosY.get(0) == posY[i]);
           
                i++;
           }

           else throw new Exception("Not the correct item");
       }

    }
   
}
