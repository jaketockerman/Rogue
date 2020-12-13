package src;

public class Armor extends Item
{
    public Armor(String _name, int _room, int _serial )
    {
        super(_name,_room,_serial);
    }

    @Override
    public void setName(String _name) {
        super.setName(_name);
    }

    @Override
    public void setID(int _room, int _serial) {
        super.setID(_room, _serial);
    }

    public void updateName() {
        if (getIntValue() > 0) {
            setName("+" + getIntValue() + " Armor");
        }
        else {
            setName(getIntValue() + " Armor");
        }
    }
}