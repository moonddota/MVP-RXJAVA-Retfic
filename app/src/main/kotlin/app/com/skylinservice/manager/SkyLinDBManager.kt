package app.com.skylinservice.manager

import app.com.skylinservice.sqlite.TeamTable
import app.com.skylinservice.ui.guide.GuideContract
import com.skylin.uav.zbzy.model.LoginTable
import org.xutils.DbManager
import org.xutils.ex.DbException
import javax.inject.Inject

/**
 * Created by liuxuan on 2017/12/27.
 */
class SkyLinDBManager constructor(private val dbManager: DbManager) {


    fun getUserById(id: Long): LoginTable {
        return try {
            dbManager.findById(LoginTable::class.java, id)
        } catch (ex: Exception) {
            null!!
        }

    }


    fun update(user: LoginTable) {
        dbManager.saveOrUpdate(user)
    }

    fun deleteUser(userId: Int) {
        dbManager.deleteById(LoginTable::class.java, userId)
    }

    fun deleteTeam() {
        dbManager.delete(TeamTable::class.java)
    }

    fun getTeam(): List<TeamTable> {
        return dbManager.findAll(TeamTable::class.java)
    }

    fun updateTeam(data: MutableList<TeamTable>) {
        try {
            var teamlist = mutableListOf<TeamTable>()
            var haslist = dbManager.selector(TeamTable::class.java)
                    .findAll()
            haslist?.let {
                if (haslist.size == 0)
                    for (teamtablhas in haslist) {
                        data.let {
                            data.filterTo(teamlist) { it.id != teamtablhas.id }
                        }

                    }
                else
                    teamlist = data
            }
            dbManager.saveOrUpdate(data)
        } catch (e: Exception) {

        }

    }


}