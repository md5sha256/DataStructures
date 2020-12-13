package me.andrewandy.datastructures;

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


    void addAll(Collection<T> collection);

    void addAll(T[] iterable);

    boolean remove(T t);

    void removeAll(Collection<T> collection);

    void removeAll(T[] iterable);

    void clear();

    int size();

}
