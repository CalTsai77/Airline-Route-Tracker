// Author: Caleb Tsai

import java.util.*;

public class Route {
    // Attributes of a route (collection of individual edges)
    private ArrayList<Edge> edges;
    private int hops;
    private double distance; 
    private double cost;
    private String thirdCity;
    private String display;

    // Constructor: Store individual edges within newly created route, perform assignments
    public Route(ArrayList<Edge> edges, int hops, double cost, double distance, String thirdCity) {
        this.edges = edges;
        this.hops = hops;
        this.cost = cost;
        this.distance = distance;
        this.thirdCity = thirdCity;

        String costFormatted = String.format("$%,.2f", cost);
        String distanceFormatted = String.format("%,.1f", distance);
        display = "Cost: " + costFormatted + " -- Distance: " + distanceFormatted + " -- Hops: " + hops + " -- Edges:\n";
        int count = 0;

        // Add edges to a String that will be displayed in toString() method
        for(Edge w : edges) {
            count++;
            display += "\t" + count + ". " + w.toStringCondensed() + "\n";
        }
    }

    // Return String display of route
    public String toString()    {return display;}

    // Return route's total number of hops
    public int getHops()    {return hops;}

    // Return route's total cost
    public double getCost()     {return cost;}

    // Return route's total distance
    public double getDistance()     {return distance;}

    // Returns true if the desired third city exists in the route to be added 
    public boolean doesThirdCityExist() {
        for(int i = 0; i < edges.size(); i++)
            if(edges.get(i).getSource().toLowerCase().equals(thirdCity.toLowerCase()) || 
               edges.get(i).getDestination().toLowerCase().equals(thirdCity.toLowerCase()))
                return true;

        return false;
    }
}