fn = function(doc) {
    var idx, key;
    if (doc.chapterIds) {
        for (idx in doc.chapterIds) {
            emit(doc.chapterIds[idx], 1);
        }
    }
}