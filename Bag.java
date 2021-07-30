/*
 A generic bag or multiset, implemented using a linked list.
 The Bag class represents a bag (or multiset) of generic items.
 It supports insertion and iterating over the items in an arbitrary order.
 The add, isEmpty, and size operations take constant time.
 Iteration takes time proportional to the number of items.
 For additional documentation, see http://algs4.cs.princeton.edu/13stacks" Section 1.3 of
 Algorithms, 4th Edition by Robert Sedgewick and Kevin Wayne.
 */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Bag<Item> implements Iterable<Item> {
    private int N;         // number of elements in bag
    private Node first;    // beginning of bag

    // Helper Linked-List class
    private class Node {
        private Item item;
        private Node next;
    }

    // Creates an empty stack
    public Bag() {
        first = null;
        N = 0;
    }

    // Checks if the Bag is empty
    public boolean isEmpty() {
        return first == null;
    }

    // Returns the number of items in the bag
    public int size() {
        return N;
    }

    // Add the item to the bag
    public void add(Item item) {
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        N++;
    }

    // Returns an iterator that iterates over the items in the bag.
    public Iterator<Item> iterator()  {
        return new LIFOIterator();  
    }

    // Iterator, doesn't implement remove() since it's optional
    private class LIFOIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }


    // Test client
    public static void main(String[] args) {
        // A bag of strings
        Bag<String> bag = new Bag<String>();
        bag.add("Vertigo");
        bag.add("Just Lose It");
        bag.add("Pieces of Me");
        bag.add("Drop It Like It's Hot");
        for (String s : bag)
            System.out.println(s);
    }
}
