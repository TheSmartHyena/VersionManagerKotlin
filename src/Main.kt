fun main() {
    val b = VersionManager("3.2.3")

    b.major().minor().patch().patch().update(UpdateType.MINOR)
    println(b.release())

    b.rollback().rollback().rollback().rollback()

    println(b.release())
}