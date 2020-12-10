import kotlin.test.Test

class Test {
  private fun part1(day: AOCDay) {
    println(day.part1())
  }

  private fun part2(day: AOCDay) {
    println(day.part2())
  }

  @Test fun `Day 1 Part 1`() = part1(AOCDay1)
  @Test fun `Day 1 Part 2`() = part2(AOCDay1)

  @Test fun `Day 2 Part 1`() = part1(AOCDay2)
  @Test fun `Day 2 Part 2`() = part2(AOCDay2)

  @Test fun `Day 3 Part 1`() = part1(AOCDay3)
  @Test fun `Day 3 Part 2`() = part2(AOCDay3)

  @Test fun `Day 4 Part 1`() = part1(AOCDay4)
  @Test fun `Day 4 Part 2`() = part2(AOCDay4)

  @Test fun `Day 5 Part 1`() = part1(AOCDay5)
  @Test fun `Day 5 Part 2`() = part2(AOCDay5)

  @Test fun `Day 6 Part 1`() = part1(AOCDay6)
  @Test fun `Day 6 Part 2`() = part2(AOCDay6)

  @Test fun `Day 7 Part 1`() = part1(AOCDay7)
  @Test fun `Day 7 Part 2`() = part2(AOCDay7)

  @Test fun `Day 8 Part 1`() = part1(AOCDay8)
  @Test fun `Day 8 Part 2`() = part2(AOCDay8)

  @Test fun `Day 9 Part 1`() = part1(AOCDay9)
  @Test fun `Day 9 Part 2`() = part2(AOCDay9)
}

