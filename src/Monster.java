package src;


public class Monster extends Creature
{
    public Monster(String _name, int _room, int _serial)
    {
        super(_name, _room, _serial);
    }

    @Override
    public void setName(String _name) {
        super.setName(_name);
    }

    @Override
    public void setID(int _room, int _serial) {
        super.setID(_room, _serial);
    }
}