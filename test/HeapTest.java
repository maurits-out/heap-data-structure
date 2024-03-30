import org.junit.jupiter.api.Test;

import java.util.*;

import static java.util.Collections.sort;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class HeapTest {

    private final Random random = new Random();

    @Test
    void insert() {
        var keys = generateRandomKeys();
        var heap = new Heap<Integer>();

        for (var key : keys) {
            heap.insert(key);
        }

        assertHeap(heap, keys);
    }

    @Test
    void findMaxOfEmptyHeap() {
        var heap = new Heap<Integer>();

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(heap::findMax);
    }

    @Test
    void extractMaxOfEmptyHeap() {
        var heap = new Heap<Integer>();

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(heap::extractMax);
    }

    @Test
    void findMaxOfNonEmptyHeap() {
        var keys = generateRandomKeys();
        var heap = Heap.heapify(keys);
        var expectedMax = keys.stream().max(naturalOrder());

        assertThat(heap.findMax()).isEqualTo(expectedMax.get());
    }

    @Test
    void extractMaxOfNonEmptyHeap() {
        var keys = generateRandomKeys();
        var heap = Heap.heapify(keys);
        sort(keys);

        assertThat(heap.extractMax()).isEqualTo(keys.getLast());
        assertThat(heap.extractMax()).isEqualTo(keys.get(keys.size() - 2));
    }

    @Test
    void deleteMaxOfEmptyHeap() {
        var heap = new Heap<Integer>();

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(heap::deleteMax);
    }

    @Test
    void deleteMaxOfNonEmptyHeap() {
        var keys = generateRandomKeys();
        var heap = Heap.heapify(keys);
        sort(keys);

        heap.deleteMax();

        assertThat(heap.findMax()).isEqualTo(keys.get(keys.size() - 2));
    }

    @Test
    void replaceForEmptyHeap() {
        var heap = new Heap<Integer>();
        var key = aRandomInt();

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> heap.replace(key));
    }

    @Test
    void replaceForNonEmptyHeap() {
        var keys = generateRandomKeys();
        var heap = Heap.heapify(keys);
        var newKey = aRandomInt();

        var expectedMax = keys.stream().max(naturalOrder());
        var max = heap.replace(newKey);

        assertThat(max).isEqualTo(expectedMax.get());

        var sortedKeys = new ArrayList<>(keys);
        sort(sortedKeys);
        sortedKeys.set(keys.size() - 1, newKey);
        assertHeap(heap, sortedKeys);
    }

    @Test
    void heapify() {
        var keys = generateRandomKeys();
        var heap = Heap.heapify(keys);

        assertHeap(heap, keys);
    }

    @Test
    void merge() {
        var keys1 = generateRandomKeys();
        var heap1 = Heap.heapify(keys1);

        var keys2 = generateRandomKeys();
        var heap2 = Heap.heapify(keys2);

        var mergedHeap = Heap.merge(heap1, heap2);

        var keys = new ArrayList<>(keys1);
        keys.addAll(keys2);
        assertHeap(mergedHeap, keys);
    }

    @Test
    void sizeForEmptyHeap() {
        var heap = new Heap<Integer>();

        assertThat(heap.size()).isZero();
    }

    @Test
    void sizeForNonEmptyHeap() {
        var keys = generateRandomKeys();
        var heap = Heap.heapify(keys);

        assertThat(heap.size()).isEqualTo(keys.size());
    }

    @Test
    void isEmptyForEmptyHeap() {
        var heap = new Heap<Integer>();

        assertThat(heap.isEmpty()).isTrue();
    }

    @Test
    void isEmptyForNonEmptyHeap() {
        var keys = generateRandomKeys();
        var heap = Heap.heapify(keys);

        assertThat(heap.isEmpty()).isFalse();
    }

    private <T extends Comparable<T>> void assertHeap(Heap<T> heap, List<T> expectedKeys) {
        expectedKeys.stream()
                .sorted(reverseOrder())
                .forEach(key -> assertThat(heap.extractMax()).isEqualTo(key));
        assertThat(heap.isEmpty()).isTrue();
    }

    private List<Integer> generateRandomKeys() {
        var keyCount = random.nextInt(1000) + 1;
        return range(0, keyCount).boxed().map(r -> random.nextInt()).collect(toList());
    }

    private int aRandomInt() {
        return random.nextInt();
    }
}
