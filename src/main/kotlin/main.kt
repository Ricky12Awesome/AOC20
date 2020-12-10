import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import java.lang.StringBuilder
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.bufferedReader
import kotlin.math.max
import kotlin.streams.asSequence

class Arguments(parser: ArgParser) {
  val input by parser.storing("Input File").default("")
  val day by parser.storing("Day")
  val part by parser.storing("Part")
}

fun main(args: Array<String>) {
  ArgParser(args).parseInto(::Arguments).run {
    Input.input = input.takeIf(String::isNotEmpty)?.let(Paths::get)

    val instance = Class.forName("AOCDay$day").kotlin.objectInstance as AOCDay
    val value = when (part) {
      "1" -> instance.part1()
      "2" -> instance.part2()
      else -> error("Invalid Part")
    }

    println(value)
  }
}

class Input(private val day: Int) {
  fun reader() = input?.bufferedReader() ?: javaClass.getResourceAsStream("/AOCDay$day").bufferedReader()
  fun lines() = reader().lines().asSequence()
  fun text() = reader().readText()

  companion object {
    var input: Path? = null
  }
}

sealed class AOCDay(day: Int) {
  protected val _input = Input(day)

  abstract fun part1(): Any
  abstract fun part2(): Any
}

object AOCDay1 : AOCDay(1) {
  private const val match = 2020

  private val input = _input
    .lines()
    .map(String::toInt)
    .toList()

  override fun part1(): Int {
    input.forEach { a ->
      input.forEach { b ->
        if (a + b == match) {
          return a * b
        }
      }
    }

    error("Didn't find anything to sum to $match")
  }

  override fun part2(): Int {
    input.forEach { a ->
      input.forEach { b ->
        input.forEach { c ->
          if (a + b + c == match) {
            return a * b * c
          }
        }
      }
    }

    error("Didn't find anything to sum to $match")
  }
}

object AOCDay2 : AOCDay(2) {
  data class Data(val range: IntRange, val letter: Char, val password: String)

  private val input = _input
    .lines()
    .map { it.split(" ") }
    .map { parts ->
      Data(
        range = parts[0].split("-").map(String::toInt).let { it[0]..it[1] },
        letter = parts[1][0],
        password = parts[2]
      )
    }

  override fun part1(): Int {
    return input.count { data ->
      data.password.count { it == data.letter } in data.range
    }
  }

  override fun part2(): Int {
    return input.count { data ->
      val (index1, index2) = data.range
      val elements = listOf(data.password[index1 - 1], data.password[index2 - 1])

      elements.count { it == data.letter } == 1
    }
  }
}

object AOCDay3 : AOCDay(3) {
  private const val tree = '#'

  private val input = _input
    .lines()
    .map(String::toList)
    .toList()

  private fun count(right: Int, down: Int): Long {
    tailrec fun recurse(startX: Int, startY: Int, count: Long): Long {
      val (y, x) = startY + down to startX + right
      val row = input.getOrNull(y) ?: return count
      val element = row[x % row.size]

      return recurse(x, y, count + if (element == tree) 1 else 0)
    }

    return recurse(0, 0, if (input[0][0] == tree) 1 else 0)
  }

  override fun part1(): Long {
    return count(3, 1)
  }

  override fun part2(): Long {
    return count(1, 1) *
        count(3, 1) *
        count(5, 1) *
        count(7, 1) *
        count(1, 2)
  }
}

object AOCDay4 : AOCDay(4) {
  private val required = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

  data class Passport(val data: Map<String, String>) {
    fun validateKeys() = data.keys.containsAll(required)
    fun validate() = if (!validateKeys()) false else data.all { validate(it.key, it.value) }
    fun validate(key: String, value: String): Boolean {
      return when (key) {
        "byr" -> value.toIntOrNull() in 1920..2002
        "iyr" -> value.toIntOrNull() in 2010..2020
        "eyr" -> value.toIntOrNull() in 2020..2030
        "hgt" -> when {
          value.endsWith("cm") -> value.dropLast(2).toIntOrNull() in 150..193
          value.endsWith("in") -> value.dropLast(2).toIntOrNull() in 59..76
          else -> false
        }
        "hcl" -> value.matches(Regex("^#[0-9a-f]{6}$"))
        "ecl" -> value in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
        "pid" -> value.length == 9 && value.all(Char::isDigit)
        "cid" -> true
        else -> false
      }
    }
  }

