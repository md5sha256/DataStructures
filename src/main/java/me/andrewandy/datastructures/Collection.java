package me.andrewandy.datastructures;

public interface Collection<T> extends Iterable<T> {

    void addAll(Collection<T> collection);

    void addAll(T[] iterable);

    void add(T t);

    boolean remove(T t);

    void removeAll(Collection<T> collection);

    void removeAll(T[] iterable);

    void clear();

    int size();

    boolean contains(T t);

}
