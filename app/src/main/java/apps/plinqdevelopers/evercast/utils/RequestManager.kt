package apps.plinqdevelopers.evercast.utils

sealed class RequestManager<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    class Success<T>(data: T) : RequestManager<T>(data = data)
    class Loading<T>(data: T? = null) : RequestManager<T>(data = data)
    class Error<T>(data: T? = null, throwable: Throwable) :
        RequestManager<T>(data = data, error = throwable)
}
