//TODO:
//  (1) Implement the hash table!
//  (2) JavaDocs

import java.util.*;

//If you uncomment the import to ArrayList below, the grader will manually
//look to make sure you are not using it anywhere else... if you use it
//somewhere else you will get 0pts on the entire project (not a joke).

//uncomment the line below ONLY if you are implementing values().
//import java.util.ArrayList; //for returning in the values() function only

/**
 * This class represents a Map (Key->Value pairings) using hashing
 * with Hopgood-Davenport probing.
 * @param <K> key
 * @param <V> value
 * @author Andrew Babilonia G01281732
 */

class ThreeTenMap<K,V> implements Map<K,V> {
    //********************************************************************************
    //   DO NOT EDIT ANYTHING IN THIS SECTION (except to add the JavaDocs)
    //********************************************************************************

    //you must use this storage for the hash table
    //and you may not alter this variable's name, type, etc.
    /** storage for the hash table.*/
    private Pair<K,V>[] storage;

    //you must use to track the current number of elements
    //and you may not alter this variable's name, type, etc.
    /** number of elements in the table.*/
    private int numElements = 0;

    //provided class, do not edit!
    /**
     * Pair class that represents a pair of unique keys and the values associated to them.
     *
     * @param <K> key
     * @param <V> value
     */
    public class Pair<K,V> {
        /** key generic type.*/
        K key;
        /** value generic type.*/
        V value;
        /**
         * Constructor for the pair class.
         *
         * @param key the key
         * @param value the value stored
         */
        Pair(K key, V value) { this.key = key; this.value = value; }
        /**
         * Returns string representation of what is stored, key and value.
         *
         * @return string representation of key and value
         */
        public String toString() { return "<" + key + "," + value + ">"; }

    }

    //********************************************************************************
    //   YOUR CODE GOES IN THIS SECTION
    //********************************************************************************

    /** max load of hash table. Indicates when to increase size.*/
    private double maxLoad;
    /** the original size of the hash table when it was first initialized.*/
    private int originalSize;
    /** size of the hash table.*/
    private int size;
    /** capacity of the hash table.*/
    private int capacity;
    /** bunny key/value for deletion.*/
    private final Pair<K,V> bunny = new Pair<K,V>(null,null);
    /** running count of bunny dummies.*/
    private int bunnies;

    //IMPORTANT NOTES FOR STUDENTS:

    //Remember... if you want an array of ClassWithGeneric<V>, the following format ___SHOULD NOT___ be used:
    //		 ClassWithGeneric<V>[] items = (ClassWithGeneric<V>[]) new Object[10];
    //instead, use this format:
    //		 ClassWithGeneric<V>[] items = (ClassWithGeneric<V>[]) new ClassWithGeneric[10];

    //If the hash of something is Integer.MIN_VALUE, you need to manually change it
    //to Integer.MAX_VALUE. Math.abs() won't work on Integer.MIN_VALUE!

    //This table does not accept null keys. Why is that important? Think happy little
    //bunnies... (or think tombstones if you don't know about the much cooler bunny thing).

    /**
     * Create a hash table where the size of the storage is the
     * smallest power of two larger than the requested size.
     * Assum the requested size is >= 1.
     *
     * @param size the size of the map
     */
    @SuppressWarnings("unchecked")
    public ThreeTenMap(int size) {
        //Create a hash table where the size of the storage is
        //the smallest power of two larger than the requested size.
        //storage size = # slots in the hash table
        //You may assume the requested size is >= 1
        this(size,0.5);
        //This will use the default maxLoad of 0.5.
    }

    /**
     * Helper function to find the smallest power of two larger than the requested size.
     * Taken from Hacker's Delight, I thought it was a pretty cool implementation.
     * @param size the size of the hash table
     * @return the new size of hash table
     */
    private int closestPwrTwo(int size){
        size = Math.max(1,size);
        size = size - 1;
        size = size | (size >> 1);
        size = size | (size >> 2);
        size = size | (size >> 4);
        size = size | (size >> 8);
        size = size | (size >> 16);
        return size + 1;
    }

