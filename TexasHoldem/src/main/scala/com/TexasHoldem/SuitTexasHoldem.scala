package com.TexasHoldem

object SuitTexasHoldem extends Enumeration{ /** Defines a finite set of Suit values specific to the enumeration */
  type Suit = Value
  val ♠ = Value("♠") /** Card suit spades (♠) */
  val ♥ = Value("♥") /** Card suit hearts (♥) */
  val ♦ = Value("♦") /** Card suit diamonds (♦) */
  val ♣ = Value("♣") /** Card suit clubs (♣) */
  val Spades: Suit = ♠ /** Card suit spades (♠) */
  val Hearts: Suit = ♥ /** Card suit hearts (♥) */
  val Diamonds: Suit = ♦ /** Card suit diamonds (♦) */
  val Clubs: Suit = ♣ /** Card suit clubs (♣) */
}
