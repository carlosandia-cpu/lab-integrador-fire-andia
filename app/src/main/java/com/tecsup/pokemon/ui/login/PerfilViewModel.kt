package com.tecsup.pokemon.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.pokemon.data.perfil.AVATAR_POR_DEFECTO
import com.tecsup.pokemon.data.perfil.PerfilRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PerfilViewModel(
    private val repo: PerfilRepository,
    private val uid: String
) : ViewModel() {

    val avatar: StateFlow<String> = repo.avatar(uid)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AVATAR_POR_DEFECTO)

    fun elegirAvatar(id: String) {
        viewModelScope.launch { repo.elegirAvatar(uid, id) }
    }
}