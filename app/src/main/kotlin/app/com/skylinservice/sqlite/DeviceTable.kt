package app.com.skylinservice.sqlite

import org.xutils.db.annotation.Column
import org.xutils.db.annotation.Table
import java.io.Serializable
import java.util.*

/**
 * Created by liuxuan on 2017/4/20.
 */
@Table(name = "DeviceTable")
class DeviceTable : Serializable {
    @Column(name = "SN", isId = true, autoGen = false)
    var SN: Int = 0

    @Column(name = "flyname")
    var flyname: String = "v02s"

    @Column(name = "productdate")
    var productdate: String = "2017.8.9"

    @Column(name = "flytype")
    var flytype: String = "v02s"

    @Column(name = "controlid")
    var controlid: Int = 0

    @Column(name = "rigistdate")
    var rigistdate: String = "2017.8.9"
}