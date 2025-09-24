import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.openclassroom.vitesse.data.Candidate
import com.openclassroom.vitesse.repository.CandidateRepository

class CandidateViewModel(
    private val repository: CandidateRepository = CandidateRepository()
) : ViewModel() {

    private val _candidates = MutableStateFlow<List<Candidate>>(emptyList())
    val candidates: StateFlow<List<Candidate>> = _candidates

    var selectedCandidate by mutableStateOf<Candidate?>(null)
        private set

    init {
        viewModelScope.launch {
            repository.candidates.collectLatest {
                _candidates.value = it
            }
        }
    }

    fun addCandidate(candidate: Candidate) {
        repository.addCandidate(candidate)
    }

    fun removeCandidate(candidate: Candidate) {
        repository.removeCandidate(candidate)
    }

    fun selectCandidate(candidate: Candidate?) {
        selectedCandidate = candidate
    }

    fun updateCandidate(candidate: Candidate) {
        repository.updateCandidate(candidate)
    }

    fun addOrUpdateCandidate(candidate: Candidate) {
        Log.d("CandidateViewModel", "candidate id=${candidate.id}")
        val exists = _candidates.value.any { it.id == candidate.id }
        if (exists) {
            Log.d("CandidateViewModel", "Updating existing candidate with id=${candidate.id}")
            updateCandidate(candidate)
        } else {
            Log.d("CandidateViewModel", "Adding new candidate with id=${candidate.id}")
            addCandidate(candidate)
        }
    }

    fun toggleFavorite(candidate: Candidate) {
        repository.toggleFavorite(candidate)
    }
}

