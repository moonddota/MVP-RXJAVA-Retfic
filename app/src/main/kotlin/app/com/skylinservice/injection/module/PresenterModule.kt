package app.com.skylinservice.injection.module

import app.com.skylinservice.data.*
import app.com.skylinservice.injection.ConfigPersistent
import app.com.skylinservice.manager.SkyLinDBManager
import app.com.skylinservice.ui.DefaultScheduler
import app.com.skylinservice.ui.device.DeviceManagerContract
import app.com.skylinservice.ui.device.DeviceManagerPresenter
import app.com.skylinservice.ui.device.devicedetail.DeviceDetailContract
import app.com.skylinservice.ui.device.devicedetail.DeviceDetailPresenter
import app.com.skylinservice.ui.device.manual.BindContract
import app.com.skylinservice.ui.device.manual.BindPresenter
import app.com.skylinservice.ui.device.unbind.UnBindContract
import app.com.skylinservice.ui.device.unbind.UnBindPresenter
import app.com.skylinservice.ui.guide.GuideContract
import app.com.skylinservice.ui.guide.GuidePresenter
import app.com.skylinservice.ui.maclist.MacContract
import app.com.skylinservice.ui.maclist.MacPresenter
import app.com.skylinservice.ui.main.MainContract
import app.com.skylinservice.ui.main.MainPresenter
import app.com.skylinservice.ui.personcenter.DetailStatisticsContract
import app.com.skylinservice.ui.personcenter.DetailtatiticsPresenter
import app.com.skylinservice.ui.personcenter.DeviceStatisticsContract
import app.com.skylinservice.ui.personcenter.DeviceStatiticsPresenter
import app.com.skylinservice.ui.set.SetContract
import app.com.skylinservice.ui.set.SetPresenter
import app.com.skylinservice.ui.set.changenick.ChangeNickNameContract
import app.com.skylinservice.ui.set.changenick.ChangeNickNamePresenter
import app.com.skylinservice.ui.softcenter.SoftCenterContract
import app.com.skylinservice.ui.softcenter.SoftCenterPresenter
import app.com.skylinservice.ui.teammanager.TeamManagerPresenter
import app.com.skylinservice.ui.teammanager.TeamMangerContract
import app.com.skylinservice.ui.teammanager.create.CreateTeamContract
import app.com.skylinservice.ui.teammanager.create.CreateTeamPresenter
import app.com.skylinservice.ui.teammanager.edit.TeamEditContract
import app.com.skylinservice.ui.teammanager.edit.TeamEditPresenter
import app.com.skylinservice.ui.teammanager.invite.InviteContract
import app.com.skylinservice.ui.teammanager.invite.InvitePresenter
import app.com.skylinservice.ui.teammanager.invite.TeamChangeContract
import app.com.skylinservice.ui.teammanager.invite.TeamChangePresenter
import app.com.skylinservice.ui.teammanager.teamlist.TeamListContract
import app.com.skylinservice.ui.teammanager.teamlist.TeamListPresenter
import app.com.skylinservice.ui.userrelative.login.LoginContract
import app.com.skylinservice.ui.userrelative.login.LoginPresenter
import app.com.skylinservice.ui.userrelative.login.changephone.ChangePhoneContract
import app.com.skylinservice.ui.userrelative.login.changephone.ChangePresenter
import app.com.skylinservice.ui.userrelative.login.changpwdbyvalid.ForgetContract
import app.com.skylinservice.ui.userrelative.login.changpwdbyvalid.ForgetPresenter
import app.com.skylinservice.ui.userrelative.login.regist.RigistContract
import app.com.skylinservice.ui.userrelative.login.regist.RigistPresenter
import dagger.Module
import dagger.Provides

@Module
class PresenterModule {


    @ConfigPersistent
    @Provides
    fun providesGuidePresenter(dataSource: PostDataSource): GuideContract.Presenter =
            GuidePresenter(dataSource, DefaultScheduler)


    @ConfigPersistent
    @Provides
    fun providesRigistPresenter(dataSource: RegistDataSource): RigistContract.Presenter =
            RigistPresenter(dataSource, DefaultScheduler)


    @ConfigPersistent
    @Provides
    fun providesTeamManagerPresenter(dataSource: TeamManagerDataSource): TeamMangerContract.Presenter {
        return TeamManagerPresenter(dataSource, DefaultScheduler)
    }


