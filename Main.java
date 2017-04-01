package com.HMH;

import java.util.*;
import java.util.function.Predicate;

//supports Java 8

public class Main {

    private static final String NON_CHARACTER_REGEX = "[^A-Za-z]";

    public static void main(String[] pArgs) {
        playTheGame();
    }

    public static void playTheGame() {
        final List<String> rawInputStrings = getNames();
        final Set<String> palindromes = getMatchingPermutations(rawInputStrings, (pPotentialPalindrome)-> {
            if (pPotentialPalindrome != null && pPotentialPalindrome.length() > 0) {
                // Massage the input: remove non-characters, make lowercase, put into a StringBuilder
                StringBuilder massagedVersion = new StringBuilder(
                        pPotentialPalindrome.replace(NON_CHARACTER_REGEX, "").toLowerCase());
                return massagedVersion.toString().equals(massagedVersion.reverse().toString());
            } return false;});
        publishResultsForPalindromeCheck(palindromes);
    }


    /**
     * Encapsulation of the getting of the strings representing dwarf names.  This implementation could be changed to
     * solicit names from user, get names from webservice call,  make up names randomly, etc.
     * @return a List of  N Strings, where N <= Integer.MAX_VALUE.  Strings may be null, or empty.  Will not
     * return a null object.
     */
    public static List<String> getNames() {
        return Arrays.asList(
                "Gimli", "Fili", "Ilif", "Ilmig", "Mark"
        );

    }

    /**
     * Get permutations that match the given filter.  The permutations will be Strings that use 1 or more of the
     * raw Strings in the given list, appended to the given prefix.
     *
     * @param pRawStrings  a list of raw Strings used to build permutations. May be null or empty.
     *                     Values in the list may be null or empty.
     * @param pFilterToUse the Predicate filter to use, as defined by the consumer of this method
     * @return a Set of permutations of the given strings, each permutation concatenated into a single string, that
     * match the given filter. Will not return null, nor null or empty results within the return object, nor duplicates
     * (naturally, since the return object is a Set)
     */
    public static Set<String> getMatchingPermutations(final List<String> pRawStrings,
                                                      final Predicate<String> pFilterToUse) {

        final Set<String> returnVal;

        if (pRawStrings != null && pRawStrings.size() > 0) {

            final int rawStringSize = pRawStrings.size();
            final int warningQtyRawData = 10;

            returnVal = getMatchingPermutationsRecursive("", pRawStrings, pFilterToUse);

        } else {
            returnVal = new HashSet<>();
        }
        return returnVal;
    }

    /**
     * Get permutations that match the given filter.  The permutations will be Strings that use 1 or more of the
     * raw Strings in the given list, appended to the given prefix.
     *
     * @param pCurrentPrefix will be used as a prefix for all permutations that can be built from the given list of Strings.
     *                       May be empty or null.
     * @param pRawStrings    a list of raw Strings used to build permutations. May be null or empty.
     *                       Values in the list may be null or empty.
     * @param pFilterToUse   the Predicate filter to use, as defined by the consumer of this method
     * @return a list of String, representing the permutations that match the tst in the given predicate
     */
    private static Set<String> getMatchingPermutationsRecursive(final String pCurrentPrefix,
                                                                final List<String> pRawStrings,
                                                                final Predicate<String> pFilterToUse) {
        final String prefixToUse = (pCurrentPrefix == null) ? "" : pCurrentPrefix;

        Set<String> returnVal = new HashSet<>();
        if (pRawStrings != null) {
            // Loop through each raw String
            for (int i = 0; i < pRawStrings.size(); i++) {
                String suffix = pRawStrings.get(i);
                if (suffix != null && suffix.length() > 0) {
                    // Check the candidate permutations
                    String newPermutation = prefixToUse + suffix;

                    if (pFilterToUse.test(newPermutation)) {
                        returnVal.add(newPermutation);
                    }

                    // Don't recurse if the new list would be empty
                    if (pRawStrings.size() > 1) {
                        List<String> newList = new ArrayList<>(pRawStrings);
                        newList.remove(i);
                        returnVal.addAll(getMatchingPermutationsRecursive(newPermutation, newList, pFilterToUse));
                    }
                }
            }

        }
        return returnVal;
    }

    /**
     * Write the found palindromes to System out println. (Could be refactored to render the results in other ways)
     *
     * @param pFoundResults a list of Strings that have been determined to be palindromes
     */
    private static void publishResultsForPalindromeCheck(Set<String> pFoundResults) {

        System.out.println("\n");
        if (pFoundResults == null || pFoundResults.size() == 0) {
            final String localizedString = "No palindromes were found - you may have to 'borrow or rob' some from elsewhere.";
            System.out.println(localizedString);
        } else {

            final int setSize = pFoundResults.size();
            Object[] args = {String.valueOf(setSize)};
            System.out.println("Here are the resulting " + setSize + " palindromes:");
            for (String foundPalindrome : pFoundResults) {
                System.out.println("  - " + foundPalindrome);
            }
        }
    }

    /**
     * @param pNumberOfElements the number of raw elements that we'll use to find all possible permutations
     * @return the potential number of permutations for the given number of items.  Returns 0 if given number
     * is <= 0.
     */
    private static long getPotentialPermutationsQuantity(final int pNumberOfElements) {
        long returnVal = 0;
        if (pNumberOfElements > 0) {
            for (int slots = 1; slots <= pNumberOfElements; slots++) {
                returnVal += getFactorialWithSlots(pNumberOfElements, slots);
            }
        }
        return returnVal;
    }

    /**
     * Calculate the number of potential permutations for N items in X slots,
     * or N! / (N! - X!),
     * or Number! / (Number - Slots)!
     *
     * @param pNumber must be a positive integer
     * @param pSlots  must be a positive integer
     * @return the number of permutations calculated for the inputs.
     */
    private static long getFactorialWithSlots(final int pNumber, final int pSlots) {
        if (pSlots > pNumber) {
            throw new IllegalArgumentException("given slots may not be larger than the given number of items");
        }
        if (pSlots <= 0) {
            throw new IllegalArgumentException("given slots must be a positive integer");
        }
        if (pNumber <= 0) {
            throw new IllegalArgumentException("given number of items must be a positive integer");
        }

        long returnVal = 1;
        if (pNumber > 1) {
            for (int i = 0; i < pSlots; i++) {
                returnVal *= (pNumber - i);
            }
        }
        return returnVal;
    }

}


