package me.andrewandy.datastructures;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Implementation of a doubly-linked list.
 *
 * @param <E> A generic type, can be anything.
 */
public class LinkedList<E> implements Collection<E> {

    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    public LinkedList() {
    }

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

    private Node<E> insertElement(final Node<E> prev, E element) {
        final Node<E> prevNext = prev.next;
        final Node<E> newNode = new Node<>(element);
        newNode.previous = prev;
        prev.next = newNode;
        newNode.next = prevNext;
        if (prevNext == null) {
            this.tail = new Node<>();
            this.tail.previous = newNode;
            newNode.next = this.tail;
        } else {
            prevNext.previous = newNode;
        }
        this.size++;
        return newNode;
    }

    private void removeNode(final Node<E> node) {
        if (this.size == 0) {
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
    public void addAll(final E[] iterable) {
        for (final E e : iterable) {
            add(e);
        }
    }

    public void add(final E element) {
        tailAdd(element);
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

    @Override public void removeAll(final E[] iterable) {
        for (final E e : iterable) {
            remove(e);
        }
    }

    @Override public void clear() {
        switch (this.size) {
            case 0:
                return;
            default:
                this.tail = null;
            case 1:
                this.head = null;
        }
        size = 0;
    }

    public int size() {
        return this.size;
    }

    @Override public boolean contains(final E element) {
        return indexOf(element) != -1;
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

    public void add(final int index, final E element) {
        final Node<E> node = getNode(index);
        insertElement(node, element);
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

    private Node<E> getNode(final int index) {
        if (index == 0) {
            return head;
        } else if (index + 1 == size) {
            return tail;
        } else if (index + 1 > size) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> node = head;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
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


    private class NodeIterator implements Iterator<E> {

        private Node<E> current;

        public NodeIterator() {
            this.current = head;
        }

        @Override public boolean hasNext() {
            return this.current != null && this.current.next != null;
        }

        @Override public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            final E val = current.val;
            current = current.next;
            return val;
        }

        @Override public void remove() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            final Node<E> next = current.next;
            removeNode(current);
            current = next;
        }
    }
}
