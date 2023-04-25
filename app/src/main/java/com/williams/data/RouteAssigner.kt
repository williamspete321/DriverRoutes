package com.williams.data

import com.williams.data.source.local.model.LocalDriver
import com.williams.data.source.local.model.LocalDriverWithRoutes
import com.williams.data.source.local.model.LocalRoute
import com.williams.data.source.network.model.Type

/**
 *  Has a public method assign() that takes one list of LocalDriver and LocalRoute each and then
 *  determines which routes belong to which drivers. One pass is made through drivers and routes each.
 */
object RouteAssigner {

    private var rCounter = 0
    private var cCounter = 0

    private lateinit var routesMap: MutableMap<Int, LocalRoute>

    private lateinit var firstRtype: LocalRoute
    private lateinit var secondCtype: LocalRoute
    private lateinit var lastItype: LocalRoute

    fun assign(drivers: List<LocalDriver>, routes: List<LocalRoute>): List<LocalDriverWithRoutes> {
        // Reset routes map
        routesMap = mutableMapOf()

        // Set route map and types first before assigning to drivers
        setRouteVariables(routes)

        // Assign and return list of drivers with theirs routes
        return assignRoutesToDrivers(drivers)
    }

    /**
     *  Makes one pass through the routes to map routes to their ids and set first R, second C,
     *  and last I routes.
     */
    private fun setRouteVariables(routes: List<LocalRoute>) {
        // Reset counters
        rCounter = 0
        cCounter = 0

        for (route in routes) {
            // Map route to it's id
            routesMap[route.routeId.toInt()] = route

            // Set the first R type
            if (route.type == Type.R && rCounter == 0) {
                firstRtype = route
                rCounter++
            }

            // Set the second C type
            if (route.type == Type.C) {
                cCounter++
                if (cCounter == 2) {
                    secondCtype = route
                }
            }

            // Set the last I type
            if (route.type == Type.I) {
                // By default the last I route will be assigned to this variable
                lastItype = route
            }
        }
    }

    /**
     * NOTE: If a driver id meets more than one route criteria, additional routes are added
     * For example: driverId=2 has a matching routeId and is also % 2, so it gets the first R type
     * route assigned as well.
     */
    private fun assignRoutesToDrivers(drivers: List<LocalDriver>): List<LocalDriverWithRoutes> {
        val driversWithRoutes = mutableListOf<LocalDriverWithRoutes>()

        for (driver in drivers) {
            val routeList = mutableListOf<LocalRoute>()

            // Handle for matching driver and route id
            if (routesMap.containsKey(driver.driverId.toInt())) {
                routeList.add(routesMap[driver.driverId.toInt()]!!)
            }

            // Handle driver id % 2
            val driverId = driver.driverId.toInt()
            if (driverId % 2 == 0) {
                routeList.add(firstRtype)
            }

            // Handle driver id % 5
            if (driverId % 5 == 0) {
                routeList.add(secondCtype)
            }

            // Handle for case that previous criteria has not been met
            if (routeList.isEmpty()) {
                routeList.add(lastItype)
            }

            // Finally, add the LocalDriver and all the Routes that driver has to list
            driversWithRoutes.add(
                LocalDriverWithRoutes(
                    driver,
                    routeList
                )
            )
        }

        return driversWithRoutes
    }

}