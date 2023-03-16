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
                if (current.left == null) {
                    current.left = new Node(current, null, null, key, definition);
                    splay(current.left);
                    return;
                }
                current = current.left;
            } else {
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
        find(word);
        Node leftNode = root.left;
        Node rightNode = root.right;
        if (leftNode == null && rightNode == null) {
            root = null;
            return;
        }

        leftNode.parent = null;
        Node current = leftNode;
        while(current.right != null){
            current = current.right;
        }
        splay(current);

        root.right = rightNode;
        if (root.right != null) {
            root.right.parent = root;
        }
    }


    // rebalance (splay)
    private void splay(Node x){
        if (x.parent == null) {
            root = x;
            return;
        }

        Node p = x.parent;
        if (p.parent == null) {
            // zig
            rotate(x);
        }
        else{
            if ((p.parent.left == p && p.left == x) || (p.parent.right == p && p.right == x)) {
                // zig zig
                rotate(p);
                rotate(x);
            }
            else{
                if ((p.parent.left == p && p.right == x) || (p.parent.right == p && p.left == x)) { 
                    // zig zag
                    rotate(x);
                    rotate(x);
                }
                else{
                    System.out.println("ERROR!!!");
                }
            }
        }      
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
        System.out.println("words:");
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
        if (current != null) {
            splay(current);
        }
        return current;
    }

    // tester for debug
    /*
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
    }
    */
}