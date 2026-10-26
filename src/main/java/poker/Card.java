package poker;

/**
 * Minimal immutable Card implementation for Exercises 5.8-5.10.
 * Card.get(...) returns one shared object per rank/suit pair.
 */
public final class Card {
    private static final Card[][] CARDS =
            new Card[Suit.values().length][Rank.values().length];

    static {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                CARDS[suit.ordinal()][rank.ordinal()] = new Card(rank, suit);
            }
        }
    }

    private final Rank rank;
    private final Suit suit;

    private Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public static Card get(Rank rank, Suit suit) {
        if (rank == null || suit == null) {
            throw new IllegalArgumentException("rank and suit must not be null");
        }
        return CARDS[suit.ordinal()][rank.ordinal()];
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}
