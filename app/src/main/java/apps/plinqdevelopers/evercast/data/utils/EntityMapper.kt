package apps.plinqdevelopers.evercast.data.utils

interface EntityMapper<T, NetworkModel> {
    fun mapToDomainEntity(network : NetworkModel) : T
}