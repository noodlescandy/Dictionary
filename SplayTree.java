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

    public void add(String key, String definition){
        if (root == null) {
            root = new Node(null, null, null, key, definition);
            return;
        }
        Node current = root;
        while (true) {
            int c = current.word.compareTo(key);
            if (c > 0) {
                // go left
                if (current.left == null) {
                    current.left = new Node(current, null, null, key, definition);
                    splay(current.left);
                    return;
                }
                current = current.left;
            } else {
                // go right
                if (current.right == null) {
                    current.right = new Node(current, null, null, key, definition);
                    splay(current.right);
                    return;
                }
                current = current.right;
            }
        }
    }

    // remove
    public void remove(String word){
        Node toRemove = find(word);

        // The node to be deleted is first splayed, i.e. brought to the root of the tree and then deleted. leaves the tree with two sub trees.
        splay(toRemove);
        Node leftNode = root.left;
        Node rightNode = root.right;
        if (leftNode == null && rightNode == null) {
            root = null;
            return;
        }


        leftNode.parent = null;
        
        // The two sub-trees are then joined using a "join" operation.
        Node current = leftNode;
        while(current.right != null){
            current = current.right;
        }
        // current is largest item in left tree
        splay(current);
        // current is now root
        root.right = rightNode;
        if (root.right != null) {
            root.right.parent = root;
        }

        // Given two trees S and T such that all elements of S are smaller than the elements of T, the following steps can be used to join them to a single tree:
        // Splay the largest item in S. Now this item is in the root of S and has a null right child.
        // Set the right child of the new root to T.
    }


    // rebalance (splay)
    private void splay(Node x){
        if (x.parent == null) { // balanced
            root = x;
            return;
        }
        // rebalance so that the great grandparent of x (if it exists) now points to x. if gg is null, x is now the root. keep doing this until x is the root
        Node p = x.parent;
        if (p.parent == null) {
            // zig -- should only be done as the last step in a splay op when x has an odd depth at the beginning of the operation
            rotate(x);
        }
        else{
            if ((p.parent.left == p && p.left == x) || (p.parent.right == p && p.right == x)) {
                // zig zag -- p and x are both left children or right children
                // rotate tree on edge joining p and its parent g
                rotate(p);
                // rotate tree on edge joining x with p
                rotate(x);
            }
            else{
                if ((p.parent.left == p && p.right == x) || (p.parent.right == p && p.left == x)) {
                    // zig zag -- x is opposite type of child
                    // rotate tree on edge between p and x
                    rotate(x);
                    // rotate tree on resulting edge between x and g
                    rotate(x);
                }
                else{
                    System.out.println("ERROR!!!");
                }
            }
        }      
        // keep going until x is root  
        splay(x);
    }

    private void rotate(Node x){
        boolean goRight = false;
        if (x.parent.left == x) {
            goRight = true;  
        }
        Node p = x.parent;
        x.parent = x.parent.parent;
        if (x.parent != null) {
            if (x.parent.left == p) {
                x.parent.left = x;
            } else {
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
        if (x.right != null) {
            x.right.parent = p;
        }
        x.right = p;
        p.parent = x;
    }
    
    private void rotateLeft(Node x, Node p){
        p.right = x.left;
        if (x.left != null) {
            x.left.parent = p;
        }
        x.left = p;
        p.parent = x;
    }

    
     

    public void print(){
        // does a inOrder print (which should always be alphabetical)
        System.out.println("Current Tree:");
        inOrder(root, "");
    }

    private void inOrder(Node c, String s){
        if (c == null) {
            return;
        }
        inOrder(c.left, s + "   ");
        System.out.println(s + c.word + ": " + c.definition);
        inOrder(c.right, s + "   ");
    }

    public Node find(String word) {
        Node current = root;
        while (current != null && !current.word.equals(word)) {
            if (current.word.compareTo(word) > 0) {
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

        tree.add("f", "");
        tree.print();
        tree.add("c", "");
        tree.print();
        tree.add("d", "");
        tree.print();
        tree.add("a", "");
        tree.print();
        tree.add("b", "");
        tree.print();
        tree.add("e", "");
        tree.print();

        tree.remove("e");
        tree.print();
        tree.remove("b");
        tree.print();
        tree.remove("f");
        tree.print();
        tree.remove("c");
        tree.print();
        tree.remove("d");
        tree.print();
        tree.remove("a");
        tree.print();


        /* 
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
        */
    }
}