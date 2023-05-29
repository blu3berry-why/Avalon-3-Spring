package blu3berry.why.avalon.dal.interfaces

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


interface ILoggingService {
    fun displayReq(request: HttpServletRequest?, body: Any?)
    fun displayResp(request: HttpServletRequest?, response: HttpServletResponse?, body: Any?)
}