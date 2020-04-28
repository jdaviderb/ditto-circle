### Using Circle
if you want to use Circle for JSON Serialization, you have to add this package in your project

```sbt
externalResolvers += "ditto-circle" at "https://maven.pkg.github.com/jdaviderb/ditto-circle"

libraryDependencies += "jdaviderb" %%  "ditto-circle" % "0.5.0"
```

**Example:**

```scala
import org.jdaviderb.DittoCircle.Enconder
import org.jdaviderb.dittoSerializer.core.Serializer

// Model definition
case class User(firstName: String, lastName: String)

// Serializer
class UserSerializer(val user: User) extends Serializer {
  attribute("name") { user.firstName }
  attribute("last_name") { user.lastName }
  attribute("full_name") { s"${user.firstName} ${user.lastName}" }
}

// JSON Serialization
val jorge = User("Jorge", "Hernandez")
val josue = User("Josue", "Hernandez")
val users = List(jorge, josue)

Enconder.from(new UserSerializer(jorge))
/*
  RESULT:
  {
    "full_name" : "Jorge Hernandez",
    "last_name" : "Hernandez",
    "name" : "Jorge"
  }
*/

Enconder.fromList(users.map(new UserSerializer(_)))
/*
  RESULT:
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
*/

```
