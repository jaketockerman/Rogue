/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author smidkiff
 */
public class Activity {

    private String name;
    private String location;
    private String meetingTime;
    private String meetingDay;

    public void setName(String _name) {
        name = _name;
    }

    public void setLocation(String _location) {
        location = _location;
    }

    public void setMeetingTime(String _meetingTime) {
        meetingTime = _meetingTime;
    }

    public void setMeetingDay(String _meetingDay) {
        meetingDay = _meetingDay;
    }

    @Override
    public String toString() {
        String str = "Activity: \n";
        str += "name: " + name + "\n";
        str += "location: " + location + "\n";
        str += "meetingTime: " + meetingTime + "\n";
        str += "meetingDay: " + meetingDay + "\n";
        return str;
    }
}