    /**
     * Create a hash table where the size of the storage is the smallest
     * power of two larger than the requested size. Allows the user to
     * input their max load.
     *
     * @param size the size of the hash table
     * @param maxLoad max load of hash table
     */
    @SuppressWarnings("unchecked")
    public ThreeTenMap(int size, double maxLoad) {
        //Create a hash table where the size of the storage is
        //the smallest power of two larged than the requested size.
        //storage size = # slots in the hash table
        //You may assume the requested size is >= 1
        size = closestPwrTwo(size);
        originalSize = size;
        this.capacity = size;
        this.size = 0;
        this.bunnies = 0;
        storage = (Pair<K,V>[]) new Pair[size];
        this.maxLoad = maxLoad;
        //The max load should also be set by this function. You may
        //assume that this will be > 0 and <= 1
    }

    /**
     * Method to clear the entire table. Return to original size when constructed.
     */
    @SuppressWarnings("unchecked")
    public void clear() {
        //the table should return to the original size it had
        //when constructed
        //O(1)
        storage = (Pair<K,V>[]) new Pair[originalSize];
    }

    /**
     * Checks if the hash table is empty.
     *
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        //O(1)
        return size() == 0;
    }

    /**
     * Return how many slots are in the table.
     *
     * @return the number of slots in the table
     */
    public int capacity() {
        //return how many "slots" are in the table
        //O(1)
        return this.capacity;
    }

    /**
     * Returns the number of elements in the table.
     * {@inheritDoc}
     */
    public int size() {
        //return the number of elements in the table
        //O(1)
        return this.size;
    }

    /**
     * Helper method for toString(). Returns if a real value
     * is being stored at the given index.
     *
     * @param index the index in query
     * @return true if it contains a value, false otherwise
     */
    private boolean slotContainsValue(int index) {
        //Private helper method for toString()
        //O(1)
        boolean validSlot = true;
        //Returns true if a "real" value is at the index given
        //Tombstones (and bunnies) are not "real" values in the map.
        if(storage[index] == null || storage[index] == bunny) {
            validSlot = false;
        }
        return validSlot;
    }


    /**
     * Return the value if given a key.
     *
     * @param key unique key given to find value
     * @return the value associated with the key
     */
    public V get(Object key) {
        //Worst case: O(n), Average case: O(1)
        int index = search(key);
        if(index == -1) return null;
        if(storage[index] == null) return null;

        return storage[index].value;
    }

    /**
     * Helper method for class. Finds the index of the value stored at the given key.
     *
     * @param key the key to use for the search
     * @return the index of the key that was input
     */
    private int search(Object key){
        if(key == null) throw new NullPointerException();
        int hash = key.hashCode();
        if(hash < 0){
            if(hash == Integer.MIN_VALUE){
                hash = Integer.MAX_VALUE;
            }
            else{
                hash = Math.abs(hash);
            }
        }
        boolean[] indexTable = new boolean[this.capacity];
        int index = hash % capacity;
        int probes = 0;
        while(storage[index] != null && !key.equals(storage[index].key)){
            if(indexTable[index] == true) return -1;
            indexTable[index] = true;
            probes++;
            int hopD = (int)((.5*probes) + (.5*probes*probes));
            index = (index + hopD) % capacity;
        }
        return index;
    }

    /**
     * Returns a set of keys.
     *
     * @return the set of keys associated to the values stored in the table
     */
    public ThreeTenSet<K> keySet() {
        //a ThreeTenSet is a Set, so return one of those
        //max of O(m) where m = number of slots in the table
        ThreeTenSet<K> retSet = new ThreeTenSet<>();
        for(int i = 0; i < storage.length; i++){
            if(storage[i] != null && storage[i] != bunny){
                retSet.add(storage[i].key);
            }
        }
        return retSet;
    }

