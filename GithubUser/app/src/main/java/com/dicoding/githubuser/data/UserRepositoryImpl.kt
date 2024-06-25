
import com.dicoding.ItemsItem
import com.dicoding.githubuser.data.UserRepository
import com.dicoding.githubuser.data.remote.retrofit.ApiConfig
import com.dicoding.githubuser.data.remote.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepositoryImpl(private val apiService: ApiService) : UserRepository {

    override suspend fun getFollowers(
        username: String,
        onSuccess: (List<ItemsItem>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val client = ApiConfig.getApiService().getFollowersData(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    onSuccess(response.body() ?: emptyList())
                } else {
                    onFailure("Failed to get followers: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                onFailure("Failed to get followers: ${t.message}")
            }
        })
    }

    override suspend fun getFollowing(
        username: String,
        onSuccess: (List<ItemsItem>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val client = ApiConfig.getApiService().getFollowingData(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    onSuccess(response.body() ?: emptyList())
                } else {
                    onFailure("Failed to get following: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                onFailure("Failed to get following: ${t.message}")
            }
        })
    }
}
