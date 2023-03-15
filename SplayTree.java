public class SplayTree {
    class Node{
        private Node parent;
        private Node left;
        private Node right;
        private String word;
        private String definition;

        public Node(SplayTree.Node parent, SplayTree.Node left, SplayTree.Node right, String word, String definition) {
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.word = word;
            this.definition = definition;
        }
    }
    private Node root;

    public SplayTree(){
        root = null;
    }

    // adds like a BST for testing
    public void add(String key, String definition){
        // key.compareTo
        //key.compareTo()
        if (root == null) {
            root = new Node(null, null, null, key, definition);
            return;
        }
        Node current = root;
        while (true) {
            if (current.word.compareTo(key) < 0) {
                // go left
                if (current.left == null) {
                    current.left = new Node(current, null, null, key, definition);
                    return;
                }
                current = current.left;
            } else {
                // go right
                if (current.right == null) {
                    current.right = new Node(current, null, null, key, definition);
                    return;
                }
                current = current.right;
            }
        }
        

    }

    // remove


    // rebalance (splay)
    private void splay(Node x){
        if (x.parent == null) { // balanced
            return;
        }
        
        
        // rebalance so that the great grandparent of x (if it exists) now points to x. if gg is null, x is now the root. keep doing this until x is the root

        // zig zig -- done when p and x are both left children or right children
        // rotate tree on edge joining p and its parent g
        // rotate tree on edge joining x with p

    // zig zag -- p is not root and x is the opposite type of child as p
        // rotate tree on edge between p and x
        // rotate tree on resulting edge between x and g

    // zig -- parent of node is root (should only be done as the last step in a splay op when x has an odd depth at the beginning of the operation)
        //rotate tree on edge between x (node) and p
    }

    private void rotate(Node x){
        boolean goRight = false;
        if (x.parent.left == x) {
            goRight = true;  
        }
        Node p = x.parent; // does this change? -- poss error if it does
        x.parent = x.parent.parent;
        if (x.parent != null) {
            if (x.parent.left == p) {
                x.parent.left = x;
            } else {
                // x.parent.right == p is true (if not there is a problem)
                x.parent.right = x;
            }
        }

        if (goRight) {
            rotateRight(x, p);
        }
        else{
            rotateLeft(x, p);
        }
    }

    private void rotateRight(Node x, Node p){
        p.left = x.right;
        x.right.parent = p;
        x.right = p;
        p.parent = x;
    }
    
    private void rotateLeft(Node x, Node p){
        p.right = x.left;
        x.left.parent = p;
        x.left = p;
        p.parent = x;
    }

    
     

    // other utility methods
    // print
    public void print(){
        // does a inOrder print
        System.out.println("Current Tree:");
        inOrder(root, "");
    }

    private void inOrder(Node c, String s){
        if (c == null) {
            return;
        }
        inOrder(c.left, s + "   ");
        System.out.println(s + c.word);
        inOrder(c.right, s + "   ");
    }

    // find
    public Node find(String word) {
        Node current = root;
        while (current != null && !current.word.equals(word)) {
            if (current.word.compareTo(word) < 0) {
                current = current.left;
            }
            else{
                current = current.right;
            }
        }
        return current;
    }

    // tester for debug
    public static void main(String[] args) {
        SplayTree tree = new SplayTree();

        // tester for testing rotate method
        tree.add("f", "");
        tree.add("d", "");
        tree.add("e", "");
        tree.add("b", "");
        tree.add("a", "");
        tree.add("c", "");
        tree.print();
        tree.rotate(tree.find("b")); // should do right rotate
        tree.print();
        tree.rotate(tree.find("d")); // should do left rotate
        tree.print(); // should match first print
    }
}