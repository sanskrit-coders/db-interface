fn = function(doc) {
    var idx, key;
    if (doc.sentenceIds) {
        for (idx in doc.sentenceIds) {
            sentenceId = doc.sentenceIds[idx];
            emit(sentenceId, 1);
        }
    }
}