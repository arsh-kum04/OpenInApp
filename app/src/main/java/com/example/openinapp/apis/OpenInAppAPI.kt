```python
# Folder Name: apis
# File Name: OpenInAppAPI.py
# Line by line documented Code:
import com.example.openinapp.data.DataResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface OpenInAppAPI {

    # This function gets data from the server.
    @GET("api/v1/dashboardNew")
    fun getData(@Header("Authorization") token: String): Call<DataResponse>
}
```