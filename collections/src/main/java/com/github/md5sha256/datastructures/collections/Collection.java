package com.github.md5sha256.datastructures.collections;

/**
 * Represents a data structure.
 *
 * @param <T> A generic type, can be any object.
 */
public interface Collection<T> extends Iterable<T> {

    /**
     * Append an object to this collection.
     *
     * @param t The object instance to append of a generic type 'T'
     */
    void add(T t);

    /**
     * Check whether a given object exists in the collection.
     *
     * @param t The object instance of a generic type 'T'
     * @return Returns true if this collection contains the object, false otherwise.
     */
    boolean contains(T t);

    /**
     * Remove the first occurrence of a given object from this collection.
     *
     * @param t The object instance to remove of a generic type 'T'
     */
    boolean removeFirst(T t);

    /**
     * Add all elements from a given collection to this collection.
     *
     * @param collection The collection to add elements from, also of generic type 'T'
     */
    void addAll(Collection<T> collection);

    /**
     * Add all elements from a given array to this collection.
     *
     * @param array The array instance
     */
    void addAll(T[] array);

    /**
     * Remove all instances of a given object from this collection.
     *
     * @param t The instance to remove
     * @return Returns true if the collection was modified, false otherwise
     */
    boolean remove(T t);

    /**
     * Remove all elements in the given collection which are in this collection.
     *
     * @param collection The collection of elements to remove, also of generic type 'T'
     */
    void removeAll(Collection<T> collection);

    /**
     * Remove all elements in the given array which are in this collection.
     *
     * @param array The array of elements to remove, also of generic type 'T'
     */
    void removeAll(T[] array);

    /**
     * Clear all elements from this collection
     */
    void clear();

    /**
     * Get the number of elements (size) of this collection.
     *
     * @return Returns an 32-bit integer representing the number of elements in this collection
     */
    int size();

}
