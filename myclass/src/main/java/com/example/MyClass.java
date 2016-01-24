package com.example;

import edu.princeton.cs.introcs.BinaryStdIn;
import edu.princeton.cs.introcs.BinaryStdOut;

public class MyClass {
    private static final int inputChars = 7;  // number of input chars
    private static final int L = 4096;          // number of codewords = 2^W
    private static final int W = 12;            // codeword width

    public static void decompressMessage(String message) {
        /*char[] st = message.toCharArray();*/

        String[] st = message.split("");
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < inputChars; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        if (codeword == inputChars) return;           // expanded message is empty string
        String val = st[codeword];

        while (true) {
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            if (codeword == inputChars) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L) st[i++] = val + s.charAt(0);
            val = s;
        }
        BinaryStdOut.close();
    }
}
