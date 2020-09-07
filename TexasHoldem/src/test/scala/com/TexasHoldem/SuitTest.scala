package com.TexasHoldem
import SuitTexasHoldem._

class SuitTest{
  @Test
  def testToString(): Unit ={
    assertEquals("♠", `♠`.toString)
    assertEquals("♠", Spades.toString)
  }
  @Test
  def testAlias(): Unit ={
    assertEquals(`♠`, Spades)
  }
}