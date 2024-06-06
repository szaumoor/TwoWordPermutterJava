package com.szaumoor.wordpermutter.entities;

import java.util.Objects;

/**
 * Record that encapsulates a Word to be combined with another word. Words are comparable in the same
 * way as {@link String} is comparable.
 *
 * @param content The content in text form of this word
 * @param type    The WordType associated with this Word
 */
public record Word(String content, WordType type) implements Comparable<Word> {

    /**
     * For convenience, a Word it's considered equal, if the text only matches.
     *
     * @param o the reference object with which to compare.
     * @return True if the text of both objects matches, false otherwise
     */
    @Override
    public boolean equals( final Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Word word = (Word) o;
        return content.equals( word.content );
    }

    @Override
    public int hashCode() {
        return Objects.hash( content );
    }

    @Override
    public String toString() {
        return content;
    }

    @Override
    public int compareTo( final Word o ) {
        return this.content.compareTo( o.content );
    }
}
