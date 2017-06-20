fn = function(doc) {
    var idx, key;
    if (doc.sentenceIds) {
        for (idx in doc.sentenceIds) {
            emit(doc.sentenceIds[idx], 1);
        }
    }
}