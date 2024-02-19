package main;

import hashBreaker.StringProvider;
import node.Node;
import hashBreaker.HashBreaker;

import java.util.Scanner;

public class MainWithHash {
    public static void main(String[] args) {
        Node.hashToFind = StringProvider.getHashFromString("zzzzz");
        Node n = Node.getInstance();


    }
}