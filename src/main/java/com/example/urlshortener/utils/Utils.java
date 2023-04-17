package com.example.urlshortener.utils;

import java.net.URL;

public class Utils {
    private static final String ALLOWED_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final char[] ALLOWED_CHARACTERS = ALLOWED_STRING.toCharArray();
    private static final int BASE = ALLOWED_CHARACTERS.length;

    /**
     * This function encode the input in base62
     * @param input long value to encode in base62
     * @return value encoded in base62
     */
    public static String encode(long input) {
        StringBuilder encodedString = new StringBuilder();

        if(input == 0) {
            return String.valueOf(ALLOWED_CHARACTERS[0]);
        }

        while (input > 0) {
            encodedString.append(ALLOWED_CHARACTERS[(int) (input % BASE)]);
            input = input / BASE;
        }

        return encodedString.reverse().toString();
    }

    /**
     * This function decode the value in argument in base 62
     * @param input value in base62 to decode
     * @return long value decoded
     */
    public static long decode(String input) {
        char[] characters = input.toCharArray();
        int length = characters.length;

        int decoded = 0;

        int counter = 1;
        for (int i = 0; i < length; i++) {
            decoded += ALLOWED_STRING.indexOf(characters[i]) * Math.pow(BASE, length - counter);
            counter++;
        }
        return decoded;
    }

    /**
     * This function verify if the URL in argument is valid
     * Returns true if url is valid
     **/
    public static boolean isUrlValid(String url)
    {
        /* Try creating a valid URL
        * The toURI() method ensures that any URL string that complies with RC 2396 is converted to URL
        * it verify that our URL is construct like this "scheme:[//authority][/path][?query][#fragment]"
        * */
        try {
            new URL(url).toURI();
            return true;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }
}
