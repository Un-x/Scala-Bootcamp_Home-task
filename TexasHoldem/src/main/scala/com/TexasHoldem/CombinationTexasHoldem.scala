package com.TexasHoldem
import com.TexasHoldem
import com.TexasHoldem.Combination._
import com.TexasHoldem.RankTexasHoldem._
import scala.annotation.tailrec
import scala.collection.mutable

/** Contains only data relevant for comparison of hands.*/
sealed abstract class CombinationTexasHoldem(
                                   protected val kindRank: Int,
                                   protected val mainRanks: List[RankTexasHoldem],
                                   protected val kickerRanks: List[RankTexasHoldem] = Nil
                                 )
  extends Ordered[CombinationTexasHoldem]{
  requireDecreasing(kickerRanks)
  protected val ranksOrdered: List[RankTexasHoldem] = mainRanks ++ kickerRanks
  requireDistinct(ranksOrdered)

  /** Compares this hand with `that`. */
  override def compare(that: CombinationTexasHoldem): Int = {
    val kindResult = this.kindRank compare that.kindRank
    if (kindResult != 0) kindResult
    else {
      @tailrec
      def compare(ranks1: List[RankTexasHoldem], ranks2: List[RankTexasHoldem]): Int = {
        if(ranks1.isEmpty || ranks2.isEmpty) ranks1.size compare ranks2.size
        else {
          val headResult = ranks1.head compare ranks2.head
          if(headResult == 0) compare(ranks1.tail, ranks2.tail)
          else headResult
        }
      }
      compare(this.ranksOrdered, that.ranksOrdered)
    }
  }
  protected def nameMainPart: String
  override lazy val toString: String = nameMainPart + (if (kickerRanks.isEmpty) "" else {
    " with " + (if (kickerRanks.size == 1) s"${kickerRanks.head.name} kicker" else {
      s"${((kickerRanks take (kickerRanks.size - 1)) map {_.name}) mkString ", "} and ${kickerRanks.last.name} kickers"
    })
  })
}
object Combination{
  /** Returns Combination corresponding to the given Hand. */
  def apply(hand: HandTexasHoldem): CombinationTexasHoldem = {
    case class RankGroup(size: Int, rank: RankTexasHoldem) extends Ordered[RankGroup] {
      override def compare(that: RankGroup): Int = {
        val sizeResult = this.size compare that.size
        if (sizeResult == 0) this.rank compare that.rank
        else sizeResult
      }
    }
    val byRank: List[RankGroup] = {
      (hand.cards groupBy {_.rank} map {case (rank, cards) => RankGroup(cards.size, rank)}).toList.sorted.reverse
    }
    if (byRank.head.size == 4) {
      FourOfAKind(byRank.head.rank, (byRank.tail map {_.rank}).sorted.lastOption)
    }
    else flushLike(hand.cards) match {
      case Some(ranks) => straightLike(ranks) match {
        case Some(orderedRanks) => TexasHoldem.StraightFlush(orderedRanks.head)
        case None =>
          val flushRanks = ranks.toList.sorted.reverse
          //noinspection ZeroIndexToHead
          TexasHoldem.Flush(flushRanks(0), flushRanks(1), flushRanks(2), flushRanks(3), flushRanks(4))
      }
      case None =>
        byRank match {
          case RankGroup(3, threeRank) :: RankGroup(x, pairRank) :: _ if x >= 2 => TexasHoldem.FullHouse(threeRank, pairRank)
          case _ =>
            straightLike(hand.cards map {_.rank}) match {
              case Some(orderedRanks) => TexasHoldem.Straight(orderedRanks.head)
              case None => byRank match {
                case RankGroup(3, rank) :: rest => TexasHoldem.ThreeOfAKind(rank, rest take 2 map {_.rank})
                case RankGroup(2, highPairRank) :: RankGroup(2, lowPairRank) :: rest =>
                  TexasHoldem.TwoPair(highPairRank, lowPairRank, (rest map {_.rank}).sorted.lastOption)
                case RankGroup(2, rank) :: rest => TexasHoldem.Pair(rank, rest take 3 map {_.rank})
                case _ => HighCard(byRank take 5 map {_.rank})
              }
            }
        }
    }
  }
  /** Throws IllegalArgumentException unless the given ranks are distinct and arranged in descending order.*/
  def requireDecreasing(ranks: List[RankTexasHoldem]): Unit ={
    if(ranks.size >= 2){
      for(i <- 0 to (ranks.size - 2)){
        val left = ranks(i)
        val right = ranks(i+1)
        require(left != right, "Duplicated rank: " + left)
        require(left > right, s"Wrong order of ranks: $right must be before $left")
      }
    }
  }
  /** Throws IllegalArgumentException unless the given ranks are distinct. */
  def requireDistinct(ranks: Iterable[RankTexasHoldem]): Unit ={
    val seen: mutable.Set[RankTexasHoldem] = new mutable.HashSet[RankTexasHoldem]()
    val duplicates: mutable.Set[RankTexasHoldem] = new mutable.HashSet[RankTexasHoldem]()
    for(rank <- ranks){
      if (seen contains rank){
        duplicates += rank
      }
      seen += rank
    }
    require(duplicates.isEmpty, s"Duplicated ranks: ${duplicates mkString ", "}")
  }
  /** Tests whether the given set contains at least 5 suited cards */
  def flushLike(cards: Set[CardTexasHoldem]): Option[Set[RankTexasHoldem]] = {
    if(cards.size < 5) None
    else {
      val flushLike: Iterable[RankTexasHoldem] = for {
        singleSuitCards <- (cards groupBy {_.suit}).values
        if singleSuitCards.size >= 5
        card <- singleSuitCards
      } yield card.rank
      if (flushLike.isEmpty) None else Some(flushLike.toSet)
    }
  }
  /** Tests whether the given set contains at least 5 ranks in a sequence or `5432A`.*/
  def straightLike(ranks: Set[RankTexasHoldem]): Option[List[RankTexasHoldem]] = {
    if(ranks.size < 5) None
    else {
      val sortedRanks = ranks.toList.sorted.reverse
      @tailrec
      def continuousStraightLike(ranks: List[RankTexasHoldem]): Option[List[RankTexasHoldem]] = {
        if(ranks.size < 5) None
        else if(ranks.head - ranks(4) == 4) Some(ranks.head downTo ranks(4))
        else continuousStraightLike(ranks.tail)
      }
      continuousStraightLike(sortedRanks) orElse {
        if(sortedRanks.head == A && sortedRanks(sortedRanks.size - 4) == `5`) Some((`5` downTo `2`) :+ A)
        else None
      }
    }
  }
}
/** Five cards in a sequence, all in the same suit. */
case class StraightFlush(rank: RankTexasHoldem) extends CombinationTexasHoldem(kindRank = 8, mainRanks = List(rank)) {
  require(rank >= 5, s"${rank.name}-high straight flush does not exist")
  /** If this straight flush is a royal flush. */
  lazy val royal: Boolean = rank == A
  override protected lazy val nameMainPart: String = if(royal) "Royal flush" else s"${rank.name}-high straight flush"
}
object StraightFlush{
  /** Extracts rank of straight flush */
  def unapply(combination: CombinationTexasHoldem): Option[RankTexasHoldem] = combination match {
    case straightFlush: StraightFlush => Some(straightFlush.rank)
    case _ => None
  }
  /** Extracts rank of straight flush */
  def unapply(hand: HandTexasHoldem): Option[RankTexasHoldem] = if(hand == null) None else unapply(hand.toCombination)
}
/** Royal flush, or ace-high straight flush. */
object RoyalFlush extends StraightFlush(A)
/** Four of a kind: a hand containing 4 cards of the same rank. */
case class FourOfAKind(rank: RankTexasHoldem, kickerRank: Option[RankTexasHoldem] = None)
  extends CombinationTexasHoldem(kindRank = 7, mainRanks = List(rank), kickerRanks = kickerRank.toList) {
  override protected lazy val nameMainPart: String = s"Quad ${rank.namePlural}"
}
object FourOfAKind{
  /** Creates four of a kind */
  def apply(rank: RankTexasHoldem, extraRank: RankTexasHoldem): FourOfAKind = FourOfAKind(rank, Some(extraRank))
  /** Extracts parameters of four of a kind */
  def unapply(combination: CombinationTexasHoldem): Option[(RankTexasHoldem, Option[RankTexasHoldem])] = combination match {
    case four: FourOfAKind => Some(four.rank, four.kickerRank)
    case _ => None
  }
  /** Extracts parameters of four of a kind */
  def unapply(hand: HandTexasHoldem): Option[(RankTexasHoldem, Option[RankTexasHoldem])] = if(hand == null) None else unapply(hand.toCombination)
}
/** Full house: a hand containing 3 cards of the same rank and 2 cards of another rank. */
case class FullHouse(threeRank: RankTexasHoldem, pairRank: RankTexasHoldem)
  extends CombinationTexasHoldem(kindRank = 6, mainRanks = List(threeRank, pairRank)){
  override protected lazy val nameMainPart: String = s"${threeRank.namePlural} full of ${pairRank.namePlural}"
}
object FullHouse{
  /** Extracts parameters of full house */
  def unapply(combination: CombinationTexasHoldem): Option[(RankTexasHoldem, RankTexasHoldem)] = combination match {
    case fh: FullHouse => Some(fh.threeRank, fh.pairRank)
    case _ => None
  }
  /** Extracts parameters of full house */
  def unapply(hand: HandTexasHoldem): Option[(RankTexasHoldem, RankTexasHoldem)] = if(hand == null) None else unapply(hand.toCombination)
}
/** Flush: a hand consisting of 5 cards of the same suit, but not in a sequence. */
case class Flush(rank1: RankTexasHoldem, rank2: RankTexasHoldem, rank3: RankTexasHoldem, rank4: RankTexasHoldem, rank5: RankTexasHoldem)
  extends CombinationTexasHoldem(kindRank = 5, mainRanks = List(rank1, rank2, rank3, rank4, rank5)){
  require(straightLike(mainRanks.toSet).isEmpty, "This is a straight flush")
  override protected lazy val nameMainPart: String = s"Flush (${mainRanks mkString " "})"
}
object Flush{
  /** Extracts parameters of flush */
  def unapply(combination: CombinationTexasHoldem): Option[(RankTexasHoldem, RankTexasHoldem, RankTexasHoldem, RankTexasHoldem, RankTexasHoldem)] = combination match {
    case f: Flush => Some(f.rank1, f.rank2, f.rank3, f.rank4, f.rank5)
    case _ => None
  }
  /** Extracts parameters of flush */
  def unapply(hand: HandTexasHoldem): Option[(RankTexasHoldem, RankTexasHoldem, RankTexasHoldem, RankTexasHoldem, RankTexasHoldem)] = {
    if(hand == null) None else unapply(hand.toCombination)
  }
}
/** Straight: a hand consisting of 5 cards in a sequence (or `5`, `4`, `3`, `2` and `A`), but not in the same suit. */
case class Straight(rank: RankTexasHoldem) extends CombinationTexasHoldem(kindRank = 4, mainRanks = List(rank)){
  require(rank >= `5`, s"${rank.name}-high straight does not exist")
  override protected lazy val nameMainPart: String = s"${rank.name}-high straight"
}
object Straight{
  /** Extracts parameters of straight */
  def unapply(combination: CombinationTexasHoldem): Option[RankTexasHoldem] = combination match {
    case s: Straight => Some(s.rank)
    case _ => None
  }
  /** Extracts parameters of straight */
  def unapply(hand: HandTexasHoldem): Option[RankTexasHoldem] = if(hand == null) None else unapply(hand.toCombination)
}
/** Three of a kind: a hand consisting of 3 cards of the same rank and at most 2 other unmatched cards */
case class ThreeOfAKind(rank: RankTexasHoldem, override val kickerRanks: List[RankTexasHoldem])
  extends CombinationTexasHoldem(kindRank = 3, mainRanks = List(rank), kickerRanks = kickerRanks){
  require(kickerRanks.size <= 2, s"A three of a kind cannot contain more than 2 kickers, but found ${kickerRanks.size}")
  override protected lazy val nameMainPart: String = s"Three ${rank.namePlural}"
}
object ThreeOfAKind{
  /** Creates three of a kind. */
  def apply(rank: RankTexasHoldem, kickerRanks: RankTexasHoldem*): ThreeOfAKind = ThreeOfAKind(rank, kickerRanks.toList)
  /** Extracts parameters of three of a kind */
  def unapply(combination: CombinationTexasHoldem): Option[(RankTexasHoldem, List[RankTexasHoldem])] = combination match {
    case t: ThreeOfAKind => Some(t.rank, t.kickerRanks)
    case _ => None
  }
  /** Extracts parameters of three of a kind */
  def unapply(hand: HandTexasHoldem): Option[(RankTexasHoldem, List[RankTexasHoldem])] = if(hand == null) None else unapply(hand.toCombination)
}
/** Two pair: a hand consisting of 2 cards of the same rank and another 2 cards with the same rank */
case class TwoPair(highPairRank: RankTexasHoldem, lowPairRank: RankTexasHoldem, kickerRank: Option[RankTexasHoldem] = None)
  extends CombinationTexasHoldem(kindRank = 2, mainRanks = List(highPairRank, lowPairRank), kickerRanks = kickerRank.toList){
  requireDecreasing(List(highPairRank, lowPairRank))
  override protected lazy val nameMainPart: String = s"${highPairRank.namePlural} and ${lowPairRank.namePlural}"
}
object TwoPair{
  /** Creates two pair. */
  def apply(highPairRank: RankTexasHoldem, lowPairRank: RankTexasHoldem, kickerRank: RankTexasHoldem): TwoPair = {
    TwoPair(highPairRank, lowPairRank, Some(kickerRank))
  }
  /** Extracts parameters of two pair */
  def unapply(combination: CombinationTexasHoldem): Option[(RankTexasHoldem, RankTexasHoldem, Option[RankTexasHoldem])] = combination match {
    case t: TwoPair => Some(t.highPairRank, t.lowPairRank, t.kickerRank)
    case _ => None
  }
  /** Extracts parameters of two pair */
  def unapply(hand: HandTexasHoldem): Option[(RankTexasHoldem, RankTexasHoldem, Option[RankTexasHoldem])] = if(hand == null) None else unapply(hand.toCombination)
}
/** One pair: a hand consisting of 2 cards of the same rank and up to 3 cards of other distinct ranks. */
case class Pair(rank: RankTexasHoldem, override val kickerRanks: List[RankTexasHoldem])
  extends CombinationTexasHoldem(kindRank = 1, mainRanks = List(rank), kickerRanks = kickerRanks){
  require(kickerRanks.size <= 3, s"A pair cannot contain more than 3 kickers, but found ${kickerRanks.size}")
  override protected lazy val nameMainPart: String = s"A pair of ${rank.namePlural}"
}
object Pair{
  /** Creates pair. */
  def apply(rank: RankTexasHoldem, kickerRanks: RankTexasHoldem*): Pair = Pair(rank, kickerRanks.toList)
  /** Extracts parameters of pair */
  def unapply(combination: CombinationTexasHoldem): Option[(RankTexasHoldem, List[RankTexasHoldem])] = combination match {
    case p: Pair => Some(p.rank, p.kickerRanks)
    case _ => None
  }
  /** Extracts parameters of pair */
  def unapply(hand: HandTexasHoldem): Option[(RankTexasHoldem, List[RankTexasHoldem])] = if(hand == null) None else unapply(hand.toCombination)
}
/** High card: a hand which contains none of those subsets:
 * 5 cards of the same suit, 5 cards in rank sequence, 2 or more cards of the same rank */
case class HighCard(ranks: List[RankTexasHoldem])
  extends CombinationTexasHoldem(kindRank = 0, mainRanks = ranks take 1, kickerRanks = ranks drop 1){
  require(
    (1 to 5) contains ranks.size,
    "A combination must contain at least 1 and at most 5 cards, but found" + ranks.size)
  require(straightLike(ranks.toSet).isEmpty, ranks.mkString(" ") + " is a straight")
  override protected lazy val nameMainPart: String = s"${ranks.head.name}-high"
}
object HighCard{
  /** Creates high card */
  def apply(ranks: RankTexasHoldem*): HighCard = HighCard(ranks.toList)
  /** Extracts parameters of high card */
  def unapply(combination: CombinationTexasHoldem): Option[List[RankTexasHoldem]] = combination match {
    case h: HighCard => Some(h.ranks)
    case _ => None
  }
  /** Extracts parameters of high card */
  def unapply(hand: HandTexasHoldem): Option[List[RankTexasHoldem]] = if(hand == null) None else unapply(hand.toCombination)
}