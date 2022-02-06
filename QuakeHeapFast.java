/**
 * Student: David Naughton
 * Student no. 320479
 * Email: dna@student.unimelb.edu.au 
 */

import java.util.*;

public class QuakeHeapFast extends IOps {
    private final int MAX_VALUE = 10000000;
    private final float invariance_parameter;
    private List<List<HeapList>> tTrees = new ArrayList<List<HeapList>>(); 
    private List<Integer> nodesAtHeight = new ArrayList<Integer>();
    private HashMap<Node, HeapList> nodeToTree = new HashMap<Node, HeapList>();
    private HeapList min=null;

    public QuakeHeapFast(float invariance_parameter) {
        // needs to be in (0.5,1)
        this.invariance_parameter = invariance_parameter;
        List<HeapList> h1 = new ArrayList<HeapList>();
        tTrees.add(h1);
    }
    
    /**
     * create a new tree and add to list
     */
    public void insert(Node node) {
        HeapList tTree = new HeapList(node);
        
        // used for decrease key to find tree which contains node
        nodeToTree.put(node, tTree);

        tTrees.get(0).add(tTree);
        addHeight(1);

        // update minimum tree
        updateMin(tTree);
        
    }

    /**
     * perform a cut and then change node key to target
     */
    public void decrease_key(int id, int key) {
        // use auxiliary data structures, to find node to decrease in O(1)
        Node node = publicNodeMap.get(id);
        
        node.key = key;
        if (node.highest.parent!=null) {
            cut(node.highest);
        } else {
            // otherwise node is root, find tree and updateMin
            if (tTrees.get(nodeToTree.get(node).root.height-1).contains(nodeToTree.get(node))) updateMin(nodeToTree.get(node));            
        }
    }

    /**
     * deletes the minimum element
     */
    public void delete_min() {

        // delete nodes on path from min root to leaf
        deletePathToLeaf(min.root);
        tTrees.get(min.root.height-1).remove(min);
        if(tTrees.get(tTrees.size()-1).size()==0) tTrees.remove(tTrees.size()-1);
        min = null;

        // link trees of same height, also finds new min
        for (int i=0;i<tTrees.size();i++) {
            int j=-1;
            while ((j=tTrees.get(i).size())>0) {
                if (j>1) {
                    link(tTrees.get(i).get(j-2), tTrees.get(i).get(j-1));
                    tTrees.get(i).remove(j-1);
                    tTrees.get(i).remove(j-2);
                } else {
                    // update min and break
                    updateMin(tTrees.get(i).get(0));
                    break;
                }
            }
        }

        // invariant maintenance
        for (int i=0;i<nodesAtHeight.size()-1;i++) {
        
            if (nodesAtHeight.get(i+1) > invariance_parameter * 
                nodesAtHeight.get(i)) {
                
                // invariant violated, need to delete all nodes above height i+1
                for (int j=tTrees.size()-1;j>i;j--) {
                    for (int k=0;k<tTrees.get(j).size();k++) {
                        invariantMaintenance(tTrees.get(j).get(k),i+1);
                    }
                    tTrees.remove(j);
                    if (j<nodesAtHeight.size()) nodesAtHeight.remove(j);
                }

                break;                                        
            }
        }
    }

    /**
     * is the root node of t1 bigger than the root node of t2
     */
    private boolean compareTrees(HeapList t1, HeapList t2) {
        return t1.root.node.compare(t2.root.node)==1;
    }

    /**
     * update minimum tree
     */
    public void updateMin(HeapList t1) {
        if (min!=null) {
            // move the min when trees have same root, old root is redundant
            if (min.root.node==t1.root.node) {
                min = t1;
            } else if (compareTrees(min, t1)) {
                min = t1;
            }
        } else {
            min = t1;
        }
    }

    /**
     * add tree 
     */
    public void addTree(HeapList t1) {
        if (tTrees.size() < t1.root.height) {
            List<HeapList> h = new ArrayList<HeapList>();
            tTrees.add(h);
        }
        tTrees.get(t1.root.height-1).add(t1);

        nodeToTree.put(t1.root.node, t1);
        // update min
        updateMin(t1);
    }

