package me.andrewandy.datastructures;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.LinkedList;

/*
 * Broken implementation
 */
@Deprecated
public class DynamicHashSet<T> implements Collection<T> {

    private final float loadCapacity;
    private Node<T>[] nodes;
    private int size;

    public DynamicHashSet() {
        this(10, 0.75f);
    }

    public DynamicHashSet(final int initialCap, final float loadCapacity) {
        if (loadCapacity < 0 || loadCapacity > 1) {
            throw new IllegalArgumentException("Invalid load capacity!");
        }
        if (initialCap < 1) {
            throw new IllegalArgumentException("Invalid initial capacity!");
        }
        this.nodes = new Node[initialCap];
        this.loadCapacity = loadCapacity;
    }

    private static int hash(final Object o) {
        final int h = o.hashCode();
        return h < 0 ? -h : h;
    }

    public Node<T> getNode(final T object) {
        if (object == null) {
            return null;
        }
        if (this.size == 0) {
            return null;
        }
        return this.nodes[hash(object) % this.nodes.length];
    }

    private void add(final T object, final boolean resize) {
        if (object == null) {
            throw new IllegalArgumentException("Does not support null types!");
        }
        Node<T> node = getNode(object);
        if (node != null && node.chain.contains(object)) {
            return;
        }
        if (node == null) {
            // Don't resize if there is space
            final Node<T> newNode = new Node<>();
            this.nodes[hash(object) % this.nodes.length] = newNode;
            newNode.chain.add(object);
            this.size++;
            return;
        }

        if (resize) {
            checkAndRehash(1);
        }
        node = getNode(object);
        if (node == null) {
            node = new Node<>();
            this.nodes[hash(object) % this.nodes.length] = node;
        }
        node.chain.add(object);
        this.size++;
    }

    public void addAll(final Collection<T> objects) {
        if (objects.size() == 0) {
            return;
        }
        final int size = this.size;
        for (final T t : objects) {
            add(t, false);
        }
        final Node<T>[] arr = resize(this.size - size);
        rehash(arr);
    }


    @Override public void addAll(final T[] objects) {
        if (objects.length == 0) {
            return;
        }
        final int size = this.size;
        for (final T t : objects) {
            add(t, false);
        }
        final Node<T>[] arr = resize(this.size - size);
        rehash(arr);
    }

    public void add(final T object) {
        add(object, true);
    }

    public boolean remove(final T object) {
        return remove(object, true);
    }

    @Override
    public boolean removeFirst(final T t) {
        return remove(t, true);
    }

    @Override public void removeAll(final Collection<T> objects) {
        if (objects.size() == 0 || this.size == 0) {
            return;
        }
        for (final T t : objects) {
            remove(t, false);
        }
        checkAndRehash();
    }

    @Override public void removeAll(final T[] objects) {
        if (objects.length == 0 || this.size == 0) {
            return;
        }
        for (final T t : objects) {
            remove(t, false);
        }
        checkAndRehash();
    }

    @Override public void clear() {
        this.nodes = new Node[(int) Math.ceil(this.loadCapacity)];
        this.size = 0;
    }

    @Override public int size() {
        return this.size;
    }

    public boolean contains(final T object) {
        if (object == null || this.size == 0) {
            return false;
        }
        final Node<T> node = this.nodes[hash(object) % this.nodes.length];
        return node != null && node.chain.contains(object);
    }

    private boolean remove(final T object, final boolean rehash) {
        final Node<T> node = getNode(object);
        if (node == null || !node.chain.removeFirstOccurrence(object)) {
            return false;
        }
        size--;
        if (node.chain.size() == 0) {
            this.nodes[hash(object) % this.nodes.length] = null;
            // Only check for rehashing if the bucket has been removed
            if (rehash) {
                checkAndRehash(-1);
            }
        }
        return true;
    }

    private void checkAndRehash() {
        final int amt = calculateResizeAmount();
        if (amt != 0) {
            final Node<T>[] nodes = resize(amt);
            rehash(nodes);
        }
    }

    private void checkAndRehash(int extra) {
        final int amt = calculateResizeAmount() + extra;
        if (amt != 0) {
            final Node<T>[] nodes = resize(amt);
            rehash(nodes);
        }
    }

    @Override public Iterator<T> iterator() {
        return new NodeIterator();
    }

    private int calculateResizeAmount() {
        final int len = this.nodes.length;
        int nulls = 0;
        for (final Node<T> node : this.nodes) {
            if (node == null) {
                nulls++;
            }
        }
        final int filledBuckets = len - nulls;
        final int maxFilled = (int) Math.ceil(loadCapacity * len);
        return filledBuckets - maxFilled;
    }

    private Node<T>[] resize(int amount) {
        final Node<T>[] prev = this.nodes;
        int amt = this.nodes.length + amount + calculateResizeAmount();
        this.nodes = new Node[amt <= 0 ? 10 : amt];
        return prev;
    }

    private void rehash(final Node<T>[] elements) {
        for (final Node<T> node : elements) {
            if (node == null) {
                continue;
            }
            //System.out.println(node.chain.toString());
            for (T element : node.chain) {
                int index = hash(element) % this.nodes.length;
                Node<T> newNode;
                if ((newNode = this.nodes[index]) == null) {
                    newNode = new Node<>();
                }
                newNode.chain.add(element);
            }
        }
    }

    @Override
    public String toString() {
        return "DynamicHashSet{" + "loadCapacity=" + loadCapacity + ", nodes=" + Arrays.toString(nodes) + ", size="
            + size + '}';
    }

    private static class Node<E> {

        private final LinkedList<E> chain = new LinkedList<>();

        @Override
        public int hashCode() {
            if (chain.size() == 0) {
                return 0;
            }
            return chain.get(0).hashCode();
        }

        @Override public boolean equals(final Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            final Node<?> node = (Node<?>) o;
            return Objects.equals(chain, node.chain);
        }

        @Override
        public String toString() {
            return "Node{" + "chain=" + chain + '}';
        }
    }


    private class NodeIterator implements Iterator<T> {

        private int index = 0;
        private int bucketIndex = 0;

        @Override public boolean hasNext() {
            return getNextBucket() != null;
        }

        @Override public T next() {
            final Node<T> node = getNextBucket();
            if (node == null) {
                throw new NoSuchElementException();
            }
            return node.chain.get(bucketIndex++);
        }

        @Override public void remove() {
            DynamicHashSet.this.remove(DynamicHashSet.this.nodes[index].chain.get(bucketIndex));
        }

        private Node<T> getNextBucket() {
            if (this.index == DynamicHashSet.this.nodes.length - 1) {
                return null;
            }
            Node<T> node = DynamicHashSet.this.nodes[index];
            if (node == null) {
                ++index;
                bucketIndex = 0;
                return getNextBucket();
            }
            if (bucketIndex == node.chain.size() - 1) {
                ++index;
                bucketIndex = 0;
                node = DynamicHashSet.this.nodes[index];
            }
            return node == null ? getNextBucket() : node;
        }
    }

}
