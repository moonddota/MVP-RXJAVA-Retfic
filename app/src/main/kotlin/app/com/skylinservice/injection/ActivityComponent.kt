package app.com.skylinservice.injection

import app.com.skylinservice.injection.module.ActivityModule
import app.com.skylinservice.ui.device.DeviceManagerActivity
import app.com.skylinservice.ui.device.devicedetail.DeviceDetailActivity
import app.com.skylinservice.ui.device.manual.ManualInputActivity
import app.com.skylinservice.ui.device.scan.ScanCodeActivity
import app.com.skylinservice.ui.device.unbind.UnbindActivity
import app.com.skylinservice.ui.forauth.ForAuthActivity
import app.com.skylinservice.ui.guide.GuideActivity
import app.com.skylinservice.ui.maclist.MacLIstActivity
import app.com.skylinservice.ui.main.MainActivity
import app.com.skylinservice.ui.personcenter.DetailStatisticsActivity
import app.com.skylinservice.ui.personcenter.DeviceStatisticesBaseActivity
import app.com.skylinservice.ui.personcenter.persionstatistics.PersonStatisticesBaseActivity
import app.com.skylinservice.ui.set.SettingActivity
import app.com.skylinservice.ui.set.WebActivity
import app.com.skylinservice.ui.set.changenick.ChangeFlyNameActivity
import app.com.skylinservice.ui.set.changenick.ChangeNickNameActivity
import app.com.skylinservice.ui.softcenter.SoftCenterActivity
import app.com.skylinservice.ui.teammanager.TeamManagerActivity
import app.com.skylinservice.ui.teammanager.create.CreateTeamActivity
import app.com.skylinservice.ui.teammanager.create.EditTeamActivity
import app.com.skylinservice.ui.teammanager.invite.InviteActivity
import app.com.skylinservice.ui.teammanager.invite.TeamerChangeActivity
import app.com.skylinservice.ui.teammanager.teamlist.TeamListActivity
import app.com.skylinservice.ui.userrelative.login.LoginActivity
import app.com.skylinservice.ui.userrelative.login.changephone.ChangePhoneActivity
import app.com.skylinservice.ui.userrelative.login.changpwdbyvalid.ForgotPwdActivity
import app.com.skylinservice.ui.userrelative.login.regist.RegistAccountActivity
import app.com.skylinservice.wxapi.WXEntryActivity
import dagger.Subcomponent

@PerActivity
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun inject(guideActivity: GuideActivity)
    fun inject(registAccountActivity: RegistAccountActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(teamManagerActivity: TeamManagerActivity)
    fun inject(createTeamActivity: CreateTeamActivity)
    fun inject(teamListActivity: TeamListActivity)
    fun inject(editTeamActivity: EditTeamActivity)
    fun inject(inviteActivity: InviteActivity)
    fun inject(deviceManagerActivity: DeviceManagerActivity)
    fun inject(manualInputActivity: ManualInputActivity)
    fun inject(unbindActivity: UnbindActivity)
    fun inject(settingActivity: SettingActivity)
    fun inject(forgotPwdActivity: ForgotPwdActivity)
    fun inject(changePhoneActivity: ChangePhoneActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(softCenterActivity: SoftCenterActivity)
    fun inject(webActivity: WebActivity)
    fun inject(changeNickNameActivity: ChangeNickNameActivity)
    fun inject(teamerChangeActivity: TeamerChangeActivity)
    fun inject(scanCodeActivity: ScanCodeActivity)
    fun inject(wxEntryActivity: WXEntryActivity)
    fun inject(forAuthActivity: ForAuthActivity)
    fun inject(deviceDetailActivity: DeviceDetailActivity)
    fun inject(deviceDetailActivity: ChangeFlyNameActivity)

    fun inject(macLIstActivity: MacLIstActivity)
    fun inject(deviceStatisticsActivity: DeviceStatisticesBaseActivity)
    fun inject(deviceStatisticsActivity: PersonStatisticesBaseActivity)
    fun inject(detailStatisticsActivity: DetailStatisticsActivity)


//    fun inject(baseActivity: BaseActivity)

//    fun inject(baseFragment: BaseFragment)

    //    fun inject(baseActivity: BaseActivity)
//    fun inject(reigstActivity: RegistAccountActivity)

}