package src;


import java.util.ArrayList;

public class Passage extends Structure
{

    private String name;
    private int room1, room2;
    private ArrayList <Item> itemList = new ArrayList<Item>();


    public Passage(int _room1 , int _room2) 
    {
        room1 = _room1;
        room2 = _room2;
    }

    ////////////////////////////////////////////////////

    public void setName(String _name)
    {
        name = _name;
    }

    public String getName()
    {
        return name;
    }

    ////////////////////////////////////////////////////

    public void setId(int _room1, int _room2 )
    {
        room1 = _room1;
        room2 = _room2;
    }

    public int getFirstRoomId()
    {
        return room1;
    }

    public int getSecondRoomId()
    {
        return room2;
    }

    ////////////////////////////////////////////////////
    @Override
    public String toString() {
        ArrayList<Integer> x = this.getPosX();
        ArrayList<Integer> y = this.getPosY();


        String str = "";
        str += "Passage between " + getFirstRoomId() + " and " + getSecondRoomId() + "\n";
        for (int i = 0 ; i < x.size(); i++ )
        {
            str += "Pos X: " + x.get(i) + " PosY: " + y.get(i) + "\n";
        }

        return str;
    }
}