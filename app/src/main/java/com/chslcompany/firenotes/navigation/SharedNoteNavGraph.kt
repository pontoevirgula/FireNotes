package com.chslcompany.firenotes.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.chslcompany.sharedNotes.ui.SharedNoteScreen
import kotlinx.serialization.Serializable

object SharedNoteNavGraph: BaseNavGraph {

    sealed interface Dest {

        @Serializable
        data object Root : Dest
        @Serializable
        data object SharedNote : Dest
    }

    override fun build(
        modifier: Modifier,
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<Dest.Root>(startDestination = Dest.SharedNote){
            composable<Dest.SharedNote>{
                SharedNoteScreen(modifier)
            }

        }
    }


}