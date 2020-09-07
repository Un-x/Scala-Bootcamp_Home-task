package com.TexasHoldem

import com.TexasHoldem
import com.TexasHoldem.RankTexasHoldem._
import scala.annotation.tailrec
import scala.util.Random
import org.junit.Assert._
import org.junit.Test

class CombinationTest{
  @Test
  def testCompare(): Unit ={
    val handsDescending = List(
      RoyalFlush, TexasHoldem.StraightFlush(5),
      FourOfAKind(A, K), FourOfAKind(A, 2), FourOfAKind(A), FourOfAKind(K, A), FourOfAKind(2),
      TexasHoldem.FullHouse(A, K), TexasHoldem.FullHouse(A, 2), TexasHoldem.FullHouse(K, A), TexasHoldem.FullHouse(2, 3),
      TexasHoldem.Flush(A, K, Q, J, 9), TexasHoldem.Flush(A, K, Q, J, 2), TexasHoldem.Flush(A, K, Q, T, 9), TexasHoldem.Flush(A, K, Q, 3, 2),
      TexasHoldem.Flush(A, K, J, T, 9), TexasHoldem.Flush(A, K, 4, 3, 2), TexasHoldem.Flush(A, Q, J, T, 9), TexasHoldem.Flush(A, 6, 4, 3, 2),
      TexasHoldem.Flush(K, Q, J, T, 8), TexasHoldem.Flush(7, 5, 4, 3, 2),
      TexasHoldem.Straight(A), TexasHoldem.Straight(5),
      ThreeOfAKind(A, K, Q), ThreeOfAKind(A, K, 2), ThreeOfAKind(A, K), ThreeOfAKind(A, Q, J),
      ThreeOfAKind(A, 3, 2), ThreeOfAKind(A, 3), ThreeOfAKind(A, 2), ThreeOfAKind(A), ThreeOfAKind(K, Q, J),
      ThreeOfAKind(2),
      TwoPair(A, K, Q), TwoPair(A, K, 2), TwoPair(A, K), TwoPair(A, Q, J), TwoPair(A, 2, 3),
      TwoPair(A, 2), TwoPair(K, Q, A), TwoPair(3, 2),
      Pair(A, K, Q, J), Pair(A, K, Q, 2), Pair(A, K, Q), Pair(A, K, J, T), Pair(A, K, 2), Pair(A, K),
      Pair(A, Q, J, T), Pair(A, 2), Pair(A), Pair(K, Q, J, T), Pair(2),
      HighCard(A, K, Q, J, 9), HighCard(A, K, Q, J, 2), HighCard(A, K, Q, T, 9),
      HighCard(A, K, Q, 3, 2), HighCard(A, K, J, T, 9), HighCard(A, K, 4, 3, 2),
      HighCard(A, Q, J, T, 9), HighCard(A, 6, 4, 3, 2), HighCard(K, Q, J, T, 8),
      HighCard(7, 5, 4, 3, 2)
    )

    @tailrec
    def test(handsDescending: List[CombinationTexasHoldem]): Unit ={
      val first = handsDescending.head
      assertEquals(0, first compare first)
      if (handsDescending.size >= 2){
        val second = handsDescending(1)
        val firstCompareSecond = first compare second
        assertTrue(
          s"Expected positive integer when comparing $first with $second, but found $firstCompareSecond",
          firstCompareSecond > 0
        )
        val secondCompareFirst = second compare first
        assertTrue(
          s"Expected negative integer when comparing $second with $first, but found $secondCompareFirst",
          secondCompareFirst < 0
        )
        test(handsDescending.tail)
      }
    }

    test(handsDescending)
  }

