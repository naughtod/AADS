/**
 * Student: David Naughton
 * Student no. 320479
 * Email: dna@student.unimelb.edu.au 
 */

/**
 * tournament tree class
 */
public class HeapList {
    Item root;

    /**
     * constructor
     */
    public HeapList(Node node) {
        this.root = new Item(node);
        this.root.height=1;
        node.highest = this.root;
    }

    /**
     * only called when cut performed
     */
    public HeapList(Item item) {
        root = item;
    }

    /**
     * data structure for tree node
     */
    public class Item {
        Node node;
        int height;

        Item leftChild=null;
        Item rightChild=null;
        Item parent=null;

        public Item(Node node) {
            this.node=node;
        }

    }

    // want sort decreasing, for efficiency
    public int compareHeight(HeapList otherHeap) {
        if(this.root.height==otherHeap.root.height) {
            return 0;
        } else if (this.root.height<otherHeap.root.height) {
            return 1;
        } else {
            return -1;
        }
    }

}