    @ConfigPersistent
    @Provides
    fun providesLoginPresenter(dataSource: LoginDataSource): LoginContract.Presenter =
            LoginPresenter(dataSource, DefaultScheduler)


    @ConfigPersistent
    @Provides
    fun providesCreateTeamPresenter(dataSource: CreateTeamDataSource): CreateTeamContract.Presenter =
            CreateTeamPresenter(dataSource, DefaultScheduler)

    @ConfigPersistent
    @Provides
    fun providesTeamListPresenter(dataSource: TeamListDataSource): TeamListContract.Presenter =
            TeamListPresenter(dataSource, DefaultScheduler)

    @ConfigPersistent
    @Provides
    fun providesTeamEditPresenter(dataSource: TeamEditDataSource): TeamEditContract.Presenter =
            TeamEditPresenter(dataSource, DefaultScheduler)


    @ConfigPersistent
    @Provides
    fun providesInvitePresenter(dataSource: TeamInviteDataSource): InviteContract.Presenter =
            InvitePresenter(dataSource, DefaultScheduler)

    @ConfigPersistent
    @Provides
    fun providesDeviceManagerPresenter(dataSource: DeviceManagerDataSource): DeviceManagerContract.Presenter =
            DeviceManagerPresenter(dataSource, DefaultScheduler)

    @ConfigPersistent
    @Provides
    fun providesBindPresenter(dataSource: BindDataSource): BindContract.Presenter =
            BindPresenter(dataSource, DefaultScheduler)

    @ConfigPersistent
    @Provides
    fun providesUnBindPresenter(dataSource: UnBindDataSource): UnBindContract.Presenter =
            UnBindPresenter(dataSource, DefaultScheduler)

    @ConfigPersistent
    @Provides
    fun providesSetPresenter(dataSource: SetDataSource, skyLinDBManager: SkyLinDBManager): SetContract.Presenter =
            SetPresenter(dataSource, skyLinDBManager, DefaultScheduler)

    @ConfigPersistent
    @Provides
    fun providesForgetPresenter(forgetDataSource: ForgetDataSource): ForgetContract.Presenter =
            ForgetPresenter(forgetDataSource, DefaultScheduler)

    @ConfigPersistent
    @Provides
    fun providesChangePresenter(forgetDataSource: ChangePhoneDataSource): ChangePhoneContract.Presenter =
            ChangePresenter(forgetDataSource, DefaultScheduler)

    @ConfigPersistent
    @Provides
    fun providesMainPresenter(mainDataSource: MainDataSource): MainContract.Presenter =
            MainPresenter(mainDataSource, DefaultScheduler)

    @ConfigPersistent
    @Provides
    fun providesSoftCenterPresenter(mainDataSource: SofCentertDataSource): SoftCenterContract.Presenter =
            SoftCenterPresenter(mainDataSource, DefaultScheduler)

    @ConfigPersistent
    @Provides
    fun providesChangeNickNamePresenter(mainDataSource: ChangNickNameDataSource): ChangeNickNameContract.Presenter =
            ChangeNickNamePresenter(mainDataSource, DefaultScheduler)

    @ConfigPersistent
    @Provides
    fun providesTeamChangePresenter(mainDataSource: TeamMangerUpdateDataSource): TeamChangeContract.Presenter =
            TeamChangePresenter(mainDataSource, DefaultScheduler)


    @ConfigPersistent
    @Provides
    fun providesDeviceDetailPresent(mainDataSource: DeviceDetailDataSource): DeviceDetailContract.Presenter =
            DeviceDetailPresenter(mainDataSource, DefaultScheduler)


    @ConfigPersistent
    @Provides
    fun providesMacPresenter(mainDataSource: MacDataSource): MacContract.Presenter =
            MacPresenter(mainDataSource, DefaultScheduler)


    @ConfigPersistent
    @Provides
    fun providesDeviceStatiticsPresenter(mainDataSource: DeviceStatitcisDatasource): DeviceStatisticsContract.Presenter =
            DeviceStatiticsPresenter(mainDataSource, DefaultScheduler)


    @ConfigPersistent
    @Provides
    fun providesDetailtatiticsPresenter(mainDataSource: DetailStatitcisDatasource): DetailStatisticsContract.Presenter =
            DetailtatiticsPresenter(mainDataSource, DefaultScheduler)
}

