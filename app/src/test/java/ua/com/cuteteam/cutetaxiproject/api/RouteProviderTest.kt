package ua.com.cuteteam.cutetaxiproject.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ua.com.cuteteam.cutetaxiproject.CuteTestCoroutineDispatcher
import ua.com.cuteteam.cutetaxiproject.api.directions.*
import ua.com.cuteteam.cutetaxiproject.api.roads.Roads
import ua.com.cuteteam.cutetaxiproject.api.roads.RoadsRequest

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RouteProviderTest {

    private var routeBuilder: RouteProvider.Builder? = null
    private var route: Route? = null
    private var road: Roads? = null
    private var directionRequest: DirectionRequest? = null
    private var roadsRequest: RoadsRequest? = null

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {

        Dispatchers.setMain(CuteTestCoroutineDispatcher)

        route = Route(
            status = "OK",
            routes = listOf(
                RouteInfo(
                    summary = "H-08",
                    legs = listOf(
                        LegInfo(
                            duration = Duration(
                                value = 9904.0,
                                text = "2 ч. 45 мин."
                            ),
                            distance = Distance(
                                value = 188546.0,
                                text = "189 км"
                            ),
                            steps = listOf(
                                Step(
                                    startLocation = LatLng(49.4442495, 32.0595199),
                                    endLocation = LatLng(49.4588119, 32.0335838),
                                    duration = Duration(
                                        value = 298.0,
                                        text = "5 мин."
                                    ),
                                    distance = Distance(
                                        value = 2477.0,
                                        text = "2,5 км"
                                    ),
                                    instructions = "Направляйтесь на <b>севе…Байди Вишневецького</b>",
                                    maneuver = null
                                ),
                                Step(
                                    startLocation = LatLng(49.4588119, 32.0335838),
                                    endLocation = LatLng(49.4655965, 32.0229632),
                                    duration = Duration(
                                        value = 100.0,
                                        text = "2 мин."
                                    ),
                                    distance = Distance(
                                        value = 1083.0,
                                        text = "1,1 км"
                                    ),
                                    instructions = "Продолжайте движение по …НГ-КЛУБ \"КОСМОС\"</b>)",
                                    maneuver = Maneuver.STRAIGHT
                                ),
                                Step(
                                    startLocation = LatLng(49.4655965, 32.0229632),
                                    endLocation = LatLng(49.6556003, 32.1124988),
                                    duration = Duration(
                                        value = 1312.0,
                                        text = "22 мин."
                                    ),
                                    distance = Distance(
                                        value = 25699.0,
                                        text = "25,7 км"
                                    ),
                                    instructions = "Резкий поворот <b>налево…выезжайте на <b>Н16</b>",
                                    maneuver = Maneuver.RAMP_LEFT
                                ),
                                Step(
                                    startLocation = LatLng(49.6556003, 32.1124988),
                                    endLocation = LatLng(49.6717464, 32.0829153),
                                    duration = Duration(
                                        value = 203.0,
                                        text = "3 мин."
                                    ),
                                    distance = Distance(
                                        value = 2951.0,
                                        text = "3,0 км"
                                    ),
                                    instructions = "На круге сверните на <b>…b>/<wbr/><b>ПИРЯТИН</b>",
                                    maneuver = Maneuver.ROUND_ABOUT_RIGHT
                                ),
                                Step(
                                    startLocation = LatLng(49.6717464, 32.0829153),
                                    endLocation = LatLng(50.44985029999999, 30.5238646),
                                    duration = Duration(
                                        value = 7991.0,
                                        text = "2 ч. 13 мин."
                                    ),
                                    distance = Distance(
                                        value = 156336.0,
                                        text = "156,0 км"
                                    ),
                                    instructions = "Поверните <b>налево</b> … по вул. Хрещатик</div>",
                                    maneuver = Maneuver.TURN_LEFT
                                )
                            )
                        )
                    ),
                    polyline = Polyline(
                        points = "qaxlH_stbEw~@jpBm[~q@wOp^oWp_@lBr@DlDiAz@qFaEcWw^wkBqtCm}BgmDgn@e`AiRiVmYaSoRwFiQqA{SbAoVnHuVlQis@f{@{VpZmQ`Q}U~LaKfCsOCuMsGiOkN_s@ar@ihAueAo`Aq~@iFcJkBgKqMqeAcCaQsCwGgGwFsImEoSBaj@lCm`@vIqPxCq~@h[}pB|q@kIlAkCdBeSvIgKfIyIpNkFpQcV~tA_FxPuGzNyEdFeAf@m@pDqSrQuq@jk@eO`PiGzM}Lxn@_Yxz@gJh\\gTzkAuFbd@yBd[dAzqAz@~wCwI|k@eLjq@yMjf@{]jdBsYdeAwYh|@iPnd@yCxOmGpk@ySxbD}EhTaEtGgf@|\\uoAt{@aEzGaFdP_Pvj@sL`Rqr@vy@wFzMaDbRwLvq@yQ|j@k[r{@qMvV{Xl`@sOjQm\\`OgmB`s@sgAbd@_ZrMmWlBkWhAoEfBks@b}@ctCduD{FtJ_VdYoyAl{BefB`nCyrD`yF_}CnyE}tCdkD}xA|aBaiDrtDutEngF}JdLmJzKgCjE@fCXvT\\lLtCjMpDhFtMnGbgAxc@jE~ExBdGhDnb@Jpc@qL~pBqHpjAoBxLg`@no@gi@hcAi`AnxAc`@vk@aCWyHny@iPvbBsDdXkKz_@cD|[sDto@sLxx@iSbfAcJ|^qb@bu@}u@l_Bw}@jhBmSz[a^bd@qg@rn@uNpRgNjZsr@`vC_`At`EaSjf@qNbZgUvp@m`@v~@{Ulf@gUh^_h@zcAw_A|yByU`^aj@lt@uUlX_ZjY}Pdc@eVry@cMlWof@pd@}q@xn@gr@fo@mMfOaMnSy_@`r@{Mvn@gEdPcHhNom@bh@{QzQuN~Rah@nu@wq@d`Agk@vf@eP`Ocj@zp@sMhOmOfLie@pQin@|NqQrFyV~N}x@dc@kNjIoFcAeBWUxAw@jDqI`OsSfj@_DpH}MpQwIrKsGjPcMnRyPfScSnp@qWlx@sOpk@wGrW{Dl[kKv}@yTx{@uC`UQlr@oSllByIlpAmElaAmMftCgH|}AcFtk@i`@`uCe_@`pCkE~^qE~z@cDxn@gIn|AeHd|Ao@pg@iDfgA_Ax`@r@~\\tQzzBjZppDnFvp@bAnd@sDnvBqApp@eAdOiMjf@eIzXaDrF{D}@_YcUaToL{RmGuQ}A}MRmTdEqJx@cBaHwAL}A~CGnIdBxLmCbSuEbKmQtFyB{@aA[_ChAwBjM{C|DiJfFiHbBqVfPwInG{PhXyAxF[nJiPfc@ySnj@oNxPtM~Rx@lA"
                    )
                ),
                RouteInfo(
                    summary = "P10",
                    legs = listOf(
                        LegInfo(
                            duration = Duration(
                                value = 10911.0,
                                text = "3 ч. 2 мин."
                            ),
                            distance = Distance(
                                value = 191524.0,
                                text = "192 км"
                            ),
                            steps = listOf(
                                Step(
                                    startLocation = LatLng(49.4442495, 32.0595199),
                                    endLocation = LatLng(49.4588119, 32.0335838),
                                    duration = Duration(
                                        value = 298.0,
                                        text = "5 мин."
                                    ),
                                    distance = Distance(
                                        value = 2477.0,
                                        text = "2,5 км"
                                    ),
                                    instructions = "Направляйтесь на <b>севе…Байди Вишневецького</b>",
                                    maneuver = null
                                ),
                                Step(
                                    startLocation = LatLng(49.4588119, 32.0335838),
                                    endLocation = LatLng(49.7044347, 31.3670327),
                                    duration = Duration(
                                        value = 3687.0,
                                        text = "1 час. 1 мин."
                                    ),
                                    distance = Distance(
                                        value = 64183.0,
                                        text = "64,2 км"
                                    ),
                                    instructions = "Продолжайте движение по …НГ-КЛУБ \"КОСМОС\"</b>)",
                                    maneuver = Maneuver.STRAIGHT
                                ),
                                Step(
                                    startLocation = LatLng(49.7044347, 31.3670327),
                                    endLocation = LatLng(49.7109381, 31.282154),
                                    duration = Duration(
                                        value = 339.0,
                                        text = "6 мин."
                                    ),
                                    distance = Distance(
                                        value = 6423.0,
                                        text = "6,4 км"
                                    ),
                                    instructions = "На круге сверните на <b>…торону <b>МИРОНІВКА</b>",
                                    maneuver = Maneuver.ROUND_ABOUT_RIGHT
                                ),
                                Step(
                                    startLocation = LatLng(49.7109381, 31.282154),
                                    endLocation = LatLng(49.71852149999999, 31.1688241),
                                    duration = Duration(
                                        value = 473.0,
                                        text = "8 мин."
                                    ),
                                    distance = Distance(
                                        value = 8765.0,
                                        text = "8,8 км"
                                    ),
                                    instructions = "На круге сверните на <b>… движение по <b>P09</b>",
                                    maneuver = Maneuver.ROUND_ABOUT_RIGHT
                                ),
                                Step(
                                    startLocation = LatLng(49.71852149999999, 31.1688241),
                                    endLocation = LatLng(49.7636601, 31.0575204),
                                    duration = Duration(
                                        value = 594.0,
                                        text = "10 мин."
                                    ),
                                    distance = Distance(
                                        value = 9527.0,
                                        text = "9,5 км"
                                    ),
                                    instructions = "Поверните <b>направо</b>",
                                    maneuver = Maneuver.TURN_RIGHT
                                ),
                                Step(
                                    startLocation = LatLng(49.7636601, 31.0575204),
                                    endLocation = LatLng(50.44985029999999, 30.5238646),
                                    duration = Duration(
                                        value = 5520.0,
                                        text = "1 ч. 32 мин."
                                    ),
                                    distance = Distance(
                                        value = 100149.0,
                                        text = "100,1 км"
                                    ),
                                    instructions = "Поверните <b>налево</b> … по вул. Хрещатик</div>",
                                    maneuver = Maneuver.TURN_RIGHT
                                )
                            )
                        )
                    ),
                    polyline = Polyline(
                        points = "qaxlH_stbE}kB|bEmk@j{@ot@v`CoEdMgJ~z@aCh~BhBbd@pHt^dI`XInJ{PfaAkGdbAaIhfBo_@tw@uOzX}EfUwL|j@aGfk@{PraAfBpz@hE|a@rHnq@iBtQ_IxKcK~Amj@gSgJ{AsH|@kJlLqH~BuObAgWbb@aOdXaCtLtDxp@zNpkAxl@p_BxXhx@r@nYiEvXkQbn@cGxOiSfUad@`h@iBpHtH|_@aLvZoQxc@iNnh@aSrfA{PvmBuA|p@Lb}AdKlh@Oxu@iCxYqGbTwi@tcAw[tpA}tApyF}Ux|@{t@tdAwa@xnCkr@xpDs[dpBcJr~@yCjQmM~Yuu@zcByz@bmBia@hr@wWfh@gFhZpBhMcEr_@iBfAuI}C_WqPiXnPaO|F}Ib@yh@}CqSpHqh@vh@q|Bb|Eyo@fnAah@hw@_EzFgg@k^id@k\\iBuB?tHzDr}DZzwD}@fW{FlQiYhx@uExG_@|JqAtXFj@~YxZdFvNnCpdAv@frC}@v~@gI|s@gYtvBiVflBmH~^{@tW}KdZuCxRuIdl@MhSyG`O{Mb`@_eAfhDqaCtwHix@||B_ItHiGnJ}E}EuPjKiIvOoFdTmX`|@uOph@wLnh@yHh`Bu@bNwA`EwOyAuMcAcUqKkFIgM_JyGvc@oEbR}D`HaFhJwAw@cC}@_HqCcDWaRh|A}l@uKef@iJgG}A_AtH{@bKmAfAsUuBqGdNR|I_Bfi@sJnfA{a@raCiJhi@aCdOdBjL`Br\\gYjpBokAb{Gio@fuDcGj^kTTcz@iF{`El_DcbBdoAkYl\\{Zjd@ur@pdAi`AzvAca@|l@mp@~y@em@rs@miBlxBqiBvjCiz@x{@cnCziCyUrHgJnOk^ns@aFfMk@lc@qEjS_HlN_IdGgHf@cVhCcNfCyl@eKaZwKkRu_@yS__@sFee@{FqTwGuGeLuBoQkDybAad@aVyNyM}NaZme@iHcD}FT{JzEgKhPeG~EgIdAe]cC{NiFkNyHiM^mUbGm}@iLylAeQm]@{t@v_@c}B{TuIn@wLjGuY|Qqw@zf@cgBlhAu}G~iEgaA|p@}]pl@}Qbb@aNd^iNxR_WrWqq@ra@ckA|q@g_AxX}VjIaMhOwNbGefApOcz@bLifAY_s@iJ}aAoX}[_Ka\\ePw`@}`@ke@yUsc@_Dw`@vGqCwAa@kD_EpAa@fIjBbLgBjQ{ErNiHlB_LfC{DUsCvMqBvDyOfHiTtLgZpXkJ`R_@|IoHtWwU|k@cGfPqJfJe@tBpBvC`HrKbB~B"
                    )
                ),
                RouteInfo(
                    summary = "H01",
                    legs = listOf(
                        LegInfo(
                            duration = Duration(
                                value = 8100.0,
                                text = "2 ч. 15 мин."
                            ),
                            distance = Distance(
                                value = 211003.0,
                                text = "211 км"
                            ),
                            steps = listOf(
                                Step(
                                    startLocation = LatLng(49.4442495, 32.0595199),
                                    endLocation = LatLng(49.4588119, 32.0335838),
                                    duration = Duration(
                                        value = 298.0,
                                        text = "5 мин."
                                    ),
                                    distance = Distance(
                                        value = 2477.0,
                                        text = "2,5 км"
                                    ),
                                    instructions = "Направляйтесь на <b>севе…Байди Вишневецького</b>",
                                    maneuver = null
                                ),
                                Step(
                                    startLocation = LatLng(49.4588119, 32.0335838),
                                    endLocation = LatLng(49.5248453, 31.74126819999999),
                                    duration = Duration(
                                        value = 1509.0,
                                        text = "25 мин."
                                    ),
                                    distance = Distance(
                                        value = 26644.0,
                                        text = "26,6 км"
                                    ),
                                    instructions = "Продолжайте движение по …НГ-КЛУБ \"КОСМОС\"</b>)",
                                    maneuver = Maneuver.STRAIGHT
                                ),
                                Step(
                                    startLocation = LatLng(49.5248453, 31.74126819999999),
                                    endLocation = LatLng(49.4212379, 31.2588566),
                                    duration = Duration(
                                        value = 2617.0,
                                        text = "44 мин."
                                    ),
                                    distance = Distance(
                                        value = 41098.0,
                                        text = "41,1 км"
                                    ),
                                    instructions = "Поверните <b>направо</b>…движение по T2403</div>",
                                    maneuver = Maneuver.TURN_RIGHT
                                ),
                                Step(
                                    startLocation = LatLng(49.4212379, 31.2588566),
                                    endLocation = LatLng(49.4212379, 31.2588566),
                                    duration = Duration(
                                        value = 3011.0,
                                        text = "50 мин."
                                    ),
                                    distance = Distance(
                                        value = 62199.0,
                                        text = "62,2 км"
                                    ),
                                    instructions = "Плавный поворот <b>направо</b> на <b>H01</b>",
                                    maneuver = Maneuver.TURN_SLIGHT_RIGHT
                                ),
                                Step(
                                    startLocation = LatLng(49.4212379, 31.2588566),
                                    endLocation = LatLng(50.44985029999999, 30.5238646),
                                    duration = Duration(
                                        value = 665.0,
                                        text = "11 мин."
                                    ),
                                    distance = Distance(
                                        value = 78585.0,
                                        text = "78,5 км"
                                    ),
                                    instructions = "Поверните <b>налево</b> … по вул. Хрещатик</div>",
                                    maneuver = Maneuver.TURN_LEFT
                                )
                            )
                        )
                    ),
                    polyline = Polyline(
                        points = "qaxlH_stbE}kB|bEmk@j{@ot@v`CwPdiAaCh~BzKxcAzHpc@{PfaAkGdbAaIhfBo_@tw@sVbo@yTdwA{PraApHn}ArHnq@iBtQ_IxKcK~Amj@gS{S]}SlPuObAgWbb@cSze@tDxp@zNpkAxl@p_BxXhx@r@nYuWzgAm[`f@ad@`h@iBpHtH|_@aLvZ}FvJGjIvEbEbBh@xEbRpGvZth@|d@xw@p_@lh@dOvGjjAt@lq@|d@p{@`Svl@bWbMfUlQfHzSn@`qAzAj~BbAhtAgCxeAkF~P{OblAc@n_@~HpMvJv\\rAf`@bMj`@_@zd@|Ozh@|R|P`k@rv@bU|j@xWnxBtZrZh_@zgAnWdw@eWds@wYjqAyAdj@`FxThBdHgYrf@_CzR@|bA`Pdl@|Gbw@pVzyB}GdzAtSvlCbQriEpL`VhKxlAnDjn@xHl`@iDnGzCrNlKvVjAUpDwBlM|k@fPtt@n[twA~C`f@gBhz@aI|XoCtPwGjF}A~N~JvYvGbQyTbZ{\\vd@}\\np@gVbx@mQtZsf@x{@m\\d{AcNfUue@`V}m@pr@qQzs@qs@f~AaZdb@g^~IsgCpJg|A|Kun@tIe`Af]_m@faBau@fnAgqAxyAi`@je@mTjj@qdAztCyVlaA}Oj}@sQv^gYnOoWSca@rIeP`Lyd@d|@gt@|oAsf@pfBoUhkAuGrg@sIpGaTyFeVsHqn@xBmhAbE{i@UczAwWqjAlI}c@|He`@`Rw_@`RwNhOuOd\\uPdJyf@`_@}N`D}J|LeT~k@um@d_AuOxFm[`Ael@bPqy@jb@ak@bw@gY~Nua@tc@e[`SiLv[sdAj_AyoCv`CctAthAkQf@ep@wNwKjJwBtN{Nj~@sEpVsb@u@sm@cCejEtgD{|AbkAsr@daAy|BhhDm{B~sCgkBt{BucBdcCw{@j|@wlCngCaUjHgJtR__@lr@}Djp@gGzUwLjOwd@zGmOlBkp@sMmKcD_KyKkPu[eSe_@eFkg@eIaS{UqGuzAmn@e^q]uYcc@aQc@wVpY{RbD}]uCcNgIuLmEiOxB_UxDajAaPkeAkO_YlCui@z[mFPgi@uEutAeM{v@na@ur@nc@}kB`lAy{GjhEe_Axq@o^pn@_Q~b@i^|n@{kAzy@gjA|o@k~@|WsV|LcLzMkUtE{cAdO_~@|Jo|@{@g}@uNw|@_W}_@uLcX{Sy]q[}g@}Ts`@iA_`@~FcBaHuDlDGnIg@|`@uEbKgI~A_KxAaEl@wBjMoXhOqVfPs[x`@uBhRiPfc@ySnj@oNxPnOlU"
                    )
                )
            )
        )

        road = Roads(
            snappedPoints = listOf()
        )

        directionRequest = mock()
        roadsRequest = mock()
        routeBuilder = RouteProvider.Builder(directionRequest!!, roadsRequest!!)
            .addOrigin("Черкассы")
            .addDestination("Киев")
    }

    @After
    fun close() {
        Dispatchers.resetMain()
        route = null
        road = null
        directionRequest = null
        routeBuilder = null
    }

    @Test
    fun check_that_the_fastest_route_found_correct() = runBlocking{

        //Setup
        val request = routeBuilder!!
            .findTheFastest()
            .build()

        doReturn(route).whenever(directionRequest)?.requestDirection(any())
        doReturn(road).whenever(roadsRequest)?.getRoads(any())

        val actual = listOf(
            request.RouteSummary(
                distance = 211003.0,
                time = 8100.0,
                polyline = arrayOf(),
                maneuvers = arrayListOf(
                    null,
                    Maneuver.STRAIGHT,
                    Maneuver.TURN_RIGHT,
                    Maneuver.TURN_SLIGHT_RIGHT,
                    Maneuver.TURN_LEFT
                ),
                instructions = arrayListOf(
                    "Направляйтесь на <b>севе…Байди Вишневецького</b>",
                    "Продолжайте движение по …НГ-КЛУБ \"КОСМОС\"</b>)",
                    "Поверните <b>направо</b>…движение по T2403</div>",
                    "Плавный поворот <b>направо</b> на <b>H01</b>",
                    "Поверните <b>налево</b> … по вул. Хрещатик</div>"
                )
            )
        )

        //Run
        val result = request.routes()

        //Assertion
        assertThat(actual, Matchers.equalTo(result))

        //Verify

        verify(directionRequest, atLeastOnce())?.requestDirection(any())
        verify(roadsRequest, atLeastOnce())?.getRoads(any())
        verifyNoMoreInteractions(directionRequest, roadsRequest)

    }

    @Test
    fun check_that_the_shortest_route_found_correct() = runBlocking{

        //Setup
        val request = routeBuilder!!
            .findTheShortest()
            .build()

        doReturn(route).whenever(directionRequest)?.requestDirection(any())
        doReturn(road).whenever(roadsRequest)?.getRoads(any())

        val actual = listOf(
            request.RouteSummary(
                distance = 188546.0,
                time = 9904.0,
                polyline = arrayOf(),
                maneuvers = arrayListOf(
                    null,
                    Maneuver.STRAIGHT,
                    Maneuver.RAMP_LEFT,
                    Maneuver.ROUND_ABOUT_RIGHT,
                    Maneuver.TURN_LEFT
                ),
                instructions = arrayListOf(
                    "Направляйтесь на <b>севе…Байди Вишневецького</b>",
                    "Продолжайте движение по …НГ-КЛУБ \"КОСМОС\"</b>)",
                    "Резкий поворот <b>налево…выезжайте на <b>Н16</b>",
                    "На круге сверните на <b>…b>/<wbr/><b>ПИРЯТИН</b>",
                    "Поверните <b>налево</b> … по вул. Хрещатик</div>"
                )
            )
        )

        //Run
        val result = request.routes()

        //Assertion
        assertThat(actual, Matchers.equalTo(result))

        //Verify

        verify(directionRequest, atLeastOnce())?.requestDirection(any())
        verify(roadsRequest, atLeastOnce())?.getRoads(any())
        verifyNoMoreInteractions(directionRequest, roadsRequest)

    }

    @Test
    fun check_that_the_fastest_and_shortest_route_found_correct() = runBlocking{

        //Setup
        val request = routeBuilder!!
            .findTheShortest()
            .findTheFastest()
            .build()

        doReturn(route).whenever(directionRequest)?.requestDirection(any())
        doReturn(road).whenever(roadsRequest)?.getRoads(any())

        val actual = listOf(
            request.RouteSummary(
                distance = 211003.0,
                time = 8100.0,
                polyline = arrayOf(),
                maneuvers = arrayListOf(
                    null,
                    Maneuver.STRAIGHT,
                    Maneuver.TURN_RIGHT,
                    Maneuver.TURN_SLIGHT_RIGHT,
                    Maneuver.TURN_LEFT
                ),
                instructions = arrayListOf(
                    "Направляйтесь на <b>севе…Байди Вишневецького</b>",
                    "Продолжайте движение по …НГ-КЛУБ \"КОСМОС\"</b>)",
                    "Поверните <b>направо</b>…движение по T2403</div>",
                    "Плавный поворот <b>направо</b> на <b>H01</b>",
                    "Поверните <b>налево</b> … по вул. Хрещатик</div>"
                )
            ),
            request.RouteSummary(
                distance = 188546.0,
                time = 9904.0,
                polyline = arrayOf(),
                maneuvers = arrayListOf(
                    null,
                    Maneuver.STRAIGHT,
                    Maneuver.RAMP_LEFT,
                    Maneuver.ROUND_ABOUT_RIGHT,
                    Maneuver.TURN_LEFT
                ),
                instructions = arrayListOf(
                    "Направляйтесь на <b>севе…Байди Вишневецького</b>",
                    "Продолжайте движение по …НГ-КЛУБ \"КОСМОС\"</b>)",
                    "Резкий поворот <b>налево…выезжайте на <b>Н16</b>",
                    "На круге сверните на <b>…b>/<wbr/><b>ПИРЯТИН</b>",
                    "Поверните <b>налево</b> … по вул. Хрещатик</div>"
                )
            )
        )

        //Run
        val result = request.routes()

        //Assertion
        assertThat(actual, Matchers.equalTo(result))

        //Verify

        verify(directionRequest, atLeastOnce())?.requestDirection(any())
        verify(roadsRequest, atLeastOnce())?.getRoads(any())
        verifyNoMoreInteractions(directionRequest, roadsRequest)

    }

    @Test
    fun check_that_all_routes_found_correct() = runBlocking{

        //Setup
        val request = routeBuilder!!.build()

        doReturn(route).whenever(directionRequest)?.requestDirection(any())
        doReturn(road).whenever(roadsRequest)?.getRoads(any())

        val actual = listOf(
            request.RouteSummary(
                distance = 188546.0,
                time = 9904.0,
                polyline = arrayOf(),
                maneuvers = arrayListOf(
                    null,
                    Maneuver.STRAIGHT,
                    Maneuver.RAMP_LEFT,
                    Maneuver.ROUND_ABOUT_RIGHT,
                    Maneuver.TURN_LEFT
                ),
                instructions = arrayListOf(
                    "Направляйтесь на <b>севе…Байди Вишневецького</b>",
                    "Продолжайте движение по …НГ-КЛУБ \"КОСМОС\"</b>)",
                    "Резкий поворот <b>налево…выезжайте на <b>Н16</b>",
                    "На круге сверните на <b>…b>/<wbr/><b>ПИРЯТИН</b>",
                    "Поверните <b>налево</b> … по вул. Хрещатик</div>"
                )
            ),
            request.RouteSummary(
                distance = 191524.0,
                time = 10911.0,
                polyline = arrayOf(),
                maneuvers = arrayListOf(
                    null,
                    Maneuver.STRAIGHT,
                    Maneuver.ROUND_ABOUT_RIGHT,
                    Maneuver.ROUND_ABOUT_RIGHT,
                    Maneuver.TURN_RIGHT,
                    Maneuver.TURN_RIGHT
                ),
                instructions = arrayListOf(
                    "Направляйтесь на <b>севе…Байди Вишневецького</b>",
                    "Продолжайте движение по …НГ-КЛУБ \"КОСМОС\"</b>)",
                    "На круге сверните на <b>…торону <b>МИРОНІВКА</b>",
                    "На круге сверните на <b>… движение по <b>P09</b>",
                    "Поверните <b>направо</b>",
                    "Поверните <b>налево</b> … по вул. Хрещатик</div>"
                )
            ),
            request.RouteSummary(
                distance = 211003.0,
                time = 8100.0,
                polyline = arrayOf(),
                maneuvers = arrayListOf(
                    null,
                    Maneuver.STRAIGHT,
                    Maneuver.TURN_RIGHT,
                    Maneuver.TURN_SLIGHT_RIGHT,
                    Maneuver.TURN_LEFT
                ),
                instructions = arrayListOf(
                    "Направляйтесь на <b>севе…Байди Вишневецького</b>",
                    "Продолжайте движение по …НГ-КЛУБ \"КОСМОС\"</b>)",
                    "Поверните <b>направо</b>…движение по T2403</div>",
                    "Плавный поворот <b>направо</b> на <b>H01</b>",
                    "Поверните <b>налево</b> … по вул. Хрещатик</div>"
                )
            )
        )

        //Run
        val result = request.routes()

        //Assertion
        assertThat(actual, Matchers.equalTo(result))

        //Verify

        verify(directionRequest, atLeastOnce())?.requestDirection(any())
        verify(roadsRequest, atLeastOnce())?.getRoads(any())
        verifyNoMoreInteractions(directionRequest, roadsRequest)

    }

    @Test
    fun check_that_maneuver_points_extract_correct_from_RouteSummary() {

        //Setup
        val actual1 = listOf(
            LatLng(49.4442495, 32.0595199),
            LatLng(49.4588119, 32.0335838),
            LatLng(49.4655965, 32.0229632),
            LatLng(49.6556003, 32.1124988),
            LatLng(49.6717464, 32.0829153),
            LatLng(50.44985029999999, 30.5238646)
        )

        val actual2 = listOf(
            LatLng(49.4442495, 32.0595199),
            LatLng(49.4588119, 32.0335838),
            LatLng(49.7044347, 31.3670327),
            LatLng(49.7109381, 31.282154),
            LatLng(49.71852149999999, 31.1688241),
            LatLng(49.7636601, 31.0575204),
            LatLng(50.44985029999999, 30.5238646)
        )

        val actual3 = listOf(
            LatLng(49.4442495, 32.0595199),
            LatLng(49.4588119, 32.0335838),
            LatLng(49.5248453, 31.74126819999999),
            LatLng(49.4212379, 31.2588566),
            LatLng(50.44985029999999, 30.5238646)
        )

        val request = routeBuilder!!.build()

        //Run
        val maneuverPoints1 = request.getManeuverPoints(route!!.routes[0])
        val maneuverPoints2 = request.getManeuverPoints(route!!.routes[1])
        val maneuverPoints3 = request.getManeuverPoints(route!!.routes[2])

        //Assert

        assertThat(actual1, Matchers.equalTo(maneuverPoints1))
        assertThat(actual2, Matchers.equalTo(maneuverPoints2))
        assertThat(actual3, Matchers.equalTo(maneuverPoints3))

    }

}
