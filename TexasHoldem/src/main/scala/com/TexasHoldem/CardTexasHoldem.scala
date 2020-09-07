package com.TexasHoldem
import SuitTexasHoldem.Suit

case class CardTexasHoldem(rank: RankTexasHoldem, suit: Suit) extends Ordered[CardTexasHoldem] /** Playing card */
{
  override def compare(that: CardTexasHoldem): Int = this.rank compare that.rank /** Compares `this` with `that` ranks. */
  override lazy val toString: String = s"$rank$suit" /** String representation of this card. */
}
