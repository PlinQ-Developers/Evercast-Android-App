package apps.plinqdevelopers.evercast.repositories

import apps.plinqdevelopers.evercast.api.ApplicationAPI
import apps.plinqdevelopers.evercast.data.mappers.AccountMapper
import apps.plinqdevelopers.evercast.data.network.NetworkAccountVerification
import apps.plinqdevelopers.evercast.data.payload.AuthenticationPayload
import apps.plinqdevelopers.evercast.room.ApplicationDAO
import apps.plinqdevelopers.evercast.room.ApplicationDatabase
import apps.plinqdevelopers.evercast.utils.RequestManager
import apps.plinqdevelopers.evercast.utils.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthenticationRepository
@Inject
constructor(
    private val applicationAPI: ApplicationAPI,
    applicationDatabase: ApplicationDatabase,
    private val accountMapper: AccountMapper
) {
    private val applicationDAO: ApplicationDAO = applicationDatabase.applicationDAO()

    fun accountLogin(payload: AuthenticationPayload) = networkBoundResource(
        query = {
            applicationDAO.loadAccountDetails()
        },
        fetch = {
            applicationAPI.accountLogin(authPayload = payload)
        },
        saveFetchedResults = { networkAccount ->
            applicationDAO.manageAccountDetails(account = accountMapper.mapToDomainEntity(network = networkAccount))
        }
    )

    fun accountRegister(payload: AuthenticationPayload) = networkBoundResource(
        query = {
            applicationDAO.loadAccountDetails()
        },
        fetch = {
            applicationAPI.accountRegister(authPayload = payload)
        },
        saveFetchedResults = { networkAccount ->
            applicationDAO.manageAccountDetails(account = accountMapper.mapToDomainEntity(network = networkAccount))
        }
    )

    fun getEmailStatus(payload: AuthenticationPayload): Flow<RequestManager<NetworkAccountVerification>> =
        flow {
            emit(RequestManager.Loading(data = null))
            try {
                emit(RequestManager.Success(data = applicationAPI.verifyAuthPayload(authPayload = payload)))
            } catch (e: Throwable) {
                emit(RequestManager.Error(data = null, throwable = e))
            }
        }
}