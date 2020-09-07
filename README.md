# Scala Bootcamp_Home task
Algorithm for comparing the strength of Texas Hold'em Hands.
## Installation
* [Java](https://java.com/en/download/)
* [Scala](https://downloads.lightbend.com/scala/2.13.3/scala-2.13.3.msi)
* [Sbt](https://piccolo.link/sbt-1.3.13.msi)
## Algorithm
### Suits
Four suits are named after the corresponding well-known unicode characters with aliases coinciding with their names:
```scala
val suits: List[Suit] = List(♠, ♥, ♦, ♣)
println(List(♠, ♥, ♦, ♣) == List(Spades, Hearts, Diamonds, Clubs))
```
### Ranks
Constants for all 13 ranks with aliases coinciding with their names:
```scala
val ranks: List[Rank] = List(A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, 2)

require(List[Rank](A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, 2)
	== List(Ace, King, Queen, Jack, Ten, Nine, Eight, Seven, Six, Five, Four, Three, Two))
```
### Cards
Card creation:
```scala
val aceSpades: Card = A♠
```
Card aliase of ranks and suits:
```scala
println((Ace spades) == (A♠))
```
### Hands
Shortest hand contains 1 card:
```scala
val shortestHand: Hand = Hand(A♠)
```
Longest one consists of 7 cards:
```scala
val longestHand = Hand(K♥, Q♥, J♥, T♥, 9♥, A♣, A♠)
```
Hands can be compared with each other:
```scala
println(Hand(A♠, A♥, A♦, A♣, K♠, K♥, K♦) < Hand(A♥, K♥, Q♥, J♥, T♥))
```
Rank of a hand is determined by the highest 5-card combination it contains:
```scala
val combination: Combination = Hand(A ♠, A ♥, A ♦, A ♣, K ♠, K ♥, K ♦).toCombination
println(combination)
println(Hand(A♥, K♥, Q♥, J♥, T♥).toCombination)
```
### Combination
Longer combination beats the shorter one:
```scala
println(Hand(A♠, A♥, A♦, K♣, Q♠, J♥) > Hand(A♠, A♥, A♦, K♣))
println(Hand(A♠, A♥, A♦, Q♠, J♥) < Hand(A♠, A♥, A♦, K♣))
```
Combination cannot contain more than 5 cards, so the following hands are considered equivalent:
```scala
println(Hand(A♠, A♥, A♦, K♣, Q♠, J♥).toCombination == Hand(A♠, A♥, A♦, K♣, Q♠).toCombination)
println(!(
	Hand(A♠, A♥, A♦, K♣, Q♠, J♥) > Hand(A♠, A♥, A♦, K♣, Q♠)
	|| Hand(A♠, A♥, A♦, K♣, Q♠, J♥) < Hand(A♠, A♥, A♦, K♣, Q♠)
```
Pattern matching:
```scala
Hand(2♠, 2♥, A♠, K♠, Q♠, J♥, 9♥) match {
	case Pair(`2`, highestKicker :: _) =>
		println(s"A pair of Twos with ${highestKicker.name} kicker and so on")
}
```
