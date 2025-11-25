import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Головний клас для демонстрації роботи колекції (Lab 6).
 */
public class Main {
    public static void main(String[] args) {
        // ---------------------------------------------------------------
        // Демонстрація роботи колекції MySet (Set + Doubly Linked List)
        // Тип елементів: Coffee
        // ---------------------------------------------------------------

        // 1. Порожній конструктор
        MySet<Coffee> coffeeSet = new MySet<>();

        Coffee c1 = new CoffeeBeans("Arabica", 400, 1.0, 5.0, 90);
        Coffee c2 = new GroundCoffee("Espresso", 150, 0.25, 0.5, 75);
        Coffee c3 = new InstantCoffee("Nescafe", 200, 0.2, 0.4, 60, "Банка");

        // 2. Додавання елементів
        System.out.println("Додаємо c1, c2, c3...");
        coffeeSet.add(c1);
        coffeeSet.add(c2);
        coffeeSet.add(c3);
        
        // Спроба додати дублікат 
        boolean addedDuplicate = coffeeSet.add(c1);
        System.out.println("Чи додався дублікат c1? " + addedDuplicate); // Має бути false

        System.out.println("\nВміст колекції (через ітератор):");
        for (Coffee c : coffeeSet) {
            System.out.println(c);
        }

        // 3. Конструктор з одним елементом
        System.out.println("\nСтворення Set з одним елементом:");
        MySet<Coffee> singleSet = new MySet<>(new CoffeeBeans("Kopi Luwak", 1000, 0.5, 2.0, 100));
        for (Coffee c : singleSet) System.out.println(c);

        // 4. Конструктор зі стандартної колекції
        System.out.println("\nСтворення Set з іншої колекції:");
        MySet<Coffee> copySet = new MySet<>(coffeeSet);
        System.out.println("Розмір копії: " + copySet.size());
        
        // 5. Видалення
        System.out.println("\nВидалення c2 (Espresso)...");
        copySet.remove(c2);
        System.out.println("Чи містить c2? " + copySet.contains(c2));
        System.out.println("Розмір після видалення: " + copySet.size());
    }
}

// ==========================================
// ВЛАСНА КОЛЕКЦІЯ (SET на Двозв'язному списку)
// ==========================================

/**
 * Типізована колекція, що реалізує інтерфейс Set.
 * Внутрішня структура: Двозв’язний список (Doubly Linked List).
 * * @param <E> Тип елементів у колекції.
 */
class MySet<E> extends AbstractSet<E> implements Set<E> {

    // Вузли двозв'язного списку
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    private Node<E> head; // Початок списку
    private Node<E> tail; // Кінець списку
    private int size = 0; // Кількість елементів

    // --------------------------------------------------
    // 1. КОНСТРУКТОРИ 
    // --------------------------------------------------

    /**
     * 1. Порожній конструктор.
     */
    public MySet() {
    }

    /**
     * 2. Конструктор, в який передається 1 об’єкт.
     * @param element Елемент для додавання.
     */
    public MySet(E element) {
        add(element);
    }

    /**
     * 3. Конструктор, в який передається стандартна колекція.
     * @param collection Колекція елементів.
     */
    public MySet(Collection<? extends E> collection) {
        this.addAll(collection);
    }

    // --------------------------------------------------
    // МЕТОДИ ІНТЕРФЕЙСУ SET / COLLECTION
    // --------------------------------------------------

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    /**
     * Додає елемент у кінець списку, якщо такого ще немає (властивість Set).
     * @param e елемент для додавання
     * @return true, якщо елемент додано.
     */
    @Override
    public boolean add(E e) {
        // У Set не може бути дублікатів
        if (contains(e)) {
            return false;
        }
        linkLast(e);
        return true;
    }

    /**
     * Видаляє об'єкт із колекції.
     */
    @Override
    public boolean remove(Object o) {
        // Знаходимо вузол
        for (Node<E> x = head; x != null; x = x.next) {
            if (o == null ? x.item == null : o.equals(x.item)) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        // Очищення всіх посилань для GC
        for (Node<E> x = head; x != null; ) {
            Node<E> next = x.next;
            x.item = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        head = tail = null;
        size = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> lastReturned = null;
            private Node<E> nextNode = head;
            private int nextIndex = 0;

            @Override
            public boolean hasNext() {
                return nextIndex < size;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                lastReturned = nextNode;
                nextNode = nextNode.next;
                nextIndex++;
                return lastReturned.item;
            }

            @Override
            public void remove() {
                if (lastReturned == null) throw new IllegalStateException();
                Node<E> lastNext = lastReturned.next;
                unlink(lastReturned);
                if (nextNode == lastReturned)
                    nextNode = lastNext;
                else
                    nextIndex--;
                lastReturned = null;
            }
        };
    }

    // --------------------------------------------------
    // ДОПОМІЖНІ МЕТОДИ 
    // --------------------------------------------------

    /**
     * Додає елемент в кінець списку (як у LinkedList).
     */
    private void linkLast(E e) {
        final Node<E> l = tail;
        final Node<E> newNode = new Node<>(l, e, null);
        tail = newNode;
        if (l == null)
            head = newNode;
        else
            l.next = newNode;
        size++;
    }

    /**
     * Видаляє вузол зі списку.
     */
    private E unlink(Node<E> x) {
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null;
        size--;
        return element;
    }

    /**
     * Шукає індекс об'єкта (потрібен для contains).
     */
    private int indexOf(Object o) {
        int index = 0;
        if (o == null) {
            for (Node<E> x = head; x != null; x = x.next) {
                if (x.item == null) return index;
                index++;
            }
        } else {
            for (Node<E> x = head; x != null; x = x.next) {
                if (o.equals(x.item)) return index;
                index++;
            }
        }
        return -1;
    }
}

// ==========================================
// ІЄРАРХІЯ З ЛАБОРАТОРНОЇ №5 
// ==========================================

abstract class Coffee {
    private String name;
    private double price;
    private double weight;
    private double volume;
    private int quality;

    public Coffee(String name, double price, double weight, double volume, int quality) {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.volume = volume;
        this.quality = quality;
    }

    // Getters
    public String getName() { return name; }
    
    @Override
    public String toString() {
        return String.format("%-20s | Ціна: %.1f | Якість: %d", name, price, quality);
    }
}

class CoffeeBeans extends Coffee {
    public CoffeeBeans(String name, double price, double weight, double volume, int quality) {
        super(name + " (Зерно)", price, weight, volume, quality);
    }
}

class GroundCoffee extends Coffee {
    public GroundCoffee(String name, double price, double weight, double volume, int quality) {
        super(name + " (Мелена)", price, weight, volume, quality);
    }
}

class InstantCoffee extends Coffee {
    public InstantCoffee(String name, double price, double weight, double volume, int quality, String packaging) {
        super(name + " (Розчинна, " + packaging + ")", price, weight, volume, quality);
    }
}