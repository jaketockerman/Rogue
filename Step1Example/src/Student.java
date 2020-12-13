/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author smidkiff
 */
public class Student {
    private String name;
    private int numActivities;
    private int activityCount = 0;
    private Activity[ ] activities;
    public Student(String _name, int _numActivities) {
        name = _name;
        numActivities = _numActivities;
        activities = new Activity[numActivities];
    }
    
    public void addActivity (Activity activity) {
        activities[activityCount++] = activity;
    }
    
    @Override
    public String toString( ) {
        String str = "Student: \n";
        str += "   name: "+name + "\n";
        str += "   numActivities "+numActivities + "\n";
        for (Activity activity : activities) {
            str += activity.toString( ) + "\n";
        }
        return str;
    }   
}
