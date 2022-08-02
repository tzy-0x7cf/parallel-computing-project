package org.example;

import java.util.ArrayList;
import java.util.List;

import BplusTree.BPlusTree;

public class Main {
    public static void main(String[] args) {
        BPlusTree<Integer, Integer> tree = new BPlusTree<>();
        tree.insert(1, 10);
        tree.insert(2, 11);
        tree.insert(3, 12);
        tree.insert(10, 22);
        tree.insert(15,99);
        tree.insert(13,20);
        System.out.println("Found value: " + tree.get(13));
    }
}