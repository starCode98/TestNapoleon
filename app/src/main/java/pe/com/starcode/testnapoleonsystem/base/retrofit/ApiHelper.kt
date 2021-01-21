package pe.com.starcode.testnapoleonsystem.base.retrofit


class ApiHelper(private val apiService: ApiService) {

        suspend fun getPosts() = apiService.getPosts()
}