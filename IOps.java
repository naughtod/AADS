/**
 * Student: David Naughton
 * Student no. 320479
 * Email: dna@student.unimelb.edu.au 
 */

import java.util.*;

/**
 * abstract class for the operations
 */
public abstract class IOps {
    private int idNext=1;
    public Map<Integer,Node> publicNodeMap = new HashMap<Integer,Node>();
    private Map<Integer,Node> nodeMap = new HashMap<Integer,Node>();
    private Comparator<Node> comp = (Node o1, Node o2) -> (o1.compare(o2));
    public TreeSet<Node> ts = new TreeSet<Node>(comp); // ordered set keeps track of minimum

    private Random random = new Random(101);
    private int MAX_VALUE=10000000;

    /**
     * generates an element from 0 to MAX_VALUE inclusive
     */
    public Node gen_element() {
        int id=this.idNext;
        this.idNext++;

        int key = random.nextInt(MAX_VALUE+1);

        // maintain private node
        Node node = new Node(id,key);
        ts.add(node);
        nodeMap.put(node.id, node);

        // public node
        return new Node(id, key);
    }

    /**
     * insert operation
     */
    public abstract void insert(Node node);
    
    /**
     * generates an insert
     */
    public Runnable gen_insert() {
        Node node = gen_element();
        publicNodeMap.put(node.id, node);

        return () -> {insert(node);};
    }

    /**
     * delete the minimum element
     */
    public abstract void delete_min();

    /**
     * generates a delete min
     */
    public Runnable gen_delete_min() {
        // removes minimum element from private node set
        Node node = ts.pollFirst();
        nodeMap.put(node.id, null);

        return () -> {delete_min();};
    }

    /**
     * decrease key
     */
    public abstract void decrease_key(int id, int key);

    /**
     * generates a decrease key
     */
    public Runnable gen_decrease_key() {
        // uniformly sample an active node with key that can be decreased
        // i.e. key must be greater than 0
        int sample_index = random.nextInt(idNext);

        while (nodeMap.get(sample_index)==null || 
            nodeMap.get(sample_index).key==0) {
            sample_index = random.nextInt(idNext);
        }
        
        Node node = nodeMap.get(sample_index);
        Boolean result = ts.remove(node);
        node.key = random.nextInt(node.key);
        ts.add(node);
        
        return () -> {decrease_key(node.id,node.key);};
    }
}
