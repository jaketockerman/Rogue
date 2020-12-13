package src;

import java.util.*;


//There is only one Dungeon so we will set it to singleton
public class Dungeon
{

    private static Dungeon uniqueDungeon; //the unique Dungeon
    private String name;
    private int width;
    private int gameHeight;
    private int topHeight;
    private int bottomHeight;

    // Use ArrayList for room because we don't know 
    // how many room it will use, same as creature,passage and item
    private ArrayList<Room> roomList = new ArrayList<Room>(); 
    private ArrayList<Creature> creatureList = new ArrayList<Creature>(); 
    private ArrayList<Passage> passageList = new ArrayList<Passage>(); 
    private ArrayList<Item> itemList = new ArrayList<Item>(); 

    private Dungeon(String _name, int _width, int _gameHeight)
    {
        name = _name;
        width = _width;
        gameHeight = _gameHeight;
    }

    //singleton function
    public static Dungeon getDungeon(String _name, int _width, int _gameHeight)
    {   
        if(uniqueDungeon == null)
        {
            uniqueDungeon = new Dungeon(_name,_width,_gameHeight);
        }
        
        return uniqueDungeon;
        
    }
    ////////////////////////////////////////////////////

    public String getName()
    {
        return name;
    }


    public int getWidth()
    {
        return width;
    }


    public int getGameHeight()
    {
        return gameHeight;
    }


    ////////////////////////////////////////////////////

    public void setTopHeight(int _topHeight)
    {   
        topHeight = _topHeight;
    }

    public int getTopHeight()
    {
        return topHeight;
    }

    ////////////////////////////////////////////////////

    public void setBottomHeight(int _bottomHeight)
    {
        bottomHeight = _bottomHeight;
    }

    public int getBottomHeight()
    {
        return bottomHeight;
    }

    ////////////////////////////////////////////////////

    public void addRoom(Room _room)
    {
        roomList.add(_room);
    }

    public ArrayList<Room> getRoom()
    {
        return roomList;
    }

    ////////////////////////////////////////////////////

    public void addCreature(Creature _creature)
    {
        creatureList.add(_creature);
    }

    public ArrayList<Creature> getCreature()
    {
        return creatureList;
    }

    ////////////////////////////////////////////////////


    public void addPassage(Passage _name)
    {
        passageList.add(_name);
    }

    public ArrayList<Passage> getPassage()
    {
        return passageList;
    }

    ////////////////////////////////////////////////////

    public void addItem (Item _item)
    {
        itemList.add(_item);
    }

    public ArrayList<Item> getItem()
    {
        return itemList;
    }

    @Override
    public String toString()
    {
        String str = "";
        //for loop prints our room creatures passage items
        str += "Rooms:\n";
        for (Room each_room : roomList) {
            str += each_room.toString() + "\n";
        }
        str += "\nPassages:\n";
        for (Passage each_passage : passageList )
        {
            str += each_passage.toString() + "\n";
        }
        str += "\nItems:\n";
        for (Item each_item: itemList)
        {
            str +=  each_item.toString() + "\n";
        }

        return str;
    }

}