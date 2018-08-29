package app.com.skylinservice.data.remote.requestmodel

import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.DownloadTask

data class SoftApps(val id: String,
                    val name: String,
                    val type: String,
                    val info: String,
                    val icon: String,
                    val version: String,
                    val time: String,
                    val packageName: String,
                    var detials: List<APPInfo>,
                    var install_state: Int,
                    var install_progress: Int,
                    var task: BaseDownloadTask?

//1:显示打开,2:显示更新,,3:显示安装,0:显示下载,4:显示正在等待,5:显示继续 ,6:下载中
)