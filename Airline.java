/*
 Please download all the files in this repository, navigate to corresponding
 directory in local storage
 Access the appropriate local machine: terminal (Mac) or command prompt (Windows)
 To COMPILE: javac Airline.java 
 To RUN: java Airline pennsylvania.txt OR java Airline global.txt
 Author: Caleb Tsai
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Airline {
    // Attributes of an airline (cities, routes, etc.)
    private final int V;
    private int E;
    private Bag<Edge>[] adj;
    private ArrayList<String> cities;
    private ArrayList<Edge> edges;
    private ArrayList<Route> routes;
    private boolean addThirdCity = false;

    // Constructor: Read/Pass in info from data file
    public Airline(String file) throws IOException {
        // Assign Scanner object to file, error if V is not valid
        Scanner read = new Scanner(new File(file));
        V = read.nextInt();
        if (V < 0) 
            throw new RuntimeException("Number of vertices must be nonnegative in this file");

        // Add cities to an ArrayList of String
        cities = new ArrayList<String>();
        
        for(int i = 0; i < V; i++)
            cities.add(read.next());
        
        // Create an adjacency list of Bags containing direct routes (Edge)
        adj = (Bag<Edge>[]) new Bag[V];
        for (int v = 0; v < V; v++) 
            adj[v] = new Bag<Edge>();

        // Add edges to adjacency list for each direct route (source, destination, distance, cost)
        while(read.hasNextLine()) {
            int v = read.nextInt();
            int w = read.nextInt();
            double distance = read.nextDouble();
            double cost = read.nextDouble();
            String source = cities.get(v);
            String destination = cities.get(w);

            // Add 2 edges for one route (route goes both ways, switch source and destination)
            Edge e = new Edge(v, w, distance, cost, source, destination);
            Edge e2 = new Edge(w, v, distance, cost, destination, source);
            addEdge(e, true);
            addEdge(e2, false);
        }
    }

    // Add edge e to corresponding adjacency list of vertices
    public void addEdge(Edge e, boolean isForward) {
        int v = e.either();
        int w = e.other(v);
        E++;
        
        // Forward edge is added (source to destination)
        if(isForward)
            if(v < w)
                adj[v].add(e);
            else
                adj[w].add(e);  
        
        // Backward edge is added (destination to source)
        else
            if(v > w)
                adj[v].add(e);
            else
                adj[w].add(e);
    }

    // Return the number of cities in the airline
    public int V()  {return V;}

    // Option 1A: Return names of the cities
    public String getCities() {
        String display = "Cities Served:\n";
        for(int i = 0; i < cities.size(); i++)
            display += "    " + cities.get(i) + "\n";

        return display;
    }

    // Option 1B: Return direct routes served by airline
    public String getDirectRoutes() {
        String display = "Direct Routes:\n";
        display += String.format("%-15s %-15s %-15s %-15s", "From (City)", "To (City)", "Cost ($)", "Distance (Miles)") + "\n";
        display += String.format("%-15s %-15s %-15s %-15s", "----------", "----------", "----------", "----------") + "\n";
        
        // Add each direct route using Edge class toString() method
        for(int i = 0; i < adj.length; i++)
            for(Edge e : adj[i])
                display += e.toString() + "\n";

        return display;
    }

    // Return total number of direct routes in airline
    public int E()  {return E;}

    // Return edges incident to vertex v as an Iterable
    public Iterable<Edge> adj(int v)    {return adj[v];}
    
    // Option 2: Find a route (source city, destination city, maximum cost, maximum # of hops)
    public void findRoute(Scanner kbd) {
        String source = ""; 
        String destination = "";
        String thirdCity = "N/A";
        double maxCost = 0.0;
        int maxHop = 0;
        boolean go = true;
        /******************************************************************************************************************/
        // User Verification of Starting City

        while(go) {
            try {
                System.out.print("Enter Starting City: ");
                source = kbd.next();

                // Validate if user inputted source city exists in the airline (accounts for arbitrary capitalization)
                for(int i = 0; i < cities.size(); i++) {
                    if(source.toLowerCase().equals(cities.get(i).toLowerCase())) {
                        source = cities.get(i);
                        go = false;
                        break;
                    }
                }
                
                // User inputted city does not exist in airline (display cities that the airline offers)
                if(go)
                    System.out.println("Sorry, your inputted city named \"" + source + "\" is not found. Please check your spelling.\n" + getCities());
                
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input: Please enter a data type of String.\n");
                kbd = new Scanner(System.in);
            }
        }
        /******************************************************************************************************************/
        // User Verification of Destination City

        go = true;
        while(go) {
            try {
                System.out.print("Enter Destination City: ");
                destination = kbd.next();

                // Validate if user inputted destination city exists in the airline (accounts for arbitrary capitalization)
                for(int i = 0; i < cities.size(); i++) {
                    if(destination.toLowerCase().equals(cities.get(i).toLowerCase())) {
                        destination = cities.get(i);
                        go = false;
                        break;
                    }
                }
                
                // User inputted city does not exist in airline (display cities that the airline offers)
                if(go) {
                    System.out.println("Sorry, your inputted city named \"" + destination + "\" is not found. Please check your spelling.\n" + 
                    getCities());
                }
                
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input: Please enter a data type of String.\n");
                kbd = new Scanner(System.in);
            }
        }
        /******************************************************************************************************************/
        // Prompt for Third City and resulting User Verification
        // User Verification of thirdChoice
        
        go = true;
        boolean go2;
        while(go) {
            System.out.print("Would you like to specify a third city such that all routes must include this city?\n(Ex: " +
                           "Display all flights from Pittsburgh to Philadelphia that go through Harrisburg)\n" + 
                           "If yes, please press 1. If no, please press any other number.\n\nChoice: ");
            try {
                int thirdChoice = kbd.nextInt();

                // User wants to add a third city
                if(thirdChoice == 1) {
                    addThirdCity = true;
                    go2 = true;
                
                    while(go2) {
                        try {
                            System.out.print("Enter Third City: ");
                            thirdCity = kbd.next();

                            // Validate if user inputted third city exists in the airline (accounts for arbitrary capitalization)
                            for(int i = 0; i < cities.size(); i++) {
                                if(thirdCity.toLowerCase().equals(cities.get(i).toLowerCase())) {
                                    thirdCity = cities.get(i);
                                    go2 = false;
                                    break;
                                }
                            }
                            
                            // User inputted city does not exist in airline (display cities that the airline offers)
                            if(go2) {
                                System.out.println("Sorry, your inputted city named \"" + thirdCity + "\" is not found. Please check your spelling.\n" + 
                                getCities());
                            }
                            
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid Input: Please enter a data type of String.\n");
                            kbd = new Scanner(System.in);
                        }
                    }
                }
                break;

            } catch (InputMismatchException e) {
                System.out.println("Invalid Input: Please enter a data type of int.\n");
                kbd = new Scanner(System.in);
            }
        }

        /******************************************************************************************************************/
        // User Verification of Maximum Cost

        go = true;
        while(go) {
            System.out.print("Enter Maximum Cost (> 0): ");
            try {
                maxCost = kbd.nextDouble();

                // Invalid Input (user typed in a value <= 0)
                if(maxCost <= 0)
                    System.out.println("Invalid Input of " + maxCost + " --> Cost must be greater than 0");
                else
                    break;

            } catch (InputMismatchException e) {
                System.out.println("Invalid Input: Please enter a data type of double.\n");
                kbd = new Scanner(System.in);
            }
        }
        /******************************************************************************************************************/
        // User Verification of Maximum # of Hops

        while(go) {
            System.out.print("Enter Maximum Number of Hops (> 0): ");
            try {
                maxHop = kbd.nextInt();
                
                // Invalid Input (user typed in a value <= 0)
                if(maxHop <= 0)
                    System.out.println("Invalid Input of " + maxHop + " --> Number of hops must be greater than 0");
                else
                    break;

            } catch (InputMismatchException e) {
                System.out.println("Invalid Input: Please enter a data type of int.\n");
                kbd = new Scanner(System.in);
            }
        }
        /******************************************************************************************************************/
        // Find ALL simple routes that meet the specified user-inputted criteria, storing them in a Route class
        // Assume the cost of a multi-hop route is additice (total cost --> sum of costs of individual hops)
        
        boolean[] marked = new boolean[V];     
        int sourceInt = cities.indexOf(source);
        int destinationInt = cities.indexOf(destination);
        edges = new ArrayList<Edge>();
        routes = new ArrayList<Route>();

        // Call dfs helper method
        dfs(marked, edges, routes, sourceInt, destinationInt, 0, maxCost, 0, maxHop, 0, thirdCity);

        // Identify the number of total routes via a collection of Routes --> ArrayList<Route>
        int numRoutes = routes.size();
        String maxCostFormatted = String.format("$%,.2f", maxCost);  

        // Special Case: No Routes
        if(numRoutes == 0) {
            if(addThirdCity) {
                System.out.println("Sorry, there are no paths from " + source + " to " + destination + 
                                   " through " + thirdCity + " that meet your desired criteria\n");
            }
                
            else
               System.out.println("Sorry, there are no paths from " + source + " to " + destination + " that meet your desired criteria\n"); 
        }
            
        
        // Special Case: One Route
        else if(numRoutes == 1) {
            if(addThirdCity) {
                System.out.println("There is 1 path from " + source + " to " + destination + 
                " through " + thirdCity + " with maximum cost " + maxCostFormatted + " and at most " + maxHop + " hops\n");
            }
                
            else {
                System.out.println("There is 1 path from " + source + " to " + destination + 
                " with maximum cost " + maxCostFormatted + " and at most " + maxHop + " hops\n");
            }
        }

        // Normal Case: Multiple Routes
        else {
            if(addThirdCity) {
                System.out.println("\nThere are " + numRoutes + " paths from " + source + " to " + destination + 
                " through " + thirdCity + " with maximum cost " + maxCostFormatted + " and at most " + maxHop + " hops\n");
            }
                
            else {
                System.out.println("\nThere are " + numRoutes + " paths from " + source + " to " + destination + 
                " with maximum cost " + maxCostFormatted + " and at most " + maxHop + " hops\n");
            }
        }
        /******************************************************************************************************************/
        // Prompt user to select how they will view the resulting routes (by hops, cost, or distance)
        // Also give another option to return back to the main menu if they do not wish to see the routes
        
        go = true;

        // Only give the user this option if there is at least one route available to view
        while(go && numRoutes > 0) {
            // Special Case: Only one route is available (slightly different prompt)
            if(numRoutes == 1) {
                System.out.print("How would you like to view this path? (Note: there is only one possible path)\n1) Ordered by hops (fewest to most)\n" + 
                "2) Ordered by cost (cheapest to most expensive)\n3) Ordered by distance (shortest to longest overall)\n" +
                "Note: Input any other number (int) to go back to the main menu\n\nChoice: ");
            }

            // Normal Case: Standard prompt giving the user all the available options
            else {
                System.out.print("How would you like to view these paths?\n1) Ordered by hops (fewest to most)\n" + 
                             "2) Ordered by cost (cheapest to most expensive)\n3) Ordered by distance (shortest to longest overall)\n" +
                             "Note: Input any other number (int) to go back to the main menu\n\nChoice: ");
            }
            
            try {
                int choice = kbd.nextInt();
                
                // Order routes by hops via a HopComparator and display (fewest to most)
                if(choice == 1) {
                    HopComparator hopComp = new HopComparator();
                    Collections.sort(routes, hopComp);

                    // Add output telling user they wanted to add a third city
                    if(addThirdCity)
                        System.out.println("\n(Note that the routes below include the city of " + thirdCity + ")");
                    
                    System.out.println("Paths from " + source + " to " + destination + " sorted by hops (fewest to most):\n");
                    for(int i = 0; i < routes.size(); i++)
                        System.out.println(routes.get(i).toString());
                }

                // Order routes by cost via a CostComparator and display (cheapest to most expensive)
                else if(choice == 2) {
                    CostComparator costComp = new CostComparator();
                    Collections.sort(routes, costComp);
                    
                    // Add output telling user they wanted to add a third city
                    if(addThirdCity)
                        System.out.println("\n(Note that the routes below include the city of " + thirdCity + ")");

                    System.out.println("Paths from " + source + " to " + destination + " sorted by cost (cheapest to most expensive):\n");
                    for(int i = 0; i < routes.size(); i++)
                        System.out.println(routes.get(i).toString());
                }
                
                // Order routes by distance via a DistanceComparator and display (shortest to longest)
                else if(choice == 3) {
                    DistanceComparator distanceComp = new DistanceComparator();
                    Collections.sort(routes, distanceComp);
                    
                    // Add output telling user they wanted to add a third city
                    if(addThirdCity)
                        System.out.println("\n(Note that the routes below include the city of " + thirdCity + ")");

                    System.out.println("Paths from " + source + " to " + destination + " sorted by distance (shortest to longest overall):\n");
                    for(int i = 0; i < routes.size(); i++)
                        System.out.println(routes.get(i).toString());
                }

                // If user enters a number (int) that is neither 1, 2, nor 3, then return to main menu
                else {
                    System.out.println(); 
                    break;
                }
                    
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input: Please enter a data type of int.\n");
                kbd = new Scanner(System.in);
            }
        }
        addThirdCity = false;
    }

    // Private helper method that recursively backtracks through cities/edges to find all routes that fulfill criteria 
    private void dfs(boolean[] marked, ArrayList<Edge> edges, ArrayList<Route> routes, int source, int destination, 
                     double currCost, double maxCost, int currHop, int maxHop, double distance, String thirdCity) {
        // Immediately mark the source as being seen
        marked[source] = true;

        // Base Case: Current vertex (source) matches the destination
        if(source == destination) {
            Route newRoute = new Route(edges, currHop, currCost, distance, thirdCity);

            // Conditions to add route to collection
            if((addThirdCity && newRoute.doesThirdCityExist()) || !addThirdCity)
                routes.add(newRoute);

            return;
        }

        // Recursive Case: Verify if the route to be added fulfills criteria of cost and hops
        else {
            // Check number of hops
            if((currHop < maxHop)) {
                // For each neighbor adjacent/reachable to source, recursively call dfs if it is unmarked
                for(Edge w : adj(source)) {
                    // Assign endpoint (int) to the neighbor of the source
                    int endpoint1 = w.either();
                    int endpoint2 = w.other(w.either());
                    int endpoint;

                    if(source == endpoint1)
                        endpoint = endpoint2;
                    else
                        endpoint = endpoint1;
                    
                    // Check if this endpoint is unmarked: if unmarked, continue
                    if(!marked[endpoint]) {
                        double newCost = currCost + w.getCost();

                        // Check if max cost has been reached if we add this next edge
                        // If resulting cost is less than max cost, add edge and recursively call dfs with endpoint as source
                        if(newCost <= maxCost) {
                            currHop++;
                            double newDistance = distance + w.getDistance();
                            edges.add(w);
                            
                            dfs(marked, edges, routes, endpoint, destination, newCost, maxCost, currHop, maxHop, newDistance, thirdCity);
                            
                            // After dfs occurs, remove edge from collection, decrement currHop, unmark that endpoint
                            marked[endpoint] = false;
                            currHop--;
                            if(edges.size() >= 1)
                                edges.remove(edges.size() - 1);
                        }
                    }
                }
            }    
        }
    }

    // Client Performance 
    public static void main(String [] args) throws IOException {
        // Create Airline object, Scanner kbd for user input (System.in)
        Airline newAirline = new Airline(args[0]);
        Scanner kbd = new Scanner(System.in);
        boolean go = true;
        System.out.println("\nWelcome to Cal's Crazily Complex & Amazingly Awesome Airline!");

        // Main Menu Loop
        while(go) {
            System.out.println("Please choose from the options below:\n" + "1) List cities served and direct routes\n2) Find a route\n3) Quit program");
            try {
                int input = kbd.nextInt();
    
                // Option 1: List the cities and direct routes served by the airline
                if(input == 1)
                    System.out.println("\n" + newAirline.getCities() + "\n" + newAirline.getDirectRoutes());
                
                // Option 2: Find a route (source city, destination city, maximum cost, maximum # of hops), pass in same kbd Scanner
                else if(input == 2)
                    newAirline.findRoute(kbd);
        
                // Option 3: Terminate program in a graceful way
                else {
                    System.out.println("Goodbye, have a great day and happyyy and safe and funnn travels!!!\n" + 
                                       "Thank you for choosing Cal's Crazily Complex & Amazingly Awesome Airline!!!\n");
                    System.exit(0);
                    kbd.close();
                }
                
            } catch (InputMismatchException e) {
                // User inputs a non-int, create new kbd Scanner
                System.out.println("Invalid Input: Please enter either 1, 2, or 3.\n");
                kbd = new Scanner(System.in);
            }
        }
    }

    // Comparator of Route objects based on number of HOPS
    private class HopComparator implements Comparator<Route> {

        public int compare(Route firstRoute, Route secondRoute) {
            return firstRoute.getHops() - secondRoute.getHops();
        }
    }

    // Comparator of Route objects based on COST
    private class CostComparator implements Comparator<Route> {

        public int compare(Route firstRoute, Route secondRoute) {
            double diff = firstRoute.getCost() - secondRoute.getCost();
            return (int) diff;
        }
    }

    // Comparator of Route objects based on DISTANCE
    private class DistanceComparator implements Comparator<Route> {

        public int compare(Route firstRoute, Route secondRoute) {
            double diff = firstRoute.getDistance() - secondRoute.getDistance();
            return (int) diff;
        }
    }
}
