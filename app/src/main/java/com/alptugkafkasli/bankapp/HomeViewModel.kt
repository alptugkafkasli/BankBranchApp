import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alptugkafkasli.bankapp.ApiClient
import com.alptugkafkasli.bankapp.ApiInterface
import com.alptugkafkasli.bankapp.BranchItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    lateinit var apiInterface: ApiInterface
    var branchsModel: List<BranchItem> = emptyList()
    val branchList: MutableLiveData<List<BranchItem>> = MutableLiveData()


    fun getBranchs() {
        apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val post = apiInterface.getBranchs()

        post.enqueue(object : Callback<List<BranchItem>> {
            override fun onFailure(call: Call<List<BranchItem>>, t: Throwable) {
                Log.d("onFailure", t.message.toString())
            }

            override fun onResponse(
                call: Call<List<BranchItem>>,
                response: Response<List<BranchItem>>
            ) {
                if (response.isSuccessful) {
                    branchsModel = response.body() ?: emptyList()
                    branchList.postValue(branchsModel)
                    Log.d("onResponse", "Received data: $branchsModel")
                } else {
                    Log.d(
                        "onResponse",
                        "Response not successful: ${response.errorBody()?.string()}"
                    )
                }
            }
        })
    }
}