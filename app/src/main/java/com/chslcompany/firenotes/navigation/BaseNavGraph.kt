package com.chslcompany.firenotes.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface BaseNavGraph {

    fun build(
        modifier: androidx.compose.ui.Modifier,
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    )
}