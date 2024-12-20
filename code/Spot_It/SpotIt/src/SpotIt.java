import java.awt.Image;


public class SpotIt {

	
	
	

private int spc = 6; // symbols per card, 3 - 10 is allowed, with the exception of 7
private int nSyms; // number of cards in deck, also number of unique symbols in deck

private Image deckImg;
private Image[] deckImgs;
private Image[] allImgs;
private String[] deckInfo;
private int imgW;
private int imgH;
private int nImages = 100;

private Deck deck;
private ArrayList<Card> cards;
private float dx = 350;
private float dy = 900;

private Player p1, p2;
private float px1 = 350;
private float py1 = 300;
private float px2 = 300;
private float py2 = 800;
private PFont font;

private Splash splash;

private final int minSPC = 3;
private final int maxSPC = 10;
private final int badSPC = 7;
private final color bg = color(50); // background color for board

private int timeLimit; // 30000 ms = 30 seconds
private final int msPerCard = 1200;
private static final int extraPerCard = 2000; // increment player's time limit by this amount



void setup() {

  size(700, 1200);
  textAlign(CENTER);
  /***************************************
   * Import and parse deck info based on spc (symbols per card)
   ****************************************/
  spc = constrain(spc, minSPC, maxSPC);
  if (spc == badSPC) spc++;

  deckInfo = loadStrings("text/spc_" + Integer.toString(spc) + ".txt");
  nSyms = Integer.parseInt(deckInfo[0]);
  timeLimit = msPerCard * nSyms;

  font = createFont("Helvetica", 32);

  /***************************************
   * Import and parse symbol images
   ****************************************/

  allImgs = new Image[nImages];
  deckImgs = new Image[nSyms];

  for (int i = 0; i < nImages; i++) {
    allImgs[i] = loadImage("images/img" + Integer.toString(i) + ".png");
  }
  imgW = allImgs[0].width;
  imgH = allImgs[0].height;

  shuffleArray(allImgs); // ensures that a random sampling of the available images are chosen for the deck
  for (int c = 0; c < nSyms; c++) deckImgs[c] = allImgs[c];


  /***************************************
   * Instantiate cards and add them to cards ArrayList
   ****************************************/
  cards = new ArrayList<Card>();
  int[] card;
  String[] symbols;

  for (int c = 1; c <= nSyms; c++) {
    card = new int[spc];
    symbols = deckInfo[c].split(" ");

    for (int j = 0; j < spc; j++) {
      card[j] = Integer.parseInt(symbols[j]);
    }
    cards.add(new Card(spc, card, allImgs, 0, 0));
    // it doesn't matter what the location of the added cards is
    // because addCard() changes the position of the card to the
    // current deck position
  }

  background(bg);
  /****************************************/
  initializeSplash();
}




void initializeSplash() {
  splash = new Splash();
}


void initializeGame() {

  /***************************************
   * Instantiate deck, shuffle cards ArrayList, add cards to deck
   ****************************************/
  deck = new Deck(dx, dy);
  Collections.shuffle(cards);
  for (Card c : cards)
    deck.addCard(c); 

  /***************************************
   * Instantiate players with a card each from the top of the deck
   ****************************************/
  p1 = new Player(px1, py1, deck.removeTop(), deck, font, timeLimit);
}




void draw() {

  if (!splash.inPlay()) {
    splash.displayAndUpdate();
  }

  else {
    background(bg);
    deck.displayDeck();
    deck.displayTopFront();

    p1.displayAndUpdate();
    //    p2.displayAndUpdate();

    if (deck.getSize() == 0)
      initializeSplash();

    if (p1.timeIsUp()) {
      initializeSplash();
    }
  }
}

void mousePressed() {
  if (splash.inPlay())
    p1.checkClick();

  if (splash.checkClick())
    initializeGame();
}

void mouseDragged() {
  if (splash.inPlay())
    p1.checkRotate();
}

void mouseReleased() {
  if (splash.inPlay())
    p1.checkRelease();
}



// implementing FisherÐYates shuffle
private void shuffleArray(Image[] a)
{
  for (int i = a.length - 1; i >= 0; i--)
  {
    int index = (int) random(i + 1);
    // Simple swap
    Image p = a[index];
    a[index] = a[i];
    a[i] = p;
  }
}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