    /**
     * Removes a value from the hash table.
     *
     * @param key the key of the value to be removed.
     *
     * @return the value that was removed
     */
    public V remove(Object key) {
        //Worst case: O(n), Average case: O(1)
        int index = search(key);
        V oldValue = null;
        if(storage[index] != null){
            oldValue = storage[index].value;
            storage[index] = bunny;
            size--;
            bunnies++;
        }
        return oldValue;
    }

    /**
     * Place the value at the key location. Does not accept
     * null keys and should throw a NullPointerException.
     *
     * @param key the key of the hash table
     * @param value the value stored
     *
     * @return value that was stored at that index
     * @throws NullPointerException for any null keys
     */
    public V put(K key, V value) {
        //Place value v at the location of key k.
        //This table does not accept null keys and you
        //should throw a NullPointerException if k
        //is null (it _is_ ok for the value to be null!).
        if(key == null) throw new NullPointerException();
        //If the key already exists in the table
        //replace the current value with v.
        int hash = key.hashCode();

        if(hash < 0){
            if(hash == Integer.MIN_VALUE){
                hash = Integer.MAX_VALUE;
            }
            else{
                hash = Math.abs(hash);
            }
        }
        if(size >= capacity && !containsKey(key)){
            rehash(capacity*2);
        }

        int index = hash % capacity;
        int probes = 0;

        while(storage[index] != null){
            if(key.equals(storage[index].key)){
                V oldVal = storage[index].value;
                storage[index].value = value;
                return oldVal;
            }
            if(storage[index] == bunny){
                storage[index] = new Pair<K,V>(key,value);
                size++;
                bunnies--;
                return null;
            }
            probes++;
            int hopD = (int)((.5*probes) + (.5*probes*probes));
            index = (index + hopD) % capacity;
        }
        storage[index] = new Pair<K,V>(key,value);
        size++;
        if(((double)size/(double)capacity) > maxLoad){
            rehash(capacity*2);
        }
        return null;

        //If _after_ adding, the load on the table is
        //greater than the max load, expand the table
        //to twice the table size and rehash. Note ">" != ">=".

        //If there is _absolutely no space_ in the table
        //_before_ adding (this can happen with max loads
        //close to 1), then rehash to twice the table size
        //before adding.

        //Worst case: O(n), Average case: O(1) if no rehashing
        //O(m) if rehashing is needed.
    }

    /**
     * Increase or decrease the size of storage
     * to the smallest power of two larger than the requested
     * size.
     *
     * @param size the size of the new table
     *
     * @return true if successful, false otherwise.
     */
    @SuppressWarnings("unchecked")
    public boolean rehash(int size) {
        //Increase or decrease the size of the storage,
        //to the smallest power of two larger than the
        //requested size (and rehashing all values).
        //storage size = # slots in the hash table

        //Note: you should start at the beginning of the
        //old table and go through each index in order
        //to move items into the new table.

        //If you go backwards, etc. your elements will end up
        //out of order compared to the expected order.
        size = closestPwrTwo(size);
        if(size < this.size){
            return false;
        }
        //If the new size would result in a load greater
        //than the max load, return false. Note ">" != ">=".
        //Return true if you were able to rehash.
        this.size = 0;

        this.capacity = size;
        Pair<K,V>[] oldStorage = storage;
        storage = (Pair<K,V>[]) new Pair[capacity];
        for(int i = 0; i < oldStorage.length; i++){
            if(oldStorage[i] != null && oldStorage[i] != bunny){
                K key = oldStorage[i].key;
                V value = oldStorage[i].value;
                this.put(key,value);
            }
        }
        return true;
    }

    //--------------------------------------------------------
    // testing code goes here... edit this as much as you want!
    //--------------------------------------------------------

