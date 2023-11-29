package com.ehabnaguib.android.privatecontacts

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ContactDetailViewModel (contactId : UUID) : ViewModel() {

    private val contactRepository = ContactRepository.get()

    private val _contact: MutableStateFlow<Contact?> = MutableStateFlow(null)
    val contact: StateFlow<Contact?> = _contact.asStateFlow()

    init {
        viewModelScope.launch {
            _contact.value = contactRepository.getContact(contactId)
        }
    }

    fun updateContact(onUpdate: (Contact) -> Contact) {
        _contact.update { oldContact ->
            oldContact?.let { onUpdate(it) }
        }
    }

    fun saveContact() {
        contact.value?.let { contactRepository.updateContact(it)}
    }

    fun deleteContact() {
        contact.value?.let { contactRepository.deleteContact(it)}
    }


    override fun onCleared() {
        super.onCleared()

        contact.value?.let { contact ->
            if (contact.name.isBlank() && contact.number.isBlank())
                contactRepository.deleteContact(contact)
            else
                contactRepository.updateContact(contact)}
    }
}

class ContactDetailViewModelFactory(
    private val contactId: UUID
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContactDetailViewModel(contactId) as T
    }
}