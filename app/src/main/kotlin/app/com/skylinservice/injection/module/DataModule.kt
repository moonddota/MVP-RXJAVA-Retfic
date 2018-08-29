package app.com.skylinservice.injection.module

import app.com.skylinservice.data.*
import app.com.skylinservice.data.remote.Api
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun providesPostDataSource(api: Api): PostDataSource = PostRepository(api)


    @Singleton
    @Provides
    fun providesRigsitDataSource(api: Api): RegistDataSource = RegistRepository(api)


    @Singleton
    @Provides
    fun providesTeamMangerDataSource(api: Api): TeamManagerDataSource = TeamMangerRepository(api)

    @Singleton
    @Provides
    fun providesLoginDataSource(api: Api): LoginDataSource = LoginRepository(api)

    @Singleton
    @Provides
    fun providesCreateTeamDataSource(api: Api): CreateTeamDataSource = CreateTeamRepository(api)

    @Singleton
    @Provides
    fun providesTeamListDataSource(api: Api): TeamListDataSource = TeamListRepository(api)

    @Singleton
    @Provides
    fun providesTeamEditDataSource(api: Api): TeamEditDataSource = TeamEditRepository(api)


    @Singleton
    @Provides
    fun providesTeamInviteDataSource(api: Api): TeamInviteDataSource = TeamInviteRepository(api)

    @Singleton
    @Provides
    fun providesDeviceManagerDataSource(api: Api): DeviceManagerDataSource = DeviceManagerRepository(api)

    @Singleton
    @Provides
    fun providesBindDataSource(api: Api): BindDataSource = BindRepository(api)

    @Singleton
    @Provides
    fun providesUnBindDataSource(api: Api): UnBindDataSource = UnBindRepository(api)


    @Singleton
    @Provides
    fun providesForgetDataSource(api: Api): ForgetDataSource = ForgetRepository(api)

    @Singleton
    @Provides
    fun providesChangePhoneDataSource(api: Api): ChangePhoneDataSource = ChangePhoneRepository(api)

    @Singleton
    @Provides
    fun providesSetDataSource(api: Api): SetDataSource = SetRepository(api)

    @Singleton
    @Provides
    fun providesMainDataSource(api: Api): MainDataSource = MainRepository(api)

    @Singleton
    @Provides
    fun providesSofCentertDataSource(api: Api): SofCentertDataSource = SoftCenterRepository(api)


    @Singleton
    @Provides
    fun providesChangNickNameDataaSource(api: Api): ChangNickNameDataSource = ChangNickNameRepository(api)

    @Singleton
    @Provides
    fun TeamMangerUpdateDataSource(api: Api): TeamMangerUpdateDataSource = TeamMangerUpdateRepository(api)

    @Singleton
    @Provides
    fun providesDeviceDetailDataSource(api: Api): DeviceDetailDataSource = DeviceDetailRepository(api)

    @Singleton
    @Provides
    fun providesMacDataSource(api: Api): MacDataSource = MacRepository(api)

    @Singleton
    @Provides
    fun providesDeviceStatitcisDatasource(api: Api): DeviceStatitcisDatasource = DeviceStatitcisRepository(api)


    @Singleton
    @Provides
    fun providesDetailStatitcisDatasource(api: Api): DetailStatitcisDatasource = DetailStatitcisRepository(api)


}