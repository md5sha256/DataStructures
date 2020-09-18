package me.andrewandy.datastructures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class FastWriteHashSet<T> implements Collection<T> {

    private final Node<T>[] nodes;
    private final int arrayLen;
    private int size;


    public FastWriteHashSet(int bucketSize) {
        if (bucketSize < 1) {
            throw new IllegalArgumentException("Invalid initial capacity: " + bucketSize);
        }
        this.nodes = new Node[bucketSize];
        for (int i = 0; i < bucketSize; i++) {
            this.nodes[i] = new Node<>();
        }
        this.arrayLen = bucketSize;
    }

    private static int hash(Object o) {
        final int h = o.hashCode();
        return h < 0 ? -h : h;
    }

    public Node<T> getNode(T object) {
        if (object == null) {
            return null;
        }
        return this.nodes[hash(object) % arrayLen];
    }

    public void addAll(Collection<T> objects) {
        if (objects.size() == 0) {
            return;
        }
        for (T t : objects) {
            add(t);
        }
    }

    @Override public void addAll(T[] objects) {
        if (objects.length == 0) {
            return;
        }
        for (T t : objects) {
            add(t);
        }
    }

    public void add(T object) {
        if (object == null) {
            throw new IllegalArgumentException("Does not support null types!");
        }
        Node<T> node = getNode(object);
        if (!node.chain.contains(object)) {
            node.chain.add(object);
            size++;
        }
    }

    public void remove(T object) {
        if (object == null || this.size == 0) {
            return;
        }
        final Node<T> node = getNode(object);
        if (node.chain.contains(object)) {
            node.chain.remove(object);
            size--;
        }
    }

    @Override public void removeAll(Collection<T> objects) {
        if (objects.size() == 0 || this.size == 0) {
            return;
        }
        for (T t : objects) {
            remove(t);
        }
    }

    @Override public void removeAll(T[] objects) {
        if (objects.length == 0 || this.size == 0) {
            return;
        }
        for (T t : objects) {
            if (t == null) {
                continue;
            }
            remove(t);
        }
    }

    @Override public void clear() {
        if (this.size == 0) {
            return;
        }
        for (Node<T> node : this.nodes) {
            node.chain.clear();
        }
        this.size = 0;
    }

    @Override public int size() {
        return this.size;
    }

    public boolean contains(T object) {
        if (object == null || this.size == 0) {
            return false;
        }
        return getNode(object).chain.contains(object);
    }

    @Override public Iterator<T> iterator() {
        return new NodeIterator();
    }


    private static class Node<E> {

        private final LinkedList<E> chain = new LinkedList<>();

        @Override public int hashCode() {
            return chain.size() == 0 ? chain.iterator().next().hashCode() : 0;
        }

        @Override public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Node<?> node = (Node<?>) o;
            return Objects.equals(chain, node.chain);
        }
    }


    private class NodeIterator implements Iterator<T> {

        private int index = 0;
        private int bucketIndex = 0;
        private boolean removed;

        @Override public boolean hasNext() {
            return getNextBucket() != null;
        }

        @Override public T next() {
            final Node<T> node = getNextBucket();
            if (node == null) {
                throw new NoSuchElementException();
            }
            removed = false;
            return node.chain.get(bucketIndex++);
        }

        @Override public void remove() {
            if (removed) {
                throw new NoSuchElementException();
            }
            FastWriteHashSet.this.nodes[index].chain.remove(bucketIndex);
            size--;
            removed = true;
        }

        private Node<T> getNextBucket() {
            if (this.index == FastWriteHashSet.this.arrayLen - 1) {
                return null;
            }
            Node<T> node = FastWriteHashSet.this.nodes[index];
            if (node.chain.size() == 0 || this.bucketIndex > node.chain.size()) {
                index += 1;
                bucketIndex = 0;
                return getNextBucket();
            }
            return node;
        }
    }

}
