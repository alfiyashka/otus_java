import java.util.*;

public class DIYarrayList <T> implements List<T> {
    private static Integer maxLength = 1000000;
    private static Integer capacity = 10;
    private Object[] array;
    private Integer currentSize = 0;

    DIYarrayList(Integer capacity) {
        array = new Object[capacity];
    }

    DIYarrayList() {
        array = new Object[capacity];
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Error");
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Error");
    }

    private class DIYIterator implements Iterator<T>  {

        int index;

        @Override
        public boolean hasNext() {

            if(index < array.length){
                return true;
            }
            return false;
        }

        @Override
        public T next() {

            if(this.hasNext()){
                return (T) array[index++];
            }
            return null;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new DIYIterator();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Error");
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException("Error");
    }

    @Override
    public boolean add(T t) {
        if (currentSize > maxLength) {
            return false;
        }
        if (array.length <= currentSize) {
            // increase by capacity
            array = Arrays.copyOf(array, array.length + capacity);
        }
        array[currentSize] = t;
        currentSize ++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Error");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Error");
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c.isEmpty()){
            return false;
        }
        if (c.size() + currentSize > maxLength) {
            return false;
        }
        c.forEach(it -> { this.add(it); });
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException("Error");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Error");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Error");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Error");
    }

    @Override
    public T get(int index) {
        throw new UnsupportedOperationException("Error");
    }

    @Override
    public T set(int index, T element) {
        throw new UnsupportedOperationException("Error");
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException("Error");
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException("Error");
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException("Error");
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("Error");
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DIYListIterator(0);
    }

    private class DIYListIterator extends DIYIterator implements ListIterator<T> {

        DIYListIterator(int index) {
            super();
            this.index = index;
        }

        public boolean hasPrevious() {
            return index != 0;
        }

        public int nextIndex() {
            return index;
        }

        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {

        }
        public T previous() {
            throw new UnsupportedOperationException("Error");
        }

        public void set(T t) {
            if (index - 1 < 0)
                throw new IllegalStateException();

            try {
                array[index - 1] = t;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(T t) {
            throw new UnsupportedOperationException("Error");
        }
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException("Error");
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Error");
    }

    @Override
    public void sort(Comparator<? super T> c) {
        Arrays.sort((T[]) array, 0, currentSize, c);
    }
}
