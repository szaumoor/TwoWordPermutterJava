package com.szaumoor.wordpermutter.entities;

/**
 * Enum that marks Word objects appropriately for the purposes of permutations.
 */
public enum WordType {
    /**
     * This marks a Word as order agnostic, i.e., can be used as first or last.
     */
    ANY_ORDER_WORD,
    /**
     * This marks a word and only valid as the first word in the permutation
     */
    FIRST_WORD,
    /**
     * This marks a word and only valid as the second word in the permutation
     */
    SECOND_WORD
}
