package poker;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test suite for Exercises 5.8, 5.9 and 5.10.
 *
 * The first three tests are the MINIMUM executions required by the exercises:
 * ex58  -> 1 execution of the UUT
 * ex59  -> 2 executions of the UUT
 * ex510 -> 3 executions of the UUT
 *
 * The remaining parameterized tests form an EXTENDED suite. They include all
 * 36 straight flushes recognized by this implementation and representative
 * invalid/robustness cases. The extended suite is broader, but it is still not
 * exhaustive over all possible five-card hands.
 */
public class TestPokerHand {

    /** Helper required by Exercise 5.8 to invoke the private method. */
    private boolean invokeIsStraightFlush(PokerHand hand) {
        try {
            Method method = PokerHand.class.getDeclaredMethod("isStraightFlush");
            method.setAccessible(true);
            return (boolean) method.invoke(hand);
        } catch (NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    // ---------------------------------------------------------------------
    // MINIMUM TESTS REQUIRED BY EXERCISES 5.8, 5.9 AND 5.10
    // ---------------------------------------------------------------------

    /** Exercise 5.8: one valid straight flush must return true. */
    @Test
    @Tags({@Tag("ex58"), @Tag("ex59"), @Tag("ex510"), @Tag("minimum")})
    public void testStraightFlushReturnsTrue() {
        PokerHand hand = new PokerHand(
                Card.get(Rank.ACE, Suit.CLUBS),
                Card.get(Rank.TWO, Suit.CLUBS),
                Card.get(Rank.THREE, Suit.CLUBS),
                Card.get(Rank.FOUR, Suit.CLUBS),
                Card.get(Rank.FIVE, Suit.CLUBS));

        assertTrue(invokeIsStraightFlush(hand));
    }

    /** Exercise 5.9: same suit, but ranks are not consecutive. */
    @Test
    @Tags({@Tag("ex59"), @Tag("ex510"), @Tag("minimum")})
    public void testSameSuitButRankGapReturnsFalse() {
        PokerHand hand = new PokerHand(
                Card.get(Rank.ACE, Suit.CLUBS),
                Card.get(Rank.TWO, Suit.CLUBS),
                Card.get(Rank.THREE, Suit.CLUBS),
                Card.get(Rank.FOUR, Suit.CLUBS),
                Card.get(Rank.SIX, Suit.CLUBS));

        assertFalse(invokeIsStraightFlush(hand));
    }

    /** Exercise 5.10: one card has a different suit. */
    @Test
    @Tags({@Tag("ex510"), @Tag("minimum")})
    public void testDifferentSuitReturnsFalse() {
        PokerHand hand = new PokerHand(
                Card.get(Rank.ACE, Suit.CLUBS),
                Card.get(Rank.TWO, Suit.DIAMONDS),
                Card.get(Rank.THREE, Suit.CLUBS),
                Card.get(Rank.FOUR, Suit.CLUBS),
                Card.get(Rank.FIVE, Suit.CLUBS));

        assertFalse(invokeIsStraightFlush(hand));
    }

    // ---------------------------------------------------------------------
    // EXTENDED TEST SUITE
    // ---------------------------------------------------------------------

    /**
     * Exercise 5.8 extended: execute every straight flush recognized by the
     * implementation: 9 rank sequences x 4 suits = 36 executions.
     */
    @ParameterizedTest(name = "Straight flush #{index}: {0}-{1}-{2}-{3}-{4} of {5}")
    @MethodSource("allStraightFlushes")
    @Tags({@Tag("extended"), @Tag("all-straight-flushes")})
    public void testAllPossibleStraightFlushes(
            Rank r1, Rank r2, Rank r3, Rank r4, Rank r5, Suit suit) {

        PokerHand hand = new PokerHand(
                Card.get(r1, suit),
                Card.get(r2, suit),
                Card.get(r3, suit),
                Card.get(r4, suit),
                Card.get(r5, suit));

        assertTrue(invokeIsStraightFlush(hand));
    }

    static Stream<Arguments> allStraightFlushes() {
        Stream.Builder<Arguments> builder = Stream.builder();
        Rank[] ranks = Rank.values();

        for (Suit suit : Suit.values()) {
            for (int start = 0; start <= ranks.length - 5; start++) {
                builder.add(Arguments.of(
                        ranks[start],
                        ranks[start + 1],
                        ranks[start + 2],
                        ranks[start + 3],
                        ranks[start + 4],
                        suit));
            }
        }

        return builder.build();
    }

    /** Additional invalid cases: same suit but different rank gaps. */
    @ParameterizedTest(name = "Rank-gap invalid case #{index}")
    @MethodSource("rankGapHands")
    @Tag("extended")
    public void testAdditionalRankGapHandsReturnFalse(Card[] cards) {
        assertFalse(invokeIsStraightFlush(new PokerHand(cards)));
    }

    static Stream<Arguments> rankGapHands() {
        return Stream.of(
                Arguments.of((Object) new Card[] {
                        Card.get(Rank.ACE, Suit.HEARTS),
                        Card.get(Rank.TWO, Suit.HEARTS),
                        Card.get(Rank.THREE, Suit.HEARTS),
                        Card.get(Rank.FOUR, Suit.HEARTS),
                        Card.get(Rank.SEVEN, Suit.HEARTS)
                }),
                Arguments.of((Object) new Card[] {
                        Card.get(Rank.TWO, Suit.SPADES),
                        Card.get(Rank.THREE, Suit.SPADES),
                        Card.get(Rank.FOUR, Suit.SPADES),
                        Card.get(Rank.FIVE, Suit.SPADES),
                        Card.get(Rank.SEVEN, Suit.SPADES)
                }),
                Arguments.of((Object) new Card[] {
                        Card.get(Rank.FIVE, Suit.DIAMONDS),
                        Card.get(Rank.SIX, Suit.DIAMONDS),
                        Card.get(Rank.SEVEN, Suit.DIAMONDS),
                        Card.get(Rank.NINE, Suit.DIAMONDS),
                        Card.get(Rank.TEN, Suit.DIAMONDS)
                }));
    }

    /** Additional invalid cases: ranks are consecutive but one suit differs. */
    @ParameterizedTest(name = "Suit-mismatch invalid case #{index}")
    @MethodSource("differentSuitHands")
    @Tag("extended")
    public void testAdditionalDifferentSuitHandsReturnFalse(Card[] cards) {
        assertFalse(invokeIsStraightFlush(new PokerHand(cards)));
    }

    static Stream<Arguments> differentSuitHands() {
        return Stream.of(
                Arguments.of((Object) new Card[] {
                        Card.get(Rank.TWO, Suit.CLUBS),
                        Card.get(Rank.THREE, Suit.CLUBS),
                        Card.get(Rank.FOUR, Suit.HEARTS),
                        Card.get(Rank.FIVE, Suit.CLUBS),
                        Card.get(Rank.SIX, Suit.CLUBS)
                }),
                Arguments.of((Object) new Card[] {
                        Card.get(Rank.FOUR, Suit.SPADES),
                        Card.get(Rank.FIVE, Suit.SPADES),
                        Card.get(Rank.SIX, Suit.SPADES),
                        Card.get(Rank.SEVEN, Suit.DIAMONDS),
                        Card.get(Rank.EIGHT, Suit.SPADES)
                }),
                Arguments.of((Object) new Card[] {
                        Card.get(Rank.NINE, Suit.HEARTS),
                        Card.get(Rank.TEN, Suit.HEARTS),
                        Card.get(Rank.JACK, Suit.HEARTS),
                        Card.get(Rank.QUEEN, Suit.HEARTS),
                        Card.get(Rank.KING, Suit.CLUBS)
                }));
    }

    /** Duplicate rank cannot be a straight flush. */
    @Test
    @Tag("extended")
    public void testDuplicateRankReturnsFalse() {
        PokerHand hand = new PokerHand(
                Card.get(Rank.ACE, Suit.SPADES),
                Card.get(Rank.TWO, Suit.SPADES),
                Card.get(Rank.TWO, Suit.SPADES),
                Card.get(Rank.THREE, Suit.SPADES),
                Card.get(Rank.FOUR, Suit.SPADES));

        assertFalse(invokeIsStraightFlush(hand));
    }

    /** Valid cards may be supplied in arbitrary order because PokerHand sorts. */
    @ParameterizedTest(name = "Shuffled valid straight flush #{index}")
    @MethodSource("shuffledStraightFlushes")
    @Tag("extended")
    public void testShuffledInputStillReturnsTrue(Card[] cards) {
        assertTrue(invokeIsStraightFlush(new PokerHand(cards)));
    }

    static Stream<Arguments> shuffledStraightFlushes() {
        return Stream.of(
                Arguments.of((Object) new Card[] {
                        Card.get(Rank.FIVE, Suit.CLUBS),
                        Card.get(Rank.ACE, Suit.CLUBS),
                        Card.get(Rank.THREE, Suit.CLUBS),
                        Card.get(Rank.TWO, Suit.CLUBS),
                        Card.get(Rank.FOUR, Suit.CLUBS)
                }),
                Arguments.of((Object) new Card[] {
                        Card.get(Rank.KING, Suit.HEARTS),
                        Card.get(Rank.NINE, Suit.HEARTS),
                        Card.get(Rank.JACK, Suit.HEARTS),
                        Card.get(Rank.TEN, Suit.HEARTS),
                        Card.get(Rank.QUEEN, Suit.HEARTS)
                }));
    }
}
