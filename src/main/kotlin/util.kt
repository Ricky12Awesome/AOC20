operator fun IntRange.component1(): Int {
  return start
}

operator fun IntRange.component2(): Int {
  return last
}