package me.andrewandy.datastructures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class LinkedList<E> implements Collection<E> {

    private Node<E> start = new Node<>();
    private Node<E> last = start;
    private int size;

    @Override
    public void addAll(Collection<E> collection) {
        for (E e : collection) {
            add(e);
        }
    }

    @Override
    public void addAll(E[] iterable) {
        for (E e : iterable) {
            add(e);
        }
    }

    public E get(int index) {
        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException();
        }
        if (index == this.size - 1) {
            return last.get();
        }
        int i = 0;
        Node<E> current = start;
        while (i < index) {
            current = current.next;
            i++;
        }
        return current.get();
    }

    public void add(E element) {
        if (last == start) {
            last = new Node<>(element);
            start.next = last;
        } else {
            last.next = new Node<>(element);
        }
        size++;
    }

    @Override
    public void remove(E e) {
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            E next = iterator.next();
            if (Objects.equals(e, next)) {
                iterator.remove();
            }
        }
    }

    @Override
    public void removeAll(Collection<E> collection) {
        for (E e : collection) {
            remove(e);
        }
    }

    @Override
    public void removeAll(E[] iterable) {
        for (E e : iterable) {
            remove(e);
        }
    }

    @Override
    public void clear() {
        this.start.next = null;
        this.start.set(null);
        this.last = start;
        this.size = 0;
    }

    public void add(int index, E element) {
        if (index + 1 == size) {
            add(element);
            return;
        }
        final Node<E> node = getNode(index);
        insertNode(node, new Node<>(element));
        size++;
    }

    public void remove(int index) {
        if (index == 0) {
            this.start = start.next;
        } else {
            Node<E> prev = getNode(index - 1);
            Node<E> toRemove = getNode(index);
            prev.next = toRemove == null ? null : toRemove.next;
        }
        size--;
    }

    @Override
    public boolean contains(E element) {
        /*
        Iterator<E> iterator = new NodeIterator();
        while (iterator.hasNext()) {
            if (Objects.equals(iterator.next(), element)) {
                return true;
            }
        }
        return false;

         */
        return indexOf(element) != -1;
    }

    public int indexOf(E element) {
        if (this.size == 0) {
            return -1;
        }
        final Iterator<E> iterator = new NodeIterator();
        int index = -1;
        if (element == null) {
            while (iterator.hasNext()) {
                index++;
                if (iterator.next() == null) {
                    return index;
                }
            }
        } else {
            while (iterator.hasNext()) {
                index++;
                if (iterator.next().equals(element)) {
                    return index;
                }
            }
        }
        return index;
    }

    private static <E> void insertNode(Node<E> prev, Node<E> toInsert) {
        Node<E> next = prev.next;
        prev.next = toInsert;
        toInsert.next = next;
    }

    private Node<E> getNode(int index) {
        if (index == 0) {
            return start;
        } else if (index + 1 == size) {
            return last;
        } else if (index + 1 > size) {
            throw new IndexOutOfBoundsException();
        }
        int i = 0;
        final NodeIterator nodeIterator = new NodeIterator();
        while (i++ < index) {
            nodeIterator.next();
        }
        return nodeIterator.getCurrent();
    }

    public int size() {
        return this.size;
    }

    @Override
    public Iterator<E> iterator() {
        return new NodeIterator();
    }


    private static class Node<E> {

        Node<E> next;
        private E val;

        public Node(E e) {
            set(e);
        }

        public Node() {

        }

        public E get() {
            return val;
        }

        public void set(E value) {
            this.val = value;
        }

        Node<E> getNext() {
            return next;
        }
    }

    private class NodeIterator implements Iterator<E> {

        private Node<E> current;
        private Node<E> prev;

        public NodeIterator() {
            this.current = start;
        }

        Node<E> getCurrent() {
            return this.current;
        }

        @Override
        public boolean hasNext() {
            return LinkedList.this.last == this.current;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            prev = current;
            current = current.next;
            return current.get();
        }

        @Override
        public void remove() {
            if (prev == null) {
                return;
            }
            --LinkedList.this.size;
            prev.next = current == null ? null : current.next;
        }
    }

}
