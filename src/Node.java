public class Node {
    private char val;
    private Node next;

    public Node(char v){
        val = v;
        next = null;
    }
    public char getVal(){
        return val;
    }
    public Node getNext(){
        return next;
    }
    public void setNext(Node node){
        this.next = node;
    }
}
