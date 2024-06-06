package com.szaumoor.wordpermutter.algorithms;

import com.szaumoor.wordpermutter.entities.Word;
import com.szaumoor.wordpermutter.entities.WordType;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.szaumoor.wordpermutter.entities.WordType.SECOND_WORD;
import static java.lang.Character.toUpperCase;

/**
 * Utility class to generate the permutations based on the provided list of Word objects
 * by {@link com.szaumoor.wordpermutter.io.IO#getWordsFromFile(Path)}.
 */
public final class Permutator {
    private Permutator() {
        throw new AssertionError( "Utility class" );
    }

    /**
     * This method takes a list of Word objects and generates a list of Strings
     * containing every permutation according to the application rules, which are:
     * <br><br>
     * - A word cannot combine with itself <br>
     * - A combination cannot be alliterative (see {@link Permutator#notAlliteration(String, String)}) <br>
     * - The combination cannot exceed {@code charLimitInclusive} in length <br>
     * - Words marked as {@link WordType#FIRST_WORD} cannot appear anywhere else but the beginning<br>
     * - Words marked as {@link WordType#SECOND_WORD} cannot appear anywhere else but the end<br><br>
     * This list is padded with helpful messages and is meant to be written to disk as a text file.
     *
     * @param words              List of Word objects to process
     * @param charLimitInclusive Character limit for the permutations, those beyond the limit are rejected.
     * @return List of strings containing the permutations.
     */
    public static List<String> generatePermutations( final List<Word> words, final int charLimitInclusive ) {
        final int wordsLength = words.size();
        final List<String> permutations = new ArrayList<>( 1 + ( wordsLength * ( wordsLength - 1 ) ) + 2 * wordsLength );
        int permCount = 0;

        var nonSecondWords = words.stream().filter( w -> w.type() != SECOND_WORD ).toList();
        for ( var word : nonSecondWords ) {
            var wordCombinations = words.stream()
                    .filter( secondWord -> !secondWord.equals( word ) )
                    .filter( secondWord -> secondWord.type() != WordType.FIRST_WORD )
                    .filter( secondWord -> notAlliteration( word.content(), secondWord.content() ) )
                    .map( secondWord -> word.content() + secondWord )
                    .filter( s -> s.length() <= charLimitInclusive )
                    .toList();
            if ( wordCombinations.isEmpty() ) continue;
            
            permutations.add( "Permutations starting with " + word + "\n" + "------------------------------------" );
            permutations.addAll( wordCombinations );
            permCount += wordCombinations.size();
            permutations.add( "\n" );
        }

        permutations.add( "------------------------------------\n"
                + "A total of " + permCount + " valid permutations were discovered" );
        return permutations;
    }

    /**
     * Checks if a combination of two words is not alliterative, which is one of the rules for
     * creating the permutations.
     *
     * @param firstWord  The first word to check
     * @param secondWord The second word to check
     * @return true if it's valid (not alliterative), false if it's invalid (alliterative)
     */
    private static boolean notAlliteration( final String firstWord, final String secondWord ) {
        return firstCharUpper( firstWord ) != firstCharUpper( secondWord ) &&
                lastCharUpper( firstWord ) != firstCharUpper( secondWord );
    }

    /**
     * Returns the last character of a String in uppercase.
     *
     * @param string String to extract the character from.
     * @return The last character uppercased
     */
    private static char lastCharUpper( final String string ) {
        return toUpperCase( string.charAt( string.length() - 1 ) );
    }

    /**
     * Returns the first character of a String in uppercase.
     *
     * @param string String to extract the character from.
     * @return The first character uppercased
     */
    private static char firstCharUpper( final String string ) {
        return toUpperCase( string.charAt( 0 ) );
    }
}
