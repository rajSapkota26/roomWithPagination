package com.bandhu.myapplication.retrofit

class RemoteApiGeneralResponse<T>(
    var status: Status,
    var data: T? = null,
    var error: String? = null,
    var progressState: ProgressState? = null
) {
    enum class Status {
        INIT, LOADING, SUCCESS, ERROR, NO_INTERNET_AVAILABLE, TOKEN_EXPIRED, IN_PROGRESS, USER_NOT_VERIFIED, EMPTY, EXCEPTION, SAVEONDB, LOADING_FINISHED
    }

    companion object {
        fun <T> init(): RemoteApiGeneralResponse<T> {
            return RemoteApiGeneralResponse(Status.INIT)
        }

        fun <T> loading(): RemoteApiGeneralResponse<T> {
            return RemoteApiGeneralResponse(Status.LOADING)
        }

        fun <T> noInternetAvailable(): RemoteApiGeneralResponse<T> {
            return RemoteApiGeneralResponse(Status.NO_INTERNET_AVAILABLE, error = "No internet connection found")
        }

        fun <T> inProgress(data: ProgressState): RemoteApiGeneralResponse<T> {
            return RemoteApiGeneralResponse(Status.IN_PROGRESS, progressState = data)
        }

        fun <T> success(data: T): RemoteApiGeneralResponse<T> {
            return RemoteApiGeneralResponse(Status.SUCCESS, data = data)
        }

        fun <T> tokenExpired(error: String?): RemoteApiGeneralResponse<T> {
            return RemoteApiGeneralResponse(Status.TOKEN_EXPIRED, error = error)
        }

        fun <T> onException(error: String?): RemoteApiGeneralResponse<T> {
            return RemoteApiGeneralResponse(Status.EXCEPTION, error = error)
        }

        fun <T> userNotVerified(error: String?): RemoteApiGeneralResponse<T> {
            return RemoteApiGeneralResponse(Status.USER_NOT_VERIFIED, error = error)
        }

        fun <T> error(error: String?): RemoteApiGeneralResponse<T> {
            return RemoteApiGeneralResponse(Status.ERROR, error = error)
        }

        fun <T> saveOnDB(data: T): RemoteApiGeneralResponse<T> {
            return RemoteApiGeneralResponse(Status.SAVEONDB, data = data)
        }

        fun <T> loadingFinished(): RemoteApiGeneralResponse<T> {
            return RemoteApiGeneralResponse(Status.LOADING_FINISHED)
        }

        fun <T> empty(message: String?): RemoteApiGeneralResponse<T> {
            return RemoteApiGeneralResponse(Status.EMPTY, error = message)
        }
    }

    class ProgressState(
        var message: String = "",
        var percent: Double = 0.0,
        var totalFileCount: Int = 0,
        var totalUploadFileCountTillNow: Int = 0,
        var totalUploadFileTillNowInMb: Double = 0.0,
        var totalSizeFileInMb: Double = 0.0
    )
}
