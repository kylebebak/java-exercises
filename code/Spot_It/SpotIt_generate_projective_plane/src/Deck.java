import java.util.ArrayList;

/**
 * There can't be less than 2 symbols per card (SPC). SPC must also be <= 10.
 * Finally, SPC can't be 7, because no projective plane of order 6 exists
 * 
 */
public class Deck {

	private ArrayList<boolean[]> cards;
	private int sym; // number of unique symbols
	private final int minCards = 2;
	private final int maxCards = 10;

	/**
	 * @param spc
	 *            : symbols per card
	 */
	public Deck(int spc) {
		if (spc < minCards)
			spc = minCards;
		if (spc > maxCards)
			spc = maxCards;
		sym = spc + (spc - 1) * (spc - 1);
		cards = new ArrayList<boolean[]>();

		boolean[] newCard;
		boolean[] oldCard;
		boolean[] shares; // does newCard already share a symbol with oldCard
		int invalid = -1;
		int soc; // symbols on a given card

		int[] sc = new int[sym]; // symbol count over all cards for given symbol

		card: for (int c = 0; c < sym; c++) {
			newCard = new boolean[sym];
			shares = new boolean[cards.size()];
			soc = 0;

			symbol: for (int j = 0; j < sym; j++) {
				if (sc[j] >= spc)
					continue;
				if (j == invalid)
					continue;

				for (int i = 0; i < cards.size(); i++) {
					oldCard = cards.get(i);
					if (shares[i])
						if (oldCard[j])
							continue symbol;
				}
				newCard[j] = true;
				soc++;
				sc[j]++;
				if (soc >= spc) {
					cards.add(newCard);
					continue card;
				}

				for (int i = 0; i < cards.size(); i++) {
					oldCard = cards.get(i);
					if (oldCard[j])
						shares[i] = true;
				}
			}

			if (soc >= spc) {
				cards.add(newCard);
				invalid = -1;
			} else {
				c--; // if card was unsuccessful repeat iteration

				for (int j = 2 * spc - 1; j < sym; j++) {
					if (newCard[j]) {
						invalid = j;
						System.out.println(invalid);
						break;
					}
				}
			}
		}
	}

	/**
	 * Return a string representation of the deck
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean[] card;
		for (int c = 0; c < cards.size(); c++) {
			card = cards.get(c);
			for (int j = 0; j < card.length; j++) {
				if (card[j])
					sb.append('X');
				else
					sb.append(' ');
				sb.append("  ");
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	/**
	 * Return a string representation a dot product matrix between each pair of
	 * cards in the deck. If there is exactly one shared symbol between every
	 * pair of cards in deck, then all entries will be 1
	 */
	public String checkDeck() {
		StringBuilder sb = new StringBuilder();
		boolean[] ca;

		for (int a = 0; a < cards.size(); a++) {
			ca = cards.get(a);
			boolean[] cb;

			for (int b = a + 1; b < cards.size(); b++) {
				cb = cards.get(b);
				int share = 0;

				for (int j = 0; j < cb.length; j++) {
					if (ca[j] && cb[j])
						share++;
				}
				sb.append(share + " ");

			}
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Return number of distinct symbols in deck
	 */
	public int symbolCount() {
		return sym;
	}

	/**
	 * Return number of distinct cards in deck
	 */
	public int deckCount() {
		return cards.size();
	}

	public static void main(String[] args) {
		for (int s = 2; s < 5; s++) {
			Deck d = new Deck(s);
			System.out.println("symbols per card : " + s
					+ "      distinct symbols : " + d.symbolCount()
					+ "      distinct cards : " + d.deckCount());
			System.out.print(d.toString());
			System.out.print(d.checkDeck());
			System.out.println();
		}
	}

}
