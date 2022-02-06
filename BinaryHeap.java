/**
 * Student: David Naughton
 * Student no. 320479
 * Email: dna@student.unimelb.edu.au 
 */

import java.util.*;

public class BinaryHeap extends IOps {
    // implementation uses dynamic array
    private final int MAX_VALUE = 10000000;
    private int INIT_ELEMENTS = 2;
    private Node[] array = new Node[INIT_ELEMENTS];
    private Map<Integer,Integer> idToIndex = new HashMap<Integer,Integer>();
    private int n = 1;

    /**
     * insert an element into the heap
     */
    public void insert(Node node) {
        idToIndex.put(node.id,n);
        safeInsert(node);
        bubbleUp(n-1);
    }

    /**
     * dynamic array insert function
     */
    private void safeInsert(Node node) {
        
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
     * decrease the key of the target node
     */
    public void decrease_key(int id, int key) {
        publicNodeMap.get(id).key = key;
        bubbleUp(idToIndex.get(id));
    }

    /**
     * moves node up the heap 
     */
    private void bubbleUp(int i) {
        int j=-1;
        while (!(i==1 || array[i].compare(array[(j=getParent(i))])==1)) {
            swap(i,j);
            i=j;
        }
    }

    /**
     * moves node down the tree
     */
    private void bubbleDown(int i) {
        int j = -1, r= -1, l = -1;
        while (true) {
            // right child and left child exists
            if ((r=getRightChild(i))<n) {
                // find smallest child
                j = array[r].compare(array[r-1])==1 ? r-1 : r;
            } else if ((l=getLeftChild(i))<n) {
                // only left child exists
                j  = l;
            } else {
                break;
            }

            // if parent is greater than child, swap
            if (array[i].compare(array[j])==1) {
                swap(i,j);
                i=j;
            } else {
                break;
            }
            
        }
    }
    
    /**
     * delete min element
     */
    public void delete_min() {
        if (n>2) {
            swap(1,n-1);
            n--;
            bubbleDown(1);
        } else if (n==2) {
            n--;
        } else {
            // do nothing
        }
    }

    /**
     * helper function to swap two array positions 
     */
    private void swap(int a, int b) {
        idToIndex.put(array[a].id,b);
        idToIndex.put(array[b].id,a);

        Node temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    private int getParent(int pos) {
        return pos/2;
    }

    private int getLeftChild(int pos) {
        return pos*2;
    }

    private int getRightChild(int pos) {
        return pos*2+1;
    }

}



