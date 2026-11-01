package evgenygl907v1.homework1;

import java.util.HashMap;
import java.util.Map;

public class MyHashMap<K, V> { //implements Map<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    private static class Node<K, V> {
        final K key;
        final int hash;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public V put(K key, V value) {
        int hash = hash(key);

        if (table == null) {
            table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
            threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        }

        int index = hash & (table.length - 1);
        Node<K, V> current = table[index];

        while (current != null) {
            if (current.hash == hash && (current.key == key || (key != null && key.equals(current.key)))) {
                V oldValue = current.value;
                current.value = value;
                return oldValue;
            }

            current = current.next;
        }

        table[index] = new Node<>(hash, key, value, table[index]);
        size++;

        if (size > threshold && table.length < MAXIMUM_CAPACITY) {
            resize(table.length << 1);
        }

        return null;
    }

    public V get(Object key) {
        if (table == null) {
            return null;
        }

        int hash = hash(key);
        int index = hash & (table.length - 1);

        Node<K, V> current = table[index];

        while (current != null) {
            if (current.hash == hash && (current.key == key || (key != null && key.equals(current.key)))) {
                return current.value;
            }

            current = current.next;
        }

        return null;
    }

    public V remove(Object key) {
        if (table == null) {
            return null;
        }

        int hash = hash(key);
        int index = hash & (table.length - 1);

        Node<K, V> current = table[index];
        Node<K, V> previous = null;

        while (current != null) {
            if (current.hash == hash && (current.key == key || (key != null && key.equals(current.key)))) {
                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }

                V oldValue = current.value;
                size--;

                if (table.length > DEFAULT_CAPACITY && size <= table.length / 4) {
                    resize(table.length / 2);
                }

                return oldValue;
            }

            previous = current;
            current = current.next;
        }

        return null;
    }

    private void resize(int newCapacity) {
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];

        for (int i = 0; i < table.length; i++) {
            Node<K, V> current = table[i];

            while (current != null) {
                Node<K, V> next = current.next;

                int newIndex = current.hash & (newCapacity - 1);

                current.next = newTable[newIndex];
                newTable[newIndex] = current;

                current = next;
            }
        }

        table = newTable;
        threshold = (int) (newCapacity * LOAD_FACTOR);
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return table == null ? 0 : table.length;
    }
}