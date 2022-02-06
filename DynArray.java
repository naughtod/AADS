/**
 * Student: David Naughton
 * Student no. 320479
 * Email: dna@student.unimelb.edu.au 
 */

import java.util.*;

public class DynArray extends IOps {
    private final int MAX_VALUE = 10000000;
    private final int INIT_ELEMENTS = 1;
    private Node[] array = new Node[INIT_ELEMENTS];
    private int n = 0;

    /**
     * insert an element into the array
     */
    public void insert(Node node) {
        if (n==this.array.length) {
            // create new array twice as big
            Node[] newArray = new Node[n*2];
            
            // copy across all elements in array
            for (int i=0;i<n;i++) {
                newArray[i] = this.array[i];
            }
            this.array = newArray;

        } 

        // add key to next available position and increment n
        array[n++] = node;
    }

    /**
     * delete the minimum element
     */
    public void delete_min() {
        if (n==0) return;

        // initialise min
        Node min = this.array[0];
        int min_index = 0;

        for (int i=0;i<n;i++) {
            // returns minimum with smallest id in event of tie
            if (this.array[i].compare(min)==-1) {
                min = this.array[i];
                min_index = i;
            }
        }

        // swap min with last element and pop back
        this.array[min_index] = this.array[--n];
    }

    /**
     * decrease the key of the target node
     */
    public void decrease_key(int id, int key) {
        // retrieve node from hashmap
        publicNodeMap.get(id).key = key;
    }

}
