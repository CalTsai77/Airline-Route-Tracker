/*
 Immutable weighted edge in an undirected graph.
 Original Documentation: Section 4.3 of Algorithms, 
 4th Edition by Robert Sedgewick and Kevin Wayne.
 Partially Modified By: Caleb Tsai
 */

public class Edge implements Comparable<Edge> { 
    // Attributes of a weighted edge (direct route)
    private final int v;
    private final int w;
    private final double distance;
    private final double cost;
    private String source;
    private String destination;

    // Create an edge between v and w along with additional parameters
    public Edge(int v, int w, double distance, double cost, String source, String destination) {
        this.v = v;
        this.w = w;
        this.distance = distance;
        this.cost = cost;
        this.source = source;
        this.destination = destination;
    }
    
    // Return edge's source 
    public String getSource()  {return source;}

    // Return edge's destination 
    public String getDestination() {return destination;}

    // Return edge's distance 
    public double getDistance()    {return distance;}

    // Return edge's cost 
    public double getCost()    {return cost;}

    // Return either endpoint
    public int either()     {return v;}

    // Return the endpoint of this edge that is different from the given vertex (unless a self-loop)
    public int other(int vertex) {
        if      (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new RuntimeException("Illegal endpoint");
    }

    //Compare edges by distance
    public int compareTo(Edge that) {
        if      (this.getDistance() < that.getDistance()) return -1;
        else if (this.getDistance() > that.getDistance()) return +1;
        else                                        return  0;
    }

    // Return a String representation of this edge (formatted in 4 columns)
    public String toString() {
        return String.format("%-15s %-15s $%-15.2f %-15.1f", getSource(), getDestination(), getCost(), getDistance()); 
    }

    // Return a String representation of this edge (formatted in a neat linear row)
    public String toStringCondensed() {
        String costFormatted = String.format("%,.2f", getCost());
        String distanceFormatted = String.format("%,.1f", getDistance());
        return "[(" + getSource() + "," + getDestination() + ") c:" + costFormatted + " d:" + distanceFormatted + "]";
    }
   
}
