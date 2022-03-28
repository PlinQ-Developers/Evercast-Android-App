package apps.plinqdevelopers.evercast.data.mappers

import apps.plinqdevelopers.evercast.data.domain.DomainAccount
import apps.plinqdevelopers.evercast.data.network.NetworkAccount
import apps.plinqdevelopers.evercast.data.utils.EntityMapper
import javax.inject.Inject

class AccountMapper @Inject constructor() : EntityMapper<DomainAccount, NetworkAccount> {
    override fun mapToDomainEntity(network: NetworkAccount): DomainAccount {
        return DomainAccount(
            accountID = network.accountID,
            email = network.email,
            password = network.password,
            username = network.username,
            created = network.created
        )
    }
}