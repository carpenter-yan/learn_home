package com.carpenter.yan.java.InnerClass;

import java.util.Iterator;

public class DataStructure {
    private static final int SIZE = 15;
    private int[] arrayOfInts = new int[SIZE];

    public DataStructure(){
        for(int i = 0; i < SIZE; i++){
            arrayOfInts[i] = i;
        }
    }

    public void printEven(){
        DataStructureIterator iterator = this.new EvenIterator();
        while(iterator.hasNext()){
            System.out.print(((EvenIterator) iterator).next() + " ");
        }
    }

    interface DataStructureIterator extends Iterator<Integer>{}

    private class EvenIterator implements DataStructureIterator {
        private int nextIndex = 0;
        @Override
        public boolean hasNext() {
            return nextIndex < SIZE;
        }

        @Override
        public Integer next() {
            Integer retValue = Integer.valueOf(arrayOfInts[nextIndex]);
            nextIndex += 2;
            return retValue;
        }
    }

    public static void main(String[] args) {
        DataStructure ds = new DataStructure();
        ds.printEven();
    }
}

