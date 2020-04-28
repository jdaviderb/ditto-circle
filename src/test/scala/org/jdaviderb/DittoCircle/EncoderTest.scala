package org.jdaviderb.DittoCircle
import io.circe.Json
import org.jdaviderb.dittoSerializer.core.Serializer
import org.jdaviderb.DittoCircle.Enconder
import org.scalatest._
import io.circe.parser._
import org.jdaviderb.dittoSerializer.types.{DittoList, DittoSerializer}

case class User(firstName: String, lastName: String)

class UserSerializer(val user: User) extends Serializer {
  attribute("name") { user.firstName }
  attribute("last_name") { user.lastName }
  attribute("full_name") { s"${user.firstName} ${user.lastName}" }
}

class TestChildSerializer extends Serializer {
  attribute[String]("string") { "string" }
  attribute[Int]("int") { 2147483647 }
}

class TestSerializer extends Serializer {
  attribute[String]("string") { "string" }
  attribute[Int]("int") { 2147483647 }
  attribute[Long]("long") { 86400000 * 150L }
  attribute[Double]("double") { 3.142 }
  attribute[BigInt]("bigint") { 1234567890 }
  attribute[BigDecimal]("bigdecimal") { 1.1 }
  attribute[Float]("float") { 3.142.toFloat }
  attribute[Boolean]("boolean") { false }
  attribute[DittoList]("dittolist") {
    list
      .add("string")
      .add(86400000 * 150)
      .add(3.142)
      .add(1234567890)
      .add(1.1)
      .add(false)
      .add(list.add("child-list"))
      .add(new TestChildSerializer().asInstanceOf[DittoSerializer])
  }
  attribute("dittomap") {
    map
      .set("message", "hello world")
      .set("child-serializer", new TestChildSerializer().asInstanceOf[DittoSerializer])
  }
  attribute("ditoseriliazer") { new TestChildSerializer().asInstanceOf[DittoSerializer] }
}

class EncoderTest extends FunSpec {
  describe("#from") {
    it("encodes a DittoSerializer to JSON") {
      val jsonExpected: String = """
        {
          "dittolist" : [
            "string",
            75098112,
            3.142,
            1234567890,
            1.1,
            false,
            [
              "child-list"
            ],
            {
              "int" : 2147483647,
              "string" : "string"
            }
          ],
          "bigint" : 1234567890,
          "int" : 2147483647,
          "bigdecimal" : 1.1,
          "boolean" : false,
          "long" : 12960000000,
          "double" : 3.142,
          "string" : "string",
          "dittomap" : {
            "child-serializer" : {
              "int" : 2147483647,
              "string" : "string"
            },
            "message" : "hello world"
          },
          "float" : 3.142,
          "ditoseriliazer" : {
            "int" : 2147483647,
            "string" : "string"
          }
        }
      """

      val result = Enconder.from(new TestSerializer())
      assert(parse(jsonExpected).right.get == result)
    }
  }

  describe("#fromList") {
    val jsonExpected: String = """
      [
        {
          "full_name" : "Jorge Hernandez",
          "last_name" : "Hernandez",
          "name" : "Jorge"
        },
        {
          "full_name" : "Josue Hernandez",
          "last_name" : "Hernandez",
          "name" : "Josue"
        }
      ]
    """

    // JSON Serialization
    val jorge = User("Jorge", "Hernandez")
    val josue = User("Josue", "Hernandez")
    val users = List(jorge, josue)

    val result = Enconder.fromList(users.map(new UserSerializer(_)))
    assert(parse(jsonExpected).right.get == result)
  }
}
