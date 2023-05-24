//TODO:
//  (1) Update this code to meet the style and JavaDoc requirements.
//			Why? So that you get experience with the code for a heap!
//			Also, this happens a lot in industry (updating old code
//			to meet your new standards). We've done this for you in
//			WeissCollection and WeissAbstractCollection.
//  (2) Implement update() method -- see project description

import java.util.Iterator;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * PriorityQueue class implemented via the binary heap.
 * From your textbook (Weiss)
 * @param <T> generic element
 */
public class WeissPriorityQueue<T> extends WeissAbstractCollection<T>
{
    //--------------------------------------------------------
    // testing code goes here... edit this as much as you want!
    //--------------------------------------------------------

    /**
     * Main method for testing purposes.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        /**
         * Student class for testing.
         *
         */
        class Student {
            /** G number.*/
            String gnum;
            /** Student name.*/
            String name;
            /**
             * Constructor for student.
             * @param gnum student's g number
             * @param name student's name
             */
            Student(String gnum, String name) { this.gnum = gnum; this.name = name; }
            /**
             * {@inheritDoc}
             */
            public boolean equals(Object o) {
                if(o instanceof Student) return this.gnum.equals(((Student)o).gnum);
                return false;
            }
            /**
             * {@inheritDoc}
             */
            public String toString() { return name + "(" + gnum + ")"; }
            public int hashCode() { return gnum.hashCode(); }
        }


        Comparator<Student> comp = new Comparator<>() {
            public int compare(Student s1, Student s2) {
                return s1.name.compareTo(s2.name);
            }
        };

        WeissPriorityQueue<Student> q = new WeissPriorityQueue<>(comp);
        q.add(new Student("G00000000", "Robert"));
        q.add(new Student("G00000001", "Cindi"));

        for(Student s : q) System.out.print(s.name + " ");
        System.out.println(); //Cindi Robert

        Student bobby = new Student("G00000000", "Bobby");
        q.update(bobby);

        for(Student s : q) System.out.print(s.name + " ");
        System.out.println(); //Bobby Cindi

        bobby.name = "Robert";
        q.update(bobby);

        for(Student s : q) System.out.print(s.name + " ");
        System.out.println(); //Cindi Robert

