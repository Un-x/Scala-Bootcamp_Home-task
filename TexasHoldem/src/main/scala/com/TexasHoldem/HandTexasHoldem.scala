package com.TexasHoldem
import com.TexasHoldem

/** Set of cards a player has. */
case class HandTexasHoldem(cards: Set[CardTexasHoldem]) extends Ordered[HandTexasHoldem]{
  require((1 to 7) contains cards.size, "A hand must contain at least 1 and at most 7 cards")
  /** Compares this hand with `that`. */
  override def compare(that: HandTexasHoldem): Int = TexasHoldem.Combination(this) compare TexasHoldem.Combination(that)
  /** Combination corresponding to this hand. */
  lazy val toCombination: CombinationTexasHoldem = TexasHoldem.Combination(this)
}
object HandTexasHoldem{
  /** Creates hand consisting of the provided cards. */
  def apply(card1: CardTexasHoldem, otherCards: CardTexasHoldem*): HandTexasHoldem = HandTexasHoldem(otherCards.toSet + card1)
}