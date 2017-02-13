package com.icatch.ismartdv2016.BaseItems;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class LimitQueue<E> implements Queue<E> {
    private int limit;
    LinkedList<E> queue = new LinkedList();

    public LimitQueue(int limit) {
        this.limit = limit;
    }

    public boolean offer(E e) {
        if (this.queue.size() >= this.limit) {
            this.queue.poll();
        }
        return this.queue.offer(e);
    }

    public E poll() {
        return this.queue.poll();
    }

    public E removeLast() {
        return this.queue.removeLast();
    }

    public Queue<E> getQueue() {
        return this.queue;
    }

    public int getLimit() {
        return this.limit;
    }

    public boolean add(E e) {
        return this.queue.add(e);
    }

    public E element() {
        return this.queue.element();
    }

    public E peek() {
        return this.queue.peek();
    }

    public boolean isEmpty() {
        return this.queue.size() == 0;
    }

    public int size() {
        return this.queue.size();
    }

    public E remove() {
        return this.queue.remove();
    }

    public boolean addAll(Collection<? extends E> c) {
        return this.queue.addAll(c);
    }

    public void clear() {
        this.queue.clear();
    }

    public boolean contains(Object o) {
        return this.queue.contains(o);
    }

    public boolean containsAll(Collection<?> c) {
        return this.queue.containsAll(c);
    }

    public Iterator<E> iterator() {
        return this.queue.iterator();
    }

    public boolean remove(Object o) {
        return this.queue.remove(o);
    }

    public boolean removeAll(Collection<?> c) {
        return this.queue.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return this.queue.retainAll(c);
    }

    public Object[] toArray() {
        return this.queue.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return this.queue.toArray(a);
    }
}
