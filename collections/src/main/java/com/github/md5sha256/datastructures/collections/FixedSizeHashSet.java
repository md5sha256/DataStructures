package com.github.md5sha256.datastructures.collections;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Implementation of a HashSet with a predetermined number of buckets on initialization.
 * This class is not thread safe and by no means performs any concurrent modification checks.
 *
 * @param <T> A generic type, can be anything.
 */
public class FixedSizeHashSet<T> implements Collection<T> {

    private final Bucket<T>[] table;
    private int size;


    /**
     * @param numBuckets The number of buckets
     * @throws IllegalArgumentException Thrown if the buckets is less than 1.
     */
    @SuppressWarnings("unchecked")
    public FixedSizeHashSet(final int numBuckets) throws IllegalArgumentException {
        if (numBuckets < 1) {
            throw new IllegalArgumentException("Invalid initial capacity: " + numBuckets);
        }
        this.table = (Bucket<T>[]) new Bucket<?>[numBuckets];
        // Initialize nodes
        for (int i = 0; i < numBuckets; i++) {
            this.table[i] = new Bucket<>();
        }
    }

    /**
     * Hash an object
     *
     * @param o The object to hash
     * @return Returns the absolute value of the object's {@link Object#hashCode()}
     */
    private static int hash(final Object o) {
        final int h = o.hashCode();
        return h < 0 ? -h : h;
    }

    /**
     * Obtain the potential {@link Bucket} instance of a given object. The bucket
     * returned by this method does not guarantee that the object will reside in this
     * bucket, only that it SHOULD resize here based on it's @{@link #hash(Object)}
     * Worst-Case Time Complexity = O(n/m), n = number of elements, m = number of buckets
     *
     * @param object The object instance
     * @return Returns the bucket the object should reside in or null if the object passed
     * is null.
     */
    private Bucket<T> getBucket(final T object) {
        if (object == null) {
            return null;
        }
        return this.table[hash(object) % this.table.length];
    }

    /**
     * {@inheritDoc}
     * Worst-Case Time Complexity = O(n/m), n = number of elements, m = number of buckets
     * @param object
     */
    @Override
    public void add(final T object) {
        if (object == null) {
            throw new IllegalArgumentException("Does not support null types!");
        }
        // Get the bucket the object should reside in
        final Bucket<T> bucket = getBucket(object);
        // Check if the chain contains the object
        // Worst-Case Time Complexity = O(n)
        if (!bucket.chain.contains(object)) {
            // Add object to the chain
            // Worst-Case Time Complexity = O(1)
            bucket.chain.add(object);
            // Increment size
            this.size++;
        }
    }

    /**
     * {@inheritDoc}
     * Worst-Case Time Complexity = O(n/m), n = number of elements, m = number of buckets
     * @param object
     * @return
     */
    @Override
    public boolean contains(final T object) {
        // Check if the object isn't null, that the set is not empty and the theoretical bucket's chain
        // contains the object
        return object != null && this.size != 0 && getBucket(object).chain.contains(object);
    }

    @Override
    public boolean removeFirst(final T t) {
        return remove(t);
    }

    @Override
    public void addAll(final Collection<T> objects) {
        if (objects.size() == 0) {
            return;
        }
        for (final T t : objects) {
            add(t);
        }
    }

    @Override
    public void addAll(final T[] array) {
        if (array.length == 0) {
            return;
        }
        for (final T t : array) {
            add(t);
        }
    }

    /**
     * {@inheritDoc}
     * Worst-Case Time Complexity = O(n/m), n = number of elements, m = number of buckets
     * @param object
     * @return
     */
    @Override
    public boolean remove(final T object) {
        if (object == null || this.size == 0) {
            return false;
        }
        // Obtain bucket | Worst-Case Time Complexity = O(1)
        final Bucket<T> bucket = getBucket(object);
        // Remove object from chain if present
        // Worst-Case Time Complexity = O(n)
        if (bucket.chain.removeFirst(object)) {
            this.size--;
            return true;
        }
        return false;
    }

    @Override
    public void removeAll(final Collection<T> objects) {
        if (objects.size() == 0 || this.size == 0) {
            return;
        }
        for (final T t : objects) {
            remove(t);
        }
    }

    @Override
    public void removeAll(final T[] array) {
        if (array.length == 0 || this.size == 0) {
            return;
        }
        for (final T t : array) {
            if (t == null) {
                continue;
            }
            remove(t);
        }
    }

    @Override
    public void clear() {
        if (this.size == 0) {
            return;
        }
        // Clear all the bucket's chains
        for (final Bucket<T> bucket : this.table) {
            bucket.chain.clear();
        }
        this.size = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<T> iterator() {
        return new BucketIterator();
    }

    @Override
    public String toString() {
        return "FixedSizeHashSet{" + "nodes=" + Arrays.toString(table) + ", size=" + size + '}';
    }

    /**
     * Represents a bucket in a hash based table which utilizes a {@link LinkedList} to implement
     * linear chaining. This class is not thread safe and will by no means perform any concurrent modification checks.
     *
     * @param <E> A generic type, can be anything.
     */
    private static class Bucket<E> {

        private final LinkedList<E> chain = new LinkedList<>();

        @Override
        public int hashCode() {
            return this.chain.size() == 0 ? 0 : this.chain.get(0).hashCode();
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            final Bucket<?> bucket = (Bucket<?>) o;
            return Objects.equals(this.chain, bucket.chain);
        }

        @Override
        public String toString() {
            return "Node{" + "chain=" + this.chain + '}';
        }
    }


    /**
     * Iterator implementation which traverse the {@link #table}. The elements returned
     * by this iterator are non-deterministic by nature and the order of elements are not
     * guaranteed to be the same once the set has been modified; However, the order is guaranteed
     * to be the same provided the set is not modified.
     */
    private class BucketIterator implements Iterator<T> {

        private int index = 0;
        private int bucketIndex = 0;
        private boolean removed;

        @Override
        public boolean hasNext() {
            return getNextBucket() != null;
        }

        @Override
        public T next() {
            final Bucket<T> bucket = getNextBucket();
            if (bucket == null) {
                throw new NoSuchElementException();
            }
            this.removed = false;
            return bucket.chain.get(bucketIndex++);
        }

        @Override
        public void remove() {
            if (this.removed) {
                throw new NoSuchElementException();
            }
            FixedSizeHashSet.this.table[index].chain.remove(bucketIndex);
            FixedSizeHashSet.this.size--;
            this.removed = true;
        }

        /**
         * Traverse to the next bucket
         *
         * @return Returns the next bucket or null
         */
        private Bucket<T> getNextBucket() {
            if (this.index == FixedSizeHashSet.this.table.length - 1) {
                return null;
            }
            final Bucket<T> node = FixedSizeHashSet.this.table[index];
            if (node.chain.size() == 0 || this.bucketIndex > node.chain.size()) {
                this.index++;
                this.bucketIndex = 0;
                return getNextBucket();
            }
            return node;
        }
    }

}
