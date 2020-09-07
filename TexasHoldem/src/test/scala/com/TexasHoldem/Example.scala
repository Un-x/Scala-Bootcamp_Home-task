import com.TexasHoldem.{
  CardTexasHoldem,
  CombinationTexasHoldem,
  HandTexasHoldem,
  RankTexasHoldem
}

object Example extends App{
  import com.TexasHoldem.SuitTexasHoldem._
  import com.TexasHoldem.RankTexasHoldem._
  val suits: List[Suit] = List(♠, ♥, ♦, ♣)
  require((suits mkString " ") == "♠ ♥ ♦ ♣")
  require(List(♠, ♥, ♦, ♣) == List(Spades, Hearts, Diamonds, Clubs))
  val ranks: List[RankTexasHoldem] = List(A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, 2)
  require((ranks mkString " ") == "A K Q J T 9 8 7 6 5 4 3 2")
  require(A > K)
  val unorderedRanks = scala.util.Random.shuffle(ranks)
  println(unorderedRanks mkString " ")
  require((unorderedRanks.sorted mkString " ") == "2 3 4 5 6 7 8 9 T J Q K A")
  require(List[RankTexasHoldem](A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, 2)
    == List(Ace, King, Queen, Jack, Ten, Nine, Eight, Seven, Six, Five, Four, Three, Two))
  val aceSpades: CardTexasHoldem = A♠
  require(aceSpades.rank == A)
  require(aceSpades.suit == ♠)
  require(aceSpades.toString == "A♠")
  require((Ace spades) == (A♠))
  require((K♦) < (A♠))
  val shortestHand: HandTexasHoldem = HandTexasHoldem(A♠)
  val longestHand = HandTexasHoldem(K♥, Q♥, J♥, T♥, 9♥, A♣, A♠)
  require(HandTexasHoldem(A♠, A♥, A♦, A♣, K♠, K♥, K♦) < HandTexasHoldem(A♥, K♥, Q♥, J♥, T♥))
  val combination: CombinationTexasHoldem = HandTexasHoldem(A ♠, A ♥, A ♦, A ♣, K ♠, K ♥, K ♦).toCombination
  require(combination.toString == "Quad Aces with King kicker")
  require(HandTexasHoldem(A♥, K♥, Q♥, J♥, T♥).toCombination.toString == "Royal flush")
  require(HandTexasHoldem(A♠, A♥, A♦, K♣, Q♠, J♥) > HandTexasHoldem(A♠, A♥, A♦, K♣))
  require(HandTexasHoldem(A♠, A♥, A♦, Q♠, J♥) < HandTexasHoldem(A♠, A♥, A♦, K♣))
  require(HandTexasHoldem(A♠, A♥, A♦, K♣, Q♠, J♥).toCombination == HandTexasHoldem(A♠, A♥, A♦, K♣, Q♠).toCombination)
  require(!(
    HandTexasHoldem(A♠, A♥, A♦, K♣, Q♠, J♥) > HandTexasHoldem(A♠, A♥, A♦, K♣, Q♠)
      || HandTexasHoldem(A♠, A♥, A♦, K♣, Q♠, J♥) < HandTexasHoldem(A♠, A♥, A♦, K♣, Q♠)
    ))
  HandTexasHoldem(2♠, 2♥, A♠, K♠, Q♠, J♥, 9♥) match {
    case Pair(`2`, highestKicker :: _) =>
      require(s"A pair of Twos with ${highestKicker.name} kicker and so on" == "A pair of Twos with Ace kicker and so on")
  }
}