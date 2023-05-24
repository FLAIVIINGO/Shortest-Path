//TODO: JavaDocs

//Don't forget, use inheritDoc to save yourself
//a lot of time for inherited methods from Set!!!

import java.util.Collection;
import java.util.Set;
import java.util.Iterator;

/**
 * This class represents a Set.
 *
 * @param <E> element generic
 * @author Andrew Babilonia G01281732
 */

class ThreeTenSet<E> implements Set<E> {
    //********************************************************************************
    //   YOU MAY, BUT DON'T NEED TO EDIT THINGS IN THIS SECTION
    // These are some methods we didn't write for you, but you could write.
    // if you need/want them for building your graph. We will not test
    // (or grade) these methods.
    //********************************************************************************

    /**
     * {@inheritDoc}
     */
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    //--------------------------------------------------------
    // testing code goes here... edit this as much as you want!
    //--------------------------------------------------------

    /**
     * Main method for testing purposes.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        ThreeTenSet<Integer> set = new ThreeTenSet<>();

        if(set.add(1) && set.add(2) && set.add(-51) && !set.add(1) && set.size() == 3) {
            System.out.println("yay 1");
        }

        if(set.remove(1) && !set.remove(1) && set.size() == 2 && set.remove(-51) && set.toArray()[0].equals(2)) {
            System.out.println("yay 2");
        }

        set.clear();
        ThreeTenSet<Integer> set2 = new ThreeTenSet<>();
        for(int i = -100; i < 100; i += 17){
            set2.add(i);
        }
        if(set.size() == 0 && set.addAll(set2) && set.size() == 12) {
            System.out.println("yay 3");
        }
        System.out.println();
        System.out.println(set);
    }

    //********************************************************************************
    //   DO NOT EDIT ANYTHING BELOW THIS LINE (except to add the JavaDocs)
    // We will grade these methods to make sure they still work, so don't break them.
    //********************************************************************************
    /** storage of three ten map.*/
    private ThreeTenMap<E,E> storage = new ThreeTenMap<>(7);

    /**
     * {@inheritDoc}
     */
    public boolean add(E e) {
        //THIS METHOD IS PROVIDED, DO NOT CHANGE IT
        if(e == null) throw new NullPointerException();
        return storage.put(e, e) == null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean addAll(Collection<? extends E> c) {
        //THIS METHOD IS PROVIDED, DO NOT CHANGE IT
        boolean changedSomething = false;

        for(E e : c) {
            if(e != null) {
                changedSomething = add(e) || changedSomething;
            }
        }

        return changedSomething;
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        //THIS METHOD IS PROVIDED, DO NOT CHANGE IT
        storage = new ThreeTenMap<>(7);
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(Object o) {
        //THIS METHOD IS PROVIDED, DO NOT CHANGE IT
        return storage.get(o) != null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        //THIS METHOD IS PROVIDED, DO NOT CHANGE IT
        return size() == 0;
    }

    /**
     * {@inheritDoc}
     */
    public boolean remove(Object o) {
        //THIS METHOD IS PROVIDED, DO NOT CHANGE IT
        return storage.remove(o) != null;
    }

    /**
     * {@inheritDoc}
     */
    public int size() {
        //THIS METHOD IS PROVIDED, DO NOT CHANGE IT
        return storage.size();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Object[] toArray() {
        //THIS METHOD IS PROVIDED, DO NOT CHANGE IT
        ThreeTenMap<E,E>.Pair<E,E>[] arr = (ThreeTenMap<E,E>.Pair<E,E>[]) storage.toArray();

        Object[] ret = new Object[arr.length];
        for(int i = 0; i < arr.length; i++) {
            ret[i] = arr[i].key;
        }

        return ret;
    }

    /**
     * {@inheritDoc}
     */
    public String toString(){
        //THIS METHOD IS PROVIDED, DO NOT CHANGE IT
        return storage.toString();
    }

    /**
     * {@inheritDoc}
     */
    public String toString(boolean showEmpty) {
        //THIS METHOD IS PROVIDED, DO NOT CHANGE IT
        return storage.toString(showEmpty);
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<E> iterator() {
        //THIS METHOD IS PROVIDED, DO NOT CHANGE IT
        return new Iterator<>() {
            private Object[] values = toArray();
            private int currentLoc = 0;

            @SuppressWarnings("unchecked")
            public E next() {
                return (E) values[currentLoc++];
            }

            public boolean hasNext() {
                return currentLoc != values.length;
            }
        };
    }
}
