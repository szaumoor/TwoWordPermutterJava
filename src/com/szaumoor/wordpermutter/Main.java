package com.szaumoor.wordpermutter;

import com.szaumoor.wordpermutter.algorithms.Permutator;
import com.szaumoor.wordpermutter.io.IO;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Main class. The program has to be called with either one, two or three arguments. <br><br>
 * <p>
 * -<b>One argument</b>: Specify which file to parse <br><br>
 * <p>
 * -<b>Two arguments</b>: Specify which file to parse, followed by the maximum length for generated combined words. <br> <br>
 * <p>
 * Examples: <br><br>
 * <p>
 * -<b>One argument</b>: {@code java -jar TwoWordPermutter.jar words}. This will
 * read {@code words} and generate an output file with the name {@code <filename>_output.txt}, unless
 * the name is the same, in which case, it will append {@code _new} to the filename. The word combination length
 * limit is set to 14.<br><br>
 * <p>
 * -<b>Two arguments</b>: {@code java -jar TwoWordPermutter.jar words 14}. This will mirror the previous
 * method, but the output will only contain permutations that are up to the passed length. Negative values or
 * improperly formatted numbers will make the program fail.<br> <br>
 */
public class Main {
    public static void main( final String[] arguments ) {
        final var args = arguments.length;
        if ( args < 1 ) {
            System.err.println( "No parameters provided" );
            System.exit( 1 );
        }

        final var firstArg = arguments[0];
        final var path = Path.of( firstArg );

        if ( Files.notExists( path ) ) {
            System.err.println( "File could not be found!" );
            System.exit( 1 );
        }

        final var secondArg = args >= 2 ? arguments[1] : "14";

        try {
            final int charLimit = Integer.parseInt( secondArg );
            if ( charLimit < 2 ) {
                System.err.println( "Combinations need to be at least 2 characters in length" );
                System.exit( 1 );
            }

            final var wordList = IO.getWordsFromFile( path );
            final var strings = Permutator.generatePermutations( wordList, charLimit );
            final var extractedFilename = IO.getNoExtFilename( path );

            IO.writeWordList( Path.of( extractedFilename + "_output.txt" ), strings );

        } catch ( final NumberFormatException ex ) {
            System.err.println( "Improperly formatted number. Exiting program." );
            System.exit( 1 );
        }
    }
}
