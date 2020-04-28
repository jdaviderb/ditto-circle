package org.jdaviderb.DittoCircle
import org.jdaviderb.dittoSerializer.core.Serializer
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.{Encoder, Json}

object Enconder {
  implicit val encodeMap: Encoder[Map[String, Any]] = new Encoder[Map[String, Any]] {
    final def apply(a: Map[String, Any]): Json = toJsonObject(a)

    def toJsonObject(value: Map[String, Any]) =
      value.foldLeft(Json.obj()) { (json, map) => json.deepMerge(toJsonDeep(map)) }

    def toJsonDeep(data: (String, Any)): Json = Json.obj((data._1, anyToJson(data._2)))

    def anyToJson(value: Any): Json = value match {
      case string: String => Json.fromString(string)
      case int: Int => Json.fromInt(int)
      case double: Double => Json.fromDouble(double).get
      case bigInt: BigInt => Json.fromBigInt(bigInt)
      case bigDecimal: BigDecimal => Json.fromBigDecimal(bigDecimal)
      case long: Long => Json.fromLong(long)
      case float: Float => Json.fromFloat(float).get
      case bool: Boolean => Json.fromBoolean(bool)
      case list: List[Any] => Json.fromValues(list.map(anyToJson(_)))
      case map: Map[String, Any] => toJsonObject(map)
      case _ => Json.fromString("unsupported-type")
    }

  }

  def from(serializer: Serializer): Json = serializer.serialize.primitiveDeep.primitive.asJson
  def fromList(serializers: List[Serializer]): Json =
    serializers.map(_.serialize.primitiveDeep.primitive).asJson
}