    /**
     * Main method for testing purposes.
     *
     * @param args string arguments given from command line
     */
    public static void main(String[] args) {
       
    }

    //********************************************************************************
    //   YOU MAY, BUT DON'T NEED TO EDIT THINGS IN THIS SECTION
    // These are some methods we didn't write for you, but you could write,
    // if you need/want them for building your graph. We will not test
    // (or grade) these methods.
    //********************************************************************************

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Collection<V> values() {
        Collection<V> output = new ArrayList<>();
        for(Pair p : storage){
            output.add((V)p.value);
        }
        return output;
    }

    /**
     * {@inheritDoc}
     */
    public void	putAll(Map<? extends K,? extends V> m) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsValue(Object value) {
        for(int i = 0; i < storage.length; i++){
            if(this.slotContainsValue(i)){
                if(storage[i].value.equals(value)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public Set<Map.Entry<K,V>> entrySet() {
        ThreeTenSet<Map.Entry<K,V>> output = new ThreeTenSet<>();
        for(int i = 0; i < storage.length; i++){
            if(storage[i] != null) {
                Map.Entry<K, V> e = new EntrySet(storage[i]);
                output.add(e);
            }
        }
        return output;
    }
    /**
     * Entry set class that implements the Map.Entry interface.
     *
     * @author Andrew Babilonia
     */
    private class EntrySet implements Map.Entry<K,V> {
        /** pair of three ten nodes.*/
        private Pair<K,V> pair = null;

        /**
         * Constructor for entry set.
         * @param pair three ten node pairs
         */
        public EntrySet(Pair<K,V> pair){
            this.pair = pair;
        }

        @Override
        public K getKey() {
            return this.pair.key;
        }

        @Override
        public V getValue() {
            return this.pair.value;
        }

        @Override
        public V setValue(V value) {
            this.pair.value = value;

            return this.pair.value;
        }

        @Override
        public boolean equals(Object o) {
            return this.pair.value.equals(o);
        }

        @Override
        public int hashCode() {
            return this.pair.key.hashCode();
        }
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

    /**
     * {@inheritDoc}
     */
    public boolean containsKey(Object key) {
        int index = search(key);
        if(index == -1) return false;
        return storage[index] != null;
    }

    //********************************************************************************
    //   DO NOT EDIT ANYTHING BELOW THIS LINE (except to add the JavaDocs)
    // We will check these things to make sure they still work, so don't break them.
    //********************************************************************************

    /**
     * Provided toString() method. Does not show empty slots.
     *
     * @return string representation of the data
     */
    public String toString() {
        //THIS METHOD IS PROVIDED, DO NOT CHANGE IT
        return toString(false);
    }

    /**
     * Provided toString() method. Accepts a parameter to see if
     * the user wants to see empty slots in the hash table.
     *
     * @param showEmpty check to show empty spaces
     * @return string representation of the data
     */
    public String toString(boolean showEmpty) {
        //THIS METHOD IS PROVIDED, DO NOT CHANGE IT
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < storage.length; i++) {
            if(showEmpty || slotContainsValue(i))  {
                s.append("[");
                s.append(i);
                s.append("]: ");
                s.append(storage[i]);
                s.append("\n");
            }
        }

        s.deleteCharAt(s.length()-1);
        return s.toString();
    }

    /**
     * Method that places key and value into array.
     *
     * @return object array containing the pair key and value
     */
    @SuppressWarnings("unchecked")
    public Object[] toArray() {
        //THIS METHOD IS PROVIDED, DO NOT CHANGE IT
        this.numElements = this.size;
        Pair<K,V>[] ret = (Pair<K,V>[]) new Pair[numElements];

        for(int i = 0, j = 0; i < storage.length; i++) {
            if(slotContainsValue(i)) {
                ret[j++] = new Pair<>(storage[i].key, storage[i].value);
            }
        }
        return (Object[]) ret;
    }
}
