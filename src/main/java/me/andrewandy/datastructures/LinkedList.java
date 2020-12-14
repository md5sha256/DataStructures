package me.andrewandy.datastructures;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Implementation of a doubly-linked list
 *
 * @param <E> A generic type, can be anything
 */
public class LinkedList<E> implements Collection<E> {

    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    public LinkedList() {
    }

    /**
     * Add an element to the tail of this list | Worst-Case Time Complexity = O(1)
     *
     * @param element The instance to append
     */
    private void tailAdd(final E element) {
        switch (size) {
            case 0:
                head = new Node<>(element);
                tail = new Node<>();
                head.next = tail;
                tail.previous = head;
                break;
            case 1:
                final Node<E> newNode = new Node<>(element);
                head.next = newNode;
                newNode.previous = head;
                tail.previous = newNode;
                break;
            default:
                final Node<E> node = new Node<>(element);
                tail.previous.next = node;
                node.previous = tail.previous;
                tail.previous = node;
                node.next = tail;
                break;
        }
        size++;
    }

    /**
     * Insert an element with respect to a given node.
     * This method will instantiate a new node, append all relevant
     * node references and increment {@link #size}.
     * Worst-Case Time Complexity = O(1)
     *
     * @param prev    The previous node to take reference from
     * @param element The instance to insert
     * @return Returns the instantiated {@link Node} instance
     */
    private Node<E> insertElement(final Node<E> prev, E element) {

        final Node<E> prevNext = prev.next;
        // Instantiate new node
        final Node<E> newNode = new Node<>(element);
        // Update node references
        newNode.previous = prev;
        prev.next = newNode;
        newNode.next = prevNext;
        // If prevNext == null means this is the tail node
        if (prevNext == null) {
            // Update tail reference
            this.tail = new Node<>();
            this.tail.previous = newNode;
            newNode.next = this.tail;
        } else {
            // Update the 'previous' reference of the prev node.
            prevNext.previous = newNode;
        }
        // Increment size
        this.size++;
        return newNode;
    }

    /**
     * Remove a given node | Time complexity = O(1)
     *
     * @param node The node instance to remove
     */
    private void removeNode(final Node<E> node) {
        if (this.size == 0) {
            // List is empty, don't need to remove anything.
            return;
        }
        final Node<E> prev = node.previous;
        final Node<E> next = node.next;
        if (node == head) {
            if (next == null) {
                head = new Node<>();
                tail = head;
            } else {
                head = next;
            }
            this.size--;
        } else {
            if (prev != null) {
                prev.next = next;
            }
            if (next != null) {
                next.previous = prev;
            }
            if (prev != null || next != null) {
                this.size--;
            }
        }
    }

    @Override
    public void addAll(final Collection<E> collection) {
        for (final E e : collection) {
            add(e);
        }
    }

    @Override
    public void addAll(final E[] array) {
        for (final E e : array) {
            add(e);
        }
    }

    @Override
    public void add(final E element) {
        tailAdd(element);
    }

    public void add(final int index, final E element) {
        final Node<E> node = getNode(index);
        insertElement(node, element);
    }

    @Override
    public boolean remove(final E e) {
        if (this.size == 0) {
            return false;
        }
        int oldSize = this.size;
        Node<E> current = head;
        while (current != null) {
            if (Objects.equals(current.val, e)) {
                removeNode(current);
            }
            current = current.next;
        }
        return this.size != oldSize;
    }
    
