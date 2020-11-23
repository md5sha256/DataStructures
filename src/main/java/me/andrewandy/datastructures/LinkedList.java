package me.andrewandy.datastructures;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class LinkedList<E> implements Collection<E> {

    private Node<E> start = new Node<>();
    private Node<E> last = start;
    private int size = 0;

    private static <E> void insertNode(final Node<E> prev, final Node<E> toInsert) {
        final Node<E> next = prev.next;
        prev.next = toInsert;
        toInsert.next = next;
        toInsert.previous = prev;
    }

    private void removeNode(final Node<E> node) {
        if (this.size == 0) {
            return;
        }
        final Node<E> prev = node.previous;
        final Node<E> next = node.next;
        if (node == start) {
            if (next == null) {
                start = new Node<>();
                last = start;
            } else {
                start = next;
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
        if (size == 0) {
            start.val = element;
            last = new Node<>();
            start.next = last;
            last.previous = start;
            size = 1;
            return;
        }
        insertNode(last, new Node<>(element));
        size++;
    }

    @Override public boolean remove(final E e) {
        if (this.size == 0) {
            return false;
        }
        int oldSize = this.size;
        Node<E> current = start;
        if (e == null) {
            while (current != null) {
                if (current.val == null) {
                    removeNode(current);
                }
                current = current.next;
            }
        } else {
            while (current != null) {
                if (Objects.equals(current.val, e)) {
                    removeNode(current);
                }
                current = current.next;
            }
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
        this.start.next = null;
        this.start.set(null);
        this.last = start;
        this.size = 0;
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
            return start.val;
        } else if (index == this.size - 1) {
            return last.val;
        }
        Node<E> current = start;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.val;
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
        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException();
        }
        if (index == size - 1) {
            removeNode(last);
            return;
        }
        Node<E> node = start;
        for (int i = 0; i < index - 1 && node != null; i++) {
            node = node.next;
        }
        removeNode(node);
    }

    public int indexOf(final E element) {
        if (this.size == 0) {
            return -1;
        }
        Node<E> current = start;
        if (element == null) {
            for (int i = 0; current != null; i++) {
                if (current.val == null) {
                    return i;
                }
                current = current.next;
            }
        } else {
            for (int i = 0; current != null; i++) {
                if (Objects.equals(current.val, element)) {
                    return i;
                }
                current = current.next;
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
        Node<E> previous;
        private E val;

        public Node(final E e) {
            set(e);
        }

        public Node() {

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

        public NodeIterator() {
            this.current = start;
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

    @Override public String toString() {
       final Object[] arr = new Object[this.size];
       int i = 0;
       for (E e : this) {
           arr[i++] = e;
       }
       return Arrays.toString(arr);
    }
}
