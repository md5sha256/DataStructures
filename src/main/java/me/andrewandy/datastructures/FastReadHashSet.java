package me.andrewandy.datastructures;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class FastReadHashSet<T> implements Collection<T> {

    private Node<T>[] nodes;

    private int size;
    private int arrayLen;

    private final float loadCapacity;

    public FastReadHashSet(int initialCap, float loadCapacity) {
        if (loadCapacity < 0 || loadCapacity > 1) {
            throw new IllegalArgumentException("Invalid load capacity!");
        }
        if (initialCap < 1) {
            throw new IllegalArgumentException("Invalid initial capacity!");
        }
        this.nodes = new Node[initialCap];
        this.arrayLen = nodes.length;
        this.loadCapacity = loadCapacity;
    }

    public Node<T> getNode(T object) {
        if (object == null) {
            return null;
        }
        if (this.size == 0) {
            return null;
        }
        if (this.size == 1) {
            return this.nodes[0];
        }
        return this.nodes[hash(object) % arrayLen];
    }


    public boolean contains(T object) {
        if (object == null || this.size == 0) {
            return false;
        }
        final Node<T> node = this.nodes[hash(object) % arrayLen];
        return node != null && node.chain.contains(object);
    }

    private static int hash(Object o) {
        final int h = o.hashCode();
        return h < 0 ? -h : h;
    }

    public void add(T object) {
    }

    private void add(T object, boolean resize) {
        if (object == null) {
            throw new IllegalArgumentException("Does not support null types!");
        }
        if (contains(object)) {
            return;
        }
        if (resize) {
            this.nodes = resize(1);
            this.arrayLen += 1;
            checkAndRehash();
        }
        Node<T> node = getNode(object);
        if (node == null) {
            node = new Node<>();
        }
        node.chain.add(object);
        this.nodes[hash(object.hashCode()) % arrayLen] = node;
        size++;
    }

    public void addAll(Collection<T> objects) {
        if (objects.size() == 0) {
            return;
        }
        for (T t : objects) {
            add(t, false);
        }
        Node<T>[] arr = resize(objects.size());
        rehash(arr);
    }

    @Override
    public void addAll(T[] objects) {
        if (objects.length == 0) {
            return;
        }
        for (T t : objects) {
            add(t, false);
        }
        Node<T>[] arr = resize(objects.length);
        rehash(arr);
    }

    public void remove(T object) {
        remove(object, true);;
    }

    private void remove(T object, boolean rehash) {
        if (object == null) {
            return;
        }
        final Node<T> node = getNode(object);
        if (node != null) {
            node.chain.remove(object);
            if (node.chain.size() == 0) {
                this.nodes[hash(object.hashCode()) % arrayLen] = null;
            }
            size--;
            if (rehash) {
                checkAndRehash();
            }
        }
    }

    private void checkAndRehash() {
        final int resize = calculateResizeAmount();
        if (Math.abs(resize) >= (1 - loadCapacity) * this.size) {
            Node<T>[] nodes = resize(resize);
            rehash(nodes);
        }
    }

    @Override
    public void removeAll(Collection<T> objects) {
        if (objects.size() == 0 || this.size == 0) {
            return;
        }
        for (T t : objects) {
            remove(t, false);
        }
        checkAndRehash();
    }

    @Override
    public void removeAll(T[] objects) {
        if (objects.length == 0 || this.size == 0) {
            return;
        }
        for (T t : objects) {
            remove(t, false);
        }
        checkAndRehash();
    }

    @Override
    public void clear() {
        this.nodes = new Node[(int) Math.ceil(this.loadCapacity)];
        this.size = 0;
        this.arrayLen = this.nodes.length;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<T> iterator() {
        return new NodeIterator();
    }

    private int calculateResizeAmount() {
        int len = arrayLen;
        int nulls = 0;
        for (Node<T> node : this.nodes) {
            if (node == null) {
                nulls++;
            }
        }
        int cap = len - nulls;
        int toResize = (int) ((len * loadCapacity) - cap);
        return toResize < 0 ? -1 : 1;
    }

    private Node<T>[] resize(int amount) {
        Node<T>[] prev = this.nodes;
        amount += calculateResizeAmount();
        this.nodes = new Node[arrayLen + amount];
        this.arrayLen = arrayLen + amount;
        return prev;
    }

    private void rehash(Node<T>[] elements, int... toExclude) {
        if (toExclude == null && elements.length > this.nodes.length) {
            throw new IllegalArgumentException("Cannot rehash larger array!");
        }
        if (toExclude != null) {
            int i = 0;
            for (Node<T> node : elements) {
                if (Arrays.binarySearch(toExclude, i++) != -1) {
                    continue;
                }
                if (node == null) {
                    continue;
                }
                int index = node.hashCode() % arrayLen;
                index = index < 0 ? -index : index;
                this.nodes[index] = node;
            }
        } else {
            for (Node<T> node : elements) {
                if (node == null) {
                    continue;
                }
                int index = node.hashCode() % arrayLen;
                index = index < 0 ? -index : index;
                this.nodes[index] = node;
            }
        }
    }


    private static class Node<E> {

        private final LinkedList<E> chain = new LinkedList<>();

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?> node = (Node<?>) o;
            return Objects.equals(chain, node.chain);
        }

        @Override
        public int hashCode() {
            return chain.size() == 0 ? chain.iterator().next().hashCode() : 0;
        }
    }

    private class NodeIterator implements Iterator<T> {

        private int index = 0;
        private int bucketIndex = 0;

        @Override
        public boolean hasNext() {
            return getNextBucket() != null;
        }

        @Override
        public T next() {
            final Node<T> node = getNextBucket();
            if (node == null) {
                throw new NoSuchElementException();
            }
            return node.chain.get(bucketIndex++);
        }

        private Node<T> getNextBucket() {
            if (this.index == FastReadHashSet.this.nodes.length - 1) {
                return null;
            }
            Node<T> node = FastReadHashSet.this.nodes[index];
            if (node == null) {
                ++index;
                bucketIndex = 0;
                return getNextBucket();
            }
            if (bucketIndex == node.chain.size() - 1) {
                ++index;
                bucketIndex = 0;
                node = FastReadHashSet.this.nodes[index];
            }
            return node == null ? getNextBucket() : node;
        }

        @Override
        public void remove() {
            FastReadHashSet.this.remove(FastReadHashSet.this.nodes[index].chain.get(bucketIndex));
        }
    }

}
