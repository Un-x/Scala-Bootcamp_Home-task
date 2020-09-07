package com.TexasHoldem
import com.TexasHoldem
import com.TexasHoldem.RankTexasHoldem._
import SuitTexasHoldem._
import org.junit.Assert._
import org.junit.Test

class CardTest{
  @Test
  def testConstruction(): Unit ={
    assertEquals(TexasHoldem.CardTexasHoldem(A, Spades), A ♠)
    assertEquals(TexasHoldem.CardTexasHoldem(A, Spades), A spades)
  }
  @Test
  def testToString(): Unit ={
    assertEquals("A♠", (A spades).toString)
  }
}