  @Test
  def testApply(): Unit ={
    def compare(expected: CombinationTexasHoldem, cards: CardTexasHoldem*): Unit = {
      assertEquals(expected, TexasHoldem.Combination(HandTexasHoldem(Random.shuffle(cards).toSet)))
    }

    compare(TexasHoldem.StraightFlush(K), K♥, Q♥, J♥, T♥, 9♥, A♣, A♠)
    compare(TexasHoldem.StraightFlush(K), K♥, Q♥, J♥, T♥, 9♥)
    compare(TexasHoldem.StraightFlush(5), 5♥, 4♥, 3♥, 2♥, A♥, K♥, 6♠)
    compare(TexasHoldem.StraightFlush(5), 5♥, 4♥, 3♥, 2♥, A♥)
    compare(FourOfAKind(K, A), K♠, K♥, K♦, K♣, A♠, Q♥, Q♦)
    compare(FourOfAKind(K, A), K♠, K♥, K♦, K♣, A♠)
    compare(FourOfAKind(K), K♠, K♥, K♦, K♣)
    compare(TexasHoldem.FullHouse(A, K), A♠, A♥, A♦, K♠, K♥, K♦, Q♠)
    compare(TexasHoldem.FullHouse(A, K), A♠, A♥, A♦, K♠, K♥)
    compare(TexasHoldem.FullHouse(K, A), K♠, K♥, K♦, A♠, A♥, Q♦, Q♠)
    compare(TexasHoldem.FullHouse(K, A), K♠, K♥, K♦, A♠, A♥)
    compare(TexasHoldem.Flush(K, Q, J, T, 8), K♥, Q♥, J♥, T♥, 8♥, A♠, A♦)
    compare(TexasHoldem.Flush(K, Q, J, T, 8), K♥, Q♥, J♥, T♥, 8♥)
    compare(TexasHoldem.Straight(Q), Q♥, J♥, T♥, 9♥, 8♦, A♦, A♠)
    compare(TexasHoldem.Straight(Q), Q♥, J♥, T♥, 9♥, 8♦)
    compare(TexasHoldem.Straight(5), 5♥, 4♥, 3♥, 2♥, A♠, A♦, A♣)
    compare(TexasHoldem.Straight(5), 5♥, 4♥, 3♥, 2♥, A♠)
    compare(ThreeOfAKind(K, A, Q), K♠, K♥, K♦, A♠, Q♠, J♠, 9♣)
    compare(ThreeOfAKind(K, A, Q), K♠, K♥, K♦, A♠, Q♠)
    compare(ThreeOfAKind(K, A), K♠, K♥, K♦, A♠)
    compare(ThreeOfAKind(K), K♠, K♥, K♦)
    compare(TwoPair(K, Q, A), K♠, K♥, Q♠, Q♥, A♠, J♠, J♣)
    compare(TwoPair(K, Q, A), K♠, K♥, Q♠, Q♥, A♠)
    compare(TwoPair(K, Q), K♠, K♥, Q♠, Q♥)
    compare(Pair(2, A, K, Q), 2♠, 2♥, A♠, K♠, Q♠, J♥, 9♥)
    compare(Pair(2, A, K, Q), 2♠, 2♥, A♠, K♠, Q♠)
    compare(Pair(2, A, K), 2♠, 2♥, A♠, K♠)
    compare(Pair(2, A), 2♠, 2♥, A♠)
    compare(Pair(2), 2♠, 2♥)
    compare(HighCard(A, K, Q, J, 9), A♠, K♠, Q♠, J♠, 9♥, 8♥, 7♥)
    compare(HighCard(A, K, Q, J, 9), A♠, K♠, Q♠, J♠, 9♥)
    compare(HighCard(A, K, Q, J), A♠, K♠, Q♠, J♠)
    compare(HighCard(A, K, Q), A♠, K♠, Q♠)
    compare(HighCard(A, K), A♠, K♠)
    compare(HighCard(A), A♠)
  }

  @Test
  def testToString(): Unit ={
    def compare(actual: CombinationTexasHoldem, expected: String) ={
      assertEquals(expected, actual.toString)
    }

    compare(RoyalFlush, "Royal flush")
    compare(TexasHoldem.StraightFlush(K), "King-high straight flush")
    compare(FourOfAKind(A), "Quad Aces")
    compare(FourOfAKind(A, K), "Quad Aces with King kicker")
    compare(TexasHoldem.FullHouse(A, K), "Aces full of Kings")
    compare(TexasHoldem.Flush(A, K, Q, J, 9), "Flush (A K Q J 9)")
    compare(TexasHoldem.Straight(A), "Ace-high straight")
    compare(ThreeOfAKind(A), "Three Aces")
    compare(ThreeOfAKind(A, K), "Three Aces with King kicker")
    compare(ThreeOfAKind(A, K, Q), "Three Aces with King and Queen kickers")
    compare(TwoPair(A, K), "Aces and Kings")
    compare(TwoPair(A, K, Q), "Aces and Kings with Queen kicker")
    compare(Pair(A), "A pair of Aces")
    compare(Pair(A, K), "A pair of Aces with King kicker")
    compare(Pair(A, K, Q), "A pair of Aces with King and Queen kickers")
    compare(Pair(A, K, Q, J), "A pair of Aces with King, Queen and Jack kickers")
    compare(HighCard(A), "Ace-high")
    compare(HighCard(A, K), "Ace-high with King kicker")
    compare(HighCard(A, K, Q), "Ace-high with King and Queen kickers")
    compare(HighCard(A, K, Q, J), "Ace-high with King, Queen and Jack kickers")
    compare(HighCard(A, K, Q, J, 9), "Ace-high with King, Queen, Jack and Nine kickers")
  }
}