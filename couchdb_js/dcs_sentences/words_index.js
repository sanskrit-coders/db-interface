fn = function(doc) {
    var idx, key;
    if (doc.dcsAnalysisDecomposition) {
        for (idx in doc.dcsAnalysisDecomposition) {
            wordGroup = doc.dcsAnalysisDecomposition[idx];
            for (idx in wordGroup) {
                wordDetail = wordGroup[idx];
                emit(wordDetail.root, wordDetail.dcsGrammarHint);
            }
        }
    }
}