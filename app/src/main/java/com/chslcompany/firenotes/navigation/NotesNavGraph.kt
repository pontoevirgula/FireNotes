package com.chslcompany.firenotes.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.chslcompany.notes.ui.addEdit.AddEditScreen
import com.chslcompany.notes.ui.notes.NotesScreen
import kotlinx.serialization.Serializable

object NotesNavGraph  : BaseNavGraph {

    sealed interface Dest {
        @Serializable
        data object Root : Dest

        @Serializable
        data object Notes : Dest

        @Serializable
        data class AddEdit(val id: String?) : Dest
    }

    override fun build(
        modifier: Modifier,
        navHostController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<Dest.Root>(startDestination = Dest.Notes) {
            composable<Dest.Notes> {
                NotesScreen(modifier = modifier, goToAddEditNoteScreen = {
                    navHostController.navigate(Dest.AddEdit(it))
                })
            }

            composable<Dest.AddEdit> {
                val id = it.toRoute<Dest.AddEdit>().id
                AddEditScreen(
                    modifier = modifier,
                    popBackStack = {
                        navHostController.popBackStack()
                    },
                    id = id
                )
            }
        }
    }
}