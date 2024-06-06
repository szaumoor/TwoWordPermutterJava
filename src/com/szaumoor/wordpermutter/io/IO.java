package com.szaumoor.wordpermutter.io;

import com.szaumoor.wordpermutter.entities.Word;
import com.szaumoor.wordpermutter.entities.WordType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Utility class dealing with the file reading and writing part of the program.
 */
public final class IO {
    private IO() {
        throw new AssertionError( "Utility class" );
    }

    /**
     * This <b>trailing</b> String marks a line as a Word of type {@link WordType#FIRST_WORD}
     */
    public static final String FIRST_WORD_MARK = "#";
    /**
     * This <b>trailing</b> String marks a line as a Word of type {@link WordType#SECOND_WORD}
     */
    public static final String SECOND_WORD_MARK = "*";
    /**
     * The file parsing will ignore strings <b>prepended</b> with this.
     */
    public static final String COMMENT_MARK = "//";

    /**
     * Extracts the text from the input file and turns them into
     * Word objects after parsing them appropriately.
     * See {@link IO#lineIsValid(String)} and {@link IO#getWord(String)}
     * for more details on the parsing rules.
     *
     * @param path Path to the file.
     * @return A List of Word objects after parsing it, or an empty list if any IOException occurs.
     */
    public static List<Word> getWordsFromFile( final Path path ) {
        try ( var lines = Files.lines( path ) ) {
            return lines.map( String::strip )
                    .filter( IO::lineIsValid )
                    .map( IO::getWord )
                    .sorted()
                    .toList();
        } catch ( final IOException ex ) {
            System.err.println( "IOException when getting to the file: " + ex.getMessage() );
            System.err.println( Arrays.toString( ex.getStackTrace() ) );
            return Collections.emptyList();
        }
    }


    /**
     * Writes a new text file into the provided {@link Path} using a
     * {@link List<String>}, where every element is separated by a
     * line break.
     *
     * @param path The path where the new file should be written.
     * @param list List of Strings to be written into file.
     */
    public static void writeWordList( final Path path, final List<String> list ) {
        try {
            Files.writeString( path, String.join( System.lineSeparator(), list ) );
        } catch ( final IOException ex ) {
            System.err.println( "IOException when writing file:" + ex.getMessage() );
            System.err.println( Arrays.toString( ex.getStackTrace() ) );
        }
    }

    /**
     * Gets a Word object from a String, which is considered {@link WordType#FIRST_WORD},
     * if it has a trailing {@link IO#FIRST_WORD_MARK}, a {@link WordType#SECOND_WORD},
     * if it has a trailing {@link IO#SECOND_WORD_MARK}. Otherwise, it is considered
     * a {@link WordType#ANY_ORDER_WORD}.
     *
     * @param word The word to process
     * @return The Word object that encapsulates the String and the type of word it is
     */
    private static Word getWord( final String word ) {
        var lastChar = word.substring( word.length() - 1 );
        return switch ( lastChar ) {
            case FIRST_WORD_MARK -> new Word( word.substring( 0, word.length() - 1 ), WordType.FIRST_WORD );
            case SECOND_WORD_MARK -> new Word( word.substring( 0, word.length() - 1 ), WordType.SECOND_WORD );
            default -> new Word( word, WordType.ANY_ORDER_WORD );
        };
    }

    /**
     * Checks if a line is parseable, which in this case means that it's
     * not blank as per the {@link String#isBlank()} method, and that
     * doesn't start by {@link IO#COMMENT_MARK}.
     *
     * @param line The line to check for validity
     * @return true if it's valid, false otherwise
     */
    private static boolean lineIsValid( final String line ) {
        return !line.isBlank() && !line.startsWith( COMMENT_MARK );
    }

    /**
     * Gets the filename without the extension. A file with extension
     * is considered a file that ends with {@code .<ext>}.
     *
     * @param path Path to the file
     * @return The name of the file without extension
     */
    public static String getNoExtFilename( final Path path ) {
        final var name = path.getFileName().toString();
        final var indexOfLastPeriod = name.lastIndexOf( "." );
        final var hasExtension = indexOfLastPeriod != -1;
        return hasExtension ? name.substring(0, indexOfLastPeriod) : name;
    }
}
