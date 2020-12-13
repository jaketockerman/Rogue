package src;

import java.util.ArrayList;

public class Room extends Structure
{
    private int roomId;
    private ArrayList <Creature> creatures = new ArrayList <Creature>(); //each room can have multiple creature
    private ArrayList <Item> itemList = new ArrayList<Item>();

    public Room(int _roomID)
    {
        roomId = _roomID;
    }

    ////////////////////////////////////////////////////

    public void setId( int _roomId )
    {
        roomId = _roomId;
    }

    public int getId()
    {
        return roomId;
    }
    
    ////////////////////////////////////////////////////

    public void setCreature( Creature _creatures )
    {
        //Maybe add one more?
        creatures.add(_creatures);
    }

    public ArrayList <Creature> getCreatures()
    {
        return creatures;
    }

    ////////////////////////////////////////////////////

     public void addItemToList (Item _item)
     {
         itemList.add(_item);
     }
 
     public ArrayList<Item> getItemList()
     {
         return itemList;
     }
 
     ////////////////////////////////////////////////////


    @Override
    public String toString() {
        String str = "";
        str += "Room " + getId() + ":\n\tCreatures in room " + getId() + ":\n";
        for (Creature creature : creatures) {
            //if it's a player
            
            String name = creature.getName();
            if(name.equals("Player"))
            {
                Player player = (Player) creature;
                str += "\t" + player.toString() + "\n";
            }
            else
            // else it's a monster
            {
                 Monster monster = (Monster) creature;
                 str += "\t" + monster.toString() + "\n";
            }
            
        }
        return str;
    }
}