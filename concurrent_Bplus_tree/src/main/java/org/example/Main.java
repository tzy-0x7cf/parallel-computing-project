package org.example;

import java.util.ArrayList;
import java.util.List;

import BplusTree.BPlusTree;
import BplusTree.BlinkTree;
import BplusTree.Btree;

public class Main {
    public static void main(String[] args) {
        Btree<Integer, Integer> tree = new BlinkTree<>(100);
        for (int i = 0; i < 50; i++) {
            tree.insert(i, i + 20);
        }
        
        for (int i = 0; i < 50; i++) {
            assert tree.get(i) == i + 20;
        }
    }
}