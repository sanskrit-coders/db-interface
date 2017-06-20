fn = function(doc) {
    var idx, key;
    if (doc.headwords) {
        for (idx in doc.headwords) {
            key = doc.headwords[idx].text;
            emit(key, 1);
        }
    }
}