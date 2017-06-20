fn = function(doc) {
    var idx, key;
    if (doc.title) {
        emit(doc.title, 1);
    }
}