  private val input = _input
    .lines()
    .fold(StringBuilder()) { acc, s -> if (s.isEmpty()) acc.append("\n") else acc.append("$s ") }
    .splitToSequence("\n")
    .map { it.split(" ").dropLast(1) }
    .map { it.map { it.split(":") }.map { it[0] to it[1] }.toTypedArray() }
    .map(::mapOf)
    .map(::Passport)

  override fun part1(): Int {
    return input.count(Passport::validateKeys)
  }

  override fun part2(): Int {
    return input.count(Passport::validate)
  }
}

object AOCDay5 : AOCDay(5) {
  const val front = 'F'
  const val back = 'B'
  const val left = 'L'
  const val right = 'R'
  const val lower = "$front$left"
  const val upper = "$back$right"

  private fun convert(input: String): Int {
    return input.map {
      when (it) {
        in lower -> '0'
        in upper -> '1'
        else -> error("Invalid Input")
      }
    }.joinToString("").toInt(2)
  }

  private val input = _input
    .lines()
    .map { it.filter { it in listOf(front, back) } to it.filter { it in listOf(left, right) } }
    .map { (row, column) -> convert(row) to convert(column) }
    .map { (row, column) -> row * 8 + column }

  override fun part1(): Int {
    return input.maxOrNull()!!
  }

  override fun part2(): Int {
    return input.sorted().windowed(2).first { it[0] != it[1] - 1 }.first() + 1
  }
}

object AOCDay6 : AOCDay(6) {
  private val input = _input
    .lines()
    .fold(StringBuilder()) { acc, s -> if (s.isEmpty()) acc.append("\n") else acc.append("$s ") }
    .splitToSequence("\n")
    .map { it.split(" ").dropLast(1) }

  override fun part1(): Int {
    return input.map { it.joinToString("").toSet() }.sumBy { it.count() }
  }

  override fun part2(): Any {
    return input
      .map { group ->
        val map = mutableMapOf<Char, Int>()

        group.forEach { person ->
          person.forEach { c ->
            map[c] = (map[c] ?: 0) + 1
          }
        }

        map.values.count { it == group.size }
      }
      .sum()
  }
}

object AOCDay7 : AOCDay(7) {
  private const val shiny = "shiny gold"

  private val input = _input
    .lines()
    .map { it.replace(Regex("\\s?bag(s?)"), "") }
    .map { it.split(" contain ") }
    .map { (bag, bags) ->
      bag to bags
        .splitToSequence(", ")
        .map { it.removeSuffix(".") }
        .map { it.split(" ", limit = 2) }
        .map { (amount, name) -> (amount.toIntOrNull() ?: 0) to name }
    }
    .map { (bag, bags) -> bag to (bags.toList().takeUnless { it.contains(0 to "other") } ?: listOf()) }
    .associate { (bag, bags) -> bag to bags }

  override fun part1(): Any {
    val hasGoldenBag = mutableMapOf<String, Boolean>()

    fun containsShiny(bags: List<String>) = shiny in bags || bags.any { hasGoldenBag[it] ?: false }
    fun setBagsWithGoldenBags(bag: String, bags: List<String>) {
      bags.forEach { name ->
        input[name]?.let {
          val it = it.map { it.second }

          setBagsWithGoldenBags(name, it)
          hasGoldenBag[name] = containsShiny(it)
        }
      }

      hasGoldenBag[bag] = containsShiny(bags)
    }

    input.forEach { (bag, bags) ->
      setBagsWithGoldenBags(bag, bags.map { it.second })
    }

    return hasGoldenBag.values.count { it }
  }

  override fun part2(): Any {
    fun sum(bags: List<Pair<Int, String>>): Int = bags.fold(1) { acc, (amount, name) ->
      acc + (input[name]!!.let(::sum) * amount)
    }

    return input[shiny]!!.let(::sum) - 1
  }
}

object AOCDay8 : AOCDay(8) {
  override fun part1(): Any {
    TODO()
  }

  override fun part2(): Any {
    TODO()
  }
}

object AOCDay9 : AOCDay(9) {
  override fun part1(): Any {
    TODO()
  }

  override fun part2(): Any {
    TODO()
  }
}


