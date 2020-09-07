package com.TexasHoldem
import com.TexasHoldem

/** Card rank.Ascending order of ranks: 2, 3, 4, 5, 6, 7, 8, 9, T, J, Q, K, A. */
sealed case class RankTexasHoldem private(symbol: Char, name: String, private val priority: Int) extends Ordered[RankTexasHoldem]{
  import RankTexasHoldem._
  PriorityToRank(priority) = this
  /** Plural of `this.name` ("Twos", "Threes", etc.) */
  val namePlural: String = name + 's'
  override def compare(that: RankTexasHoldem): Int = this.priority compare that.priority
  override val toString: String = "" + symbol
  lazy val ♠ = CardTexasHoldem(this : RankTexasHoldem, SuitTexasHoldem.♠)/** Card which has this rank and suit of spades. */
  lazy val ♥ = TexasHoldem.CardTexasHoldem(this : RankTexasHoldem, SuitTexasHoldem.♥)/** Card which has this rank and suit of hearts. */
  lazy val ♦ = TexasHoldem.CardTexasHoldem(this : RankTexasHoldem, SuitTexasHoldem.♦)/** Card which has this rank and suit of diamonds. */
  lazy val ♣ = TexasHoldem.CardTexasHoldem(this : RankTexasHoldem, SuitTexasHoldem.♣)/** Card which has this rank and suit of clubs. */
  lazy val spades = TexasHoldem.CardTexasHoldem(this : RankTexasHoldem, SuitTexasHoldem.Spades)/** Card which has this rank and suit of spades. */
  lazy val hearts = TexasHoldem.CardTexasHoldem(this : RankTexasHoldem, SuitTexasHoldem.Hearts)/** Card which has this rank and suit of hearts. */
  lazy val diamonds = TexasHoldem.CardTexasHoldem(this : RankTexasHoldem, SuitTexasHoldem.Diamonds)/** Card which has this rank and suit of diamonds. */
  lazy val clubs = TexasHoldem.CardTexasHoldem(this : RankTexasHoldem, SuitTexasHoldem.Clubs)/** Card which has this rank and suit of clubs. */
  /** Returns list of ranks from this rank (inclusive) to the specified rank (inclusive), in ascending order. */
  def to(upper: RankTexasHoldem): List[RankTexasHoldem] = {
    require(upper >= this, s"$upper is less than $this")
    (this.priority to upper.priority).toList map PriorityToRank.apply
  }
  /** Returns list of ranks from this rank (inclusive) to the specified rank (inclusive), in descending order. */
  def downTo(lower: RankTexasHoldem): List[RankTexasHoldem] = {
    require(this >= lower, s"$lower is greater that $this")
    (this.priority to lower.priority by -1).toList map PriorityToRank.apply
  }
  /** Returns difference of this rank and `that`. */
  def -(that: RankTexasHoldem): Int = this.priority - that.priority
}
object RankTexasHoldem {
  private val PriorityToRank: scala.collection.mutable.Map[Int, RankTexasHoldem] = scala.collection.mutable.HashMap()
  val `2` = RankTexasHoldem('2', "Two", 2)
  val Two: RankTexasHoldem = `2`
  val `3` = RankTexasHoldem('3', "Three", 3)
  val Three: RankTexasHoldem = `3`
  val `4` = RankTexasHoldem('4', "Four", 4)
  val Four: RankTexasHoldem = `4`
  val `5` = RankTexasHoldem('5', "Five", 5)
  val Five: RankTexasHoldem = `5`
  val `6` = new RankTexasHoldem('6', "Six", 6)
  {
    override val namePlural: String = "Sixes"
  }
  val Six: RankTexasHoldem = `6`
  val `7` = RankTexasHoldem('7', "Seven", 7)
  val Seven: RankTexasHoldem = `7`
  val `8` = RankTexasHoldem('8', "Eight", 8)
  val Eight: RankTexasHoldem = `8`
  val `9` = RankTexasHoldem('9', "Nine", 9)
  val Nine: RankTexasHoldem = `9`
  val T = RankTexasHoldem('T', "Ten", 10)
  val J = RankTexasHoldem('J', "Jack", 11)
  val Q = RankTexasHoldem('Q', "Queen", 12)
  val K = RankTexasHoldem('K', "King", 13)
  val A = RankTexasHoldem('A', "Ace", 14)
  val Ten: RankTexasHoldem = T
  val Jack: RankTexasHoldem = J
  val Queen: RankTexasHoldem = Q
  val King: RankTexasHoldem = K
  val Ace: RankTexasHoldem = A
  /** All ranks from 2 to A */
  val RanksInAscendingOrder: List[RankTexasHoldem] = PriorityToRank.toList sortBy {_._1} map {_._2}
  /** All ranks from A to 2 */
  val RanksInDescendingOrder: List[RankTexasHoldem] = RanksInAscendingOrder.reverse
  /** Converts an integer from `2` to `9` (both inclusive) to the corresponding rank. */
  implicit def numberToRank(number: Int): RankTexasHoldem = {
    require((2 to 9) contains number, s"Invalid rank: $number")
    PriorityToRank(number)
  }
}