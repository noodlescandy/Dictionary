public class SplayTree {
    class Node{
        private Node parent;
        private Node left;
        private Node right;
        private String word;
        private Definition definition;

        public Node(Node parent, Node left, Node right, String word, String def) {
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.word = word;
            this.definition = new Definition(null, def);
        }
        public void addDefinition(String def){
            Definition current = definition;
            while (current.next != null) {
                current = current.next;
            }
            current.next = new Definition(null, def);
        }
        public String getDefinitions(){
            if (definition.next == null) {
                return definition.words;
            }
            String out = "";
            Definition current = definition;
            while (current != null) {
                out += "\n - " + current.words;
                current = current.next;
            }
            return out;
        }
    }
    class Definition{
        private String words;
        private Definition next;
        public Definition(Definition next, String definition){
            this.next = next;
            words = definition;
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
            if (current.word.equals(key)) {
                current.addDefinition(definition);
                splay(current);
                return;
            }
            if (comesBefore(current.word, key)) {
                if (current.left == null) {
                    current.left = new Node(current, null, null, key, definition);
                    splay(current.left);
                    return;
                }
                current = current.left;
            } 
            else{
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

    public void preOrder(){
        preOrder(root, "");
    }

    private void preOrder(Node c, String s){
        if (c == null) {
            return;
        }
        System.out.println(s + c.word);
        preOrder(c.left, s + "   ");
        preOrder(c.right, s + "   ");
    }
    
    public void print(){
        System.out.println("\nCurrent Words:");
        inOrder(root);
    }
    
    public void print(String start, String end){
        inOrder(root, start, end);
    }

    private void inOrder(Node c){
        if (c == null) {
            return;
        }
        inOrder(c.left);
        System.out.println(c.word + ": " + c.getDefinitions());
        inOrder(c.right);
    }

    private boolean inOrder(Node c, String startWord, String endWord){
        if (c == null) {
            return false;
        }
        inOrder(c.left, startWord, endWord);
        if (comesBefore(c.word, startWord) && !comesBefore(c.word, endWord)) {
            System.out.println(c.word + ": " + c.getDefinitions());
        }   
        inOrder(c.right, startWord, endWord);
        return false;
    }
    
    public String find(String word){
        Node out = find(word, true);
        return out == null ? word + " not found." : out.word + ": " + out.getDefinitions();
    }

    public Node find(String word, boolean notListing) {
        Node current = root;
        Node parent = null;
        while (current != null && !current.word.equals(word)) {
            if (comesBefore(current.word, word)) {
                parent = current;
                current = current.left;
            }
            else{
                parent = current;
                current = current.right;
            }
        }
        if (current != null) {
            if (notListing) {
                splay(current);
            }
            return current;
        }
        if (notListing) {
            return null;
        }
        return parent; // closest word
    }

    /**
     * Compares the first String and the second string, determining which comes first.
     * However, for this program, uppercase letters must show up after their lowercase equivalent rather than before as in the compareTo String method to match the order in dict1ordered.txt
     * Additionally, in all other cases, the comparison should ignore the case of the word.
     * 
     * This method returns true if the first string comes first in this ordering and false if it comes after.
     */
    private boolean comesBefore(String first, String second){
        int compareIgnoreCase = first.toLowerCase().compareTo(second.toLowerCase());
        if (compareIgnoreCase == 0) {
            int compare = first.compareTo(second);
            compare *= -1;
            return compare >= 0;
        }
        return compareIgnoreCase >= 0;
    }
}