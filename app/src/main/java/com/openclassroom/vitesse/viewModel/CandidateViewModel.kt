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
        // Observe la liste des candidats depuis le repository
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
}

