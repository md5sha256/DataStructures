package me.andrewandy.datastructures;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<E> implements Collection<E> {

    private Node<E> start = new Node<>();
    private Node<E> last = start;
    private int size;

    private static <E> void insertNode(final Node<E> prev, final Node<E> toInsert) {
        final Node<E> next = prev.next;
        prev.next = toInsert;
        toInsert.next = next;
    }

    @Override public void addAll(final Collection<E> collection) {
        for (final E e : collection) {
            add(e);
        }
    }

    @Override public void addAll(final E[] iterable) {
        for (final E e : iterable) {
            add(e);
        }
    }

    public void add(final E element) {
        if (last == start) {
            last = new Node<>(element);
            start.next = last;
        } else {
            last.next = new Node<>(element);
            this.last = last.next;
        }
        size++;
    }

    @Override public boolean remove(final E e) {
        if (this.size == 0) {
            return false;
        }
        Node<E> node = start;
        Node<E> prev = node;
        boolean mod = false;
        if (e == null) {
            while (node != null) {
                if (node.val == null) {
                    prev.next = node.next;
                    mod = true;
                    this.size--;
                }
                prev = node;
                node = node.next;
            }
        } else {
            while (node != null) {
                if (e.equals(node.val)) {
                    prev.next = node.next;
                    mod = true;
                    this.size--;
                }
                prev = node;
                node = node.next;
            }
        }
        return mod;
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
        this.start.next = null;
        this.start.set(null);
        this.last = start;
        this.size = 0;
    }

    public int size() {
        return this.size;
    }

    @Override public boolean contains(final E element) {
        return indexOf(element) >= 0;
    }

    public E get(final int index) {
        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException();
        }
        if (index == this.size - 1) {
            return last.get();
        } else if (index == 0) {
            return start.next.get();
        }
        int i = 0;
        Node<E> current = start;
        while (i < index) {
            current = current.next;
            i++;
        }
        return current.get();
    }

    public void add(final int index, final E element) {
        if (index + 1 == size) {
            add(element);
            return;
        }
        final Node<E> node = getNode(index);
        insertNode(node, new Node<>(element));
        size++;
    }

    public void remove(final int index) {
        if (index == 0) {
           if (this.start.next == null) {
               this.last = this.start;
               this.start.val = null;
               this.size = 0;
           } else {
               this.start = this.start.next;
           }
        } else {
            final Node<E> prev = getNode(index - 1);
            final Node<E> toRemove = getNode(index);
            prev.next = toRemove == null ? null : toRemove.next;
        }
        size--;
    }

    public int indexOf(final E element) {
        if (this.size == 0) {
            return -1;
        }
        int index = 0;
        if (element == null) {
            for (E e : this) {
                if (e == null) {
                    return index;
                }
                index++;
            }
        } else {
            for (E e : this) {
                if (element.equals(e)) {
                    return index;
                }
                index++;
            }
        }
        return -1;
    }

    private Node<E> getNode(final int index) {
        if (index == 0) {
            return start;
        } else if (index + 1 == size) {
            return last;
        } else if (index + 1 > size) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> node = start;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
    }

    @Override public Iterator<E> iterator() {
        return new NodeIterator();
    }


    private static class Node<E> {

        Node<E> next;
        private E val;

        public Node(final E e) {
            set(e);
        }

        public Node() {

        }

        public E get() {
            return val;
        }

        public void set(final E value) {
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
            this.prev = start;
        }

        Node<E> getCurrent() {
            return this.current;
        }

        @Override public boolean hasNext() {
            return this.current != null && this.current.next != null;
        }

        @Override public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            prev = current;
            current = current.next;
            return current.get();
        }

        @Override public void remove() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            prev.next = current.next;
        }
    }

    @Override public String toString() {
       final Object[] arr = new Object[this.size];
       int i = 0;
       for (E e : this) {
           arr[i++] = e;
       }
       return Arrays.toString(arr);
    }
}
