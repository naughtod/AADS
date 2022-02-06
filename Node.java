/**
 * Student: David Naughton
 * Student no. 320479
 * Email: dna@student.unimelb.edu.au 
 */

/**
 *  node contains a key and id
 */ 
public class Node {
    int key;
    int id;
    HeapList.Item highest; // only required for Quake Heap

    public Node(int id, int key) {
        this.key=key;
        this.id=id;
    }

    /**
     * node's can't be equal, so always returns true or false
     * if node > otherNode returns true 
     */
    public int compare(Node otherNode) {
        if (this.key==otherNode.key) {
            if (this.id==otherNode.id) {
                return 0;
            } else{
                return (this.id>otherNode.id) ? 1:-1;
            }            
        } else {
            return (this.key>otherNode.key) ? 1:-1;
        }
    }
}
