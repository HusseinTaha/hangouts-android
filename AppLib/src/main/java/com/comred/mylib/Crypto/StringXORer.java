package com.comred.mylib.Crypto;

import android.util.Base64;


public class StringXORer {

    public static String ReverseS(String args )
    {
        String original, reverse = "";
        original = args;

        int length = original.length();

        for ( int i = length - 1 ; i >= 0 ; i-- )
            reverse = reverse + original.charAt(i);

        return reverse;
    }

    public String encode(String s, String key) {
        return base64Encode(xorWithKey(s.getBytes(), key.getBytes()));
    }

    public String decode(String s, String key) {
        return new String(xorWithKey(base64Decode(s), key.getBytes()));
    }

    private byte[] xorWithKey(byte[] a, byte[] key) {
        byte[] out = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = (byte) (a[i] ^ key[i%key.length]);
        }
        return out;
    }

    private byte[] base64Decode(String s) {
        try {

            return Base64.decode(s,Base64.DEFAULT );
        } catch (Exception e) {throw new RuntimeException(e);}
    }

    private String base64Encode(byte[] bytes) {

        return Base64.encodeToString(bytes,Base64.DEFAULT).replaceAll("\\s", "");

    }
}