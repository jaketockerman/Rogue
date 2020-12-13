package src;

import java.util.ArrayList;
// don't need setHP (alr provided in displayable class)

public class Creature extends Displayable
{

    private String name;
    private int room;
    private int serial;
    private int hpMoves;
    private int maxHit;
    private int hp;
    private ArrayList <CreatureAction> deathAction = new ArrayList <CreatureAction>();
    private ArrayList <CreatureAction> hitAction = new ArrayList<CreatureAction>();

    public Creature( String _name, int _room, int _serial) 
    {
        name = _name;
        room = _room;
        serial = _serial;
    }

    ////////////////////////////////////////////////////

    // Getter and setter for constructor

    public void setName(String _name)
    {
        name = _name;
    }

    public String getName()
    {
        return name;
    }

    public void setID(int _room, int _serial)
    {
        room = _room;
        serial = _serial;
    }

    public int getRoom()
    {
        return room;
    }

    public int getSerial()
    {
        return serial;
    }


    ////////////////////////////////////////////////////

    public void setMaxHit(int _maxHit)
    {
        maxHit = _maxHit;
    }

    public int getMaxHit()
    {
        return maxHit;
    }

  
    ////////////////////////////////////////////////////

    public void setHp(int _hp)
    {
        hp = _hp;
    }

    public int getHp()
    {
        return hp;
    }

    ////////////////////////////////////////////////////

    public void setHpMoves(int _hpMoves)
    {
        hpMoves = _hpMoves;
    }

    public int getHpMoves()
    {
        return hpMoves;
    }

    ////////////////////////////////////////////////////
    
    public void setDeathAction(CreatureAction _deathAction)
    {
        deathAction.add(_deathAction);
    }

    public ArrayList <CreatureAction> getDeathAction()
    {
        return deathAction;
    }

    ////////////////////////////////////////////////////

    public void setHitAction(CreatureAction _hitAction)
    {
        hitAction.add(_hitAction);
    }

    public ArrayList <CreatureAction> getHitAction()
    {
        return hitAction;
    }    

    public String toString() {
        String str = "";
        str += getName() + " Room " + getRoom() + " Serial " + getSerial() + " hpMoves " + getHpMoves() + " maxHit " + getMaxHit() + " hp " + getHp();
        str += "\n\tHit Actions:\n\t\t";
        for(CreatureAction action : hitAction) {
            str += action.getName() + " ";
        }
        str += "\n\tDeath Actions:\n\t\t";
        for(CreatureAction action : deathAction) {
            str += action.getName() + " ";
        }
        return str;
    }
}