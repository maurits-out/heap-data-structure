import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

public class Heap<T extends Comparable<T>> {

    private final ArrayList<T> keys = new ArrayList<>();

    public Heap() {
    }

    public Heap(Collection<T> keys) {
        this.keys.addAll(keys);
        heapify();
    }


    public void insert(T key) {
        keys.add(key);
        siftUp(keys.size() - 1);
    }

    public T findMax() {
        if (keys.isEmpty()) {
            throw new NoSuchElementException("heap is empty");
        }
        return keys.getFirst();
    }

    public T extractMax() {
        if (keys.isEmpty()) {
            throw new NoSuchElementException("heap is empty");
        }
        var max = keys.getFirst();
        keys.set(0, keys.getLast());
        keys.removeLast();
        siftDown(0);
        return max;
    }

    public void deleteMax() {
        if (keys.isEmpty()) {
            throw new NoSuchElementException("heap is empty");
        }
        keys.set(0, keys.getLast());
        keys.removeLast();
        siftDown(0);
    }

    public T replace(T key) {
        if (keys.isEmpty()) {
            throw new NoSuchElementException("heap is empty");
        }
        var max = keys.getFirst();
        keys.set(0, key);
        siftDown(0);
        return max;
    }

    public int size() {
        return keys.size();
    }

    public boolean isEmpty() {
        return keys.isEmpty();
    }

    public static <T extends Comparable<T>> Heap<T> heapify(Collection<T> keys) {
        return new Heap<T>(keys);
    }

    public static <T extends Comparable<T>> Heap<T> merge(Heap<T> heap1, Heap<T> heap2) {
        var concatenatedKeys = new ArrayList<>(heap1.keys);
        concatenatedKeys.addAll(heap2.keys);
        return Heap.heapify(concatenatedKeys);
    }

    private void heapify() {
        for (var i = (keys.size() >>> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    private void siftUp(int index) {
        for (var current = index; current >= 1; ) {
            var parent = (current - 1) >>> 1;
            if (compare(parent, current) >= 0) {
                break;
            }
            swap(current, parent);
            current = parent;
        }
    }

    private void siftDown(int index) {
        for (var current = index; current < keys.size(); ) {
            var largest = current;
            var left = (current << 1) + 1;
            if (left < keys.size() && compare(left, largest) > 0) {
                largest = left;
            }
            var right = (current << 1) + 2;
            if (right < keys.size() && compare(right, largest) > 0) {
                largest = right;
            }
            if (current == largest) {
                break;
            }
            swap(current, largest);
            current = largest;
        }
    }

    private int compare(int i, int j) {
        return keys.get(i).compareTo(keys.get(j));
    }

    private void swap(int i, int j) {
        var key = keys.get(i);
        keys.set(i, keys.get(j));
        keys.set(j, key);
    }
}
