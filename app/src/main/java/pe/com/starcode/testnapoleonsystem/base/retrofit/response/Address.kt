package pe.com.starcode.testnapoleonsystem.base.retrofit.response

data class Address(
    val city: String,
    val geo: Geo,
    val street: String,
    val suite: String,
    val zipcode: String
)