package src;


public class Sword extends Item 
{
    public Sword(String _name, int _room, int _serial )
    {
        super(_name,_room,_serial);
    }

    @Override
    public void setID(int _room, int _serial) {
        super.setID(_room, _serial);
    }

    public void updateName() {
        if (getIntValue() > 0) {
            setName("+" + getIntValue() + " Sword");
        }
        else {
            setName(getIntValue() + " Sword");
        }
    }
}