    /**
     * link trees O(1)
     */
    public void link(HeapList t1, HeapList t2) {
        if (compareTrees(t1, t2)) {
            // t2 is min, create new node from t2, set left and right child, 
            // no parent, set highest, set parent for t1 and t2, set root of t2
            HeapList.Item newRoot = t2.new Item(t2.root.node);
            newRoot.node.highest = newRoot;
            newRoot.height=t2.root.height+1;
            newRoot.leftChild = t1.root;
            newRoot.rightChild = t2.root;
            t1.root.parent=newRoot;
            t2.root.parent=newRoot;
            addHeight(newRoot.height);
            addTree(new HeapList(newRoot));
            
        } else {
            HeapList.Item newRoot = t1.new Item(t1.root.node);
            newRoot.node.highest = newRoot;
            newRoot.height=t1.root.height+1;
            newRoot.leftChild = t1.root;
            newRoot.rightChild = t2.root;
            t1.root.parent=newRoot;
            t2.root.parent=newRoot;
            addHeight(newRoot.height);
            addTree(new HeapList(newRoot));
        }
    }

    /**
     * given an item, cut off from parent and add new tree
     */
    public void cut(HeapList.Item item) {
        if (item.parent!=null) {
            if (item.parent.leftChild == item) {
                item.parent.leftChild = null;
            } else {
                item.parent.rightChild = null;
            }
            item.parent=null;
        
            HeapList newTree = new HeapList(item);
            addTree(newTree);
        } 
    }

    /**
     * deletes path to leaf
     */
    private void deletePathToLeaf(HeapList.Item root) {
        HeapList.Item head = root;

        while (head!=null) {
            removeHeight(head.height);
            // if two children 
            if (head.leftChild!=null && head.rightChild!=null) {
                
                if (head.leftChild.node==head.node) {
                    // cut the right child off
                    cut(head.rightChild);
                    head = head.leftChild;
                } else {
                    // cut the left child off
                    cut(head.leftChild);
                    head = head.rightChild;
                }
            } else if (head.leftChild==null) {
                head = head.rightChild;
            } else {
                head = head.leftChild;
            } 
        }
    }

    /**
     * BFS to delete nodes above given height
     */
    private void invariantMaintenance(HeapList h, int height) {
        LinkedList<HeapList.Item> q = new LinkedList<>();
        
        q.add(h.root);

        while (!q.isEmpty()) {
            // pop the queue
            HeapList.Item g = q.remove();

            if (g.height==height) {
                g.node.highest=g;
                cut(g);
                continue;
            } 

            if (g.leftChild != null) q.add(g.leftChild);
            if (g.rightChild != null) q.add(g.rightChild);
        } 
    }

    /**
     * used to track heights for invariant maintenance
     */
    private void addHeight(int height) {
        // index 0 counts nodes of height 1
        if (height==nodesAtHeight.size()+1) {
            nodesAtHeight.add(1);
        } else {
            nodesAtHeight.set(height-1,nodesAtHeight.get(height-1)+1);
        }
    }

    /**
     * used to track heights for invariant maintenance
     */
    private void removeHeight(int height) {
        int curr = nodesAtHeight.get(height-1);
        if (curr==1) {
            nodesAtHeight.remove(height-1);
        } else {
            nodesAtHeight.set(height-1,curr-1);
        }
    }

    /**
     * print whole forest, for testing
     */
    public void printForest() {
        int count=0;
        for (int i=0;i<tTrees.size();i++) {
            for (int j=0;j<tTrees.get(i).size();j++){
                count++;
                System.out.println("TREE:"+count);
                printTree(tTrees.get(i).get(j));
                System.out.println("END\n");
            }
        }
    }

     /**
     * print tree keys using bfs
     */
    public void printTree(HeapList t) {
        LinkedList<HeapList.Item> q = new LinkedList<>();
        q.add(t.root);
        while (!q.isEmpty()) {
            // pop the queue
            HeapList.Item g = q.remove();
            int lk = g.leftChild==null ? -1: g.leftChild.node.key;
            int rk = g.rightChild==null ? -1: g.rightChild.node.key;
            System.out.println("id: "+ g.node.id + " | key : " + g.node.key + 
            " left child : " + lk + " right child : " + rk);
            if (g.leftChild != null) q.add(g.leftChild);
            if (g.rightChild != null) q.add(g.rightChild);
        } 
    }

}