    public void remove(final int index) {
        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException();
        }
        if (index == size - 1) {
            removeNode(tail);
            return;
        } else if (index == 0) {
            removeNode(head);
            return;
        }
        Node<E> node = head;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        removeNode(node);
    }

    @Override
    public boolean removeFirst(final E e) {
        if (this.size == 0) {
            return false;
        }
        int oldSize = this.size;
        Node<E> current = head;
        while (current != null) {
            if (Objects.equals(current.val, e)) {
                removeNode(current);
                break;
            }
            current = current.next;
        }
        return this.size != oldSize;
    }

    @Override public void removeAll(final Collection<E> collection) {
        for (final E e : collection) {
            remove(e);
        }
    }

    @Override public void removeAll(final E[] array) {
        for (final E e : array) {
            remove(e);
        }
    }

    @Override public void clear() {
        switch (this.size) {
            case 0:
                return;
            case 1:
                this.head = null;
            default:
                this.tail = null;
        }
        size = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    /**
     * Check whether a given element is in this list
     * This is equivalent to checking if {@link #indexOf(Object)} returns -1.
     * Worst-Case Time Complexity = O(n)
     *
     * @param element The instance to check
     * @return Returns true if the element exists, false otherwise.
     */
    @Override
    public boolean contains(final E element) {
        return indexOf(element) != -1;
    }

    @Override
    public Iterator<E> iterator() {
        return new NodeIterator();
    }

    @Override
    public String toString() {
        final Object[] arr = new Object[this.size];
        Node<E> node = head;
        for (int index = 0; index < this.size && node != null; index++) {
            arr[index] = node.val;
            node = node.next;
        }
        return Arrays.toString(arr);
    }

    public E get(final int index) {
        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            return head.val;
        } else if (index == this.size - 1) {
            return tail.val;
        }
        Node<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.val;
    }

    /**
     * Get the first index of an element | Worst-Case Time Complexity = O(n)
     *
     * @param element The instance of the element
     * @return Returns the index of the element (0 being the head) or -1 if the
     * element is not in this list.
     */
    public int indexOf(final E element) {
        if (this.size == 0) {
            return -1;
        }
        Node<E> current = head;
        for (int i = 0; current != null; i++) {
            if (Objects.equals(current.val, element)) {
                return i;
            }
            current = current.next;
        }
        return -1;
    }

    /**
     * Get a node at a specific index | Worst-Case Time Complexity = O(n), n being the index
     *
     * @param index The index, must be within 0 and 1 - {@link #size()}
     * @return Returns the {@link Node} reference at a specific index.
     * @throws IndexOutOfBoundsException Thrown if the index parameter is less than 0
     *                                   or if it is greater than the size of the list minus 1
     */
    private Node<E> getNode(final int index) {
        if (index == 0) {
            return head;
        } else if (index + 1 == size) {
            return tail;
        } else if (index + 1 > size) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> node = head;
        // Traverse through the list
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
    }

    /**
     * Represents an object which can be chained together (through object references). This class
     * holds a "next" and "previous" node references alongside the value of this "node". This class is
     * used to implement traversal through an {@link LinkedList}
     *
     * @param <E> A generic type, can be anything.
     */
    private static class Node<E> {

        Node<E> next;
        Node<E> previous;
        private E val;

        public Node(final E e) {
            this.val = e;
        }

        public Node() {
        }

        @Override
        public String toString() {
            return "Node{" + "next=" + (next == null ? "null" : next.val) + ", previous=" + (previous == null ?
                "null" :
                previous.val) + ", val=" + val + '}';
        }
    }


    /**
     * Implementation of an iterator. This class is NOT thread-safe, however, it will not attempt
     * to check for concurrent modification by any means.
     */
    private class NodeIterator implements Iterator<E> {

        private Node<E> current;

        public NodeIterator() {
            this.current = head;
        }

        @Override
        public boolean hasNext() {
            // Check whether the current node is null or the 'tail' node
            return this.current != null && this.current.next != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                // Cannot traverse to the next node
                throw new NoSuchElementException();
            }
            // Save the current value to return
            final E val = current.val;
            // Move to the next node
            current = current.next;
            return val;
        }

        @Override public void remove() {
            if (current == null) {
                // If the current node is a null reference, throw and error.
                // At this point, the iterator should be considered invalid.
                throw new NoSuchElementException();
            }
            // Get the reference of the "next" node
            final Node<E> next = current.next;
            // Remove the current node
            removeNode(current);
            // Re-assign the object reference to the next node (which may be null)
            current = next;
        }
    }
}