        Student andrew = new Student("G00000009","Andrew");
        q.add(andrew);
        q.add(new Student("G00000010","Dwayne"));
        for(Student s : q) System.out.print(s.name + " ");
        System.out.println();
        q.remove();
        for(Student s : q) System.out.print(s.name + " ");
        System.out.println();
        Student cindy = new Student("G00000001", "Betty");
        q.update(cindy);
        for(Student s : q) System.out.print(s.name + " ");
        System.out.println();
        //you'll need more testing...
    }

    /**
     * update.
     * @param x element
     * @return false
     */
    public boolean update(T x) {
        int index = getIndex(x);
        if(index != -1){
            randomRemoval(index);
            add(x);
            return true;
        }
        return false;
    }

    /**
     * swap elements in pq.
     * @param index the index of the element to swap with the last element
     */
    private void swap(int index){
        T temp = array[currentSize];
        array[currentSize] = array[index];
        array[index] = temp;
    }

    /**
     * removes an element at any location.
     * @param index the index of the element
     * @return the data that was stored at that location
     */
    private T randomRemoval(int index){
        T data = array[index];
        if(index == currentSize){
            array[currentSize--] = null;
            return data;
        }
        else{
            swap(index);
            array[currentSize--] = null;
        }
        if(hasChildren(index)){
            percolateDown(index);
        }
        else if(currentSize != 1){
            if(compare(array[index],array[index/2]) < 0){
                percolateUp(index);
            }
        }
        return data;
    }

    /**
     * method to percolate the element up the tree if needed.
     * @param hole the index to start percolating from.
     */
    private void percolateUp(int hole){
        T temp = array[hole];
        for( ; compare(temp, array[hole/2]) < 0; hole /= 2)
            array[hole] = array[hole/2];
        array[hole] = temp;
    }

    /**
     * method to search for the element in the heap and return the index.
     * @param x the element
     * @return the index
     */
    private int getIndex(T x){
        final int ERR = -1;
        for(int i = 1; i < this.currentSize + 1; i++){
            if(x.hashCode() == this.array[i].hashCode()){
                return i;
            }
        }
        return ERR;
    }

    /**
     * method query to check if the element in question has children.
     * @param index the index of the element to check
     * @return true if it has children, false if not
     */
    private boolean hasChildren(int index){
        if((index * 2) + 1 >= array.length) return false;

        if(this.array[(index*2)] != null){
            return true;
        }
        return false;
    }

    /**
     * Construct an empty PriorityQueue.
     */
    @SuppressWarnings("unchecked")
    public WeissPriorityQueue( )
    {
        currentSize = 0;
        cmp = null;
        array = (T[]) new Object[ DEFAULT_CAPACITY + 1 ];
    }

    /**
     * Construct an empty PriorityQueue with a specified comparator.
     * @param c comparator
     */
    @SuppressWarnings("unchecked")
    public WeissPriorityQueue( Comparator<? super T> c )
    {
        currentSize = 0;
        cmp = c;
        array = (T[]) new Object[ DEFAULT_CAPACITY + 1 ];
    }


    /**
     * Construct a PriorityQueue from another Collection.
     * @param coll collection
     */
    @SuppressWarnings("unchecked")
    public WeissPriorityQueue( WeissCollection<? extends T> coll )
    {
        cmp = null;
        currentSize = coll.size( );
        array = (T[]) new Object[ ( currentSize + 2 ) * 11 / 10 ];

        int i = 1;
        for( T item : coll )
            array[ i++ ] = item;
        buildHeap( );
    }

    /**
     * Compares lhs and rhs using comparator if
     * provided by cmp, or the default comparator.
     * @param lhs left hand side
     * @param rhs right hand side
     * @return difference between two objects
     */
    @SuppressWarnings("unchecked")
    private int compare( T lhs, T rhs )
    {
        if( cmp == null )
            return ((Comparable)lhs).compareTo( rhs );
        else
            return cmp.compare( lhs, rhs );
    }

    /**
     * Adds an item to this PriorityQueue.
     * @param x any object.
     * @return true.
     */
    public boolean add( T x )
    {
        if( currentSize + 1 == array.length )
            doubleArray( );

        // Percolate up
        int hole = ++currentSize;
        array[ 0 ] = x;

        for( ; compare( x, array[ hole / 2 ] ) < 0; hole /= 2 )
            array[ hole ] = array[ hole / 2 ];
        array[ hole ] = x;

        return true;
    }

    /**
     * Returns the number of items in this PriorityQueue.
     * @return the number of items in this PriorityQueue.
     */
    public int size( )
    {
        return currentSize;
    }

    /**
     * Make this PriorityQueue empty.
     */
    public void clear( )
    {
        currentSize = 0;
    }

    /**
     * Returns an iterator over the elements in this PriorityQueue.
     * The iterator does not view the elements in any particular order.
     * @return new iterator
     */
    public Iterator<T> iterator( )
    {
        return new Iterator<T>( )
        {
            int current = 0;

            public boolean hasNext( )
            {
                return current != size( );
            }

            @SuppressWarnings("unchecked")
            public T next( )
            {
                if( hasNext( ) )
                    return array[ ++current ];
                else
                    throw new NoSuchElementException( );
            }

            public void remove( )
            {
                throw new UnsupportedOperationException( );
            }
        };
    }

    /**
     * Returns the smallest item in the priority queue.
     * @return the smallest item.
     * @throws NoSuchElementException if empty.
     */
    public T element( )
    {
        if( isEmpty( ) )
            throw new NoSuchElementException( );
        return array[ 1 ];
    }

    /**
     * Removes the smallest item in the priority queue.
     * @return the smallest item.
     * @throws NoSuchElementException if empty.
     */
    public T remove( )
    {
        T minItem = element( );
        array[ 1 ] = array[ currentSize-- ];
        percolateDown( 1 );

        return minItem;
    }


    /**
     * Establish heap order property from an arbitrary
     * arrangement of items. Runs in linear time.
     */
    private void buildHeap( )
    {
        for( int i = currentSize / 2; i > 0; i-- )
            percolateDown( i );
    }

    /** default capacity of heap.*/
    private static final int DEFAULT_CAPACITY = 100;

    /** current size of elements in the heap.*/
    private int currentSize;   // Number of elements in heap
    /** array for the heap.*/
    private T [ ] array; // The heap array
    /** comparator.*/
    private Comparator<? super T> cmp;

    /**
     * Internal method to percolate down in the heap.
     * @param hole the index at which the percolate begins.
     */
    private void percolateDown( int hole )
    {
        int child;
        T tmp = array[ hole ];

        for( ; hole * 2 <= currentSize; hole = child )
        {
            child = hole * 2;
            if( child != currentSize &&
                    compare( array[ child + 1 ], array[ child ] ) < 0 )
                child++;
            if( compare( array[ child ], tmp ) < 0 )
                array[ hole ] = array[ child ];
            else
                break;
        }
        array[ hole ] = tmp;
    }

    /**
     * Internal method to extend array.
     */
    @SuppressWarnings("unchecked")
    private void doubleArray( )
    {
        T [ ] newArray;

        newArray = (T []) new Object[ array.length * 2 ];
        for( int i = 0; i < array.length; i++ )
            newArray[ i ] = array[ i ];
        array = newArray;
    }
}

