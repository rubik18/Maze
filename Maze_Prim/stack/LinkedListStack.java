package stack;

import java.util.Iterator;

public class LinkedListStack<E> implements StackInterface<E> {

    class Node {
        E element;
        Node next;
    }

    Node stack = null;

    @Override
    public void push(E element) {
        if (stack == null) {
            stack = new Node();
            stack.element = element;
            stack.next = null;
        } else {
            Node temp = new Node();
            temp.element = element;
            temp.next = stack;
            stack = temp;
        }
    }
    
    public E peek() throws NullPointerException{
    	if (stack != null) {
            return stack.element;
    	}
    	throw new NullPointerException("Stack is empty");
    }

    @Override
    public E pop() throws NullPointerException {
        if (stack != null) {
            E elemt = stack.element;
            stack = stack.next;
            return elemt;

        }
        throw new NullPointerException("Stack is empty");
    }

    @Override
    public boolean isEmpty() {
        return stack == null;
    }

    @Override
    public E top() throws NullPointerException {
       if(stack != null){
           return stack.element;
       }
        throw new NullPointerException("Stack is empty");
    }

    public void print(){
        Iterator<E> it = new StackIterator();
        while(it.hasNext()){
            System.out.print(it.next() + " ");
        }
        System.out.println();
    }
    @Override
    public Iterator<E> iterator() {
        return new StackIterator();
    }

    class StackIterator implements Iterator<E> {

        private Node currentNode = stack;

        @Override
        public boolean hasNext() {

            return currentNode != null;
        }

        @Override
        public E next() {

            E data = currentNode.element;
            currentNode = currentNode.next;
            return data;
        }
    }
}
