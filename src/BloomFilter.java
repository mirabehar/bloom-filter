import java.util.*;

public class BloomFilter {

    public static final int K = 3;  // number of hash functions
    public static final int B = 2;  // number of bits per element
    public static final int N = 4;  // capacity of the bloom filter
    public static final int SIZE = K*B*N; // size of bloom filter array
    public static final ArrayList<int> VAL_LIST = new ArrayList<int>(); // stores values put in bloomfilter
    public static final int[] BIT_ARRAY = new int[SIZE]; // boolean array representing bloom filter
    public static final int TOTAL_ADDED = 0; // the current number of numbers stored in the bloom filter
    public static final ArrayList<int> coefs = new ArrayList<int>(); //stores the unique hash function coefficients
    public static final int[] PRIME_NUMS = new int[] {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61,
            67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179,
            181, 191, 193, 197, 199, 211, 223, 227, 229}; //50 prime numbers for generating a maximum of 50 hash functions

    //empty constructor
    public BloomFilter() {
    }

    //run the given value through each hash function, use the resulting number as the index into the BIT_ARRAY.
    //Add value itself to VAL_LIST Return true if the add is successful, false otherwise.
    //increase TOTAL_ADDED;
    //input- int val, the value we wish to query and int[] funcCoefficients, coefficients for our hash functions
    boolean add(int val, int[] funcCoefficients){
        for(int i=0; i<K; i++){
            int index = (funcCoefficients[i]*val) mod SIZE;
            BIT_ARRAY[index] = 1;
        }
        TOTAL_ADDED += 1;
        return VAL_LIST.add(val); // add actual value to "library"
    }


    /*
     same initial functionality as add()- run through each hash function to find indices. check if the values
     of BIT_ARRAY at each index found is 1. If all are 1, return true, otherwise return false.
     input- int val, the value we wish to query and int[] funcCoefficients, coefficients for our hash functions
    */
    boolean query(int val, int[] funcCoefficients){
        for(int i=0; i<K; i++;){
            int index = (funcCoefficients[i]*val) mod SIZE;
            /*
             need to change implementation of hash functions based on potential new creation of them
             --> new helper function?
             */
            if(BIT_ARRAY[index]==0){
                return false;
            }
        }
        return true;
    }
    /*
    Search through VAL_LIST (for loop) checking if the given value matches any in our list
     */
    boolean authoritative(int val){
        for(int i = 0; i < VAL_LIST.size(); i++){
            if(VAL_LIST.get(i) == val){
                return true;
            }
        }
        return false;
    }

    //Generate a list of unique values to add/ experiment with bloom filter
    ArrayList generateVals(){
        ArrayList<int> values = new ArrayList<int>();
        for(int i = 0; i < 2K; i++){
            int value = (Math.random()*1000);
            if(!values.contains(value)){
                values.add(value);
            }
            else{
                i--;
            }
        }
        return values;
    }

    ArrayList generateFunc(){  //Mira
        //generate mutable ArrayList of prime numbers
        ArrayList<int> primes = new ArrayList<int>();
        for(int i = 0; i < PRIME_NUMS.length; i++){
            primes.add(PRIME_NUMS[i]);
        }
        //append randomly selected numbers to return array
        int[] funcCoefs = new int[K];
        for(int i = 0; i < K; i++){
            rand_idx = (int) (Math.random() * primes.size());
            funcCoefs[i] = primes.get(rand_idx);
            primes.remove(rand_idx);
        }
        return funcCoefs;
    }

    public static void main(String[] args){
        int[] funcCoefficients = generateFunc(); //generate hash function coefficients
        ArrayList vals = generateVals(); // generate values to add and test
         //add half of the generated values to the BloomFilter
        for(int i=0; i< (0.5 * vals.length); i++){
            add(vals[i], funcCoefficients);
        }
        //Use second half of list to check false positive rate-- none should return true for query()
        for(int i=(0.5*vals.length); i<vals.length; i++){
            boolean res = query(vals[i], funcCoefficients);
            if(res){                            //if query returns true, authoritatively check
                authoritative(vals[i]);
            }
        }





    }

}
